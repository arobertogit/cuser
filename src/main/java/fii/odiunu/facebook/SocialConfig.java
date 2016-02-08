package fii.odiunu.facebook;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

/**
 * Created by ojrobert on 1/28/2016.
 */
@Configuration
public class SocialConfig {
    public static final String FB_APP_ID = "252255351633571";
    public static final String FB_SECRET = "7b9a6ecc8b381c79440115321f872a30";

    @Bean
    public ConnectionFactoryLocator connectionFactoryLocator() {
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
        registry.addConnectionFactory(new FacebookConnectionFactory(
                FB_APP_ID,
                FB_SECRET));
        return registry;
    }

}
