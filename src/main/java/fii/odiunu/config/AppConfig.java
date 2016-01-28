package fii.odiunu.config;

/**
 * Created by ojrobert on 1/26/2016.
 */
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
@Configuration
@ComponentScan("fii.odiunu")
@EnableWebMvc
public class AppConfig {
}