package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

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
    public ModelAndView signup() { return new ModelAndView("signup"); }

    @RequestMapping("/signin")
    public ModelAndView signin() { return new ModelAndView("signin"); }

    @RequestMapping(value = "/signin", method = {RequestMethod.POST})
    public ModelAndView validateSignIn(@RequestParam String username,
                                       @RequestParam String password,
                                       RedirectAttributes redir) {

        ModelAndView mav = new ModelAndView("redirect:home");
        //User user = us.findByUsername(username).orElseThrow(UserNotFoundException::new);
        Optional<User> user = us.findByUsername(username);
        if(user.isPresent()) {
            if (user.get().getPassword().equals(password)) {
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
                                       @RequestParam String pswrepeat) {

        ModelAndView mav = new ModelAndView("signin");
        User user = us.create(firstname,lastname,email,password);
        return mav;
    }

    @RequestMapping("/test")
    public ModelAndView test() {
        User user = us.findByUsername("123").get();
        ModelAndView mav =  new ModelAndView("test");
        mav.addObject("test","hellow testing");
        mav.addObject("test2",user);
        return mav;
    }
}
