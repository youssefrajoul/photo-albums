package com.youssefrajoul.photoalbums.repository;

import org.springframework.data.repository.CrudRepository;

import com.youssefrajoul.photoalbums.model.Picture;
import com.youssefrajoul.photoalbums.model.User;

import java.util.List;

public interface PictureRepository extends CrudRepository<Picture, Long> {
    List<Picture> findByUsername(User username);
}
