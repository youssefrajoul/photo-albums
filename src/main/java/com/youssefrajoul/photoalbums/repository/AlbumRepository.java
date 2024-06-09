package com.youssefrajoul.photoalbums.repository;

import org.springframework.data.repository.CrudRepository;

import com.youssefrajoul.photoalbums.model.Album;
import com.youssefrajoul.photoalbums.model.User;

import java.util.List;

public interface AlbumRepository extends CrudRepository<Album, Long> {
    List<Album> findByOwner(User owner);
}
