package com.youssefrajoul.photoalbums.repository;

import org.springframework.data.repository.CrudRepository;

import com.youssefrajoul.photoalbums.model.User;

public interface UserRepository extends CrudRepository<User, String> {

}
