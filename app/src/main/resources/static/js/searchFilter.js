function setFilterElements(filterButton = HTMLButtonElement, filtersContainer = HTMLDivElement, filterForm = HTMLFormElement){
    filterButton.onclick = function (){
        filtersContainer.classList.toggle("visible");
        filterForm.classList.remove("visible");
    }

    setFilterHandlers(filtersContainer, filterForm);

    filterForm.onsubmit = function (event){
        event.preventDefault();

        let input = filterForm.querySelector("input");
        let inputValue = input.value;
        input.value = "";

        console.log(inputValue);

        filterForm.classList.toggle("visible");
    }
}

function setFilterHandlers(filtersContainer = HTMLDivElement, filterForm  = HTMLFormElement){
    let filtersContainerChildren = filtersContainer.children;
    for(let i = 0; i < filtersContainerChildren.length; i++){
        let element = filtersContainerChildren[i];
        element.onclick = function (){setSelectedFilter(element, filtersContainer, filterForm)}; 
    }
}

function setSelectedFilter(filter = HTMLButtonElement, filtersContainer = HTMLDivElement, filterForm = HTMLFormElement){
    if(!filter){
        return;
    }

    filtersContainer.classList.remove("visible");
    filterForm.classList.add("visible");
}