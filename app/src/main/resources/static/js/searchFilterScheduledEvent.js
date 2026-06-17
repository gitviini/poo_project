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

async function loadNotifications() {
  const container = document.getElementById("notifications-container");
  const badge = document.getElementById("notification-badge");

  try {
    const response = await fetch("/api/notifications");
    const notifications = await response.json();

    if (notifications.length === 0) {
      container.innerHTML = '<p style="font-size: 0.9rem; color: #666;">Nenhuma notificação.</p>';
      badge.style.display = "none";
      return;
    }

    let unreadCount = 0;
    container.innerHTML = notifications.map(n => {
      if (!n.isRead) unreadCount++;
      return `<div class="notification-item" style="padding: 10px; border-bottom: 1px solid #eee; ${n.isRead ? '' : 'background: #f9f9f9;'}">
        <p style="margin: 0; font-size: 0.9rem;">${n.message}</p>
        <small style="color: #999;">${new Date(n.createdAt).toLocaleString()}</small>
        ${n.isRead ? '' : `<button onclick="markAsRead('${n.id}')" style="font-size: 0.7rem; margin-left: 10px; cursor: pointer;">Lido</button>`}
      </div>`;
    }).join("");

    if (unreadCount > 0) {
      badge.innerText = unreadCount;
      badge.style.display = "inline-block";
    } else {
      badge.style.display = "none";
    }
  } catch (error) {
    console.error("Erro ao carregar notificações:", error);
  }
}

async function markAsRead(id) {
  await fetch(`/api/notifications/${id}/read`, { method: 'POST' });
  loadNotifications();
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

    const [eventsRes, visitsRes] = await Promise.all([
      fetch(`/api/scheduled-events?${params.toString()}`),
      fetch(`/api/scheduled-visits?userId=${userId}`)
    ]);

    const scheduledEvents = await eventsRes.json();
    const scheduledVisits = await visitsRes.json();

    if (scheduledEvents.length === 0 && scheduledVisits.length === 0) {
      container.innerHTML =
        '<p style="grid-column: 1 / -1; text-align: center;">Nenhum agendamento encontrado.</p>';
      return;
    }

    console.log(scheduledEvents)

    let html = scheduledEvents.map(se => `
      <div class="container-card scheduled">
        <div class="card">
          <img src="${se.event.imageBase64 || "/img/arena_banner.svg"}" alt="${se.event.title}" />
          <p class="date">${new Date(se.event.date).toLocaleDateString()} ${se.event.category || ""}</p>
          <p class="title">${se.event.title}</p>
          <p class="description">Preço: ${se.currency} ${se.totalPrice}</p>
          <p class="description">Assentos: ${se.seats || ""}</p>
        </div>
        <div class="container-buttons">
          <form action="/scheduled-event/cancel/${se.id}" method="POST" onsubmit="return confirm('Deseja cancelar este agendamento?')">
            <button type="submit" class="btn second danger">Cancelar</button>
          </form>
        </div>
      </div>`).join("");

    html += scheduledVisits.map(sv => `
      <div class="container-card scheduled">
        <div class="card">
          <img src="/img/arena_banner.svg" alt="Visita" />
          <p class="date">${new Date(sv.visit.date).toLocaleDateString()} às ${sv.visit.time}</p>
          <p class="title">Visita Guiada</p>
          <p class="description">Pessoas: ${sv.numberOfPeople}</p>
          <p class="description">${sv.visit.description}</p>
        </div>
        <div class="container-buttons">
          <form action="/scheduled-visit/cancel/${sv.id}" method="POST" onsubmit="return confirm('Deseja cancelar este agendamento?')">
            <button type="submit" class="btn second danger">Cancelar</button>
          </form>
        </div>
      </div>`).join("");

    container.innerHTML = html;

  } catch (error) {
    console.error("Erro ao carregar agendamentos:", error);
    if (container)
      container.innerHTML =
        '<p style="grid-column: 1 / -1; text-align: center;">Erro ao carregar seus agendamentos.</p>';
  }
}

document.addEventListener("DOMContentLoaded", () => {
  // ... (keep filter logic)
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

  loadNotifications();
  loadScheduledEvents();
});
