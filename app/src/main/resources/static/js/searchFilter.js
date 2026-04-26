function setFilterElements(
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
