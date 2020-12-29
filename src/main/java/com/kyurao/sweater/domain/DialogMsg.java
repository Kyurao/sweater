package com.kyurao.sweater.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "dialog_msg")
public class DialogMsg {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dialog_id")
    private Dialog dialog;

    @Length(max = 2048, message = "Message to long (more than 2 kB)")
    private String text;

    private String file;

    private LocalDateTime createTime;
}
