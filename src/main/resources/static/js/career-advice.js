
"use strict";

(function () {
    const ENDPOINT = "/api/career/advice";
    const REQUEST_TIMEOUT = 180000;

    const form = document.getElementById("career-advice-form");
    if (!form) return;

    const interestsInput = document.getElementById("interests");
    const subjectsInput = document.getElementById("favoriteSubjects");
    const skillsInput = document.getElementById("skills");
    const goalsInput = document.getElementById("goals");

    const submitButton = document.getElementById("advice-submit");
    const buttonLabel = submitButton.querySelector(".button-label");
    const emptyState = document.getElementById("advice-empty");
    const loadingState = document.getElementById("advice-loading");
    const errorBox = document.getElementById("advice-error");
    const results = document.getElementById("advice-results");
    const cardsContainer = document.getElementById("advice-cards");
    const copyButton = document.getElementById("copy-advice");

    let latestAdvice = "";
    let activeController = null;

    function show(element, visible) {
        element.hidden = !visible;
    }

    function setLoading(loading) {
        submitButton.disabled = loading;
        buttonLabel.textContent = loading
            ? "AI karyera planını hazırlayır..."
            : "✦ AI ilə karyera planımı hazırla";

        show(loadingState, loading);

        if (loading) {
            show(emptyState, false);
            show(errorBox, false);
            show(results, false);
        }
    }

    function showError(message) {
        errorBox.textContent = message;
        show(errorBox, true);
        show(emptyState, false);
        show(results, false);
    }

    function normalizeText(value) {
        return String(value || "")
            .replace(/\r\n/g, "\n")
            .replace(/\u00a0/g, " ")
            .trim();
    }

    function extractAdvice(data) {
        if (typeof data === "string" && data.trim()) {
            return data.trim();
        }

        if (data && typeof data.advice === "string" && data.advice.trim()) {
            return data.advice.trim();
        }

        if (data && typeof data.content === "string" && data.content.trim()) {
            return data.content.trim();
        }

        if (data && typeof data.message === "string" && data.message.trim()) {
            return data.message.trim();
        }

        throw new Error("Server cavabında AI məsləhəti tapılmadı.");
    }

    function splitSections(text) {
        const patterns = [
            {
                title: "Üç uyğun ixtisas",
                number: "01",
                regex: /(?:\*{1,2}\s*)?(?:\d+[.)]\s*)?(?:ÜÇ|3)\s+UYĞUN\s+İXTİSAS(?:LAR)?(?:\s*\*{1,2})?/i
            },
            {
                title: "İnkişaf planı",
                number: "02",
                regex: /(?:\*{1,2}\s*)?(?:\d+[.)]\s*)?İNKİŞAF\s+PLANI(?:\s*\*{1,2})?/i
            },
            {
                title: "Yekun tövsiyə",
                number: "03",
                regex: /(?:\*{1,2}\s*)?(?:\d+[.)]\s*)?YEKUN\s+TÖVSİYƏ(?:\s*\*{1,2})?/i
            }
        ];

        const found = patterns.map(function (item) {
            const match = item.regex.exec(text);

            return {
                title: item.title,
                number: item.number,
                index: match ? match.index : -1,
                end: match ? match.index + match[0].length : -1
            };
        }).filter(function (item) {
            return item.index >= 0;
        }).sort(function (a, b) {
            return a.index - b.index;
        });

        if (found.length === 0) {
            return [{
                title: "Fərdi karyera məsləhətin",
                number: "✦",
                content: text
            }];
        }

        const sections = [];

        if (found[0].index > 0) {
            const introduction = text.slice(0, found[0].index).trim();
            if (introduction) {
                sections.push({
                    title: "Ümumi qiymətləndirmə",
                    number: "✦",
                    content: introduction
                });
            }
        }

        found.forEach(function (item, index) {
            const nextIndex = index + 1 < found.length
                ? found[index + 1].index
                : text.length;

            const content = text.slice(item.end, nextIndex).trim();

            if (content) {
                sections.push({
                    title: item.title,
                    number: item.number,
                    content: content
                });
            }
        });

        return sections;
    }

    function appendFormattedText(container, text) {
        const lines = normalizeText(text).split("\n");
        let currentList = null;

        lines.forEach(function (rawLine) {
            const line = rawLine.trim();

            if (!line) {
                currentList = null;
                return;
            }

            const heading = line.match(/^#{1,4}\s+(.+)$/);

            if (heading) {
                currentList = null;
                const h = document.createElement("h4");
                h.textContent = heading[1].replace(/\*+/g, "").trim();
                container.appendChild(h);
                return;
            }

            const bullet = line.match(/^(?:[-*•]|\d+[.)])\s+(.+)$/);

            if (bullet) {
                if (!currentList) {
                    currentList = document.createElement("ul");
                    container.appendChild(currentList);
                }

                const li = document.createElement("li");
                li.textContent = bullet[1].replace(/\*\*/g, "").trim();
                currentList.appendChild(li);
                return;
            }

            currentList = null;

            const paragraph = document.createElement("p");
            paragraph.textContent = line.replace(/\*\*/g, "").trim();
            container.appendChild(paragraph);
        });
    }

    function renderAdvice(text) {
        latestAdvice = text;
        cardsContainer.replaceChildren();

        const sections = splitSections(text);

        sections.forEach(function (section, index) {
            const details = document.createElement("details");
            details.className = "advice-card";
            details.open = index === 0;

            const summary = document.createElement("summary");

            const number = document.createElement("span");
            number.className = "advice-number";
            number.textContent = section.number;

            const title = document.createElement("span");
            title.textContent = section.title;

            summary.append(number, title);

            const content = document.createElement("div");
            content.className = "advice-card-content";
            appendFormattedText(content, section.content);

            details.append(summary, content);
            cardsContainer.appendChild(details);
        });

        show(loadingState, false);
        show(emptyState, false);
        show(errorBox, false);
        show(results, true);

        results.scrollIntoView({ behavior: "smooth", block: "nearest" });
    }

    function getErrorMessage(error, status) {
        if (error && error.name === "AbortError") {
            return "Sorğu vaxt limitini keçdi. Ollama modelinin işlədiyini yoxla və yenidən cəhd et.";
        }

        if (status === 400) {
            return "Sorğu məlumatları qəbul edilmədi. Formanı və CareerAdviceRequest sahələrini yoxla.";
        }

        if (status === 404) {
            return "AI endpoint tapılmadı. CareerAdviceController daxilində POST /api/career/advice ünvanını yoxla.";
        }

        if (status >= 500) {
            return "Server AI məsləhətini hazırlaya bilmədi. IntelliJ konsolunda və Ollama xidmətində xətanı yoxla.";
        }

        if (error instanceof TypeError) {
            return "Serverlə əlaqə qurulmadı. Saytı http://localhost:8080 ünvanından aç və backend-in işlədiyini yoxla.";
        }

        return error && error.message
            ? error.message
            : "Gözlənilməz xəta baş verdi. Yenidən cəhd et.";
    }

    form.addEventListener("submit", async function (event) {
        event.preventDefault();

        if (!form.reportValidity()) return;

        if (activeController) {
            activeController.abort();
        }

        activeController = new AbortController();
        const timeoutId = setTimeout(function () {
            activeController?.abort();
        }, REQUEST_TIMEOUT);

        const payload = {
            interests: interestsInput.value.trim(),
            favoriteSubjects: subjectsInput.value.trim(),
            skills: skillsInput.value.trim(),
            goals: goalsInput.value.trim()
        };

        setLoading(true);

        try {
            const response = await fetch(ENDPOINT, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json"
                },
                body: JSON.stringify(payload),
                signal: activeController.signal
            });

            const responseText = await response.text();

            let data = responseText;

            if (responseText) {
                try {
                    data = JSON.parse(responseText);
                } catch (_) {
                    data = responseText;
                }
            }

            if (!response.ok) {
                const detail = data && typeof data === "object"
                    ? (data.message || data.error || "")
                    : "";

                throw Object.assign(
                    new Error(detail || "Server sorğunu qəbul etmədi."),
                    { status: response.status }
                );
            }

            renderAdvice(extractAdvice(data));
        } catch (error) {
            console.error("AI karyera məsləhəti xətası:", error);
            showError(getErrorMessage(error, error.status));
        } finally {
            clearTimeout(timeoutId);
            activeController = null;
            setLoading(false);
        }
    });

    copyButton.addEventListener("click", async function () {
        if (!latestAdvice) return;

        try {
            await navigator.clipboard.writeText(latestAdvice);
            copyButton.textContent = "Köçürüldü ✓";
        } catch (_) {
            copyButton.textContent = "Köçürmək mümkün olmadı";
        }

        setTimeout(function () {
            copyButton.textContent = "Mətni köçür";
        }, 2000);
    });
})();