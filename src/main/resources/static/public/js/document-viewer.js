/**
 * ClusterBR | arcbrth@gmail.com | 2024-1210
 */
const baseUrl = '/api/documents'; 

const EMPTY = '';
const ALL_DOCUMENTS_TAB = document.getElementById('allDocumentsTab');
const SEARCH_TAB = document.getElementById('searchTab');
const ALL_DOCUMENTS_CONTENT = document.getElementById('allDocumentsContent');
const SEARCH_CONTENT = document.getElementById('searchContent');

const DOCUMENT_LIST = document.getElementById('allDocumentsList');
const SEARCH_BUTTON = document.getElementById('searchButton');
const SEARCH_INPUT = document.getElementById('searchInput');
const SEARCH_RESULTS = document.getElementById('searchResultsList');
const DOCUMENT_DETAILS = document.getElementById('documentDetails');

//-- events

window.onload = function () {
    getAllDocuments();
};

ALL_DOCUMENTS_TAB.addEventListener('click', function () {
    ALL_DOCUMENTS_TAB.classList.add('active');
    SEARCH_TAB.classList.remove('active');
    ALL_DOCUMENTS_CONTENT.classList.remove('hidden');
    SEARCH_CONTENT.classList.add('hidden');
    getAllDocuments();
});

SEARCH_TAB.addEventListener('click', function () {
    SEARCH_TAB.classList.add('active');
    ALL_DOCUMENTS_TAB.classList.remove('active');
    SEARCH_CONTENT.classList.remove('hidden');
    ALL_DOCUMENTS_CONTENT.classList.add('hidden');
});


SEARCH_INPUT.addEventListener('keydown', function (event) {
    if (event.key === 'Enter') {
        performSearch();
    }
});

SEARCH_BUTTON.addEventListener('click', function () {
    performSearch();
});

//-- data-methods

function renderXmlHeaderSimple(content) {
    if(content == null) return EMPTY;
    let html = content;
    html = html.includes('<header>') ? html.replace(/<header>/g, '<h3>') : html;
    html = html.includes('</header>') ? html.replace(/<\/header>/g, '</h3>') : html;
    html = html.includes('<title>') ? html.replace(/<title>/g, '<label class="header-left-item">') : html;
    html = html.includes('</title>') ? html.replace(/<\/title>/g, '</label>') : html;
    return html;
}

function renderXmlHeader(content) {
    if(content == null) return EMPTY;
    let html = content;
    html = html.includes('<header>') ? html.replace(/<header>/g, EMPTY) : html;
    html = html.includes('</header>') ? html.replace(/<\/header>/g, EMPTY) : html;
    html = html.includes('<title>') ? html.replace(/<title>/g, '<label class="header-right-item">') : html;
    html = html.includes('</title>') ? html.replace(/<\/title>/g, '</label>') : html;
    return html;
}

function renderXmlContent(content)  {
    if(content == null) return EMPTY;
    let html = content;
    html = html.includes('<content>') ? html.replace(/<content>/g, '<div class="content">') : html;
    html = html.includes('<steps>') ? html.replace(/<steps>/g, '<ul class="steps">') : html;
    html = html.includes('</steps>') ? html.replace(/<\/steps>/g, '</ul>') : html;

    html = html.includes('<step>') ? html.replace(/<step>/g, '<li class="step">') : html;
    html = html.includes('</step>') ? html.replace(/<\/step>/g, '</li>') : html;
    html = html.includes('</content>') ? html.replace(/<\/content>/g, '</div>') : html;
    return html;
}

function getDocument(idSource) {
    fetch(`${baseUrl}/id/${idSource}`)
        .then(response => response.json())
        .then(data => {
            console.log("getDocument()->data:", data);
            if (data.error) {
                DOCUMENT_DETAILS.innerHTML = `<p>Error: ${data.error}</p>`;
                return;
            }            
            let content = `
                <h2>${data.documentType} - ${data.category}</h2>
                <p><strong>Header:</strong></p> ${renderXmlHeader(data.xmlHeader)}
                <p><strong>Content:</strong></p> ${renderXmlContent(data.xmlContent)}
                <p><strong>Created At:</strong></p> ${new Date(data.createdAt).toLocaleString()}
            `;
            DOCUMENT_DETAILS.innerHTML = content;
        })
        .catch(err => {
            DOCUMENT_DETAILS.innerHTML = `<p>Error fetching document: ${err}</p>`;
        });
}

function getAllDocuments(limitRows = 0) {
    fetch(`${baseUrl}/all?limit=${limitRows}`)
        .then(response => response.json())
        .then(data => {
            console.log("getAllDocuments()->data:", data);
            if (data.error) {
                DOCUMENT_LIST.innerHTML = `<li>Error: ${data.error}</li>`;
                return;
            }
            let content = '';
            data.forEach(item => {
                content +=`
                    <li onclick="getDocument('${item.idSource}')">
                        <strong>${item.documentType}</strong> &rarr; ${item.category} &rarr; ${renderXmlHeaderSimple(data.xmlHeader)}
                    </li>`
                    ;
            });
            DOCUMENT_LIST.innerHTML = content;
        })
        .catch(err => {
            DOCUMENT_LIST.innerHTML = `<li>Error fetching documents: ${err}</li>`;
        });
}

function performSearch() {

    const keyword = SEARCH_INPUT.value.trim();
    if (!keyword) {
        SEARCH_RESULTS.innerHTML = '<li>Keyword must not be empty.</li>';
        return;
    }

    fetch(`${baseUrl}/search?keyword=${keyword}`)
        .then(response => response.json())
        .then(data => {            
            console.log("performSearch()->data:", data);
            if (data.error) {
                SEARCH_RESULTS.innerHTML = `<li> ${data.error}</li>`;
                return;
            }
            if (data.length === 0) {
                SEARCH_RESULTS.innerHTML = '<li>No documents found for the given keyword.</li>';
                return;
            }
            let content = '';
            data.forEach(item => {
                content += `
                    <li onclick="getDocument('${item.idSource}')">
                        <strong>${item.documentType}</strong> &rarr; ${item.category} &rarr; ${renderXmlHeaderSimple(data.xmlHeader)}
                    </li>`;
            });
            SEARCH_RESULTS.innerHTML = content;
        })
        .catch(err => {
            SEARCH_RESULTS.innerHTML = `<li> ${err}</li>`;
        });    
}

