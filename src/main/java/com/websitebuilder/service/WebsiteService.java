package com.websitebuilder.service;

import com.websitebuilder.model.UserWebsite;
import com.websitebuilder.repository.UserWebsiteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WebsiteService {

    private final UserWebsiteRepository websiteRepository;

    public WebsiteService(UserWebsiteRepository websiteRepository) {
        this.websiteRepository = websiteRepository;
    }

    public List<UserWebsite> getAllWebsites() {
        return websiteRepository.findAllByOrderByUpdatedAtDesc();
    }

    public Optional<UserWebsite> getWebsiteById(Long id) {
        return websiteRepository.findById(id);
    }

    public UserWebsite saveWebsite(UserWebsite website) {
        return websiteRepository.save(website);
    }

    public UserWebsite updateWebsite(Long id, UserWebsite updatedData) {
        return websiteRepository.findById(id).map(existing -> {
            if (updatedData.getTitle() != null) {
                existing.setTitle(updatedData.getTitle());
            }
            if (updatedData.getThemeConfigJson() != null) {
                existing.setThemeConfigJson(updatedData.getThemeConfigJson());
            }
            if (updatedData.getContentJson() != null) {
                existing.setContentJson(updatedData.getContentJson());
            }
            return websiteRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Website not found with id: " + id));
    }

    public void deleteWebsite(Long id) {
        websiteRepository.deleteById(id);
    }
}
