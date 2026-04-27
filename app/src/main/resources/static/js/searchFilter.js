/* function setFilterElements(
  filterButton = HTMLButtonElement,
  filtersContainer = HTMLDivElement,
  filterForm = HTMLFormElement,
) {
  filterButton.onclick = function () {
    filtersContainer.classList.toggle("visible");
    filterForm.classList.remove("visible");
  };

  setFilterHandlers(filtersContainer, filterForm);
}

function setFilterHandlers(
  filtersContainer = HTMLDivElement,
  filterForm = HTMLFormElement,
) {
  let filters = filtersContainer.querySelectorAll("button.filter");

  filters.forEach((element) => {
    element.onclick = function () {
      setSelectedFilter(element, filtersContainer, filterForm);
    };
  });
}

function setSelectedFilter(
  filter = HTMLButtonElement,
  filtersContainer = HTMLDivElement,
  filterForm = HTMLFormElement,
) {
  if (!filter) {
    return;
  }

  filtersContainer.classList.remove("visible");
  filterForm.classList.add("visible");

  let filterInput = filterForm.querySelector("input");

  switch (filter.textContent) {
    case "categoria":
      filterInput.type = "text";
      filterInput.name = "category";
      filterInput.placeholder = "Informe a categoria.";
      break;
    case "data":
      filterInput.type = "date";
      filterInput.name = "date";
      filterInput.placeholder = "Informe a data.";
      break;
  }
}

 */

// ==========================================
// 1. Lógica de Interface (Seu código original)
// ==========================================

function setFilterElements(filterButton, filtersContainer, filterForm) {
  filterButton.onclick = function () {
    filtersContainer.classList.toggle("visible");
    filterForm.classList.remove("visible");
  };

  setFilterHandlers(filtersContainer, filterForm);
}

function setFilterHandlers(filtersContainer, filterForm) {
  let filters = filtersContainer.querySelectorAll("button.filter");

  filters.forEach((element) => {
    element.onclick = function () {
      setSelectedFilter(element, filtersContainer, filterForm);
    };
  });
}

function setSelectedFilter(filter, filtersContainer, filterForm) {
  if (!filter) {
    return;
  }

  filtersContainer.classList.remove("visible");
  filterForm.classList.add("visible");

  let filterInput = filterForm.querySelector("input:not([type='submit'])"); // Pega o input de dados, não o botão

  switch (filter.textContent) {
    case "categoria":
      filterInput.type = "text";
      filterInput.name = "category";
      filterInput.placeholder = "Informe a categoria.";
      break;
    case "data":
      filterInput.type = "date";
      filterInput.name = "date";
      filterInput.placeholder = "Informe a data.";
      break;
  }
}

// ==========================================
// 2. Integração com a API
// ==========================================

// Função principal de requisição
async function loadEvents() {
  // Pega os elementos
  const container = document.querySelector(".cards-event"); // Certifique-se de que este ID está na sua div de grid de eventos no HTML
  const searchBar = document.querySelector(".container-search-bar-and-filter input.search-bar");
  const filterInput = document.querySelector(
    '.filter-form input:not([type="submit"])',
  );
  const filterForm = document.querySelector(".filter-form");

  // Constrói os parâmetros da URL para a API
  const params = new URLSearchParams();

  // Se o formulário de filtro estiver visível e o input tiver valor, adiciona na busca
  if (filterForm.classList.contains("visible") && filterInput.value) {
    params.append(filterInput.name, filterInput.value);
    filterInput.value = "";
  }

  // Se quiser integrar a barra de pesquisa de texto também (assumindo que o backend aceite 'title')
  if (searchBar && searchBar.value) {
    params.append("title", searchBar.value);
  }

  try {
    // Renderiza estado de carregamento
    if (container)
      container.innerHTML =
        '<p style="grid-column: 1 / -1; text-align: center;">Buscando eventos...</p>';

    // Chama a API
    const response = await fetch(`/api/events?${params.toString()}`);
    const events = await response.json();

    if (events.length === 0) {
      container.innerHTML =
        '<p style="grid-column: 1 / -1; text-align: center;">Nenhum evento encontrado.</p>';
      return;
    }

    // Renderiza os cards atualizados
    container.innerHTML = events
      .map(
        (event) => `<a
            class="container-card"
            href="/event/${event.title}"
          >
            <div class="card">
                <img src="${event.imageBase64 || "/img/arena_banner.svg"}" alt="${event.title}" />
                <p class="date">${new Date(event.date).toLocaleDateString()} ${event.category || ""}</p>
                <p class="title">${event.title}</p>
                <p class="description">${event.description}</p>
            </div>
            </a>`,
      )
      .join("");
  } catch (error) {
    console.error("Erro ao carregar eventos:", error);
    if (container)
      container.innerHTML =
        '<p style="grid-column: 1 / -1; text-align: center;">Erro ao carregar os eventos da Arena.</p>';
  }
}

// ==========================================
// 3. Inicialização no carregamento da página
// ==========================================

document.addEventListener("DOMContentLoaded", () => {
  // 1. Inicializa os controles de interface
  const filterButton = document.querySelector(".filter-button");
  const filtersContainer = document.querySelector(".container-filters");
  const filterForm = document.querySelector(".filter-form");

  if (filterButton && filtersContainer && filterForm) {
    setFilterElements(filterButton, filtersContainer, filterForm);
  }

  // 2. Intercepta o "Submit" do formulário de filtros para usar a API em vez de recarregar a página
  if (filterForm) {
    filterForm.addEventListener("submit", (e) => {
      e.preventDefault(); // Impede que o HTML faça o GET padrão que recarrega a tela
      loadEvents(); // Dispara nossa função assíncrona
    });
  }

  // 3. (Opcional) Executa a busca automaticamente enquanto o usuário digita na barra de pesquisa principal
  const searchBar = document.querySelector(".search-bar");
  if (searchBar) {
    let timeout = null;
    searchBar.addEventListener("input", () => {
      clearTimeout(timeout);
      timeout = setTimeout(loadEvents, 500); // Aguarda meio segundo após parar de digitar
    });
  }

  // 4. Carrega todos os eventos ao abrir a página pela primeira vez
  loadEvents();
});
