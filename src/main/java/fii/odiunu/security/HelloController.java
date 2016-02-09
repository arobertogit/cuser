package fii.odiunu.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class HelloController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
    public String homePage(ModelMap model) {
        model.addAttribute("user", getPrincipal());
        return "welcome.jsp";
    }

    private String getPrincipal() {
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

    @RequestMapping(value = {"/pages/main"}, method = RequestMethod.GET)
    public String main(ModelMap model) {
        model.addAttribute("user", getPrincipal());
        return "pages/main.jsp";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(ModelMap model) {
        model.addAttribute("user", getPrincipal());
        return "pages/admin.jsp";
    }

    @RequestMapping(value = "/AccessDenied", method = RequestMethod.GET)
    public String accessDeniedPage(ModelMap model) {
        model.addAttribute("user", getPrincipal());
        return "accessDenied.jsp";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        return "login.jsp";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerPages() {
        return "register.jsp";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerPagesPost(HttpServletRequest request) {
        Map parameterMap = request.getParameterMap();
        String id = ((String[]) parameterMap.get("ssoId"))[0];
        String password = ((String[]) parameterMap.get("password"))[0];
        try {
            User userByUsername = userService.getUserByUsername(id);
            if (userByUsername == null)
                return "redirect:/";
        } catch (Exception ex) {
            return "redirect:/";
        }
        return "redirect:/register?error";
    }


}
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.servlet.ModelAndView;
//
///**
// * Created by ojrobert on 2/7/2016.
// */
//
//@Controller
//public class HelloController {
//
//    @Autowired
//    UserDetailsService userService;
//
//    @RequestMapping(value = {"/", "/hello**"}, method = RequestMethod.GET)
//    public ModelAndView welcomePage() {
//        ModelAndView model = new ModelAndView();
//        model.addObject("title", "Spring Security Example");
//        model.addObject("message", "This is Hello World!");
//        model.setViewName("/hello.jsp");
//        return model;
//
//    }
//
//    @RequestMapping(value = "/admin**", method = RequestMethod.GET)
//    public ModelAndView adminPage() {
//        UserDetails userDetails = userService.loadUserByUsername("admin");
//        System.out.println(userDetails.getUsername());
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName();
//        ModelAndView model = new ModelAndView();
//        model.addObject("title", "Spring Security Example");
//        model.addObject("message", "Logged In as  " + name + "!");
//        model.setViewName("/admin.jsp");
//        return model;
//    }
//
//    @RequestMapping(value = "/super**", method = RequestMethod.GET)
//    public ModelAndView dbaPage() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName();
//        ModelAndView model = new ModelAndView();
//        model.addObject("title", "Spring Security Example");
//        model.addObject("message", "Logged In as  " + name + "!");
//        model.setViewName("/admin.jsp");
//        return model;
//    }
//
//}
