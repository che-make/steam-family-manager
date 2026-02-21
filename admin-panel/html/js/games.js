async function openGamesModal(steamId, personaName) {
    const modal = document.getElementById('games-modal');
    const title = document.getElementById('games-modal-title');
    const list = document.getElementById('games-list');

    title.textContent = `${personaName}'s Games`;
    list.innerHTML = '<div class="loading">Loading games...</div>';
    modal.classList.add('open');

    try {
        const games = await getMemberGames(steamId);
        if (!games.length) {
            list.innerHTML = '<p class="empty-state">No games found for this member.</p>';
            return;
        }
        list.innerHTML = games
            .sort((a, b) => a.name.localeCompare(b.name))
            .map(g => `
                <div class="game-item">
                    <img src="${g.iconUrl || 'data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%2232%22 height=%2232%22%3E%3Crect fill=%22%232a475e%22 width=%2232%22 height=%2232%22/%3E%3C/svg%3E'}"
                         alt="" class="game-icon" onerror="this.src='data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%2232%22 height=%2232%22%3E%3Crect fill=%22%232a475e%22 width=%2232%22 height=%2232%22/%3E%3C/svg%3E'">
                    <div class="game-info">
                        <span class="game-name">${escapeHtml(g.name)}</span>
                        <span class="game-detected">Added ${formatDate(g.firstDetectedAt)}</span>
                    </div>
                </div>
            `).join('');
    } catch (err) {
        list.innerHTML = `<p class="error-state">${escapeHtml(err.message)}</p>`;
    }
}

function closeGamesModal() {
    document.getElementById('games-modal').classList.remove('open');
}
