package fii.odiunu.facebook;

/**
 * Created by ojrobert on 1/28/2016.
 */
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("fb")
public class FacebookController {

//    private Facebook facebook;
//
//    @Inject
//    public FacebookController(Facebook facebook) {
//        this.facebook = facebook;
//    }
//
//    @RequestMapping(method=RequestMethod.GET)
//    public String helloFacebook(Model model) {
//        if (!facebook.isAuthorized()) {
//            return "redirect:/connect/facebook";
//        }
//        model.addAttribute(facebook.userOperations().getUserProfile());
//        PagedList<Post> homeFeed = facebook.feedOperations().getHomeFeed();
//        model.addAttribute("feed", homeFeed);
//
//        return "hello";
//    }

}