package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.webapp.form.EditProfileForm;
import ar.edu.itba.paw.webapp.form.UserCreateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController extends MainController{

    @Autowired
    UserPicturesService ups;

    @Autowired
    private MailingService ms;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService us;

    @Autowired
    private TripService ts;

    @Autowired
    private TripPicturesService tripPicturesService;

    private static final long MAX_UPLOAD_SIZE = 5242880;


    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @RequestMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index"); }


    @RequestMapping("/home")
    public ModelAndView home(@ModelAttribute("user") User user) {
        ModelAndView mav = new ModelAndView("home");
        List<Trip> trips = ts.getAllTrips();
        List<DataPair<Trip, Boolean>> list = new ArrayList<>();
        for(Trip trip : trips) {
            list.add(new DataPair<>(trip, tripPicturesService.findByTripId(trip.getId()).isPresent()));
        }
        mav.addObject("dateFormat", dateFormat);
        mav.addObject("tripList", list);
        return mav;
    }

    @RequestMapping("/signup")
    public ModelAndView signup(@ModelAttribute("signupForm") final UserCreateForm form) {
        return new ModelAndView("signup");
    }

    @RequestMapping(value ="/signin", method = {RequestMethod.GET})
    public ModelAndView signin() { return new ModelAndView("signin"); }


    @RequestMapping(value = "/signup", method = {RequestMethod.POST})
    public ModelAndView validateSignUp(@Valid @ModelAttribute("signupForm") final UserCreateForm form,
                                       final BindingResult errors) {

        ModelAndView mav = new ModelAndView("signup");
        if(errors.hasErrors()) {
            return mav;
        }
        if(!form.checkPswRepeat()) {
            mav.addObject("pswRepeatError", true);
            return mav;
        }
        Optional userOpt = us.findByUsername(form.getEmail());
        if(userOpt.isPresent()) {
            mav.addObject("alreadyExists", true);
            return mav;
        }

        if(!ms.sendRegisterMail(form.getEmail(), form.getFirstname() , form.getLastname())) {
            mav.addObject("invalidEmail", true);
            return mav;
        }
        String encodedPassword =  passwordEncoder.encode(form.getPassword());
        User user = us.create(form.getFirstname(), form.getLastname(), form.getEmail(), encodedPassword,
                DateManipulation.stringToCalendar(form.getBirthday()), form.getNationality());

        mav.setViewName("redirect:/signin");
        return mav;
    }


    @RequestMapping(value = "/home/profile/{userId}", method = {RequestMethod.GET})
    public ModelAndView profile( @PathVariable(value = "userId") long userProfileId) {
        ModelAndView mav = new ModelAndView("profile");
        Optional<User> profileUser = us.findByid(userProfileId);
        if(!profileUser.isPresent()) {
            mav.setViewName("404");
            return mav;
        }
        Optional<UserPicture> userPictureOptional = ups.findByUserId(userProfileId);
        if(userPictureOptional.isPresent()) {
            mav.addObject("hasProfilePicture", true);
        }
        String birthday = dateFormat.format(profileUser.get().getBirthday().getTime());
        mav.addObject("birthday", birthday);
        mav.addObject("userProfile", profileUser.get());
        return mav;
    }

    @RequestMapping(value = "/home/profile/{userId}/image", method = {RequestMethod.GET})
    public void getProfileImage(@PathVariable(value = "userId") long userProfileId, HttpServletResponse response) {
        Optional<UserPicture> userPictureOptional = ups.findByUserId(userProfileId);
        try {
            if(userPictureOptional.isPresent()) {
                UserPicture up = userPictureOptional.get();
                String mimeType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(up.getPicture()));
                response.setContentType(mimeType);
                response.getOutputStream().write(up.getPicture());
            }
        }
        catch (IOException ex) {
            //nothing to do here
        }
    }

    @RequestMapping(value = "/home/profile/{userId}/edit", method = {RequestMethod.GET})
    public ModelAndView editProfileGet(@ModelAttribute("user") User user, @PathVariable(value = "userId") long userId,
                                @ModelAttribute("editProfileForm") final EditProfileForm form) {
        ModelAndView mav = new ModelAndView("editProfile");
        if(user.getId() != userId) {
            mav.setViewName("403");
            return mav;
        }
        return mav;
    }

    @RequestMapping(value = "/home/profile/{userId}/edit", method = {RequestMethod.POST})
    public ModelAndView profile(@ModelAttribute("user") User user, @PathVariable(value = "userId") long userId,
                                @Valid @ModelAttribute("editProfileForm") final EditProfileForm form,
                                final BindingResult errors) {

        ModelAndView mav = new ModelAndView("editProfile");
        if(user.getId() != userId) {
            mav.setViewName("403");
            return mav;
        }
        if(errors.hasErrors()) {
            return mav;
        }
        MultipartFile profilePicture = form.getImageUpload();
        byte[] imageBytes = null;
        if(profilePicture != null && !profilePicture.isEmpty()) {
            String contentType = profilePicture.getContentType();
            if(!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
                mav.addObject("invalidContentError", true);
                return mav;
            }
            else if(profilePicture.getSize() > MAX_UPLOAD_SIZE) {
                mav.addObject("fileSizeError", true);
                return mav;
            }
            try {
                imageBytes = profilePicture.getBytes();
            } catch (IOException e) {
                mav.addObject("generalError", true);
                return mav;
            }
        }
        else {
            mav.addObject("generalError", true);
            return mav;
        }
        if(ups.findByUserId(userId).isPresent()) {
            if(!ups.deleteByUserId(userId)) {
                mav.addObject("generalError", true);
                return mav;
            }
        }
        UserPicture userPicture = ups.create(userId, imageBytes);
        String redirectFormat = String.format("redirect:/home/profile/%d", userId);
        mav.setViewName(redirectFormat);
        return mav;
    }

    @RequestMapping("/about")
    public ModelAndView aboutUs() { return new ModelAndView("about"); }

}
