package com.kyurao.sweater.controller;

import com.kyurao.sweater.domain.Dialog;
import com.kyurao.sweater.domain.DialogMsg;
import com.kyurao.sweater.domain.User;
import com.kyurao.sweater.service.DialogService;
import com.kyurao.sweater.service.UserService;
import com.kyurao.sweater.util.SweaterUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user/dialogs")
public class DialogController {

    private final UserService userService;
    private final DialogService dialogService;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping
    public String getDialogs(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(name = "user", required = false) User user,
            @RequestParam(name = "cr", required = false) boolean create,
            Dialog dialog,
            Model model
    ) {
        if (create && isNull(dialog.getId())) {
            dialogService.createDialog(currentUser, user, dialog);
        }
            List<Dialog> dialogs = currentUser.getDialogs();
            model.addAttribute("dialogs", dialogs);
            model.addAttribute("users", userService.findAll());

        return  "userDialogs";
    }

    @GetMapping("/{dialog}")
    public String dialog(@PathVariable Dialog dialog, Model model) {
        Set<User> users = dialog.getUsers();
        List<DialogMsg> messages = dialog.getMessages();

        model.addAttribute("users", users);
        model.addAttribute("messages", messages);

        return "dialog";
    }

    @PostMapping("/{dialog}")
    public String addMessageToDialog(
            @AuthenticationPrincipal User user,
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam String text,
            @PathVariable Dialog dialog,
            Model model
    ) throws IOException {

        dialogService.addDialogMsg(dialog, user, text, SweaterUtils.getNewFile(file, uploadPath));
        Set<User> users = dialog.getUsers();
        List<DialogMsg> messages = dialog.getMessages();

        model.addAttribute("users", users);
        model.addAttribute("messages", messages);

        return "dialog";
    }
}
