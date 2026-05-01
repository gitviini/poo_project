function setFilterElements(filterButton, filtersContainer) {
  filterButton.onclick = function () {
    const isVisible = filtersContainer.style.display === "flex";
    filtersContainer.style.display = isVisible ? "none" : "flex";
  };
}

function getFilterInputs() {
  return Array.from(
    document.querySelectorAll(".container-filters input[name]"),
  );
}

function updateClearButton() {
  const clearBtn = document.querySelector(".filter-clear");
  if (!clearBtn) return;
  const hasValue = getFilterInputs().some((input) => input.value);
  clearBtn.hidden = !hasValue;
}

async function loadScheduledEvents() {
  const container = document.querySelector(".cards-scheduled-event");
  const searchBar = document.querySelector(
    ".container-search-bar-and-filter input.search-bar",
  );
  const userId = document.getElementById("userId").innerText;

  const params = new URLSearchParams();
  params.append("userId", userId);

  getFilterInputs().forEach((input) => {
    if (input.value) params.append(input.name, input.value);
  });

  if (searchBar && searchBar.value) {
    params.append("title", searchBar.value);
  }

  try {
    if (container)
      container.innerHTML =
        '<p style="grid-column: 1 / -1; text-align: center;">Buscando agendamentos...</p>';

    const response = await fetch(`/api/scheduled-events?${params.toString()}`);
    const scheduledEvents = await response.json();

    if (scheduledEvents.length === 0) {
      container.innerHTML =
        '<p style="grid-column: 1 / -1; text-align: center;">Nenhum agendamento encontrado.</p>';
      return;
    }

    container.innerHTML = scheduledEvents
      .map(
        // se : ScheduledEvent
        (se) => `<div class="container-card">
            <div class="card">
                <img src="${se.event.imageBase64 || "/img/arena_banner.svg"}" alt="${se.event.title}" />
                <p class="date">${new Date(se.event.date).toLocaleDateString()} ${se.event.category || ""}</p>
                <p class="title">${se.event.title}</p>
                <p class="description">${se.event.description}</p>
                </div>
                </div>`,
      )
      .join("");

    //<p class="date">${new Date(se.event.date).toLocaleDateString()} - Area: ${se.arenaArea || "N/A"}</p>
    //<p class="description">Assentos: ${se.seats ? se.seats.join(", ") : "N/A"}</p>
    //<p class="price" style="font-weight: bold; color: var(--primary-color);">Total: ${se.currency} ${se.totalPrice || "0.00"}</p>
  } catch (error) {
    console.error("Erro ao carregar agendamentos:", error);
    if (container)
      container.innerHTML =
        '<p style="grid-column: 1 / -1; text-align: center;">Erro ao carregar seus agendamentos.</p>';
  }
}

document.addEventListener("DOMContentLoaded", () => {
  const filterButton = document.querySelector(".filter-button");
  const filtersContainer = document.querySelector(".container-filters");

  if (filterButton && filtersContainer) {
    setFilterElements(filterButton, filtersContainer);
  }

  const applyBtn = document.querySelector(".filter-apply");
  if (applyBtn) {
    applyBtn.addEventListener("click", () => {
      updateClearButton();
      loadScheduledEvents();
    });
  }

  const clearBtn = document.querySelector(".filter-clear");
  if (clearBtn) {
    clearBtn.addEventListener("click", () => {
      getFilterInputs().forEach((input) => (input.value = ""));
      updateClearButton();
      loadScheduledEvents();
    });
  }

  getFilterInputs().forEach((input) => {
    input.addEventListener("input", updateClearButton);
  });

  const searchBar = document.querySelector(".search-bar");
  if (searchBar) {
    let timeout = null;
    searchBar.addEventListener("input", () => {
      clearTimeout(timeout);
      timeout = setTimeout(loadScheduledEvents, 500);
    });
  }

  loadScheduledEvents();
});
