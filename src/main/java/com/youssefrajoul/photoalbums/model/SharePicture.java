package com.youssefrajoul.photoalbums.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SharePicture {
    @Id
    private Long id;
    private String name;
    @Column(columnDefinition = "LONGTEXT")
    @NotEmpty
    private String data;

    private String owner;

    private String otherUser;
}
