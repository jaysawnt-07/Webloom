package com.websitebuilder.controller;

import com.websitebuilder.model.Template;
import com.websitebuilder.service.ExportService;
import com.websitebuilder.service.TemplateService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/templates")
@CrossOrigin(origins = "*")
public class TemplateController {

    private final TemplateService templateService;
    private final ExportService exportService;

    public TemplateController(TemplateService templateService, ExportService exportService) {
        this.templateService = templateService;
        this.exportService = exportService;
    }

    @GetMapping
    public ResponseEntity<List<Template>> getTemplates(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(templateService.getAllTemplates(category, search));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Template> getTemplateById(@PathVariable String id) {
        return templateService.getTemplateById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/preview")
    public ResponseEntity<String> previewTemplate(@PathVariable String id) {
        return templateService.getTemplateById(id)
                .map(tmpl -> {
                    String themeJson = String.format("{\"primaryColor\":\"%s\",\"secondaryColor\":\"%s\",\"fontFamily\":\"%s\",\"buttonRadius\":\"%s\"}",
                            tmpl.getPrimaryColor(), tmpl.getSecondaryColor(), tmpl.getFontFamily(), tmpl.getButtonRadius());
                    String html = exportService.generateHtmlPage(tmpl.getName(), themeJson, tmpl.getContentJson());
                    return ResponseEntity.ok()
                            .contentType(MediaType.TEXT_HTML)
                            .body(html);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Map<String, String>>> getCategories() {
        return ResponseEntity.ok(List.of(
                Map.of("id", "all", "name", "All Templates", "icon", "fa-grip"),
                Map.of("id", "SaaS", "name", "SaaS & AI", "icon", "fa-brain"),
                Map.of("id", "Business", "name", "Agencies & Corporate", "icon", "fa-briefcase"),
                Map.of("id", "Portfolio", "name", "Portfolios & Creators", "icon", "fa-user-tie"),
                Map.of("id", "E-Commerce", "name", "E-Commerce & Retail", "icon", "fa-bag-shopping"),
                Map.of("id", "FinTech", "name", "FinTech & Banking", "icon", "fa-vault"),
                Map.of("id", "Restaurant", "name", "Dining & Hospitality", "icon", "fa-utensils"),
                Map.of("id", "Fitness", "name", "Fitness & Wellness", "icon", "fa-dumbbell"),
                Map.of("id", "Real Estate", "name", "Real Estate & Architecture", "icon", "fa-city")
        ));
    }
}
