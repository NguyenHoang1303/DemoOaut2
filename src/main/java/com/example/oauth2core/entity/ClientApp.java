package com.example.oauth2core.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class ClientApp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientId;
    private String secretId;
    private String logo;
    private String url;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;


}
