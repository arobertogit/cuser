package fii.odiunu.security;

import org.springframework.security.core.authority.AuthorityUtils;

/**
 * Created by ojrobert on 2/7/2016.
 */
public class CurrentUser extends org.springframework.security.core.userdetails.User {

    public CurrentUser(User user) {
        super(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().toString()));

    }
}
