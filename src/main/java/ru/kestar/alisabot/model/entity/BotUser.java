package ru.kestar.alisabot.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class BotUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "telegram_id")
    private Long telegramId;

    @Column(name = "username")
    private String userName;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "login")
    private String login;

    @Column(name = "login_date")
    private LocalDateTime loginDate;

    @Column(name = "token")
    private String token;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private BotUser parent;

    @OneToMany(mappedBy = "owner")
    private List<Invite> inviteCodes;

    @Transient
    public boolean isAuthenticated() {
        return token != null;
    }
}
