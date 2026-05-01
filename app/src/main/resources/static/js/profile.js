document.addEventListener("DOMContentLoaded", () => {
    const editBtn = document.getElementById("editProfileBtn");
    const saveBtn = document.getElementById("saveProfileBtn");
    const cancelBtn = document.getElementById("cancelEditBtn");
    const changeImageBtn = document.getElementById("changeImageBtn");
    const fileInput = document.getElementById("fileInput");
    
    const nameDisplay = document.getElementById("nameDisplay");
    const nameInput = document.getElementById("nameInput");
    const bioDisplay = document.getElementById("bioDisplay");
    const bioInput = document.getElementById("bioInput");
    const profileImageDisplay = document.getElementById("profileImageDisplay");
    const userIdElement = document.getElementById("userId");

    let currentBase64Image = null;

    const toggleEditMode = (editing) => {
        const editModeElements = document.querySelectorAll(".edit-mode-only");
        editModeElements.forEach(el => el.style.display = editing ? "block" : "none");

        nameDisplay.style.display = editing ? "none" : "block";
        bioDisplay.style.display = editing ? "none" : "block";
        editBtn.style.display = editing ? "none" : "inline-block";
        document.getElementById("deleteAccountBtn").style.display = editing ? "none" : "inline-block";
    };

    if (editBtn) {
        editBtn.addEventListener("click", () => {
            nameInput.value = nameDisplay.innerText;
            bioInput.value = bioDisplay.innerText;
            toggleEditMode(true);
        });
    }

    if (cancelBtn) {
        cancelBtn.addEventListener("click", () => {
            toggleEditMode(false);
        });
    }

    if (changeImageBtn) {
        changeImageBtn.addEventListener("click", () => fileInput.click());
    }

    if (fileInput) {
        fileInput.addEventListener("change", (e) => {
            const file = e.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onloadend = () => {
                    currentBase64Image = reader.result;
                    profileImageDisplay.src = currentBase64Image;
                };
                reader.readAsDataURL(file);
            }
        });
    }

    if (saveBtn) {
        saveBtn.addEventListener("click", async () => {
            const data = {
                name: nameInput.value,
                bio: bioInput.value
            };

            if (currentBase64Image) {
                data.imageBase64 = currentBase64Image;
            }

            await updateProfile(data);
        });
    }

    async function updateProfile(data) {
        const userId = userIdElement.innerText;
        
        try {
            const response = await fetch(`/profile/${userId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data),
            });

            if (response.ok) {
                alert("Perfil atualizado com sucesso!");
                window.location.reload();
            } else {
                const errorData = await response.json();
                alert("Erro ao atualizar perfil: " + (errorData.message || "Erro desconhecido"));
            }
        } catch (error) {
            console.error("Erro na requisição:", error);
            alert("Erro ao conectar ao servidor.");
        }
    }
});
