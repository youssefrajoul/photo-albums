package com.youssefrajoul.photoalbums.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShareAlbum {
    @Id
    private Long id;

    private Long albumId;

    private String owner;

    private String otherUser;
}
