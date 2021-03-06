package com.kyurao.sweater.controller;

import com.kyurao.sweater.domain.Message;
import com.kyurao.sweater.domain.User;
import com.kyurao.sweater.repository.MessageRepository;
import com.kyurao.sweater.service.MessageService;
import com.kyurao.sweater.util.SweaterUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping(path="/")
public class MainController {

    private final MessageRepository messageRepository;
    private final MessageService messageService;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter,
                       Model model) {

        Iterable<Message> messages;

         if (filter != null && !filter.isEmpty()) {
             messages = messageRepository.findByTag(filter);
         } else {
             messages = messageRepository.findAll();
         }

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        return "main";
    }

   @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @Valid Message message,
            BindingResult bindingResult,
            @RequestParam("file") MultipartFile file,
            Model model
   ) throws IOException {

       if (bindingResult.hasErrors()) {
           Map<String, String> errorsMap = SweaterUtils.getErrors(bindingResult);
           model.mergeAttributes(errorsMap);
           model.addAttribute("message", message);
       } else {
           message.setAuthor(user);
           messageService.saveMessage(message, SweaterUtils.getNewFile(file, uploadPath));
           model.addAttribute("message", null);
       }

       Iterable<Message> messages = messageRepository.findAll();
       model.addAttribute("messages", messages);

        return "main";
   }
}
