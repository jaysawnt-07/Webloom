package com.websitebuilder.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ExportService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public byte[] generateWebsiteZip(String title, String themeJson, String contentJson) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            // 1. Generate index.html
            String htmlContent = generateHtmlPage(title, themeJson, contentJson);
            addZipEntry(zos, "index.html", htmlContent);

            // 2. Generate style.css
            String cssContent = generateCss(themeJson);
            addZipEntry(zos, "style.css", cssContent);

            // 3. Generate script.js
            String jsContent = generateJs();
            addZipEntry(zos, "script.js", jsContent);

            // 4. Generate README.md
            String readmeContent = generateReadme(title);
            addZipEntry(zos, "README.md", readmeContent);
        }
        return baos.toByteArray();
    }

    private void addZipEntry(ZipOutputStream zos, String filename, String content) throws IOException {
        ZipEntry entry = new ZipEntry(filename);
        zos.putNextEntry(entry);
        zos.write(content.getBytes(StandardCharsets.UTF_8));
        zos.closeEntry();
    }

    public String generateHtmlPage(String title, String themeJson, String contentJson) {
        String primaryColor = "#2563eb";
        String secondaryColor = "#0f172a";
        String fontFamily = "Inter";
        String buttonRadius = "8px";

        try {
            if (themeJson != null && !themeJson.isBlank()) {
                Map<String, Object> themeMap = objectMapper.readValue(themeJson, new TypeReference<>() {});
                if (themeMap.containsKey("primaryColor")) primaryColor = String.valueOf(themeMap.get("primaryColor"));
                if (themeMap.containsKey("secondaryColor")) secondaryColor = String.valueOf(themeMap.get("secondaryColor"));
                if (themeMap.containsKey("fontFamily")) fontFamily = String.valueOf(themeMap.get("fontFamily"));
                if (themeMap.containsKey("buttonRadius")) buttonRadius = String.valueOf(themeMap.get("buttonRadius"));
            }
        } catch (Exception ignored) {
        }

        StringBuilder sectionsHtml = new StringBuilder();
        try {
            if (contentJson != null && !contentJson.isBlank()) {
                List<Map<String, Object>> sections = objectMapper.readValue(contentJson, new TypeReference<>() {});
                for (Map<String, Object> sec : sections) {
                    sectionsHtml.append(renderSectionHtml(sec, primaryColor, buttonRadius));
                }
            }
        } catch (Exception e) {
            sectionsHtml.append("<div class='py-12 text-center text-red-500'>Error parsing sections: ")
                    .append(e.getMessage()).append("</div>");
        }

        return """
        <!DOCTYPE html>
        <html lang="en" class="scroll-smooth">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>%s</title>
            <link rel="preconnect" href="https://fonts.googleapis.com">
            <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
            <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&family=Outfit:wght@400;500;600;700;800&family=Playfair+Display:ital,wght@0,500;0,700;1,400&family=Space+Grotesk:wght@400;500;600;700&display=swap" rel="stylesheet">
            <script src="https://cdn.tailwindcss.com"></script>
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
            <link rel="stylesheet" href="style.css">
            <style>
                :root {
                    --primary-color: %s;
                    --secondary-color: %s;
                    --font-family: '%s', sans-serif;
                    --btn-radius: %s;
                }
                body {
                    font-family: var(--font-family);
                    color: #1e293b;
                    background-color: #ffffff;
                }
                .btn-custom {
                    background-color: var(--primary-color);
                    border-radius: var(--btn-radius);
                    color: #ffffff;
                    transition: all 0.2s ease-in-out;
                }
                .btn-custom:hover {
                    opacity: 0.9;
                    transform: translateY(-1px);
                    box-shadow: 0 4px 14px rgba(0,0,0,0.12);
                }
                .btn-outline-custom {
                    border: 2px solid var(--primary-color);
                    color: var(--primary-color);
                    border-radius: var(--btn-radius);
                    transition: all 0.2s ease-in-out;
                }
                .btn-outline-custom:hover {
                    background-color: var(--primary-color);
                    color: #ffffff;
                }
                .text-custom-primary {
                    color: var(--primary-color);
                }
                .bg-custom-primary {
                    background-color: var(--primary-color);
                }
            </style>
        </head>
        <body class="antialiased selection:bg-blue-500 selection:text-white">
            %s
            <script src="script.js"></script>
        </body>
        </html>
        """.formatted(title != null ? title : "My Custom Website",
                primaryColor, secondaryColor, fontFamily, buttonRadius, sectionsHtml.toString());
    }

    @SuppressWarnings("unchecked")
    private String renderSectionHtml(Map<String, Object> sec, String primaryColor, String btnRadius) {
        String type = String.valueOf(sec.getOrDefault("type", ""));
        StringBuilder sb = new StringBuilder();

        switch (type.toLowerCase()) {
            case "navbar" -> {
                String brand = String.valueOf(sec.getOrDefault("brand", "WebForge"));
                String ctaText = String.valueOf(sec.getOrDefault("ctaText", "Get Started"));
                String ctaLink = String.valueOf(sec.getOrDefault("ctaLink", "#contact"));
                List<String> links = (List<String>) sec.getOrDefault("links", List.of("Home", "Services", "About", "Contact"));

                sb.append("""
                <header class="sticky top-0 z-50 bg-white/90 backdrop-blur-md border-b border-slate-100">
                    <div class="max-w-7xl mx-auto px-6 h-20 flex items-center justify-between">
                        <a href="#" class="text-2xl font-bold tracking-tight text-slate-900 flex items-center gap-2">
                            <span class="w-3 h-3 rounded-full bg-custom-primary"></span>
                            %s
                        </a>
                        <nav class="hidden md:flex items-center space-x-8 text-sm font-medium text-slate-600">
                """.formatted(brand));

                for (String link : links) {
                    sb.append("<a href='#").append(link.toLowerCase()).append("' class='hover:text-slate-900 transition'>")
                            .append(link).append("</a>\n");
                }

                sb.append("""
                        </nav>
                        <div class="hidden md:flex items-center">
                            <a href="%s" class="btn-custom px-5 py-2.5 text-sm font-medium inline-block">%s</a>
                        </div>
                        <button id="mobileMenuBtn" class="md:hidden text-slate-700 text-xl focus:outline-none">
                            <i class="fa-solid fa-bars"></i>
                        </button>
                    </div>
                    <div id="mobileMenu" class="hidden md:hidden px-6 pt-2 pb-6 space-y-3 bg-white border-b border-slate-100">
                """.formatted(ctaLink, ctaText));

                for (String link : links) {
                    sb.append("<a href='#").append(link.toLowerCase()).append("' class='block text-base font-medium text-slate-700 py-1'>")
                            .append(link).append("</a>\n");
                }

                sb.append("""
                        <a href="%s" class="btn-custom block text-center mt-3 px-5 py-2.5 text-sm font-medium">%s</a>
                    </div>
                </header>
                """.formatted(ctaLink, ctaText));
            }
            case "hero" -> {
                String heading = String.valueOf(sec.getOrDefault("heading", "Build Faster With Quality"));
                String subheading = String.valueOf(sec.getOrDefault("subheading", "Designed for conversion and speed."));
                String pBtn = String.valueOf(sec.getOrDefault("primaryBtnText", "Get Started"));
                String pLink = String.valueOf(sec.getOrDefault("primaryBtnLink", "#contact"));
                String sBtn = String.valueOf(sec.getOrDefault("secondaryBtnText", "Learn More"));
                String sLink = String.valueOf(sec.getOrDefault("secondaryBtnLink", "#services"));
                String img = String.valueOf(sec.getOrDefault("imageUrl", "https://images.unsplash.com/photo-1522071820081-009f0129c71c?auto=format&fit=crop&w=1000&q=80"));

                sb.append("""
                <section class="py-20 lg:py-32 overflow-hidden bg-gradient-to-b from-slate-50 to-white">
                    <div class="max-w-7xl mx-auto px-6 grid grid-cols-1 lg:grid-cols-2 gap-16 items-center">
                        <div class="space-y-8">
                            <h1 class="text-4xl sm:text-5xl lg:text-6xl font-extrabold text-slate-900 tracking-tight leading-[1.15]">
                                %s
                            </h1>
                            <p class="text-lg sm:text-xl text-slate-600 leading-relaxed max-w-xl">
                                %s
                            </p>
                            <div class="flex flex-wrap items-center gap-4">
                                <a href="%s" class="btn-custom px-7 py-3.5 text-base font-semibold shadow-lg shadow-blue-500/10">%s</a>
                                <a href="%s" class="btn-outline-custom px-7 py-3.5 text-base font-semibold">%s</a>
                            </div>
                        </div>
                        <div class="relative">
                            <div class="absolute -inset-4 bg-gradient-to-r from-blue-500 to-indigo-500 rounded-2xl opacity-10 blur-xl"></div>
                            <img src="%s" alt="Hero Banner" class="relative rounded-2xl shadow-2xl object-cover w-full h-[440px] transform hover:scale-[1.01] transition duration-500" />
                        </div>
                    </div>
                </section>
                """.formatted(heading, subheading, pLink, pBtn, sLink, sBtn, img));
            }
            case "stats" -> {
                List<Map<String, String>> items = (List<Map<String, String>>) sec.getOrDefault("items", List.of());
                sb.append("""
                <section class="py-14 bg-slate-900 text-white">
                    <div class="max-w-7xl mx-auto px-6 grid grid-cols-2 md:grid-cols-4 gap-8 text-center">
                """);
                for (Map<String, String> item : items) {
                    sb.append("""
                        <div class="space-y-2">
                            <div class="text-4xl sm:text-5xl font-black text-white tracking-tight">%s</div>
                            <div class="text-sm font-medium text-slate-400">%s</div>
                        </div>
                    """.formatted(item.getOrDefault("number", "100+"), item.getOrDefault("label", "Metric")));
                }
                sb.append("""
                    </div>
                </section>
                """);
            }
            case "services" -> {
                String title = String.valueOf(sec.getOrDefault("title", "Our Offerings"));
                String subtitle = String.valueOf(sec.getOrDefault("subtitle", "Tailored solutions built for real results."));
                List<Map<String, String>> items = (List<Map<String, String>>) sec.getOrDefault("items", List.of());

                sb.append("""
                <section id="services" class="py-24 bg-white">
                    <div class="max-w-7xl mx-auto px-6">
                        <div class="text-center max-w-2xl mx-auto mb-16 space-y-4">
                            <h2 class="text-3xl sm:text-4xl font-bold text-slate-900 tracking-tight">%s</h2>
                            <p class="text-base sm:text-lg text-slate-600">%s</p>
                        </div>
                        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
                """.formatted(title, subtitle));

                for (Map<String, String> item : items) {
                    String icon = item.getOrDefault("icon", "fa-star");
                    sb.append("""
                            <div class="p-8 rounded-2xl border border-slate-100 bg-slate-50/50 hover:bg-white hover:shadow-xl hover:border-slate-200 transition duration-300 group space-y-4">
                                <div class="w-14 h-14 rounded-xl bg-custom-primary/10 text-custom-primary flex items-center justify-center text-2xl group-hover:scale-110 transition">
                                    <i class="fa-solid %s"></i>
                                </div>
                                <h3 class="text-xl font-bold text-slate-900">%s</h3>
                                <p class="text-slate-600 text-sm leading-relaxed">%s</p>
                            </div>
                    """.formatted(icon, item.getOrDefault("title", "Feature"), item.getOrDefault("desc", "Description")));
                }

                sb.append("""
                        </div>
                    </div>
                </section>
                """);
            }
            case "portfolio" -> {
                String title = String.valueOf(sec.getOrDefault("title", "Selected Works"));
                String subtitle = String.valueOf(sec.getOrDefault("subtitle", "Recent creative projects."));
                List<Map<String, String>> items = (List<Map<String, String>>) sec.getOrDefault("items", List.of());

                sb.append("""
                <section id="portfolio" class="py-24 bg-slate-50">
                    <div class="max-w-7xl mx-auto px-6">
                        <div class="text-center max-w-2xl mx-auto mb-16 space-y-4">
                            <h2 class="text-3xl sm:text-4xl font-bold text-slate-900 tracking-tight">%s</h2>
                            <p class="text-base sm:text-lg text-slate-600">%s</p>
                        </div>
                        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
                """.formatted(title, subtitle));

                for (Map<String, String> item : items) {
                    sb.append("""
                            <div class="group rounded-2xl overflow-hidden bg-white shadow-sm hover:shadow-xl transition duration-300">
                                <div class="overflow-hidden h-60">
                                    <img src="%s" alt="Project" class="w-full h-full object-cover group-hover:scale-105 transition duration-500" />
                                </div>
                                <div class="p-6 space-y-2">
                                    <span class="text-xs font-semibold tracking-wider uppercase text-custom-primary">%s</span>
                                    <h4 class="text-lg font-bold text-slate-900">%s</h4>
                                </div>
                            </div>
                    """.formatted(
                            item.getOrDefault("image", "https://images.unsplash.com/photo-1460925895917-afdab827c52f?auto=format&fit=crop&w=600&q=80"),
                            item.getOrDefault("category", "Design"),
                            item.getOrDefault("title", "Project Title")
                    ));
                }

                sb.append("""
                        </div>
                    </div>
                </section>
                """);
            }
            case "pricing" -> {
                String title = String.valueOf(sec.getOrDefault("title", "Pricing Plans"));
                String subtitle = String.valueOf(sec.getOrDefault("subtitle", "Transparent options for every stage."));
                List<Map<String, Object>> items = (List<Map<String, Object>>) sec.getOrDefault("items", List.of());

                sb.append("""
                <section id="pricing" class="py-24 bg-white">
                    <div class="max-w-7xl mx-auto px-6">
                        <div class="text-center max-w-2xl mx-auto mb-16 space-y-4">
                            <h2 class="text-3xl sm:text-4xl font-bold text-slate-900 tracking-tight">%s</h2>
                            <p class="text-base sm:text-lg text-slate-600">%s</p>
                        </div>
                        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
                """.formatted(title, subtitle));

                for (Map<String, Object> item : items) {
                    List<String> features = (List<String>) item.getOrDefault("features", List.of());
                    sb.append("""
                            <div class="p-8 rounded-2xl border border-slate-200 bg-white hover:border-slate-300 hover:shadow-xl transition flex flex-col justify-between">
                                <div class="space-y-6">
                                    <div>
                                        <h3 class="text-xl font-bold text-slate-900">%s</h3>
                                        <p class="text-sm text-slate-500 mt-1">%s</p>
                                    </div>
                                    <div class="flex items-baseline gap-1">
                                        <span class="text-4xl font-extrabold text-slate-900">%s</span>
                                        <span class="text-sm font-medium text-slate-500">%s</span>
                                    </div>
                                    <ul class="space-y-3 text-sm text-slate-600 pt-4 border-t border-slate-100">
                    """.formatted(
                            item.getOrDefault("plan", "Tier"),
                            item.getOrDefault("desc", "Description"),
                            item.getOrDefault("price", "$29"),
                            item.getOrDefault("period", "/mo")
                    ));

                    for (String f : features) {
                        sb.append("<li class='flex items-center gap-2.5'><i class='fa-solid fa-check text-custom-primary text-xs'></i>").append(f).append("</li>\n");
                    }

                    sb.append("""
                                    </ul>
                                </div>
                                <div class="mt-8">
                                    <a href="#contact" class="btn-custom block text-center py-3 text-sm font-semibold w-full">%s</a>
                                </div>
                            </div>
                    """.formatted(item.getOrDefault("btnText", "Choose Plan")));
                }

                sb.append("""
                        </div>
                    </div>
                </section>
                """);
            }
            case "testimonials" -> {
                String title = String.valueOf(sec.getOrDefault("title", "What Clients Say"));
                String subtitle = String.valueOf(sec.getOrDefault("subtitle", "Stories from founders who achieved success with us."));
                List<Map<String, String>> items = (List<Map<String, String>>) sec.getOrDefault("items", List.of());

                sb.append("""
                <section class="py-24 bg-slate-50">
                    <div class="max-w-7xl mx-auto px-6">
                        <div class="text-center max-w-2xl mx-auto mb-16 space-y-4">
                            <h2 class="text-3xl sm:text-4xl font-bold text-slate-900 tracking-tight">%s</h2>
                            <p class="text-base sm:text-lg text-slate-600">%s</p>
                        </div>
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-8 max-w-5xl mx-auto">
                """.formatted(title, subtitle));

                for (Map<String, String> item : items) {
                    sb.append("""
                            <div class="p-8 rounded-2xl bg-white shadow-sm border border-slate-100 space-y-6">
                                <div class="flex text-amber-400 gap-1 text-sm">
                                    <i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i>
                                    <i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i>
                                    <i class="fa-solid fa-star"></i>
                                </div>
                                <p class="text-slate-700 italic leading-relaxed text-base">"%s"</p>
                                <div class="flex items-center gap-4 pt-2">
                                    <img src="%s" alt="Avatar" class="w-12 h-12 rounded-full object-cover" />
                                    <div>
                                        <div class="font-bold text-slate-900">%s</div>
                                        <div class="text-xs text-slate-500">%s</div>
                                    </div>
                                </div>
                            </div>
                    """.formatted(
                            item.getOrDefault("quote", "Outstanding work!"),
                            item.getOrDefault("avatar", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=150&q=80"),
                            item.getOrDefault("author", "Client Name"),
                            item.getOrDefault("role", "Executive")
                    ));
                }

                sb.append("""
                        </div>
                    </div>
                </section>
                """);
            }
            case "contact" -> {
                String title = String.valueOf(sec.getOrDefault("title", "Get In Touch"));
                String subtitle = String.valueOf(sec.getOrDefault("subtitle", "We would love to hear from you."));
                String btn = String.valueOf(sec.getOrDefault("buttonText", "Send Message"));
                String placeholder = String.valueOf(sec.getOrDefault("emailPlaceholder", "your@email.com"));

                sb.append("""
                <section id="contact" class="py-24 bg-white">
                    <div class="max-w-3xl mx-auto px-6 text-center space-y-8">
                        <div class="space-y-4">
                            <h2 class="text-3xl sm:text-4xl font-bold text-slate-900 tracking-tight">%s</h2>
                            <p class="text-base sm:text-lg text-slate-600">%s</p>
                        </div>
                        <form id="contactForm" class="space-y-4 max-w-xl mx-auto text-left bg-slate-50 p-8 rounded-2xl border border-slate-100">
                            <div>
                                <label class="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-2">Your Name</label>
                                <input type="text" required placeholder="John Doe" class="w-full px-4 py-3 rounded-lg border border-slate-200 focus:outline-none focus:ring-2 focus:ring-blue-500 text-sm" />
                            </div>
                            <div>
                                <label class="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-2">Email Address</label>
                                <input type="email" required placeholder="%s" class="w-full px-4 py-3 rounded-lg border border-slate-200 focus:outline-none focus:ring-2 focus:ring-blue-500 text-sm" />
                            </div>
                            <div>
                                <label class="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-2">Message</label>
                                <textarea rows="4" required placeholder="Tell us about your project..." class="w-full px-4 py-3 rounded-lg border border-slate-200 focus:outline-none focus:ring-2 focus:ring-blue-500 text-sm"></textarea>
                            </div>
                            <button type="submit" class="btn-custom w-full py-3.5 text-sm font-semibold tracking-wide">%s</button>
                        </form>
                    </div>
                </section>
                """.formatted(title, subtitle, placeholder, btn));
            }
            case "footer" -> {
                String copyright = String.valueOf(sec.getOrDefault("copyright", "© 2026 WebForge. All rights reserved."));
                List<String> links = (List<String>) sec.getOrDefault("links", List.of("Privacy", "Terms", "Support"));

                sb.append("""
                <footer class="py-12 bg-slate-950 text-slate-400 text-sm border-t border-slate-800">
                    <div class="max-w-7xl mx-auto px-6 flex flex-col sm:flex-row items-center justify-between gap-6">
                        <p class="text-center sm:text-left">%s</p>
                        <div class="flex items-center space-x-6">
                """.formatted(copyright));

                for (String link : links) {
                    sb.append("<a href='#' class='hover:text-white transition'>").append(link).append("</a>\n");
                }

                sb.append("""
                        </div>
                    </div>
                </footer>
                """);
            }
            default -> {
                sb.append("<div class='py-8 text-center text-slate-400'>[Section: ").append(type).append("]</div>");
            }
        }

        return sb.toString();
    }

    private String generateCss(String themeJson) {
        return """
        /* Exported Website Stylesheet */
        html {
            scroll-behavior: smooth;
        }
        ::selection {
            background: #3b82f6;
            color: #ffffff;
        }
        /* Custom responsive enhancements */
        @media (max-width: 640px) {
            h1 {
                font-size: 2.25rem !important;
                line-height: 1.2 !important;
            }
        }
        """;
    }

    private String generateJs() {
        return """
        // Interactive behaviors for exported website
        document.addEventListener('DOMContentLoaded', () => {
            // Mobile Menu Toggle
            const mobileBtn = document.getElementById('mobileMenuBtn');
            const mobileMenu = document.getElementById('mobileMenu');
            if (mobileBtn && mobileMenu) {
                mobileBtn.addEventListener('click', () => {
                    mobileMenu.classList.toggle('hidden');
                });
            }

            // Contact Form Submit Handler
            const contactForm = document.getElementById('contactForm');
            if (contactForm) {
                contactForm.addEventListener('submit', (e) => {
                    e.preventDefault();
                    alert('Thank you! Your message has been received.');
                    contactForm.reset();
                });
            }
        });
        """;
    }

    private String generateReadme(String title) {
        return """
        # %s

        This website was built and exported using **WebForge - Java Website Builder & Template Studio**.

        ## Getting Started
        1. Double-click `index.html` to open it in any web browser.
        2. To publish online:
           - Drag and drop this folder onto [Netlify Drop](https://app.netlify.com/drop) or [Vercel](https://vercel.com).
           - Or push to GitHub and enable GitHub Pages.

        ## Files Included
        - `index.html`: Fully responsive semantic HTML5 markup.
        - `style.css`: Custom CSS styling and typography.
        - `script.js`: Interactive navigation and form submission script.

        Enjoy your new website!
        """.formatted(title != null ? title : "Custom Website");
    }
}
