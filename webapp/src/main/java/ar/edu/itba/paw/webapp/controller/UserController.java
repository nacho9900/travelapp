package ar.edu.itba.paw.webapp.controller;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class UserController extends MainController{

/*
    @Autowired
    private AuthenticationManager authenticationManager;



        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getEmail(),
                user.getPassword());
        /*authToken.setDetails(new WebAuthenticationDetails(request));
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

*/

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService us;


    @RequestMapping("/")
    public ModelAndView index() { return new ModelAndView("index"); }


    @RequestMapping("/home")
    public ModelAndView home() {
        ModelAndView mav = new ModelAndView("home");
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
                                       final BindingResult errors, HttpServletRequest request) {

        ModelAndView mav = new ModelAndView("signup");
        if(errors.hasErrors()) {
            return mav;
        }
        String encodedPassword =  passwordEncoder.encode(form.getPassword());
        User user = us.create(form.getFirstname(), form.getLastname(), form.getEmail(), encodedPassword,
                DateManipulation.stringToCalendar(form.getBirthday()), form.getNationality());

        mav.setViewName("redirect:signin");
        return mav;
    }

}
