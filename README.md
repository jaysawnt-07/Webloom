# WebForge - Java Website Builder & Interactive Template Studio

An end-to-end, production-grade **Website Builder** built with **Java (Spring Boot 3.2.5)**, featuring a full-featured **Website Templates Gallery** and a **Real-Time Visual Customization Studio**.

---

## 🎯 Project Overview & Your Role

Your role in this project is the **Website Template Section & Customizer**, which gives users the ability to:
1. **Browse & Filter Curated Website Templates** (Agency, Portfolio, SaaS, Bistro/Restaurant, E-Commerce, Fitness).
2. **Interactive Device Previews** (Instant Desktop, Tablet, and Mobile simulation modals).
3. **Full Visual Customization Studio (WYSIWYG)**:
   - Click-to-edit text directly on the page (`contenteditable`).
   - Add, remove, and reorder page sections (Hero, Features, Stats, Portfolio, Pricing, Testimonials, Contact, Footer).
   - Element inspector for button links, copy, and stock photo swapping.
   - 1-click theme color palettes and typography switcher.
4. **Save Drafts**: Persists custom websites to the embedded **H2 Database** via Spring Data JPA REST APIs.
5. **1-Click Export to Standalone ZIP**: Downloads a clean, responsive static website (`index.html`, `style.css`, `script.js`, `README.md`) ready to deploy on GitHub Pages, Netlify, or Vercel.

---

## 🏗️ Architecture & Technology Stack

- **Backend**: Java 17, Spring Boot 3.2.5
  - `spring-boot-starter-web`: RESTful API endpoints and static resource serving.
  - `spring-boot-starter-data-jpa`: Object-relational mapping and entity persistence.
  - `com.h2database:h2`: Zero-configuration embedded database.
  - `java.util.zip`: Dynamic in-memory website packaging & ZIP archive generation.
- **Frontend**:
  - Semantic HTML5, Tailwind CSS, FontAwesome 6, Google Fonts (Inter, Outfit, Playfair Display, Space Grotesk).
  - Modern vanilla JavaScript engine (zero heavy build setup needed for the frontend).
- **Embedded Server**: Apache Tomcat on `http://localhost:8080`.

---

## 🚀 Quick Start (How to Run)

### Method 1: One-Click Windows Batch Script
Simply double-click:
```cmd
run.cmd
```
*(This sets the JDK 17 path and executes `mvn spring-boot:run`)*

### Method 2: Command Line (Maven)
```cmd
mvn spring-boot:run
```

Once started, open your browser and visit:
- **Application Homepage**: [http://localhost:8080](http://localhost:8080)
- **Visual Studio**: [http://localhost:8080/editor.html?template=business-agency](http://localhost:8080/editor.html?template=business-agency)
- **H2 Database Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  - JDBC URL: `jdbc:h2:mem:websitebuilder`
  - User: `sa`
  - Password: *(leave blank)*

---

## 📁 Project Structure

```
templates/
├── pom.xml                               # Maven project dependencies and build configuration
├── run.cmd                               # Double-click launcher script
├── README.md                             # Documentation
└── src/
    └── main/
        ├── java/com/websitebuilder/
        │   ├── WebsiteBuilderApplication.java   # Spring Boot Main Entrypoint
        │   ├── model/
        │   │   ├── Template.java                # JPA Entity for Template metadata & sections
        │   │   └── UserWebsite.java             # JPA Entity for User-customized websites
        │   ├── repository/
        │   │   ├── TemplateRepository.java      # Spring Data JPA Repository for Templates
        │   │   └── UserWebsiteRepository.java   # Spring Data JPA Repository for User Websites
        │   ├── service/
        │   │   ├── TemplateService.java         # Seeds & serves 6 realistic starter templates
        │   │   ├── WebsiteService.java          # CRUD business logic for user websites
        │   │   └── ExportService.java           # Generates standalone HTML/CSS/JS and ZIPs
        │   └── controller/
        │       ├── TemplateController.java      # REST API for templates & category filters
        │       └── WebsiteController.java       # REST API for site saving & ZIP downloading
        └── resources/
            ├── application.properties           # Spring Boot & H2 DB configuration
            └── static/
                ├── index.html                   # Templates Gallery & Showcase Page
                ├── editor.html                  # Visual Template Customizer Studio
                ├── css/
                │   └── editor.css               # Studio and canvas styling
                └── js/
                    ├── app.js                   # Gallery filtering & preview modal logic
                    └── editor.js                # Drag/drop, WYSIWYG, and export studio engine
```

---

## 🌐 REST API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/templates` | Retrieve all templates (supports `?category=` and `?search=`) |
| `GET` | `/api/templates/{id}` | Get detailed schema and sections for a specific template |
| `GET` | `/api/templates/{id}/preview` | Render live standalone HTML of a template |
| `GET` | `/api/templates/categories` | Get category list with icons and counts |
| `GET` | `/api/websites` | List all user-saved customized websites |
| `GET` | `/api/websites/{id}` | Get a specific saved website draft |
| `POST` | `/api/websites` | Save a new custom website to the database |
| `PUT` | `/api/websites/{id}` | Update an existing customized website |
| `DELETE` | `/api/websites/{id}` | Delete a website draft |
| `GET` | `/api/websites/{id}/preview` | Preview user's custom website in full screen |
| `GET` | `/api/websites/{id}/export` | **Download complete static website ZIP file** |

---

## 🎨 Pre-Bundled Templates

1. **Apex Digital Agency** (`business-agency`): Corporate layout with metric counters, service cards, testimonials, and lead forms.
2. **Nova Creative Studio** (`portfolio-creative`): Dark aesthetic showcase for UI/UX designers, developers, and photographers.
3. **CloudPulse Platform** (`saas-launch`): Tech landing page with pricing tier comparison, feature breakdown, and quick trial CTAs.
4. **L'Aura Artisan Bistro** (`restaurant-bistro`): Culinary layout with signature dish highlights, chef philosophy, and reservation form.
5. **Luxe Minimal Store** (`ecommerce-minimal`): Minimalist storefront with product cards, customer guarantees, and email drops.
6. **PulseFit Athletic Club** (`fitness-gym`): Bold fitness layout with workout programs, trainer highlights, and free pass sign-ups.

---

## 💡 How Your Team Can Integrate With This

- **User Accounts / Auth**: Link `UserWebsite.userId` to your team's user authentication module.
- **Custom Domain Hosting**: Point the HTML generated by `ExportService` to a cloud bucket (AWS S3 / Google Cloud Storage) or your hosting server.
- **New Templates**: Add additional template configurations to `TemplateService.java` without modifying frontend code.
