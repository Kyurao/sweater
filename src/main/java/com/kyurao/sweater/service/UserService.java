package com.kyurao.sweater.service;

import com.kyurao.sweater.domain.User;
import com.kyurao.sweater.domain.enums.Role;
import com.kyurao.sweater.domain.enums.Sex;
import com.kyurao.sweater.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    @Value("${site.address}")
    private String siteAddress;

    private final UserRepository userRepository;
    private final MailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public boolean registerUser(User user) {
        User userFromDb = userRepository.findByUsername(user.getUsername());

        if (userFromDb != null) {
            return false;
        }

        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        setDefaultAvatar(user);

        userRepository.save(user);

        if (!ObjectUtils.isEmpty(user.getEmail())) {
            String text = String.format(
                    "Hello, %s!\n" +
                            "Welcome to Sweater.\n" +
                            "Please, visit next link %sactivate/%s",
                    user.getUsername(),
                    siteAddress,
                    user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation code", text);
        }

        return true;
    }

    private void setDefaultAvatar(User user) {
        switch (user.getSex()) {
            case FEMALE: {
                user.setAvatarPhoto("avatar_female.png");
            } break;
            case MALE: {
                user.setAvatarPhoto("avatar_male.png");
            } break;
            default: {
                user.setAvatarPhoto("avatar_unknown.png");
            } break;
        }
    }

    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);

        if (user == null) {
            return false;
        }
        user.setActivationCode(null);
        user.setActive(true);
        userRepository.save(user);

        return true;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void saveUserRole(User user, Map<String, String> form) {

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key: form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepository.save(user);
    }

    public void updateProfile(User user, Map<String, String> form) {
        String userEmail = user.getEmail();
        String email = form.get("email");
        String password = form.get("password");
        String sex = form.get("sex");
        String photo = form.get("photo");


        boolean isEmailChanged = (userEmail != null && !userEmail.equals(email) ||
                (email != null && !email.equals(userEmail)));
        if (isEmailChanged && !email.isEmpty()) {
            user.setEmail(email);
        }

        if (password != null && !password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        if (sex != null && !sex.isEmpty()) {
            user.setSex(Sex.valueOf(sex.toUpperCase()));
        }

        if (photo != null) {
            user.setAvatarPhoto(photo);
        }

        userRepository.save(user);
    }
}
