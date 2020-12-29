package com.kyurao.sweater.domain;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@Entity
@Table(name = "dialog")
public class Dialog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dialog")
    private List<DialogMsg> messages;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_dialogs",
            joinColumns = { @JoinColumn(name = "dialog_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    private Set<User> users;

    public Dialog(Set<User> users) {
        this.users = users;
    }
}
