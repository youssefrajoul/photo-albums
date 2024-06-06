package com.youssefrajoul.photoalbums.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne()
    @JoinColumn(name = "username")
    private User username;
    @Column(columnDefinition = "LONGTEXT")
    @NotEmpty
    private String data;
    @Column(columnDefinition = "LONGTEXT")
    private String encryptedPicture;
    @Column(columnDefinition = "LONGTEXT")
    private String encryptedName;
    @ManyToOne()
    private Album album;
}
