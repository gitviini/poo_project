function setFilterElements(filterButton, filtersContainer) {
  filterButton.onclick = function () {
    filtersContainer.classList.toggle("visible");
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

async function loadEvents() {
  const container = document.querySelector(".cards-event");
  const searchBar = document.querySelector(
    ".container-search-bar-and-filter input.search-bar",
  );

  const params = new URLSearchParams();

  getFilterInputs().forEach((input) => {
    if (input.value) params.append(input.name, input.value);
  });

  if (searchBar && searchBar.value) {
    params.append("title", searchBar.value);
  }

  try {
    if (container)
      container.innerHTML =
        '<p style="grid-column: 1 / -1; text-align: center;">Buscando eventos...</p>';

    const response = await fetch(`/api/events?${params.toString()}`);
    const events = await response.json();

    if (events.length === 0) {
      container.innerHTML =
        '<p style="grid-column: 1 / -1; text-align: center;">Nenhum evento encontrado.</p>';
      return;
    }

    container.innerHTML = events
      .map((event) => {
        const isAdmin = window.location.pathname.includes("/admin/dashboard");
        const adminActions = isAdmin
          ? `<div class="container-buttons">
                <a href="/admin/event/edit/${event.id}" class="btn second">Editar</a>
                <form action="/admin/event/delete/${event.id}" method="POST">
                    <button type="submit" class="btn second danger" onclick="return confirm('Tem certeza que deseja deletar este evento?')">Deletar</button>
                </form>
             </div>`
          : "";

        return `<div class="container-card ${isAdmin ? "admin" : ""}">
          <a href="/event/${event.title}" style="text-decoration: none; color: inherit;">
          <div class="card">
          <img src="${event.imageBase64 || "/img/arena_banner.svg"}" alt="${event.title}" />
          <p class="date">${new Date(event.date).toLocaleDateString()} ${event.category || ""}</p>
          <p class="title">${event.title}</p>
          <p class="description">${event.description}</p>
          </div>
          </a>
          ${adminActions}
          </div>`;
      })
      .join("");
  } catch (error) {
    console.error("Erro ao carregar eventos:", error);
    if (container)
      container.innerHTML =
        '<p style="grid-column: 1 / -1; text-align: center;">Erro ao carregar os eventos da Arena.</p>';
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
      loadEvents();
    });
  }

  const clearBtn = document.querySelector(".filter-clear");
  if (clearBtn) {
    clearBtn.addEventListener("click", () => {
      getFilterInputs().forEach((input) => (input.value = ""));
      updateClearButton();
      loadEvents();
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
      timeout = setTimeout(loadEvents, 500);
    });
  }

  loadEvents();
});
