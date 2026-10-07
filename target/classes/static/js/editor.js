// WebForge Visual Studio Engine
let currentWebsite = {
    id: null,
    title: 'My Custom Website',
    templateId: 'business-agency',
    theme: {
        primaryColor: '#2563eb',
        secondaryColor: '#0f172a',
        fontFamily: 'Inter',
        buttonRadius: '8px'
    },
    sections: []
};

let activeSectionId = null;
let isPreviewMode = false;

document.addEventListener('DOMContentLoaded', () => {
    initEditor();
});

async function initEditor() {
    const params = new URLSearchParams(window.location.search);
    const savedId = params.get('id');
    const templateId = params.get('template') || 'business-agency';

    if (savedId) {
        await loadSavedWebsite(savedId);
    } else {
        await loadTemplate(templateId);
    }

    setupToolbarListeners();
    applyThemeStyles();
    renderAll();
}

function setupToolbarListeners() {
    const titleInput = document.getElementById('siteTitleInput');
    titleInput.addEventListener('input', (e) => {
        currentWebsite.title = e.target.value.trim() || 'Untitled Website';
        markUnsaved();
    });
}

// Load from Template
async function loadTemplate(templateId) {
    try {
        const res = await fetch(`/api/templates/${templateId}`);
        if (!res.ok) throw new Error('Template not found');
        const tmpl = await res.json();

        currentWebsite.templateId = tmpl.id;
        currentWebsite.title = tmpl.name + ' (Customized)';
        document.getElementById('siteTitleInput').value = currentWebsite.title;

        currentWebsite.theme = {
            primaryColor: tmpl.primaryColor || '#2563eb',
            secondaryColor: tmpl.secondaryColor || '#0f172a',
            fontFamily: tmpl.fontFamily || 'Inter',
            buttonRadius: tmpl.buttonRadius || '8px'
        };

        if (tmpl.contentJson) {
            currentWebsite.sections = JSON.parse(tmpl.contentJson);
        }
    } catch (err) {
        console.error('Error loading template:', err);
        showToast('Error loading template. Loading default layout.');
        loadDefaultFallbackSections();
    }
}

// Load from Saved Website
async function loadSavedWebsite(id) {
    try {
        const res = await fetch(`/api/websites/${id}`);
        if (!res.ok) throw new Error('Website not found');
        const site = await res.json();

        currentWebsite.id = site.id;
        currentWebsite.title = site.title;
        currentWebsite.templateId = site.templateId;
        document.getElementById('siteTitleInput').value = site.title;

        if (site.themeConfigJson) {
            currentWebsite.theme = JSON.parse(site.themeConfigJson);
        }
        if (site.contentJson) {
            currentWebsite.sections = JSON.parse(site.contentJson);
        }

        document.getElementById('saveStatusBadge').innerText = 'Saved (#' + site.id + ')';
    } catch (err) {
        console.error('Error loading saved site:', err);
        showToast('Failed to load website #' + id);
    }
}

function loadDefaultFallbackSections() {
    currentWebsite.sections = [
        {
            id: 'sec-hero',
            type: 'hero',
            title: 'Hero Section',
            heading: 'Build Your Next High Impact Website',
            subheading: 'Customize text and layout right inside your browser.',
            primaryBtnText: 'Get Started',
            primaryBtnLink: '#contact',
            secondaryBtnText: 'Learn More',
            secondaryBtnLink: '#services',
            imageUrl: 'https://images.unsplash.com/photo-1522071820081-009f0129c71c?auto=format&fit=crop&w=1000&q=80'
        }
    ];
}

// Apply Theme CSS Variables to Canvas
function applyThemeStyles() {
    const root = document.documentElement;
    root.style.setProperty('--primary-color', currentWebsite.theme.primaryColor);
    root.style.setProperty('--secondary-color', currentWebsite.theme.secondaryColor);
    root.style.setProperty('--font-family', `'${currentWebsite.theme.fontFamily}', sans-serif`);
    root.style.setProperty('--btn-radius', currentWebsite.theme.buttonRadius);

    // Sync sidebar controls
    const customPicker = document.getElementById('customColorPicker');
    const customHex = document.getElementById('customColorHex');
    const fontSelect = document.getElementById('fontSelect');

    if (customPicker) customPicker.value = currentWebsite.theme.primaryColor;
    if (customHex) customHex.value = currentWebsite.theme.primaryColor;
    if (fontSelect) fontSelect.value = currentWebsite.theme.fontFamily;
}

// Render All (Canvas + Sidebar List)
function renderAll() {
    renderCanvas();
    renderSectionsSidebarList();
}

// Render Canvas
function renderCanvas() {
    const container = document.getElementById('canvasInner');
    const sections = currentWebsite.sections;

    if (!sections || sections.length === 0) {
        container.innerHTML = `
            <div class="py-24 text-center text-slate-400">
                <i class="fa-solid fa-layer-group text-4xl mb-3 text-slate-300"></i>
                <p class="text-sm font-semibold text-slate-600">Your page is currently empty</p>
                <button onclick="openAddSectionModal()" class="mt-4 px-4 py-2 bg-blue-600 text-white rounded-xl text-xs font-bold">Add First Section</button>
            </div>
        `;
        return;
    }

    let html = '';
    sections.forEach((sec, index) => {
        html += renderSectionDivider(index);
        html += renderEditableSection(sec, index);
    });
    html += renderSectionDivider(sections.length);

    container.innerHTML = html;
    attachInlineListeners();
}

// Render Section Divider "+ Add Section"
function renderSectionDivider(index) {
    return `
        <div class="add-section-divider">
            <button onclick="openAddSectionModal(${index})">
                <i class="fa-solid fa-plus text-[10px] mr-1"></i> Add Section Here
            </button>
        </div>
    `;
}

// Render Single Editable Section with Action Bar
function renderEditableSection(sec, index) {
    const isFirst = index === 0;
    const isLast = index === currentWebsite.sections.length - 1;
    const isActive = activeSectionId === sec.id ? 'active-section' : '';

    return `
        <div id="${sec.id}" class="editable-section ${isActive} relative group" onclick="selectSection('${sec.id}', event)">
            <!-- Floating Quick Action Bar -->
            <div class="section-actions-bar">
                <span class="text-[10px] font-bold text-slate-300 uppercase px-1.5 py-0.5 tracking-wider">${sec.type}</span>
                ${!isFirst ? `<button onclick="moveSection(${index}, -1, event)" class="w-6 h-6 rounded bg-slate-800 text-slate-300 hover:text-white flex items-center justify-center text-xs" title="Move Up"><i class="fa-solid fa-arrow-up"></i></button>` : ''}
                ${!isLast ? `<button onclick="moveSection(${index}, 1, event)" class="w-6 h-6 rounded bg-slate-800 text-slate-300 hover:text-white flex items-center justify-center text-xs" title="Move Down"><i class="fa-solid fa-arrow-down"></i></button>` : ''}
                <button onclick="editSectionInInspector('${sec.id}', event)" class="w-6 h-6 rounded bg-blue-600 text-white flex items-center justify-center text-xs" title="Edit in Inspector"><i class="fa-solid fa-pen"></i></button>
                <button onclick="deleteSection(${index}, event)" class="w-6 h-6 rounded bg-red-600/80 hover:bg-red-600 text-white flex items-center justify-center text-xs" title="Delete Section"><i class="fa-solid fa-trash"></i></button>
            </div>

            <!-- Section Content -->
            ${generateSectionInnerHtml(sec)}
        </div>
    `;
}

// Generate Raw HTML for Each Section Type with Inline Editing
function generateSectionInnerHtml(sec) {
    const type = sec.type;
    const pColor = currentWebsite.theme.primaryColor;
    const bRad = currentWebsite.theme.buttonRadius;

    switch (type) {
        case 'navbar': {
            const links = sec.links || ['Home', 'Services', 'About', 'Contact'];
            return `
                <header class="bg-white/90 backdrop-blur-md border-b border-slate-100 py-4 px-6 md:px-12 flex items-center justify-between">
                    <div class="flex items-center gap-2">
                        <span class="w-3 h-3 rounded-full" style="background-color: ${pColor};"></span>
                        <span contenteditable="true" data-field="brand" data-sec="${sec.id}" class="text-xl font-bold tracking-tight text-slate-900">${sec.brand || 'WebForge'}</span>
                    </div>
                    <nav class="hidden md:flex items-center space-x-6 text-sm font-medium text-slate-600">
                        ${links.map(l => `<span class="hover:text-slate-900 transition cursor-pointer">${l}</span>`).join('')}
                    </nav>
                    <div>
                        <span contenteditable="true" data-field="ctaText" data-sec="${sec.id}" 
                              class="inline-block px-5 py-2 text-xs font-bold text-white transition shadow-sm cursor-pointer"
                              style="background-color: ${pColor}; border-radius: ${bRad};">
                            ${sec.ctaText || 'Get Started'}
                        </span>
                    </div>
                </header>
            `;
        }
        case 'hero': {
            return `
                <section class="py-16 md:py-24 px-6 md:px-12 bg-gradient-to-b from-slate-50 to-white">
                    <div class="max-w-6xl mx-auto grid grid-cols-1 lg:grid-cols-2 gap-12 items-center">
                        <div class="space-y-6">
                            <h1 contenteditable="true" data-field="heading" data-sec="${sec.id}" 
                                class="text-3xl sm:text-4xl lg:text-5xl font-black text-slate-900 tracking-tight leading-[1.2]">
                                ${sec.heading || 'Accelerate Your Digital Presence'}
                            </h1>
                            <p contenteditable="true" data-field="subheading" data-sec="${sec.id}" 
                               class="text-base sm:text-lg text-slate-600 leading-relaxed max-w-lg">
                                ${sec.subheading || 'Clean, modern, and high-performance design that converts visitors.'}
                            </p>
                            <div class="flex flex-wrap items-center gap-3 pt-2">
                                <span contenteditable="true" data-field="primaryBtnText" data-sec="${sec.id}" 
                                      class="inline-block px-6 py-3 text-sm font-bold text-white shadow-md cursor-pointer transition hover:opacity-95"
                                      style="background-color: ${pColor}; border-radius: ${bRad};">
                                    ${sec.primaryBtnText || 'Start Free'}
                                </span>
                                <span contenteditable="true" data-field="secondaryBtnText" data-sec="${sec.id}" 
                                      class="inline-block px-6 py-3 text-sm font-bold cursor-pointer transition"
                                      style="border: 2px solid ${pColor}; color: ${pColor}; border-radius: ${bRad};">
                                    ${sec.secondaryBtnText || 'Explore Work'}
                                </span>
                            </div>
                        </div>
                        <div class="relative">
                            <img src="${sec.imageUrl || 'https://images.unsplash.com/photo-1522071820081-009f0129c71c?auto=format&fit=crop&w=1000&q=80'}" 
                                 alt="Hero" class="rounded-2xl shadow-xl w-full h-[360px] object-cover" />
                        </div>
                    </div>
                </section>
            `;
        }
        case 'stats': {
            const items = sec.items || [
                { number: '250+', label: 'Delivered' },
                { number: '99.4%', label: 'Satisfaction' },
                { number: '10x', label: 'ROI' },
                { number: '15+', label: 'Awards' }
            ];
            return `
                <section class="py-12 px-6 bg-slate-900 text-white">
                    <div class="max-w-6xl mx-auto grid grid-cols-2 md:grid-cols-4 gap-6 text-center">
                        ${items.map((item, idx) => `
                            <div class="space-y-1">
                                <div contenteditable="true" data-itemidx="${idx}" data-itemfield="number" data-sec="${sec.id}" class="text-3xl sm:text-4xl font-black text-white">${item.number}</div>
                                <div contenteditable="true" data-itemidx="${idx}" data-itemfield="label" data-sec="${sec.id}" class="text-xs font-semibold text-slate-400 uppercase tracking-wider">${item.label}</div>
                            </div>
                        `).join('')}
                    </div>
                </section>
            `;
        }
        case 'services': {
            const items = sec.items || [];
            return `
                <section class="py-20 px-6 md:px-12 bg-white">
                    <div class="max-w-6xl mx-auto">
                        <div class="text-center max-w-xl mx-auto mb-12 space-y-3">
                            <h2 contenteditable="true" data-field="title" data-sec="${sec.id}" class="text-3xl font-bold text-slate-900 tracking-tight">${sec.title || 'Our Services'}</h2>
                            <p contenteditable="true" data-field="subtitle" data-sec="${sec.id}" class="text-slate-600 text-sm">${sec.subtitle || 'Tailored solutions built for real results.'}</p>
                        </div>
                        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
                            ${items.map((item, idx) => `
                                <div class="p-6 rounded-2xl border border-slate-100 bg-slate-50/60 hover:bg-white hover:shadow-lg transition space-y-3">
                                    <div class="w-12 h-12 rounded-xl flex items-center justify-center text-xl" style="background-color: ${pColor}15; color: ${pColor};">
                                        <i class="fa-solid ${item.icon || 'fa-star'}"></i>
                                    </div>
                                    <h3 contenteditable="true" data-itemidx="${idx}" data-itemfield="title" data-sec="${sec.id}" class="text-lg font-bold text-slate-900">${item.title}</h3>
                                    <p contenteditable="true" data-itemidx="${idx}" data-itemfield="desc" data-sec="${sec.id}" class="text-slate-600 text-xs leading-relaxed">${item.desc}</p>
                                </div>
                            `).join('')}
                        </div>
                    </div>
                </section>
            `;
        }
        case 'portfolio': {
            const items = sec.items || [];
            return `
                <section class="py-20 px-6 md:px-12 bg-slate-50">
                    <div class="max-w-6xl mx-auto">
                        <div class="text-center max-w-xl mx-auto mb-12 space-y-3">
                            <h2 contenteditable="true" data-field="title" data-sec="${sec.id}" class="text-3xl font-bold text-slate-900 tracking-tight">${sec.title || 'Selected Projects'}</h2>
                            <p contenteditable="true" data-field="subtitle" data-sec="${sec.id}" class="text-slate-600 text-sm">${sec.subtitle || 'Recent creative projects.'}</p>
                        </div>
                        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
                            ${items.map((item, idx) => `
                                <div class="rounded-2xl overflow-hidden bg-white shadow-sm border border-slate-100">
                                    <img src="${item.image || 'https://images.unsplash.com/photo-1460925895917-afdab827c52f?auto=format&fit=crop&w=600&q=80'}" class="w-full h-48 object-cover" />
                                    <div class="p-5 space-y-1">
                                        <span contenteditable="true" data-itemidx="${idx}" data-itemfield="category" data-sec="${sec.id}" class="text-[11px] font-bold uppercase tracking-wider block" style="color: ${pColor};">${item.category}</span>
                                        <h4 contenteditable="true" data-itemidx="${idx}" data-itemfield="title" data-sec="${sec.id}" class="text-base font-bold text-slate-900">${item.title}</h4>
                                    </div>
                                </div>
                            `).join('')}
                        </div>
                    </div>
                </section>
            `;
        }
        case 'pricing': {
            const items = sec.items || [];
            return `
                <section class="py-20 px-6 md:px-12 bg-white">
                    <div class="max-w-6xl mx-auto">
                        <div class="text-center max-w-xl mx-auto mb-12 space-y-3">
                            <h2 contenteditable="true" data-field="title" data-sec="${sec.id}" class="text-3xl font-bold text-slate-900 tracking-tight">${sec.title || 'Pricing Plans'}</h2>
                            <p contenteditable="true" data-field="subtitle" data-sec="${sec.id}" class="text-slate-600 text-sm">${sec.subtitle || 'Transparent options for every stage.'}</p>
                        </div>
                        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
                            ${items.map((item, idx) => `
                                <div class="p-6 rounded-2xl border border-slate-200 bg-white hover:shadow-xl transition flex flex-col justify-between space-y-6">
                                    <div class="space-y-4">
                                        <div>
                                            <h3 contenteditable="true" data-itemidx="${idx}" data-itemfield="plan" data-sec="${sec.id}" class="text-lg font-bold text-slate-900">${item.plan}</h3>
                                            <p contenteditable="true" data-itemidx="${idx}" data-itemfield="desc" data-sec="${sec.id}" class="text-xs text-slate-500 mt-1">${item.desc}</p>
                                        </div>
                                        <div class="flex items-baseline gap-1">
                                            <span contenteditable="true" data-itemidx="${idx}" data-itemfield="price" data-sec="${sec.id}" class="text-3xl font-extrabold text-slate-900">${item.price}</span>
                                            <span class="text-xs font-medium text-slate-500">${item.period || '/mo'}</span>
                                        </div>
                                    </div>
                                    <div>
                                        <span contenteditable="true" data-itemidx="${idx}" data-itemfield="btnText" data-sec="${sec.id}" 
                                              class="block text-center py-2.5 text-xs font-bold text-white shadow-sm cursor-pointer"
                                              style="background-color: ${pColor}; border-radius: ${bRad};">
                                            ${item.btnText || 'Choose Plan'}
                                        </span>
                                    </div>
                                </div>
                            `).join('')}
                        </div>
                    </div>
                </section>
            `;
        }
        case 'testimonials': {
            const items = sec.items || [];
            return `
                <section class="py-20 px-6 md:px-12 bg-slate-50">
                    <div class="max-w-5xl mx-auto">
                        <div class="text-center max-w-xl mx-auto mb-12 space-y-3">
                            <h2 contenteditable="true" data-field="title" data-sec="${sec.id}" class="text-3xl font-bold text-slate-900 tracking-tight">${sec.title || 'What Clients Say'}</h2>
                            <p contenteditable="true" data-field="subtitle" data-sec="${sec.id}" class="text-slate-600 text-sm">${sec.subtitle || 'Real feedback from satisfied customers.'}</p>
                        </div>
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                            ${items.map((item, idx) => `
                                <div class="p-6 rounded-2xl bg-white shadow-sm border border-slate-100 space-y-4">
                                    <div class="text-amber-400 text-xs flex gap-1">
                                        <i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i>
                                        <i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i>
                                        <i class="fa-solid fa-star"></i>
                                    </div>
                                    <p contenteditable="true" data-itemidx="${idx}" data-itemfield="quote" data-sec="${sec.id}" class="text-slate-700 italic text-sm">"${item.quote}"</p>
                                    <div class="flex items-center gap-3 pt-2">
                                        <img src="${item.avatar || 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=150&q=80'}" class="w-10 h-10 rounded-full object-cover" />
                                        <div>
                                            <div contenteditable="true" data-itemidx="${idx}" data-itemfield="author" data-sec="${sec.id}" class="font-bold text-sm text-slate-900">${item.author}</div>
                                            <div contenteditable="true" data-itemidx="${idx}" data-itemfield="role" data-sec="${sec.id}" class="text-xs text-slate-400">${item.role}</div>
                                        </div>
                                    </div>
                                </div>
                            `).join('')}
                        </div>
                    </div>
                </section>
            `;
        }
        case 'contact': {
            return `
                <section class="py-20 px-6 md:px-12 bg-white">
                    <div class="max-w-2xl mx-auto text-center space-y-6">
                        <div class="space-y-2">
                            <h2 contenteditable="true" data-field="title" data-sec="${sec.id}" class="text-3xl font-bold text-slate-900 tracking-tight">${sec.title || 'Get In Touch'}</h2>
                            <p contenteditable="true" data-field="subtitle" data-sec="${sec.id}" class="text-slate-600 text-sm">${sec.subtitle || 'Reach out for inquiries or proposals.'}</p>
                        </div>
                        <div class="bg-slate-50 p-6 rounded-2xl border border-slate-200 text-left space-y-4">
                            <div class="space-y-1">
                                <label class="text-[11px] font-bold text-slate-600 uppercase">Your Name</label>
                                <input type="text" placeholder="Alex Morgan" disabled class="w-full px-3 py-2 text-xs rounded-lg border border-slate-200 bg-white" />
                            </div>
                            <div class="space-y-1">
                                <label class="text-[11px] font-bold text-slate-600 uppercase">Email Address</label>
                                <input type="email" placeholder="${sec.emailPlaceholder || 'your@email.com'}" disabled class="w-full px-3 py-2 text-xs rounded-lg border border-slate-200 bg-white" />
                            </div>
                            <span contenteditable="true" data-field="buttonText" data-sec="${sec.id}" 
                                  class="block text-center py-3 text-xs font-bold text-white shadow-sm cursor-pointer"
                                  style="background-color: ${pColor}; border-radius: ${bRad};">
                                ${sec.buttonText || 'Send Message'}
                            </span>
                        </div>
                    </div>
                </section>
            `;
        }
        case 'footer': {
            const links = sec.links || ['Privacy', 'Terms', 'Support'];
            return `
                <footer class="py-8 px-6 bg-slate-950 text-slate-400 text-xs border-t border-slate-800">
                    <div class="max-w-6xl mx-auto flex flex-col sm:flex-row items-center justify-between gap-4">
                        <p contenteditable="true" data-field="copyright" data-sec="${sec.id}">${sec.copyright || '© 2026 WebForge. All rights reserved.'}</p>
                        <div class="flex items-center space-x-4">
                            ${links.map(l => `<span class="hover:text-white cursor-pointer">${l}</span>`).join('')}
                        </div>
                    </div>
                </footer>
            `;
        }
        default:
            return `<div class="p-8 text-center text-slate-400">Section: ${type}</div>`;
    }
}

// Attach Inline Contenteditable Listeners
function attachInlineListeners() {
    const editables = document.querySelectorAll('#canvasInner [contenteditable="true"]');
    editables.forEach(el => {
        el.addEventListener('blur', (e) => {
            const secId = e.target.getAttribute('data-sec');
            const field = e.target.getAttribute('data-field');
            const itemIdx = e.target.getAttribute('data-itemidx');
            const itemField = e.target.getAttribute('data-itemfield');
            const newText = e.target.innerText.trim();

            const sec = currentWebsite.sections.find(s => s.id === secId);
            if (!sec) return;

            if (itemIdx !== null && itemField) {
                if (sec.items && sec.items[itemIdx]) {
                    sec.items[itemIdx][itemField] = newText;
                    markUnsaved();
                }
            } else if (field) {
                sec[field] = newText;
                markUnsaved();
            }
        });
    });
}

// Render Sidebar Sections List
function renderSectionsSidebarList() {
    const list = document.getElementById('sectionsList');
    list.innerHTML = currentWebsite.sections.map((sec, index) => `
        <div class="p-3 bg-slate-50 border border-slate-200 rounded-xl flex items-center justify-between gap-2 hover:border-slate-300 transition ${activeSectionId === sec.id ? 'border-blue-500 bg-blue-50/30' : ''}">
            <div class="flex items-center gap-2 overflow-hidden cursor-pointer" onclick="selectSection('${sec.id}')">
                <i class="fa-solid fa-grip-vertical text-slate-300 text-xs"></i>
                <div class="truncate">
                    <div class="text-xs font-bold text-slate-800 truncate">${sec.title || sec.type}</div>
                    <div class="text-[10px] text-slate-400 uppercase font-medium">${sec.type}</div>
                </div>
            </div>

            <div class="flex items-center gap-1 flex-shrink-0">
                ${index > 0 ? `<button onclick="moveSection(${index}, -1)" class="w-6 h-6 rounded hover:bg-slate-200 text-slate-500 text-xs flex items-center justify-center"><i class="fa-solid fa-arrow-up"></i></button>` : ''}
                ${index < currentWebsite.sections.length - 1 ? `<button onclick="moveSection(${index}, 1)" class="w-6 h-6 rounded hover:bg-slate-200 text-slate-500 text-xs flex items-center justify-center"><i class="fa-solid fa-arrow-down"></i></button>` : ''}
                <button onclick="deleteSection(${index})" class="w-6 h-6 rounded hover:bg-red-100 text-red-500 text-xs flex items-center justify-center"><i class="fa-solid fa-trash"></i></button>
            </div>
        </div>
    `).join('');
}

// Select Active Section & Open Inspector
function selectSection(secId, event) {
    if (event) event.stopPropagation();
    activeSectionId = secId;
    document.querySelectorAll('.editable-section').forEach(el => el.classList.remove('active-section'));
    const target = document.getElementById(secId);
    if (target) target.classList.add('active-section');
    renderSectionsSidebarList();
}

function editSectionInInspector(secId, event) {
    if (event) event.stopPropagation();
    selectSection(secId);
    switchSidebarTab('inspector');
    populateInspector(secId);
}

// Populate Inspector Tab
function populateInspector(secId) {
    const sec = currentWebsite.sections.find(s => s.id === secId);
    if (!sec) return;

    document.getElementById('inspectorEmptyState').classList.add('hidden');
    document.getElementById('inspectorContent').classList.remove('hidden');

    document.getElementById('inspectorTypeBadge').innerText = sec.type;
    document.getElementById('inspectorSectionTitle').innerText = sec.title || 'Section Settings';

    const fieldsContainer = document.getElementById('inspectorFormFields');
    let html = '';

    if (sec.heading !== undefined) {
        html += createInputField(sec.id, 'heading', 'Heading Text', sec.heading);
    }
    if (sec.subheading !== undefined) {
        html += createTextareaField(sec.id, 'subheading', 'Subheading', sec.subheading);
    }
    if (sec.title !== undefined && sec.type !== 'hero') {
        html += createInputField(sec.id, 'title', 'Section Title', sec.title);
    }
    if (sec.subtitle !== undefined) {
        html += createInputField(sec.id, 'subtitle', 'Section Subtitle', sec.subtitle);
    }
    if (sec.primaryBtnText !== undefined) {
        html += createInputField(sec.id, 'primaryBtnText', 'Primary Button Text', sec.primaryBtnText);
    }
    if (sec.imageUrl !== undefined) {
        html += createInputField(sec.id, 'imageUrl', 'Image URL', sec.imageUrl);
        html += `
            <div class="pt-1">
                <label class="text-[10px] font-bold text-slate-400 block mb-1">Stock Image Presets:</label>
                <div class="grid grid-cols-3 gap-1.5">
                    <button onclick="setSectionImage('${sec.id}', 'https://images.unsplash.com/photo-1522071820081-009f0129c71c?auto=format&fit=crop&w=1000&q=80')" class="text-[10px] py-1 bg-slate-100 rounded hover:bg-blue-50 text-slate-700">Team / Work</button>
                    <button onclick="setSectionImage('${sec.id}', 'https://images.unsplash.com/photo-1460925895917-afdab827c52f?auto=format&fit=crop&w=1000&q=80')" class="text-[10px] py-1 bg-slate-100 rounded hover:bg-blue-50 text-slate-700">Analytics</button>
                    <button onclick="setSectionImage('${sec.id}', 'https://images.unsplash.com/photo-1507238691740-187a5b1d37b8?auto=format&fit=crop&w=1000&q=80')" class="text-[10px] py-1 bg-slate-100 rounded hover:bg-blue-50 text-slate-700">Workspace</button>
                </div>
            </div>
        `;
    }

    fieldsContainer.innerHTML = html;
}

function setSectionImage(secId, url) {
    const sec = currentWebsite.sections.find(s => s.id === secId);
    if (sec) {
        sec.imageUrl = url;
        renderCanvas();
        populateInspector(secId);
        markUnsaved();
    }
}

function createInputField(secId, field, label, val) {
    return `
        <div class="space-y-1">
            <label class="text-[11px] font-bold text-slate-600 uppercase">${label}</label>
            <input type="text" value="${escapeHtml(val)}" 
                   oninput="updateSectionProperty('${secId}', '${field}', this.value)"
                   class="w-full px-3 py-2 text-xs rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-blue-500" />
        </div>
    `;
}

function createTextareaField(secId, field, label, val) {
    return `
        <div class="space-y-1">
            <label class="text-[11px] font-bold text-slate-600 uppercase">${label}</label>
            <textarea rows="3" oninput="updateSectionProperty('${secId}', '${field}', this.value)"
                      class="w-full px-3 py-2 text-xs rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-blue-500">${escapeHtml(val)}</textarea>
        </div>
    `;
}

function updateSectionProperty(secId, field, value) {
    const sec = currentWebsite.sections.find(s => s.id === secId);
    if (sec) {
        sec[field] = value;
        renderCanvas();
        markUnsaved();
    }
}

// Move Section
function moveSection(index, delta, event) {
    if (event) event.stopPropagation();
    const newIdx = index + delta;
    if (newIdx < 0 || newIdx >= currentWebsite.sections.length) return;

    const [moved] = currentWebsite.sections.splice(index, 1);
    currentWebsite.sections.splice(newIdx, 0, moved);
    activeSectionId = moved.id;

    renderAll();
    markUnsaved();
}

// Delete Section
function deleteSection(index, event) {
    if (event) event.stopPropagation();
    if (!confirm('Delete this section from your website?')) return;

    currentWebsite.sections.splice(index, 1);
    activeSectionId = null;
    renderAll();
    markUnsaved();
}

// Add Section Modal & Insertion
let insertPositionIndex = null;

function openAddSectionModal(index = null) {
    insertPositionIndex = index;
    document.getElementById('addSectionModal').classList.remove('hidden');
}

function closeAddSectionModal() {
    document.getElementById('addSectionModal').classList.add('hidden');
}

function insertSection(type) {
    closeAddSectionModal();
    const newSec = createSectionTemplate(type);

    if (insertPositionIndex !== null && insertPositionIndex <= currentWebsite.sections.length) {
        currentWebsite.sections.splice(insertPositionIndex, 0, newSec);
    } else {
        currentWebsite.sections.push(newSec);
    }

    activeSectionId = newSec.id;
    renderAll();
    markUnsaved();
    showToast(`Added ${newSec.title} section`);
}

function createSectionTemplate(type) {
    const randomId = 'sec-' + Math.random().toString(36).substring(2, 8);
    switch (type) {
        case 'hero':
            return {
                id: randomId,
                type: 'hero',
                title: 'Hero Section',
                heading: 'Empowering Next-Generation Digital Experiences',
                subheading: 'We engineer memorable web products with cutting-edge velocity and design precision.',
                primaryBtnText: 'Get Started Today',
                secondaryBtnText: 'View Showcase',
                imageUrl: 'https://images.unsplash.com/photo-1522071820081-009f0129c71c?auto=format&fit=crop&w=1000&q=80'
            };
        case 'services':
            return {
                id: randomId,
                type: 'services',
                title: 'Core Capabilities',
                subtitle: 'High impact solutions tailored to scale your organization.',
                items: [
                    { icon: 'fa-rocket', title: 'High Velocity Execution', desc: 'Fast turnaround times engineered with scalable architecture.' },
                    { icon: 'fa-shield-halved', title: 'Enterprise Security', desc: 'Compliant and robust data integrity built into every module.' },
                    { icon: 'fa-chart-line', title: 'Conversion Focused', desc: 'Proven interfaces optimized to drive engagement and retention.' }
                ]
            };
        case 'stats':
            return {
                id: randomId,
                type: 'stats',
                title: 'Impact Metrics',
                items: [
                    { number: '500+', label: 'Happy Clients' },
                    { number: '99.9%', label: 'Uptime SLA' },
                    { number: '24/7', label: 'Support' },
                    { number: '10M+', label: 'Users Reached' }
                ]
            };
        case 'portfolio':
            return {
                id: randomId,
                type: 'portfolio',
                title: 'Featured Case Studies',
                subtitle: 'Take a look at some of our proudest digital deliveries.',
                items: [
                    { title: 'Cloud Banking Platform', category: 'FinTech', image: 'https://images.unsplash.com/photo-1551288049-bebda4e38f71?auto=format&fit=crop&w=600&q=80' },
                    { title: 'AI Automation Suite', category: 'Enterprise AI', image: 'https://images.unsplash.com/photo-1460925895917-afdab827c52f?auto=format&fit=crop&w=600&q=80' },
                    { title: 'Eco Commerce Platform', category: 'Retail', image: 'https://images.unsplash.com/photo-1441986300917-64674bd600d8?auto=format&fit=crop&w=600&q=80' }
                ]
            };
        case 'pricing':
            return {
                id: randomId,
                type: 'pricing',
                title: 'Transparent Membership Plans',
                subtitle: 'Pick the tier that best matches your scale.',
                items: [
                    { plan: 'Starter', price: '$29', desc: 'Basic essentials', btnText: 'Select Starter' },
                    { plan: 'Professional', price: '$79', desc: 'For growing brands', btnText: 'Select Pro' },
                    { plan: 'Enterprise', price: '$249', desc: 'Custom scale', btnText: 'Contact Sales' }
                ]
            };
        case 'testimonials':
            return {
                id: randomId,
                type: 'testimonials',
                title: 'Client Recommendations',
                subtitle: 'What people have to say about working with us.',
                items: [
                    { quote: 'Absolute game changer for our business metrics.', author: 'Emily Watson', role: 'Head of Growth', avatar: 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=150&q=80' },
                    { quote: 'The attention to craftsmanship is second to none.', author: 'Marcus Vance', role: 'CEO, TechVentures', avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=150&q=80' }
                ]
            };
        case 'contact':
            return {
                id: randomId,
                type: 'contact',
                title: 'Let’s Start a Conversation',
                subtitle: 'Send us a note and we will get back within 24 hours.',
                buttonText: 'Send Inquiry',
                emailPlaceholder: 'hello@yourdomain.com'
            };
        default:
            return { id: randomId, type: 'hero', title: 'New Section' };
    }
}

// Sidebar Tabs
function switchSidebarTab(tab) {
    const tabs = ['sections', 'inspector', 'theme'];
    tabs.forEach(t => {
        const btn = document.getElementById(`tabBtn${capitalize(t)}`);
        const content = document.getElementById(`tab${capitalize(t)}`);
        if (t === tab) {
            btn.className = 'flex-1 py-3 text-center border-b-2 border-blue-600 text-blue-600 bg-white flex items-center justify-center gap-1.5 transition font-semibold';
            content.classList.remove('hidden');
        } else {
            btn.className = 'flex-1 py-3 text-center border-b-2 border-transparent text-slate-500 hover:text-slate-900 flex items-center justify-center gap-1.5 transition font-medium';
            content.classList.add('hidden');
        }
    });
}

// Theme Controls
function setPrimaryColor(color) {
    currentWebsite.theme.primaryColor = color;
    applyThemeStyles();
    renderCanvas();
    markUnsaved();

    document.querySelectorAll('.palette-btn').forEach(btn => {
        if (btn.getAttribute('title')?.toLowerCase().includes(color) || btn.style.backgroundColor === color) {
            btn.classList.add('active');
        } else {
            btn.classList.remove('active');
        }
    });
}

function setFontFamily(font) {
    currentWebsite.theme.fontFamily = font;
    applyThemeStyles();
    markUnsaved();
}

function setButtonRadius(radius) {
    currentWebsite.theme.buttonRadius = radius;
    applyThemeStyles();
    renderCanvas();
    markUnsaved();
}

// Viewport Switcher
function setViewport(vp) {
    const container = document.getElementById('canvasContainer');
    const bDesk = document.getElementById('vpDesktop');
    const bTab = document.getElementById('vpTablet');
    const bMob = document.getElementById('vpMobile');

    [bDesk, bTab, bMob].forEach(b => {
        b.className = 'px-3.5 py-1.5 rounded-lg text-xs font-semibold text-slate-400 hover:text-white transition flex items-center gap-1.5';
    });

    container.className = `bg-white min-h-[90vh] shadow-2xl transition-all duration-300 relative overflow-hidden view-${vp}`;

    if (vp === 'desktop') bDesk.className = 'px-3.5 py-1.5 rounded-lg text-xs font-semibold bg-blue-600 text-white transition flex items-center gap-1.5';
    if (vp === 'tablet') bTab.className = 'px-3.5 py-1.5 rounded-lg text-xs font-semibold bg-blue-600 text-white transition flex items-center gap-1.5';
    if (vp === 'mobile') bMob.className = 'px-3.5 py-1.5 rounded-lg text-xs font-semibold bg-blue-600 text-white transition flex items-center gap-1.5';
}

// Toggle Preview Mode
function togglePreviewMode() {
    isPreviewMode = !isPreviewMode;
    const body = document.body;
    const btn = document.getElementById('previewToggleBtn');

    if (isPreviewMode) {
        body.classList.remove('editor-mode');
        body.classList.add('preview-mode');
        btn.innerHTML = `<i class="fa-solid fa-pen-to-square text-blue-400"></i> <span class="hidden sm:inline">Edit Mode</span>`;
        btn.classList.add('bg-blue-900', 'text-white');
    } else {
        body.classList.add('editor-mode');
        body.classList.remove('preview-mode');
        btn.innerHTML = `<i class="fa-regular fa-eye"></i> <span class="hidden sm:inline">Preview</span>`;
        btn.classList.remove('bg-blue-900', 'text-white');
    }
}

// Save Website to Backend
async function saveWebsite() {
    const saveBtn = document.getElementById('saveBtn');
    const badge = document.getElementById('saveStatusBadge');

    saveBtn.disabled = true;
    saveBtn.innerHTML = `<i class="fa-solid fa-spinner fa-spin"></i> Saving...`;

    const payload = {
        title: currentWebsite.title,
        templateId: currentWebsite.templateId,
        themeConfigJson: JSON.stringify(currentWebsite.theme),
        contentJson: JSON.stringify(currentWebsite.sections)
    };

    try {
        let res;
        if (currentWebsite.id) {
            // Update
            res = await fetch(`/api/websites/${currentWebsite.id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });
        } else {
            // Create
            res = await fetch('/api/websites', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });
        }

        if (!res.ok) throw new Error('Save failed');
        const saved = await res.json();
        currentWebsite.id = saved.id;

        // Update URL query param quietly
        const newUrl = `${window.location.pathname}?id=${saved.id}`;
        window.history.replaceState({ path: newUrl }, '', newUrl);

        badge.innerText = `Saved (#${saved.id})`;
        badge.className = 'text-[11px] font-semibold text-emerald-400 px-2 py-0.5 rounded-full bg-emerald-950/60 border border-emerald-800';
        showToast('Website saved to database successfully!');
    } catch (err) {
        console.error('Error saving:', err);
        showToast('Error saving website to server.');
    } finally {
        saveBtn.disabled = false;
        saveBtn.innerHTML = `<i class="fa-regular fa-floppy-disk"></i> Save`;
    }
}

// Export Code / Download ZIP
async function exportWebsite() {
    if (!currentWebsite.id) {
        // Save first then export
        showToast('Saving your website draft before exporting...');
        await saveWebsite();
    }

    if (currentWebsite.id) {
        window.location.href = `/api/websites/${currentWebsite.id}/export`;
        showToast('Generating and downloading website ZIP archive...');
    }
}

function markUnsaved() {
    const badge = document.getElementById('saveStatusBadge');
    badge.innerText = 'Unsaved Changes';
    badge.className = 'text-[11px] font-medium text-amber-400 px-2 py-0.5 rounded-full bg-amber-950/60 border border-amber-800';
}

function showToast(msg) {
    const toast = document.getElementById('toast');
    const toastMsg = document.getElementById('toastMsg');
    toastMsg.innerText = msg;
    toast.classList.remove('opacity-0', 'translate-y-20', 'pointer-events-none');
    setTimeout(() => {
        toast.classList.add('opacity-0', 'translate-y-20', 'pointer-events-none');
    }, 3000);
}

function escapeHtml(str) {
    if (!str) return '';
    return String(str).replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;").replace(/'/g, "&#039;");
}

function capitalize(str) {
    return str.charAt(0).toUpperCase() + str.slice(1);
}
