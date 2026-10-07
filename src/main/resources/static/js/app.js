// Webloom Application - Template Gallery & Manager
let allTemplates = [];
let currentCategory = 'all';
let searchQuery = '';
let currentSort = 'popular';

document.addEventListener('DOMContentLoaded', () => {
    loadCategories();
    loadTemplates();
    loadSavedSitesCount();
    setupEventListeners();
});

function setupEventListeners() {
    const searchInput = document.getElementById('searchInput');
    const clearBtn = document.getElementById('clearSearchBtn');
    const sortSelect = document.getElementById('sortSelect');

    searchInput.addEventListener('input', (e) => {
        searchQuery = e.target.value.trim().toLowerCase();
        if (searchQuery.length > 0) {
            clearBtn.classList.remove('hidden');
        } else {
            clearBtn.classList.add('hidden');
        }
        applyFiltersAndRender();
    });

    clearBtn.addEventListener('click', () => {
        searchInput.value = '';
        searchQuery = '';
        clearBtn.classList.add('hidden');
        applyFiltersAndRender();
    });

    sortSelect.addEventListener('change', (e) => {
        currentSort = e.target.value;
        applyFiltersAndRender();
    });
}

// Load Categories
async function loadCategories() {
    try {
        const res = await fetch('/api/templates/categories');
        const categories = await res.json();
        const container = document.getElementById('categoriesContainer');

        container.innerHTML = categories.map(cat => `
            <button onclick="selectCategory('${cat.id}')"
                    id="catBtn-${cat.id}"
                    class="category-pill px-4 py-2 rounded-xl text-xs sm:text-sm font-bold transition flex items-center gap-2 border ${
                        cat.id === 'all' 
                            ? 'bg-slate-900 text-white border-slate-900 shadow-sm' 
                            : 'bg-white text-slate-600 border-slate-200 hover:border-slate-300 hover:bg-slate-50'
                    }">
                <i class="fa-solid ${cat.icon} text-xs"></i>
                ${cat.name}
            </button>
        `).join('');
    } catch (err) {
        console.error('Error fetching categories:', err);
    }
}

// Select Category
function selectCategory(catId) {
    currentCategory = catId;
    document.querySelectorAll('.category-pill').forEach(btn => {
        btn.className = 'category-pill px-4 py-2 rounded-xl text-xs sm:text-sm font-bold transition flex items-center gap-2 border bg-white text-slate-600 border-slate-200 hover:border-slate-300 hover:bg-slate-50';
    });

    const activeBtn = document.getElementById(`catBtn-${catId}`);
    if (activeBtn) {
        activeBtn.className = 'category-pill px-4 py-2 rounded-xl text-xs sm:text-sm font-bold transition flex items-center gap-2 border bg-slate-900 text-white border-slate-900 shadow-sm';
    }

    applyFiltersAndRender();
}

// Load Templates from API
async function loadTemplates() {
    try {
        const res = await fetch('/api/templates');
        allTemplates = await res.json();
        applyFiltersAndRender();
    } catch (err) {
        console.error('Error loading templates:', err);
        document.getElementById('templatesGrid').innerHTML = `
            <div class="col-span-full py-16 text-center text-red-500">
                <i class="fa-solid fa-triangle-exclamation text-3xl mb-2"></i>
                <p>Failed to load templates from the Webloom Java backend. Please verify server status.</p>
            </div>
        `;
    }
}

// Filter, Sort, and Render
function applyFiltersAndRender() {
    let filtered = [...allTemplates];

    if (currentCategory !== 'all') {
        filtered = filtered.filter(t => t.category.toLowerCase() === currentCategory.toLowerCase());
    }

    if (searchQuery) {
        filtered = filtered.filter(t => 
            t.name.toLowerCase().includes(searchQuery) ||
            t.category.toLowerCase().includes(searchQuery) ||
            t.description.toLowerCase().includes(searchQuery)
        );
    }

    if (currentSort === 'popular') {
        filtered.sort((a, b) => (b.downloads || 0) - (a.downloads || 0));
    } else if (currentSort === 'rating') {
        filtered.sort((a, b) => (b.rating || 0) - (a.rating || 0));
    } else if (currentSort === 'name') {
        filtered.sort((a, b) => a.name.localeCompare(b.name));
    }

    document.getElementById('templateCountLabel').innerText = `Showing ${filtered.length} of ${allTemplates.length} industry-grade templates`;
    renderTemplateCards(filtered);
}

// Render Template Cards
function renderTemplateCards(templates) {
    const grid = document.getElementById('templatesGrid');

    if (templates.length === 0) {
        grid.innerHTML = `
            <div class="col-span-full py-16 text-center text-slate-500 bg-white rounded-2xl border border-slate-200">
                <i class="fa-solid fa-folder-open text-4xl mb-3 text-slate-300"></i>
                <h4 class="text-base font-bold text-slate-800">No matching templates found</h4>
                <p class="text-sm text-slate-500 mt-1">Try another search keyword or switch to "All Templates".</p>
                <button onclick="selectCategory('all')" class="mt-4 px-4 py-2 bg-slate-900 text-white rounded-xl text-xs font-bold">View All Templates</button>
            </div>
        `;
        return;
    }

    grid.innerHTML = templates.map(tmpl => `
        <div class="template-card group bg-white rounded-2xl border border-slate-200/90 shadow-sm hover:shadow-2xl hover:border-indigo-300 transition-all duration-300 overflow-hidden flex flex-col justify-between">
            <div>
                <!-- Thumbnail with Hover Action Buttons -->
                <div class="relative overflow-hidden aspect-[16/10] bg-slate-900">
                    <img src="${tmpl.thumbnailUrl}" alt="${tmpl.name}" class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500" />
                    
                    <!-- Badge -->
                    <span class="absolute top-3 left-3 bg-slate-950/85 backdrop-blur-md text-white text-[11px] font-bold px-3 py-1 rounded-lg border border-white/10 shadow-sm">
                        ${tmpl.badge || tmpl.category}
                    </span>

                    <!-- Quick Preview Hover Overlay -->
                    <div class="preview-overlay absolute inset-0 bg-slate-950/70 backdrop-blur-[2px] opacity-0 transition-opacity duration-200 flex items-center justify-center gap-3 p-4">
                        <button onclick="openLivePreview('${tmpl.id}', '${escapeHtml(tmpl.name)}', '${tmpl.category}')" 
                                class="px-4 py-2 bg-white text-slate-900 rounded-xl text-xs font-bold hover:bg-slate-100 transition shadow-lg flex items-center gap-1.5">
                            <i class="fa-solid fa-eye text-indigo-600"></i> Live Preview
                        </button>
                        <a href="editor.html?template=${tmpl.id}" 
                           class="px-4 py-2 bg-indigo-600 text-white rounded-xl text-xs font-bold hover:bg-indigo-700 transition shadow-lg flex items-center gap-1.5">
                            <i class="fa-solid fa-wand-magic-sparkles"></i> Customize
                        </a>
                    </div>
                </div>

                <!-- Template Information -->
                <div class="p-6 space-y-3">
                    <div class="flex items-center justify-between text-xs">
                        <span class="font-extrabold text-indigo-600 uppercase tracking-wider text-[11px]">${tmpl.category}</span>
                        <div class="flex items-center gap-1 text-amber-500 font-bold">
                            <i class="fa-solid fa-star text-xs"></i>
                            <span>${tmpl.rating}</span>
                            <span class="text-slate-400 font-normal">(${tmpl.downloads})</span>
                        </div>
                    </div>

                    <h3 class="text-lg font-bold text-slate-900 group-hover:text-indigo-600 transition font-heading leading-snug">
                        ${tmpl.name}
                    </h3>

                    <p class="text-xs sm:text-sm text-slate-600 line-clamp-2 leading-relaxed">
                        ${tmpl.description}
                    </p>

                    <!-- Feature Tags -->
                    <div class="flex flex-wrap gap-1.5 pt-1">
                        <span class="text-[10px] font-semibold bg-slate-100 text-slate-600 px-2.5 py-0.5 rounded-md">Fully Responsive</span>
                        <span class="text-[10px] font-semibold bg-slate-100 text-slate-600 px-2.5 py-0.5 rounded-md">WYSIWYG</span>
                        <span class="text-[10px] font-semibold bg-slate-100 text-slate-600 px-2.5 py-0.5 rounded-md">HTML5 / Tailwind</span>
                    </div>
                </div>
            </div>

            <!-- Bottom Actions -->
            <div class="px-6 py-4 bg-slate-50/80 border-t border-slate-100 flex items-center justify-between gap-3">
                <button onclick="openLivePreview('${tmpl.id}', '${escapeHtml(tmpl.name)}', '${tmpl.category}')" 
                        class="text-xs font-bold text-slate-600 hover:text-slate-900 transition flex items-center gap-1.5 py-2">
                    <i class="fa-regular fa-eye"></i> Quick Preview
                </button>
                <a href="editor.html?template=${tmpl.id}" 
                   class="px-4 py-2 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl text-xs font-bold transition shadow-sm flex items-center gap-1.5">
                    <i class="fa-solid fa-pen-to-square"></i> Use &amp; Edit
                </a>
            </div>
        </div>
    `).join('');
}

// Live Preview Modal
function openLivePreview(templateId, name, category) {
    const modal = document.getElementById('previewModal');
    const iframe = document.getElementById('previewIframe');
    const titleEl = document.getElementById('previewModalTitle');
    const badgeEl = document.getElementById('previewModalBadge');
    const editBtn = document.getElementById('previewModalEditBtn');

    titleEl.innerText = name;
    badgeEl.innerText = category;
    editBtn.href = `editor.html?template=${templateId}`;
    iframe.src = `/api/templates/${templateId}/preview`;

    setPreviewDevice('desktop');
    modal.classList.remove('hidden');
    document.body.style.overflow = 'hidden';
}

function closePreviewModal() {
    const modal = document.getElementById('previewModal');
    const iframe = document.getElementById('previewIframe');
    iframe.src = 'about:blank';
    modal.classList.add('hidden');
    document.body.style.overflow = '';
}

function setPreviewDevice(device) {
    const wrapper = document.getElementById('previewDeviceWrapper');
    const btnDesk = document.getElementById('devBtnDesktop');
    const btnTab = document.getElementById('devBtnTablet');
    const btnMob = document.getElementById('devBtnMobile');

    [btnDesk, btnTab, btnMob].forEach(b => {
        b.className = 'px-3 py-1.5 text-xs font-bold rounded-lg text-slate-400 hover:text-white transition flex items-center gap-1.5';
    });

    if (device === 'desktop') {
        wrapper.style.maxWidth = '100%';
        wrapper.style.width = '100%';
        btnDesk.className = 'px-3 py-1.5 text-xs font-bold rounded-lg bg-indigo-600 text-white transition flex items-center gap-1.5';
    } else if (device === 'tablet') {
        wrapper.style.maxWidth = '768px';
        wrapper.style.width = '768px';
        btnTab.className = 'px-3 py-1.5 text-xs font-bold rounded-lg bg-indigo-600 text-white transition flex items-center gap-1.5';
    } else if (device === 'mobile') {
        wrapper.style.maxWidth = '390px';
        wrapper.style.width = '390px';
        btnMob.className = 'px-3 py-1.5 text-xs font-bold rounded-lg bg-indigo-600 text-white transition flex items-center gap-1.5';
    }
}

// Switch between Gallery and My Sites
function switchTab(tab) {
    const galleryView = document.getElementById('galleryView');
    const mySitesView = document.getElementById('mySitesView');

    if (tab === 'my-sites') {
        galleryView.classList.add('hidden');
        mySitesView.classList.remove('hidden');
        loadMySavedSites();
        window.scrollTo({ top: 0, behavior: 'smooth' });
    } else {
        mySitesView.classList.add('hidden');
        galleryView.classList.remove('hidden');
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }
}

// Load Saved Sites from Backend
async function loadSavedSitesCount() {
    try {
        const res = await fetch('/api/websites');
        const list = await res.json();
        document.getElementById('savedCountBadge').innerText = list.length;
    } catch (e) {
        console.error(e);
    }
}

async function loadMySavedSites() {
    const container = document.getElementById('savedSitesGrid');
    try {
        const res = await fetch('/api/websites');
        const websites = await res.json();
        document.getElementById('savedCountBadge').innerText = websites.length;

        if (websites.length === 0) {
            container.innerHTML = `
                <div class="col-span-full py-20 text-center bg-white rounded-2xl border border-slate-200">
                    <div class="w-16 h-16 rounded-2xl bg-indigo-50 text-indigo-600 flex items-center justify-center text-2xl mx-auto mb-4">
                        <i class="fa-solid fa-shapes"></i>
                    </div>
                    <h3 class="text-xl font-bold text-slate-800">You haven't saved any websites yet</h3>
                    <p class="text-sm text-slate-500 max-w-md mx-auto mt-2">
                        Select any template from the gallery, make your customizations in Webloom Studio, and click "Save Site".
                    </p>
                    <button onclick="switchTab('gallery')" class="mt-6 px-5 py-2.5 bg-indigo-600 text-white rounded-xl text-sm font-bold shadow-sm hover:bg-indigo-700 transition">
                        Explore Templates
                    </button>
                </div>
            `;
            return;
        }

        container.innerHTML = websites.map(site => `
            <div class="bg-white rounded-2xl border border-slate-200 shadow-sm hover:shadow-lg transition p-6 flex flex-col justify-between space-y-5">
                <div>
                    <div class="flex items-center justify-between">
                        <span class="text-xs font-bold px-2.5 py-1 bg-indigo-50 text-indigo-700 rounded-md uppercase tracking-wider">
                            ${site.templateId || 'Custom'}
                        </span>
                        <span class="text-xs text-slate-400">ID: #${site.id}</span>
                    </div>

                    <h3 class="text-xl font-bold text-slate-900 mt-3 font-heading">${site.title}</h3>
                    <p class="text-xs text-slate-500 mt-1">Saved: ${new Date(site.updatedAt || site.createdAt).toLocaleString()}</p>
                </div>

                <div class="pt-4 border-t border-slate-100 flex flex-wrap items-center justify-between gap-2">
                    <div class="flex items-center gap-2">
                        <a href="editor.html?id=${site.id}" class="px-3.5 py-2 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl text-xs font-bold transition flex items-center gap-1.5 shadow-sm">
                            <i class="fa-solid fa-pen"></i> Edit in Studio
                        </a>
                        <a href="/api/websites/${site.id}/preview" target="_blank" class="px-3 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 rounded-xl text-xs font-semibold transition flex items-center gap-1">
                            <i class="fa-solid fa-arrow-up-right-from-square"></i> Preview
                        </a>
                        <a href="/api/websites/${site.id}/export" class="px-3 py-2 bg-emerald-50 hover:bg-emerald-100 text-emerald-700 border border-emerald-200 rounded-xl text-xs font-bold transition flex items-center gap-1">
                            <i class="fa-solid fa-download"></i> ZIP
                        </a>
                    </div>
                    <button onclick="deleteWebsite(${site.id})" class="text-red-500 hover:text-red-700 text-xs p-2 rounded-lg hover:bg-red-50 transition" title="Delete Website">
                        <i class="fa-regular fa-trash-can"></i>
                    </button>
                </div>
            </div>
        `).join('');
    } catch (err) {
        console.error('Error loading saved sites:', err);
    }
}

// Delete Website
async function deleteWebsite(id) {
    if (!confirm('Are you sure you want to delete this saved website?')) return;
    try {
        const res = await fetch(`/api/websites/${id}`, { method: 'DELETE' });
        if (res.ok) {
            showToast('Website deleted successfully');
            loadMySavedSites();
            loadSavedSitesCount();
        }
    } catch (e) {
        alert('Failed to delete website');
    }
}

// Helpers
function escapeHtml(str) {
    if (!str) return '';
    return str.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;").replace(/'/g, "&#039;");
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
