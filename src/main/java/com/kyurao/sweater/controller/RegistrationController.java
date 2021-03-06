package com.kyurao.sweater.controller;

import com.kyurao.sweater.domain.User;
import com.kyurao.sweater.service.UserService;
import com.kyurao.sweater.util.SweaterUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class RegistrationController {

    private final UserService userService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam(name = "password2") String password2,
            @Valid User user,
            BindingResult bindingResult, //Validation result
            Model model) {
        if (user.getPassword() != null && !user.getPassword().equals(password2)) {
            model.addAttribute("passwordError", "Passwords are different!");
        }

        if (bindingResult.hasErrors() || model.containsAttribute("passwordError")) {
            Map<String, String> errors = SweaterUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        }

        if (!userService.registerUser(user)) {
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }

        model.addAttribute("message", "Confirm registration by clicking the hyperlink " +
                "sent to the email address you specified.");

        return "registration";
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code, Model model) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("message", "User successfully activated!");
        } else {
            model.addAttribute("message", "Wrong activation link!");
        }

        return "login";
    }
}
