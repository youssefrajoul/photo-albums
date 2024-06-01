package com.youssefrajoul.photoalbums.repository;

import org.springframework.data.repository.CrudRepository;

import com.youssefrajoul.photoalbums.model.Album;

public interface AlbumRepository extends CrudRepository<Album, Long> {

}
