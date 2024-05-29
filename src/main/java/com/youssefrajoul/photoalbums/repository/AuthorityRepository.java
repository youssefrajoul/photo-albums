package com.youssefrajoul.photoalbums.repository;

import org.springframework.data.repository.CrudRepository;

import com.youssefrajoul.photoalbums.model.Authority;

public interface AuthorityRepository extends CrudRepository<Authority, Long> {
    
}
