package com.youssefrajoul.photoalbums.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @NotBlank(message = "Name cannot be blank")
    private String username;
    @NotBlank(message = "Email cannot be blank")
    private String password;
    private boolean enabled;
    @Column(columnDefinition = "LONGTEXT")
    private String publicKey;
    @Column(columnDefinition = "LONGTEXT")
    private String encryptedPrivateKey;
    private String salt;
}