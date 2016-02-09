package fii.odiunu.security;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by ojrobert on 2/7/2016.
 */
@Component
public class UserService {
    public static final String splitter = "<-!->";
    public static final String USERS_TXT = "users.txt";

    public UserService() {
        boolean exists = Files.exists(Paths.get(USERS_TXT));
        if (!exists) {
            try {
                Files.write(Paths.get(USERS_TXT),
                            getInitialUsers(),
                            StandardOpenOption.CREATE_NEW);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private ArrayList<String> getInitialUsers() {
        return Lists.newArrayList(
                getLine("admin", "admin", Role.ROLE_ADMIN.toString()),
                getLine("user", "user", Role.ROLE_USER.toString()),
                getLine("super", "super", Role.ROLE_SUPER.toString()));
    }

    private String getLine(String adminUser, String adminPw, String adminRole) {
        return adminUser + splitter + adminPw + splitter + adminRole;
    }

    public User getUserByUsername(String username) {
        try {
            Optional<String> first = Files.readAllLines(Paths.get(USERS_TXT))
                                          .stream()
                                          .filter(l -> isUserWithUsername(l, username))
                                          .findFirst();
            if (first.isPresent())
                return new User(first.get());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private boolean isUserWithUsername(String line, String username) {
        return line.split(splitter)[0].equals(username);
    }

    public void addUser(String username, String password, Role roleUser) {
        try {
            List<String> strings = Files.readAllLines(Paths.get(USERS_TXT));
            strings.add(username + splitter + password + splitter + roleUser.toString());
            Files.deleteIfExists(Paths.get(USERS_TXT));
            Files.write(Paths.get(USERS_TXT),
                        strings,
                        StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            int x = 1;
        }
    }
}
