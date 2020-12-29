package com.kyurao.sweater.service;


import com.kyurao.sweater.domain.Dialog;
import com.kyurao.sweater.domain.DialogMsg;
import com.kyurao.sweater.domain.User;
import com.kyurao.sweater.repository.DialogMsgRepository;
import com.kyurao.sweater.repository.DialogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class DialogService {

    private final DialogRepository dialogRepository;
    private final DialogMsgRepository dialogMsgRepository;

    public void createDialog(User currentUser, User user, Dialog dialog) {

        Set<User> users = new HashSet<>();
        users.add(currentUser);
        users.add(user);

        dialog.setUsers(users);

        dialogRepository.save(dialog);
    }


    public void addDialogMsg(Dialog dialog, User user, String text, String file) {
        DialogMsg msg = new DialogMsg();

        msg.setDialog(dialog);
        msg.setAuthor(user);
        msg.setText(text);
        msg.setCreateTime(LocalDateTime.now());
        if (file != null && !file.isEmpty()) {
            msg.setFile(file);
        }
        dialogMsgRepository.save(msg);
    }
}
