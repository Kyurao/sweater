package com.kyurao.sweater.controller;

import com.kyurao.sweater.domain.Message;
import com.kyurao.sweater.domain.User;
import com.kyurao.sweater.service.MessageService;
import com.kyurao.sweater.util.SweaterUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user-messages")
public class MessageController {

    private final MessageService messageService;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/{user}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            Model model,
            @RequestParam(required = false) Message message
    ) {
        Set<Message> messages = user.getMessages();
        model.addAttribute("messages", messages);
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", currentUser.equals(user));

        return "userMessages";
    }

    @PostMapping("/{user}")
    public String editMessage(
            @PathVariable Long user,
            @RequestParam(name = "id") Message currentMessage,
            @RequestParam(name = "file") MultipartFile file,
            @Valid Message newMessage,
            BindingResult bindingResult,
            Model model
    ) throws IOException {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = SweaterUtils.getErrors(bindingResult);

            model.mergeAttributes(errorsMap);
            model.addAttribute("message", newMessage);

        } else {
            messageService.updateMessage(currentMessage, newMessage, SweaterUtils.getNewFile(file, uploadPath));
            model.addAttribute("message", null);
        }

        return "redirect:/user-messages/" + user;
    }
}
