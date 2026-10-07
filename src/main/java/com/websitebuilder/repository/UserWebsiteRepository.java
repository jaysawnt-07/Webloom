package com.websitebuilder.repository;

import com.websitebuilder.model.UserWebsite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserWebsiteRepository extends JpaRepository<UserWebsite, Long> {
    List<UserWebsite> findAllByOrderByUpdatedAtDesc();
}
