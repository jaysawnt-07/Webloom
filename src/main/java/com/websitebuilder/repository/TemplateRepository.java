package com.websitebuilder.repository;

import com.websitebuilder.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateRepository extends JpaRepository<Template, String> {
    List<Template> findByCategoryIgnoreCase(String category);
    List<Template> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String desc);
}
