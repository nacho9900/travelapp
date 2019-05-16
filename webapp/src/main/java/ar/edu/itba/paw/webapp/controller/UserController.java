package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.MailingService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.DateManipulation;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.form.UserCreateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Controller
public class UserController extends MainController{

    @Autowired
    private MailingService ms;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService us;


    @RequestMapping("/")
    public ModelAndView index() { return new ModelAndView("index"); }


    @RequestMapping("/home")
    public ModelAndView home() {
        return new ModelAndView("home");
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

    @RequestMapping("/home/profile/{userId}")
    public ModelAndView profile( @PathVariable(value = "userId") long userProfileId) {
        ModelAndView mav = new ModelAndView("profile");
        Optional<User> profileUser = us.findByid(userProfileId);
        if(!profileUser.isPresent()) {
            mav.setViewName("404");
            return mav;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String birthday = dateFormat.format(profileUser.get().getBirthday().getTime());
        mav.addObject("birthday", birthday);
        mav.addObject("userProfile", profileUser.get());
        return mav;
    }

    @RequestMapping("/about")
    public ModelAndView aboutUs() { return new ModelAndView("about"); }

}
