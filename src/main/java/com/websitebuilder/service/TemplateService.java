package com.websitebuilder.service;

import com.websitebuilder.model.Template;
import com.websitebuilder.repository.TemplateRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TemplateService {

    private final TemplateRepository templateRepository;

    public TemplateService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @PostConstruct
    public void seedTemplates() {
        // Clear old seeds to ensure the updated industry-grade templates load cleanly
        templateRepository.deleteAll();

        List<Template> templates = new ArrayList<>();

        // 1. SaaS & AI Platform
        templates.add(new Template(
                "saas-ai",
                "Nexus AI — Autonomous Intelligence Platform",
                "SaaS",
                "Enterprise AI startup layout featuring bento-grid feature highlights, metric counters, pricing comparison tiers, and developer documentation CTA.",
                "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=800&q=80",
                "AI Featured",
                4.98,
                3820,
                "#6366f1",
                "#090d16",
                "Outfit",
                "10px",
                """
                [
                  {
                    "id": "sec-nav",
                    "type": "navbar",
                    "title": "Navigation Header",
                    "brand": "Nexus AI",
                    "links": ["Capabilities", "Bento Features", "Pricing", "Docs"],
                    "ctaText": "Start Free Trial",
                    "ctaLink": "#pricing"
                  },
                  {
                    "id": "sec-hero",
                    "type": "hero",
                    "title": "Hero Section",
                    "heading": "Autonomous Enterprise Intelligence at Scale",
                    "subheading": "Accelerate mission-critical workflows with generative agents that reason, self-correct, and integrate across your multi-cloud data layer.",
                    "primaryBtnText": "Deploy in 60 Seconds",
                    "primaryBtnLink": "#pricing",
                    "secondaryBtnText": "View Architecture",
                    "secondaryBtnLink": "#services",
                    "imageUrl": "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=1000&q=80"
                  },
                  {
                    "id": "sec-stats",
                    "type": "stats",
                    "title": "Enterprise Scale",
                    "items": [
                      {"number": "99.99%", "label": "Enterprise SLA"},
                      {"number": "1.2B+", "label": "Tokens Processed / Day"},
                      {"number": "<12ms", "label": "Median Latency"},
                      {"number": "500+", "label": "Fortune 2000 Orgs"}
                    ]
                  },
                  {
                    "id": "sec-services",
                    "type": "services",
                    "title": "Engineered For Uncompromising Precision",
                    "subtitle": "Modular architectural building blocks designed for high-concurrency production deployments.",
                    "items": [
                      {
                        "icon": "fa-brain",
                        "title": "Self-Optimizing LLM Mesh",
                        "desc": "Dynamically routes prompts to optimal specialized models to balance cost and accuracy."
                      },
                      {
                        "icon": "fa-shield-halved",
                        "title": "Zero-Knowledge Data Enclaves",
                        "desc": "Air-gapped enterprise memory guarantees your confidential IP is never trained upon."
                      },
                      {
                        "icon": "fa-bolt-lightning",
                        "title": "Sub-millisecond Vector DB",
                        "desc": "Ultra-dense semantic retrieval indexing billions of multi-modal records in real-time."
                      }
                    ]
                  },
                  {
                    "id": "sec-pricing",
                    "type": "pricing",
                    "title": "Predictable, Scalable Pricing",
                    "subtitle": "Transparent plans with zero unexpected compute surges.",
                    "items": [
                      {
                        "plan": "Developer",
                        "price": "$39",
                        "period": "/month",
                        "desc": "For engineering teams prototyping AI agents.",
                        "features": ["10M monthly model tokens", "5 concurrent agent threads", "Community Discord support", "Standard API rate limits"],
                        "btnText": "Start Dev Sandbox"
                      },
                      {
                        "plan": "Scale Team",
                        "price": "$129",
                        "period": "/month",
                        "desc": "For growth companies with active user traffic.",
                        "features": ["50M monthly model tokens", "Unlimited agent threads", "24/7 dedicated engineering support", "SOC2 compliance attestation", "Custom fine-tuning endpoints"],
                        "btnText": "Deploy Scale Team"
                      },
                      {
                        "plan": "Enterprise Grid",
                        "price": "$499",
                        "period": "/month",
                        "desc": "Custom infrastructure with dedicated GPU nodes.",
                        "features": ["Dedicated inference clusters", "Custom on-premise VPC peering", "99.99% uptime guarantee", "Named technical architect"],
                        "btnText": "Talk to Solutions Architect"
                      }
                    ]
                  },
                  {
                    "id": "sec-testimonials",
                    "type": "testimonials",
                    "title": "Trusted by Engineering Leaders",
                    "subtitle": "Here is why leading CTOs choose Nexus AI for production workloads.",
                    "items": [
                      {
                        "quote": "Nexus reduced our inference pipeline costs by 68% while delivering 3x higher accuracy on complex reasoning tasks.",
                        "author": "Elena Rostova",
                        "role": "VP of Engineering, DataCore",
                        "avatar": "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=150&q=80"
                      },
                      {
                        "quote": "The zero-knowledge privacy guarantees made enterprise compliance effortless for our regulated banking partners.",
                        "author": "Marcus Sterling",
                        "role": "Chief Information Officer, VaultPay",
                        "avatar": "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80"
                      }
                    ]
                  },
                  {
                    "id": "sec-contact",
                    "type": "contact",
                    "title": "Ready to Modernize Your AI Infrastructure?",
                    "subtitle": "Request a dedicated pilot sandbox with $500 in complimentary API credits.",
                    "buttonText": "Request Enterprise Access",
                    "emailPlaceholder": "work.email@enterprise.com"
                  },
                  {
                    "id": "sec-footer",
                    "type": "footer",
                    "title": "Footer",
                    "copyright": "© 2026 Nexus AI Systems Inc. Built with Webloom.",
                    "links": ["Status Monitor", "Security & SOC2", "Developer Docs", "Privacy"]
                  }
                ]
                """
        ));

        // 2. Creative Agency & Corporate
        templates.add(new Template(
                "agency-modern",
                "Verve — Digital Architecture & Brand Studio",
                "Business",
                "High-impact agency layout tailored for design studios, consulting firms, and innovative corporate brands.",
                "https://images.unsplash.com/photo-1460925895917-afdab827c52f?auto=format&fit=crop&w=800&q=80",
                "Best Seller",
                4.95,
                4100,
                "#2563eb",
                "#0f172a",
                "Inter",
                "8px",
                """
                [
                  {
                    "id": "sec-nav",
                    "type": "navbar",
                    "title": "Navigation Header",
                    "brand": "Verve Studio",
                    "links": ["Case Studies", "Services", "About", "Contact"],
                    "ctaText": "Start Project",
                    "ctaLink": "#contact"
                  },
                  {
                    "id": "sec-hero",
                    "type": "hero",
                    "title": "Hero Section",
                    "heading": "We Architect Transformative Digital Identities & Software",
                    "subheading": "Award-winning strategy, bespoke UI/UX design systems, and robust engineering for companies shaping the future.",
                    "primaryBtnText": "View Our Portfolio",
                    "primaryBtnLink": "#portfolio",
                    "secondaryBtnText": "Book Consultation",
                    "secondaryBtnLink": "#contact",
                    "imageUrl": "https://images.unsplash.com/photo-1522071820081-009f0129c71c?auto=format&fit=crop&w=1000&q=80"
                  },
                  {
                    "id": "sec-stats",
                    "type": "stats",
                    "title": "Track Record",
                    "items": [
                      {"number": "350+", "label": "Global Launches"},
                      {"number": "$2.4B+", "label": "Client Valuations"},
                      {"number": "28", "label": "Awwwards & D&AD"},
                      {"number": "100%", "label": "On-Time Delivery"}
                    ]
                  },
                  {
                    "id": "sec-portfolio",
                    "type": "portfolio",
                    "title": "Signature Case Studies",
                    "subtitle": "Recent digital products crafted with precision and intention.",
                    "items": [
                      {
                        "title": "Aura Autonomous EV Dashboard",
                        "category": "Automotive UI/UX",
                        "image": "https://images.unsplash.com/photo-1551288049-bebda4e38f71?auto=format&fit=crop&w=600&q=80"
                      },
                      {
                        "title": "Monolith Cloud Asset Management",
                        "category": "FinTech Platform",
                        "image": "https://images.unsplash.com/photo-1460925895917-afdab827c52f?auto=format&fit=crop&w=600&q=80"
                      },
                      {
                        "title": "Kinetic Spatial Architecture",
                        "category": "Brand Identity & 3D Web",
                        "image": "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&w=600&q=80"
                      }
                    ]
                  },
                  {
                    "id": "sec-services",
                    "type": "services",
                    "title": "Full-Lifecycle Agency Capabilities",
                    "subtitle": "From initial napkin concept to global scalable deployment.",
                    "items": [
                      {
                        "icon": "fa-compass-drafting",
                        "title": "Design Systems & Prototyping",
                        "desc": "Living, accessible token-based component libraries built for design-to-code speed."
                      },
                      {
                        "icon": "fa-code",
                        "title": "Modern Cloud Engineering",
                        "desc": "Resilient backend microservices and headless frontend applications engineered to scale."
                      },
                      {
                        "icon": "fa-chart-pie",
                        "title": "Conversion Rate Optimization",
                        "desc": "Data-informed product funnels designed to maximize recurring customer lifetime value."
                      }
                    ]
                  },
                  {
                    "id": "sec-contact",
                    "type": "contact",
                    "title": "Have an Ambitious Project in Mind?",
                    "subtitle": "Tell us about your timeline and vision. We will return with a detailed roadmap.",
                    "buttonText": "Schedule Discovery Session",
                    "emailPlaceholder": "founder@brand.com"
                  },
                  {
                    "id": "sec-footer",
                    "type": "footer",
                    "title": "Footer",
                    "copyright": "© 2026 Verve Digital Architecture Ltd. Handcrafted in Webloom.",
                    "links": ["Case Studies", "Company Ethos", "Careers", "Legal Notice"]
                  }
                ]
                """
        ));

        // 3. Executive Portfolio & Resume
        templates.add(new Template(
                "portfolio-pro",
                "Aria Vance — Staff Product Architect",
                "Portfolio",
                "Minimalist, typography-forward personal portfolio for staff engineers, tech leads, and product designers.",
                "https://images.unsplash.com/photo-1507238691740-187a5b1d37b8?auto=format&fit=crop&w=800&q=80",
                "Trending",
                4.99,
                2950,
                "#d946ef",
                "#09090b",
                "Outfit",
                "9999px",
                """
                [
                  {
                    "id": "sec-nav",
                    "type": "navbar",
                    "title": "Navigation Header",
                    "brand": "Aria Vance",
                    "links": ["Selected Works", "Philosophy", "Writing", "Contact"],
                    "ctaText": "Let's Connect",
                    "ctaLink": "#contact"
                  },
                  {
                    "id": "sec-hero",
                    "type": "hero",
                    "title": "Hero Section",
                    "heading": "Designing Systems at the Intersection of Art & Scalable Code",
                    "subheading": "Staff Product Designer & Systems Engineer. Previously leading core design infrastructure at Figma, Stripe, and Apple.",
                    "primaryBtnText": "Explore Case Studies",
                    "primaryBtnLink": "#portfolio",
                    "secondaryBtnText": "Read Architecture Notes",
                    "secondaryBtnLink": "#services",
                    "imageUrl": "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=1000&q=80"
                  },
                  {
                    "id": "sec-portfolio",
                    "type": "portfolio",
                    "title": "Selected Works & Systems",
                    "subtitle": "A retrospective of key engineering systems and product experiences.",
                    "items": [
                      {
                        "title": "Prism Multi-Token Design Engine",
                        "category": "Open Source Tooling",
                        "image": "https://images.unsplash.com/photo-1551288049-bebda4e38f71?auto=format&fit=crop&w=600&q=80"
                      },
                      {
                        "title": "Spatial Audio OS Interface",
                        "category": "Hardware & Embedded Systems",
                        "image": "https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?auto=format&fit=crop&w=600&q=80"
                      },
                      {
                        "title": "Carbon Accounting Real-Time Dashboard",
                        "category": "Enterprise Data Architecture",
                        "image": "https://images.unsplash.com/photo-1460925895917-afdab827c52f?auto=format&fit=crop&w=600&q=80"
                      }
                    ]
                  },
                  {
                    "id": "sec-contact",
                    "type": "contact",
                    "title": "Initiate a Strategic Dialogue",
                    "subtitle": "Available for select advisory roles, keynotes, and design system consultations.",
                    "buttonText": "Send Direct Inquiry",
                    "emailPlaceholder": "your.email@domain.com"
                  },
                  {
                    "id": "sec-footer",
                    "type": "footer",
                    "title": "Footer",
                    "copyright": "© 2026 Aria Vance. Hosted with Webloom.",
                    "links": ["Substack", "GitHub", "LinkedIn", "X / Twitter"]
                  }
                ]
                """
        ));

        // 4. FinTech & Digital Wealth
        templates.add(new Template(
                "fintech-vault",
                "Strata — Next-Gen Digital Banking & Treasury",
                "FinTech",
                "Institutional-grade financial technology layout with high-security assurances, real-time yield cards, and compliance badges.",
                "https://images.unsplash.com/photo-1559526324-4b87b5e36e44?auto=format&fit=crop&w=800&q=80",
                "Enterprise",
                4.92,
                1850,
                "#0ea5e9",
                "#0a192f",
                "Inter",
                "12px",
                """
                [
                  {
                    "id": "sec-nav",
                    "type": "navbar",
                    "title": "Navigation Header",
                    "brand": "Strata Treasury",
                    "links": ["Global Accounts", "Yield & Hedging", "Security", "Contact"],
                    "ctaText": "Open Account",
                    "ctaLink": "#contact"
                  },
                  {
                    "id": "sec-hero",
                    "type": "hero",
                    "title": "Hero Section",
                    "heading": "Unified Global Treasury & Multi-Currency Liquidity",
                    "subheading": "Move millions seamlessly across 45+ currencies with zero wire delay and automated high-yield overnight treasury sweeps.",
                    "primaryBtnText": "Open Corporate Account",
                    "primaryBtnLink": "#contact",
                    "secondaryBtnText": "Inspect Security Standard",
                    "secondaryBtnLink": "#services",
                    "imageUrl": "https://images.unsplash.com/photo-1559526324-4b87b5e36e44?auto=format&fit=crop&w=1000&q=80"
                  },
                  {
                    "id": "sec-stats",
                    "type": "stats",
                    "title": "Global Metrics",
                    "items": [
                      {"number": "$18B+", "label": "Annual Volume"},
                      {"number": "45+", "label": "Supported Currencies"},
                      {"number": "$250M", "label": "FDIC Insurance Coverage"},
                      {"number": "0 bps", "label": "Hidden FX Markup"}
                    ]
                  },
                  {
                    "id": "sec-services",
                    "type": "services",
                    "title": "Institutional Financial Infrastructure",
                    "subtitle": "Built alongside tier-one central banks and regulated clearinghouses.",
                    "items": [
                      {
                        "icon": "fa-vault",
                        "title": "Multi-Entity Sweep Accounts",
                        "desc": "Automate cash distribution across subsidiaries to maximize interest yield and minimize idle float."
                      },
                      {
                        "icon": "fa-credit-card",
                        "title": "Corporate Virtual Credit",
                        "desc": "Issue unlimited virtual corporate cards with custom spend rules, approvals, and instant receipt OCR."
                      },
                      {
                        "icon": "fa-lock",
                        "title": "Hardware Security Modules",
                        "desc": "Multi-signature quorum approvals backed by FIPS 140-2 Level 3 cryptographic hardware."
                      }
                    ]
                  },
                  {
                    "id": "sec-contact",
                    "type": "contact",
                    "title": "Scale Your Financial Operations",
                    "subtitle": "Meet with a dedicated treasury director to onboard your corporation.",
                    "buttonText": "Schedule VIP Onboarding",
                    "emailPlaceholder": "cfo@corporation.com"
                  },
                  {
                    "id": "sec-footer",
                    "type": "footer",
                    "title": "Footer",
                    "copyright": "© 2026 Strata Treasury Inc. Member FDIC. Powered by Webloom.",
                    "links": ["Regulatory Disclosures", "Privacy Policy", "SOC2 Compliance", "API Docs"]
                  }
                ]
                """
        ));

        // 5. Luxury E-Commerce & Concept Store
        templates.add(new Template(
                "ecommerce-luxe",
                "Aura — Minimalist Concept Store",
                "E-Commerce",
                "High-end retail storefront designed for sustainable fashion houses, artisan luxury goods, and premium lifestyle drops.",
                "https://images.unsplash.com/photo-1441986300917-64674bd600d8?auto=format&fit=crop&w=800&q=80",
                "Curated",
                4.94,
                3200,
                "#10b981",
                "#18181b",
                "Inter",
                "4px",
                """
                [
                  {
                    "id": "sec-nav",
                    "type": "navbar",
                    "title": "Navigation Header",
                    "brand": "AURA STORE",
                    "links": ["Lookbook", "Capsule Drops", "Materials", "About"],
                    "ctaText": "Cart (0)",
                    "ctaLink": "#portfolio"
                  },
                  {
                    "id": "sec-hero",
                    "type": "hero",
                    "title": "Hero Section",
                    "heading": "Architectural Silhouettes. Ethical Italian Craftsmanship.",
                    "subheading": "Carefully considered modern apparel designed in Milan. Made exclusively from certified regenerative organic textiles.",
                    "primaryBtnText": "Shop Collection 2026",
                    "primaryBtnLink": "#portfolio",
                    "secondaryBtnText": "Our Sustainability Audit",
                    "secondaryBtnLink": "#services",
                    "imageUrl": "https://images.unsplash.com/photo-1490481651871-ab68de25d43d?auto=format&fit=crop&w=1000&q=80"
                  },
                  {
                    "id": "sec-portfolio",
                    "type": "portfolio",
                    "title": "New Season Arrivals",
                    "subtitle": "Numbered editions tailored with singular craftsmanship.",
                    "items": [
                      {
                        "title": "Oversized Merino Coat - $480",
                        "category": "Limited Outerwear",
                        "image": "https://images.unsplash.com/photo-1598033129183-c4f50c736f10?auto=format&fit=crop&w=600&q=80"
                      },
                      {
                        "title": "Vachetta Leather Tote - $360",
                        "category": "Artisan Leather",
                        "image": "https://images.unsplash.com/photo-1548036328-c9fa89d128fa?auto=format&fit=crop&w=600&q=80"
                      },
                      {
                        "title": "Titanium Chronograph - $650",
                        "category": "Precision Watchmaking",
                        "image": "https://images.unsplash.com/photo-1524805444758-089113d48a6d?auto=format&fit=crop&w=600&q=80"
                      }
                    ]
                  },
                  {
                    "id": "sec-stats",
                    "type": "stats",
                    "title": "Our Manifesto",
                    "items": [
                      {"number": "100%", "label": "Traceable Fibers"},
                      {"number": "0%", "label": "Plastic Packaging"},
                      {"number": "14-Day", "label": "Complimentary Returns"},
                      {"number": "Lifetime", "label": "Repair Guarantee"}
                    ]
                  },
                  {
                    "id": "sec-contact",
                    "type": "contact",
                    "title": "Join The Private Collectors Salon",
                    "subtitle": "Receive invitations to private capsule drops and a 15% inaugural gift.",
                    "buttonText": "Request Invitation",
                    "emailPlaceholder": "collector@domain.com"
                  },
                  {
                    "id": "sec-footer",
                    "type": "footer",
                    "title": "Footer",
                    "copyright": "© 2026 Aura Concept Store. Worldwide Carbon-Neutral Delivery. Built with Webloom.",
                    "links": ["Lookbook", "Shipping & Duties", "Transparency", "Client Care"]
                  }
                ]
                """
        ));

        // 6. Fine Dining & Culinary Restaurant
        templates.add(new Template(
                "restaurant-gourmet",
                "Maison Lumière — Michelin-Star Tasting Room",
                "Restaurant",
                "Opulent culinary experience layout with seasonal tasting menus, sommelier cellar pairings, and reservation concierge.",
                "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=800&q=80",
                "Sensory",
                4.97,
                1620,
                "#f59e0b",
                "#1c1917",
                "Playfair Display",
                "6px",
                """
                [
                  {
                    "id": "sec-nav",
                    "type": "navbar",
                    "title": "Navigation Header",
                    "brand": "Maison Lumière",
                    "links": ["Culinary Journey", "Menu Dégustation", "Private Cellar", "Reserve"],
                    "ctaText": "Reserve Table",
                    "ctaLink": "#contact"
                  },
                  {
                    "id": "sec-hero",
                    "type": "hero",
                    "title": "Hero Section",
                    "heading": "Haute Gastronomy Reimagined Through Seasonal Poetry",
                    "subheading": "Two Michelin stars. A multi-sensory symphony of hyper-local French botanicals and rare coastal harvests.",
                    "primaryBtnText": "Reserve an Evening",
                    "primaryBtnLink": "#contact",
                    "secondaryBtnText": "View 12-Course Tasting Menu",
                    "secondaryBtnLink": "#services",
                    "imageUrl": "https://images.unsplash.com/photo-1544025162-d76694265947?auto=format&fit=crop&w=1000&q=80"
                  },
                  {
                    "id": "sec-services",
                    "type": "services",
                    "title": "Autumn 12-Course Highlights",
                    "subtitle": "Every course paired with rare biodynamic vintage cellars.",
                    "items": [
                      {
                        "icon": "fa-utensils",
                        "title": "Brittany Blue Lobster",
                        "desc": "Charred kelp beurre blanc, fermented mirabelle plum, yuzu pearls, and golden oscietra caviar."
                      },
                      {
                        "icon": "fa-wine-glass",
                        "title": "A5 Miyazaki Wagyu Ribcap",
                        "desc": "Smoked bone marrow emulsion, chanterelle mushrooms, black winter truffle reduction."
                      },
                      {
                        "icon": "fa-mug-hot",
                        "title": "Madagascar Vanilla & Saffron Soufflé",
                        "desc": "Warm Grand Marnier sabayon, salted hazelnut crisp, gold leaf infusion."
                      }
                    ]
                  },
                  {
                    "id": "sec-contact",
                    "type": "contact",
                    "title": "Table Reservations & Private Dinners",
                    "subtitle": "Bookings release on the first day of each calendar month at 10:00 AM.",
                    "buttonText": "Check Table Availability",
                    "emailPlaceholder": "guest.name@domain.com"
                  },
                  {
                    "id": "sec-footer",
                    "type": "footer",
                    "title": "Footer",
                    "copyright": "© 2026 Maison Lumière Gastronomie. All rights reserved. Created in Webloom.",
                    "links": ["Dress Code & Valet", "Cellar List", "Chef's Bio", "Press & Honors"]
                  }
                ]
                """
        ));

        // 7. High Performance Fitness Club
        templates.add(new Template(
                "fitness-pulse",
                "Kore — High Performance Athletic Club",
                "Fitness",
                "High-energy athletic layout featuring progressive strength protocols, trainer dossiers, and complimentary day passes.",
                "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?auto=format&fit=crop&w=800&q=80",
                "High Energy",
                4.89,
                2450,
                "#f43f5e",
                "#111827",
                "Space Grotesk",
                "8px",
                """
                [
                  {
                    "id": "sec-nav",
                    "type": "navbar",
                    "title": "Navigation Header",
                    "brand": "KORE CLUB",
                    "links": ["Disciplines", "Coaching Staff", "Memberships", "Pass"],
                    "ctaText": "Claim 3-Day Pass",
                    "ctaLink": "#contact"
                  },
                  {
                    "id": "sec-hero",
                    "type": "hero",
                    "title": "Hero Section",
                    "heading": "Peak Athletic Potential. Engineered With Scientific Precision.",
                    "subheading": "Olympic-grade biomechanical training, contrast therapy recovery suites, and private elite coaching designed to push human thresholds.",
                    "primaryBtnText": "Claim Complimentary Pass",
                    "primaryBtnLink": "#contact",
                    "secondaryBtnText": "Explore Training Classes",
                    "secondaryBtnLink": "#services",
                    "imageUrl": "https://images.unsplash.com/photo-1517838277536-f5f99be501cd?auto=format&fit=crop&w=1000&q=80"
                  },
                  {
                    "id": "sec-services",
                    "type": "services",
                    "title": "Performance Disciplines",
                    "subtitle": "Rooted in sports science, biometric feedback, and progressive load.",
                    "items": [
                      {
                        "icon": "fa-dumbbell",
                        "title": "Hypertrophy & Neuromuscular Load",
                        "desc": "Elite barbell cages, force-plate velocity tracking, and personalized coaching."
                      },
                      {
                        "icon": "fa-heart-pulse",
                        "title": "Metabolic Conditioning & VO2 Max",
                        "desc": "High-intensity anaerobic intervals monitored with real-time biometric telemetry."
                      },
                      {
                        "icon": "fa-spa",
                        "title": "Contrast Therapy & Recovery",
                        "desc": "Full-spectrum infrared saunas, 48°F cold plunge tanks, and percussion therapy."
                      }
                    ]
                  },
                  {
                    "id": "sec-contact",
                    "type": "contact",
                    "title": "Begin Your Physical Evolution",
                    "subtitle": "Receive a 3-day access trial, InBody scan, and initial coach movement screen.",
                    "buttonText": "Activate Free Pass",
                    "emailPlaceholder": "athlete@domain.com"
                  },
                  {
                    "id": "sec-footer",
                    "type": "footer",
                    "title": "Footer",
                    "copyright": "© 2026 Kore Athletic Club. 24/7 Access. Powered by Webloom.",
                    "links": ["Daily Schedule", "Facility Specs", "Personal Coaches", "Member Portal"]
                  }
                ]
                """
        ));

        // 8. Luxury Real Estate & Architectural Estates
        templates.add(new Template(
                "realestate-luxe",
                "Solarium — Architectural Residences & Estates",
                "Real Estate",
                "Ultra-luxury architectural portfolio for estate brokers, modern architects, and waterfront residential developers.",
                "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?auto=format&fit=crop&w=800&q=80",
                "New Release",
                4.96,
                1120,
                "#3b82f6",
                "#0f172a",
                "Outfit",
                "6px",
                """
                [
                  {
                    "id": "sec-nav",
                    "type": "navbar",
                    "title": "Navigation Header",
                    "brand": "Solarium Estates",
                    "links": ["Properties", "Developments", "Private Sales", "Contact"],
                    "ctaText": "Book Private Tour",
                    "ctaLink": "#contact"
                  },
                  {
                    "id": "sec-hero",
                    "type": "hero",
                    "title": "Hero Section",
                    "heading": "Timeless Architectural Masterpieces in Prime Locations",
                    "subheading": "Curated modern villas, panoramic penthouses, and bespoke coastal estates designed by Pritzker prize-winning architects.",
                    "primaryBtnText": "View Curated Residences",
                    "primaryBtnLink": "#portfolio",
                    "secondaryBtnText": "Private Client Inquiries",
                    "secondaryBtnLink": "#contact",
                    "imageUrl": "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?auto=format&fit=crop&w=1000&q=80"
                  },
                  {
                    "id": "sec-portfolio",
                    "type": "portfolio",
                    "title": "Featured Residential Offerings",
                    "subtitle": "Unrivaled privacy, panoramic views, and world-class structural craftsmanship.",
                    "items": [
                      {
                        "title": "The Glasshouse Villa - $12.5M",
                        "category": "Bel Air, California",
                        "image": "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=600&q=80"
                      },
                      {
                        "title": "Horizon Penthouse - $8.9M",
                        "category": "Tribeca, New York",
                        "image": "https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?auto=format&fit=crop&w=600&q=80"
                      },
                      {
                        "title": "Azure Coastal Retreat - $15.2M",
                        "category": "Cap d'Antibes, France",
                        "image": "https://images.unsplash.com/photo-1600566753376-12c8ab7fb75b?auto=format&fit=crop&w=600&q=80"
                      }
                    ]
                  },
                  {
                    "id": "sec-contact",
                    "type": "contact",
                    "title": "Request Confidential Property Portfolio",
                    "subtitle": "Receive our unlisted pocket property dossier and private viewing arrangements.",
                    "buttonText": "Request Private Dossier",
                    "emailPlaceholder": "investor@familyoffice.com"
                  },
                  {
                    "id": "sec-footer",
                    "type": "footer",
                    "title": "Footer",
                    "copyright": "© 2026 Solarium Architectural Estates. Powered by Webloom.",
                    "links": ["Private Collection", "Architectural Advisors", "Press Features", "Contact Broker"]
                  }
                ]
                """
        ));

        templateRepository.saveAll(templates);
    }

    public List<Template> getAllTemplates(String category, String search) {
        if (category != null && !category.equalsIgnoreCase("all") && !category.isBlank()) {
            return templateRepository.findByCategoryIgnoreCase(category);
        }
        if (search != null && !search.isBlank()) {
            return templateRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search, search);
        }
        return templateRepository.findAll();
    }

    public Optional<Template> getTemplateById(String id) {
        return templateRepository.findById(id);
    }
}
