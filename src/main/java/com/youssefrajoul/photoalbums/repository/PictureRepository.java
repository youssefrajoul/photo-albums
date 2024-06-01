package com.youssefrajoul.photoalbums.repository;

import org.springframework.data.repository.CrudRepository;

import com.youssefrajoul.photoalbums.model.Picture;

public interface PictureRepository extends CrudRepository<Picture, Long> {

}
