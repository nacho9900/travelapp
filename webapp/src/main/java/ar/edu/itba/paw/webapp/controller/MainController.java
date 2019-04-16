package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.DateManipulation;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.form.UserCreateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService us;

    @ModelAttribute("user")
    public User loggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null) {
            return null;
        }
        if(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))) {
            return null;
        }
        final Optional<User> user = us.findByUsername(auth.getName());
        return user.get();
    }

    @RequestMapping("/")
    public ModelAndView index() { return new ModelAndView("index"); }

    //@ModelAttribute("userObj") final Object userMapping
    @RequestMapping("/home")
    public ModelAndView home(User user) {

        ModelAndView mav = new ModelAndView("home");
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping("/signup")
    public ModelAndView signup(@ModelAttribute("signupForm") final UserCreateForm form) {
        return new ModelAndView("signup");
    }

    @RequestMapping("/signin")
    public ModelAndView signin() { return new ModelAndView("signin"); }

    /*
    @RequestMapping(value = "/signin", method = {RequestMethod.POST})
    public ModelAndView validateSignIn(@RequestParam String username, @RequestParam String password,
                                       RedirectAttributes redir, HttpServletRequest request) {

        ModelAndView mav = new ModelAndView("redirect:home");
        //User user = us.findByUsername(username).orElseThrow(UserNotFoundException::new);
        Optional<User> user = us.findByUsername(username);
        if(user.isPresent()) {
            if (user.get().getPassword().equals(password)) {
                //request.getSession().setAttribute("user", user.get());
                //redir.addFlashAttribute("userObj", user.get());
                return mav;
            }
        }
        mav.setViewName("signin");
        return mav;
    }*/

    @RequestMapping(value = "/signup", method = {RequestMethod.POST})
    public ModelAndView validateSignUp(@Valid @ModelAttribute("signupForm") final UserCreateForm form,
                                       final BindingResult errors) {

        ModelAndView mav = new ModelAndView("signup");
        if(errors.hasErrors()) {
            return mav;
        }
        String encodedPassword =  passwordEncoder.encode(form.getPassword());
        System.out.println(passwordEncoder.matches("gax100gax100",encodedPassword));
        System.out.println(encodedPassword);
        System.out.println(passwordEncoder.encode("gax100gax100"));
        User user = us.create(form.getFirstname(), form.getLastname(), form.getEmail(), encodedPassword,
                DateManipulation.stringToCalendar(form.getBirthday()), form.getNationality());
        mav.setViewName("redirect:signin");
        return mav;
    }

}
