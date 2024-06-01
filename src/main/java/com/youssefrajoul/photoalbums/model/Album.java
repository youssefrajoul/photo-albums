package com.youssefrajoul.photoalbums.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Album {
    @Id
    private Long id;
    private String userId;
    // @OneToMany(mappedBy = "album")
    // private java.util.List<Picture> pictures;
}
