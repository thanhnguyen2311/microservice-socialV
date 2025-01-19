package com.example.userservice.repository;

import com.example.userservice.entity.SocialVUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<SocialVUser, Long> {
    boolean existsSocialVUserById(Long id);
}
