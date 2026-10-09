
"use strict";

(function () {
    const API = {
        majors: "/api/catalog/majors",
        mentors: "/api/catalog/mentors",
        articles: "/api/knowledge/articles",
        events: "/api/events"
    };

    const state = {
        majors: [],
        mentors: [],
        articles: [],
        events: []
    };

    const CONFIG = {
        requestTimeout: 10000
    };

    const SECTIONS = {
        majors: {
            container: "majors-list",
            empty: "Hələlik ixtisas məlumatı yoxdur."
        },
        mentors: {
            container: "mentors-list",
            empty: "Hələlik mentor məlumatı yoxdur."
        },
        articles: {
            container: "articles-list",
            empty: "Hələlik məqalə yoxdur."
        },
        events: {
            container: "events-list",
            empty: "Hələlik görüş məlumatı yoxdur."
        }
    };

    function getList(data) {
        if (Array.isArray(data)) return data;
        if (!data || typeof data !== "object") return [];

        for (const key of ["content", "items", "data"]) {
            if (Array.isArray(data[key])) return data[key];
        }

        return [];
    }

    function getField(item, names) {
        if (!item || typeof item !== "object") return "";

        for (const name of names) {
            const value = item[name];

            if (value === null || value === undefined || value === "") {
                continue;
            }

            if (Array.isArray(value)) {
                return value.map(function (part) {
                    return typeof part === "object"
                        ? ""
                        : String(part);
                }).filter(Boolean).join(", ");
            }

            if (typeof value === "object") continue;

            return String(value);
        }

        return "";
    }

    function createElement(tag, className, text) {
        const element = document.createElement(tag);

        if (className) element.className = className;
        if (text !== undefined) element.textContent = String(text);

        return element;
    }

    function showMessage(container, message, isError) {
        if (!container) return;

        container.replaceChildren(
            createElement(
                "p",
                isError ? "status-message error-message" : "status-message",
                message
            )
        );
    }

    async function fetchData(url) {
        const controller = new AbortController();
        const timer = setTimeout(
            function () { controller.abort(); },
            CONFIG.requestTimeout
        );

        try {
            const response = await fetch(url, {
                headers: { "Accept": "application/json" },
                signal: controller.signal
            });

            if (!response.ok) {
                throw new Error("HTTP " + response.status);
            }

            return await response.json();
        } finally {
            clearTimeout(timer);
        }
    }

    function createCard(icon, title, category) {
        const card = createElement("article", "data-card");
        card.appendChild(createElement("div", "card-icon", icon));

        if (category) {
            card.appendChild(createElement("span", "card-tag", category));
        }

        card.appendChild(createElement("h3", "", title));
        return card;
    }

    function addDescription(card, text) {
        if (text) card.appendChild(createElement("p", "", text));
    }

    function addMeta(card, text) {
        if (text) card.appendChild(createElement("div", "card-meta", text));
    }

    function createMajorCard(item) {
        const card = createCard(
            "🎓",
            getField(item, ["name", "title"]) || "İxtisas",
            getField(item, ["category"])
        );

        addDescription(
            card,
            getField(item, ["description"]) ||
            "Bu ixtisas haqqında əlavə məlumat yoxdur."
        );

        const subjects = getField(item, ["requiredSubjects", "required_subjects"]);
        const careers = getField(item, ["careers"]);

        if (subjects) addMeta(card, "Tələb olunan fənlər: " + subjects);
        if (careers) addMeta(card, "Karyera imkanları: " + careers);

        return card;
    }

    function createMentorCard(item) {
        const card = createCard(
            "🤝",
            getField(item, ["fullName", "full_name", "name"]) || "Mentor",
            getField(item, ["category"]) || "Mentor"
        );

        const profession = getField(item, ["profession"]);
        const bio = getField(item, ["bio", "description"]);

        addDescription(card, profession);
        addDescription(card, bio || "Bu mentor haqqında əlavə məlumat yoxdur.");

        return card;
    }

    function createArticleCard(item) {
        const card = createCard(
            "📚",
            getField(item, ["title", "name"]) || "Məqalə",
            getField(item, ["category"]) || "Karyera bilikləri"
        );

        const content = getField(item, ["summary", "description", "content"]);
        addDescription(
            card,
            content
                ? content.slice(0, 260) + (content.length > 260 ? "..." : "")
                : "Məqalənin əlavə məlumatı yoxdur."
        );

        return card;
    }

    function formatDate(value) {
        if (!value) return "";

        let date;

        if (Array.isArray(value) && value.length >= 3) {
            date = new Date(
                value[0], value[1] - 1, value[2],
                value[3] || 0, value[4] || 0, value[5] || 0
            );
        } else {
            date = new Date(value);
        }

        if (Number.isNaN(date.getTime())) return String(value);

        return new Intl.DateTimeFormat("az-AZ", {
            day: "2-digit",
            month: "long",
            year: "numeric",
            hour: "2-digit",
            minute: "2-digit"
        }).format(date);
    }

    function createEventCard(item) {
        const card = createCard(
            "📅",
            getField(item, ["title", "name"]) || "Görüş",
            "Görüş"
        );

        addDescription(
            card,
            getField(item, ["description"]) ||
            "Görüş haqqında əlavə məlumat yoxdur."
        );

        const date = getField(item, [
            "dateTime", "date_time", "eventDate", "startDate"
        ]);
        const location = getField(item, ["location"]);
        const capacity = getField(item, ["capacity"]);

        if (date) addMeta(card, "Tarix: " + formatDate(date));
        if (location) addMeta(card, "Məkan: " + location);
        if (capacity) addMeta(card, "İştirakçı tutumu: " + capacity);

        return card;
    }

    function renderList(key, items, creator) {
        const config = SECTIONS[key];
        const container = document.getElementById(config.container);

        if (!container) return;

        container.replaceChildren();

        if (!items.length) {
            showMessage(container, config.empty, false);
            return;
        }

        const fragment = document.createDocumentFragment();

        items.forEach(function (item) {
            fragment.appendChild(creator(item));
        });

        container.appendChild(fragment);
    }

    function updateMajorCount(count, label) {
        const element = document.getElementById("majors-count");
        if (!element) return;

        element.textContent = label || ("İxtisas sayı: " + count);
    }

    function filterMajors() {
        const input = document.getElementById("major-search");
        if (!input) return;

        const query = input.value.trim().toLocaleLowerCase("az");

        const filtered = state.majors.filter(function (item) {
            const text = [
                getField(item, ["name", "title"]),
                getField(item, ["category"]),
                getField(item, ["description"]),
                getField(item, ["requiredSubjects", "required_subjects"]),
                getField(item, ["careers"]),
                getField(item, ["universities"])
            ].join(" ").toLocaleLowerCase("az");

            return text.includes(query);
        });

        renderList("majors", filtered, createMajorCard);
        updateMajorCount(
            filtered.length,
            query
                ? "Nəticə: " + filtered.length
                : "İxtisas sayı: " + state.majors.length
        );
    }

    async function loadSection(key, url, creator) {
        const config = SECTIONS[key];
        const container = document.getElementById(config.container);

        if (!container) return;

        showMessage(container, "Məlumat yüklənir...", false);

        try {
            const data = await fetchData(url);
            state[key] = getList(data);

            if (key === "majors") {
                filterMajors();
            } else {
                renderList(key, state[key], creator);
            }

            console.info(key + ": " + state[key].length + " məlumat yükləndi.");
        } catch (error) {
            console.error(key + " yüklənmə xətası:", error);

            showMessage(
                container,
                error.name === "AbortError"
                    ? "Server cavabı gecikdi. Sonra yenidən yoxla."
                    : "Məlumat yüklənmədi. API ünvanını və serveri yoxla.",
                true
            );

            if (key === "majors") updateMajorCount(0, "İxtisaslar yüklənmədi");
        }
    }

    function initializeApp() {
        const year = document.getElementById("current-year");
        if (year) year.textContent = new Date().getFullYear();

        const search = document.getElementById("major-search");
        if (search) search.addEventListener("input", filterMajors);

        loadSection("majors", API.majors, createMajorCard);
        loadSection("mentors", API.mentors, createMentorCard);
        loadSection("articles", API.articles, createArticleCard);
        loadSection("events", API.events, createEventCard);
    }

    document.addEventListener("DOMContentLoaded", initializeApp);
})();