package com.websitebuilder.controller;

import com.websitebuilder.model.UserWebsite;
import com.websitebuilder.service.ExportService;
import com.websitebuilder.service.WebsiteService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/websites")
@CrossOrigin(origins = "*")
public class WebsiteController {

    private final WebsiteService websiteService;
    private final ExportService exportService;

    public WebsiteController(WebsiteService websiteService, ExportService exportService) {
        this.websiteService = websiteService;
        this.exportService = exportService;
    }

    @GetMapping
    public ResponseEntity<List<UserWebsite>> getAllWebsites() {
        return ResponseEntity.ok(websiteService.getAllWebsites());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserWebsite> getWebsiteById(@PathVariable Long id) {
        return websiteService.getWebsiteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserWebsite> createWebsite(@RequestBody UserWebsite website) {
        UserWebsite saved = websiteService.saveWebsite(website);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserWebsite> updateWebsite(@PathVariable Long id, @RequestBody UserWebsite website) {
        try {
            UserWebsite updated = websiteService.updateWebsite(id, website);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWebsite(@PathVariable Long id) {
        websiteService.deleteWebsite(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/preview")
    public ResponseEntity<String> previewUserWebsite(@PathVariable Long id) {
        return websiteService.getWebsiteById(id)
                .map(site -> {
                    String html = exportService.generateHtmlPage(site.getTitle(), site.getThemeConfigJson(), site.getContentJson());
                    return ResponseEntity.ok()
                            .contentType(MediaType.TEXT_HTML)
                            .body(html);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/export")
    public ResponseEntity<byte[]> exportWebsiteZip(@PathVariable Long id) {
        return websiteService.getWebsiteById(id)
                .map(site -> {
                    try {
                        byte[] zipBytes = exportService.generateWebsiteZip(site.getTitle(), site.getThemeConfigJson(), site.getContentJson());
                        String safeTitle = site.getTitle().replaceAll("[^a-zA-Z0-9-_]", "-").toLowerCase();
                        if (safeTitle.isBlank()) safeTitle = "website";

                        return ResponseEntity.ok()
                                .contentType(MediaType.parseMediaType("application/zip"))
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + safeTitle + "-export.zip\"")
                                .body(zipBytes);
                    } catch (IOException e) {
                        return ResponseEntity.internalServerError().<byte[]>build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
