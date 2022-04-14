package com.example.oauth2core.entity;

import com.example.oauth2core.dto.FormUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scope")
    private String scope;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private int status;

    @OneToMany(mappedBy="user")
    private Set<ClientApp> clientApps;

    public User(FormUser formUser) {
        this.status = 1;
        this.username = formUser.getUsername();
        this.password = formUser.getPassword();
        this.scope = "api/v1/articles.read,api/v1/articles.update,api/v1/articles.delete,api/v1/articles.create";
    }

}
