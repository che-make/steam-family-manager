document.addEventListener('DOMContentLoaded', () => {
    requireAuth();

    document.getElementById('add-member-form').addEventListener('submit', handleAddMember);
    document.getElementById('logout-btn').addEventListener('click', logout);
    document.getElementById('games-modal-close').addEventListener('click', closeGamesModal);
    document.getElementById('games-modal').addEventListener('click', (e) => {
        if (e.target.id === 'games-modal') closeGamesModal();
    });

    loadMembers();
});

async function loadMembers() {
    const grid = document.getElementById('members-grid');
    try {
        const members = await getMembers();
        renderMembers(members);
    } catch (err) {
        grid.innerHTML = `<p class="error-state">Failed to load members: ${escapeHtml(err.message)}</p>`;
    }
}

function showToast(message, type = 'info') {
    const container = document.getElementById('toast-container');
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.textContent = message;
    container.appendChild(toast);
    requestAnimationFrame(() => toast.classList.add('show'));
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}
