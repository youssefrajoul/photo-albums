package com.youssefrajoul.photoalbums.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youssefrajoul.photoalbums.model.Album;
import com.youssefrajoul.photoalbums.model.User;
import com.youssefrajoul.photoalbums.repository.AlbumRepository;
import com.youssefrajoul.photoalbums.repository.UserRepository;

@Service
public class AlbumService {
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Album> getAlbums(String username) {
        User user = userRepository.findById(username).orElseThrow(() -> new RuntimeException("User not found"));
        return albumRepository.findByOwner(user);
    }

    public void createAlbum(String albumName, String username) {
        User user = userRepository.findById(username).orElseThrow(() -> new RuntimeException("User not found"));
        Album album = new Album();
        album.setName(albumName);
        album.setOwner(user);
        albumRepository.save(album);
    }

    public Album getAlbum(Long albumId) {
        return albumRepository.findById(albumId).get();
    }
}
