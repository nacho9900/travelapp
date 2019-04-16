package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.DateManipulation;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.form.UserCreateForm;
import org.springframework.beans.factory.annotation.Autowired;
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
public class MainController {

    @Autowired
    private UserService us;

    @RequestMapping("/")
    public ModelAndView index() { return new ModelAndView("index"); }

    @RequestMapping("/home")
    public ModelAndView home(@ModelAttribute("userObj") final Object userMapping) {
        ModelAndView mav = new ModelAndView("home");
        mav.addObject("user", userMapping);
        return mav;
    }

    @RequestMapping("/signup")
    public ModelAndView signup(@ModelAttribute("signupForm") final UserCreateForm form) {
        return new ModelAndView("signup");
    }

    @RequestMapping("/signin")
    public ModelAndView signin() { return new ModelAndView("signin"); }

    @RequestMapping(value = "/signin", method = {RequestMethod.POST})
    public ModelAndView validateSignIn(@RequestParam String username, @RequestParam String password,
                                       RedirectAttributes redir, HttpServletRequest request) {

        //TODO: validate user input correctly
        ModelAndView mav = new ModelAndView("redirect:home");
        //User user = us.findByUsername(username).orElseThrow(UserNotFoundException::new);
        Optional<User> user = us.findByUsername(username);
        if(user.isPresent()) {
            if (user.get().getPassword().equals(password)) {
                request.getSession().setAttribute("user", user.get());
                redir.addFlashAttribute("userObj", user.get());
                return mav;
            }
        }
        mav.setViewName("signin");
        return mav;
    }

    @RequestMapping(value = "/signup", method = {RequestMethod.POST})
    public ModelAndView validateSignUp(@RequestParam String firstname,
                                       @RequestParam String lastname,
                                       @RequestParam String email,
                                       @RequestParam String password,
                                       @RequestParam String pswrepeat,
                                       @Valid @ModelAttribute("signupForm") final UserCreateForm form,
                                       final BindingResult errors) {

        ModelAndView mav = new ModelAndView("signup");

        if(errors.hasErrors()) {
            return mav;
        }

        User user = us.create(firstname,lastname,email,password,DateManipulation.stringToCalendar(form.getBirthday()),
                form.getNationality());
        mav.setViewName("redirect:signin");
        return mav;
    }

}
