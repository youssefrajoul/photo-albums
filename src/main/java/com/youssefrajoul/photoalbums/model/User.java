package com.youssefrajoul.photoalbums.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    private String salt;
    @OneToMany(mappedBy = "username")
    private List<Picture> pictures;
    @OneToMany(mappedBy = "username")
    private List<Album> albums;
}