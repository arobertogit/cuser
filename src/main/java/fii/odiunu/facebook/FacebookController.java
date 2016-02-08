package fii.odiunu.facebook;

/**
 * Created by ojrobert on 1/28/2016.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("fb")
public class FacebookController {

    private Facebook facebook;

//    @Inject
//    public FacebookController(Facebook facebook) {
//        this.facebook = facebook;
//
//    }

    @Autowired
    ConnectionFactoryLocator connectionFactoryLocator;

    @RequestMapping(method = RequestMethod.GET)
    public String helloFacebook(Model model) {
        if (facebook==null){
            ConnectionFactory<Facebook> connectionFactory = connectionFactoryLocator.getConnectionFactory(Facebook.class);
        }
        if (!facebook.isAuthorized()) {
            return "redirect:/connect/facebook";
        }
        model.addAttribute(facebook.userOperations().getUserProfile());
        PagedList<Post> homeFeed = facebook.feedOperations().getHomeFeed();
        model.addAttribute("feed", homeFeed);

        return "hello";
    }

}