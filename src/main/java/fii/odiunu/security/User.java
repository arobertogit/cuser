package fii.odiunu.security;

/**
 * Created by ojrobert on 2/7/2016.
 */
public class User {
    private String username;
    private String password;
    private Role role;

    public User(String line) {
        String[] split = line.split(UserService.splitter);
        username = split[0];
        password = split[1];
        role = Role.valueOf(split[2]);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}
