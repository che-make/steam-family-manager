function renderMembers(members) {
    const grid = document.getElementById('members-grid');
    if (!members.length) {
        grid.innerHTML = '<p class="empty-state">No family members yet. Add one above.</p>';
        return;
    }
    grid.innerHTML = members.map(m => `
        <div class="member-card ${m.active ? '' : 'inactive'}">
            <div class="member-header">
                <img src="${escapeAttr(m.avatarUrl)}" alt="${escapeHtml(m.personaName)}" class="member-avatar">
                <div class="member-info">
                    <h3 class="member-name">${escapeHtml(m.personaName)}</h3>
                    <span class="member-id">${escapeHtml(m.steamId)}</span>
                    <span class="member-status ${m.active ? 'active' : 'inactive'}">${m.active ? 'Active' : 'Inactive'}</span>
                </div>
            </div>
            <div class="member-stats">
                <span class="game-count">${m.gameCount} games</span>
                <span class="member-since">Since ${formatDate(m.createdAt)}</span>
            </div>
            <div class="member-actions">
                <button class="btn btn-accent" onclick="openGamesModal('${escapeAttr(m.steamId)}', '${escapeAttr(m.personaName)}')">View Games</button>
                ${m.active ? `<button class="btn btn-success" onclick="handleSync('${escapeAttr(m.steamId)}')">Sync</button>` : ''}
                ${m.active ? `<button class="btn btn-warn" onclick="handleDeactivate('${escapeAttr(m.steamId)}')">Deactivate</button>` : ''}
                <button class="btn btn-danger" onclick="handleDelete('${escapeAttr(m.steamId)}', '${escapeAttr(m.personaName)}')">Delete</button>
            </div>
        </div>
    `).join('');
}

async function handleAddMember(e) {
    e.preventDefault();
    const input = document.getElementById('steam-id-input');
    const steamId = input.value.trim();
    if (!/^\d{17}$/.test(steamId)) {
        showToast('Steam ID must be exactly 17 digits', 'error');
        return;
    }
    try {
        await addMember(steamId);
        input.value = '';
        showToast('Member added successfully', 'success');
        await loadMembers();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

async function handleDeactivate(steamId) {
    if (!confirm('Deactivate this member? Game tracking will stop.')) return;
    try {
        await deactivateMember(steamId);
        showToast('Member deactivated', 'success');
        await loadMembers();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

async function handleDelete(steamId, name) {
    if (!confirm(`Delete ${name}? This will remove all their data permanently.`)) return;
    try {
        await deleteMember(steamId);
        showToast('Member deleted', 'success');
        await loadMembers();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

function formatDate(dateStr) {
    if (!dateStr) return 'N/A';
    const d = new Date(dateStr);
    return d.toLocaleDateString('en-GB', { day: '2-digit', month: 'short', year: 'numeric' });
}

function escapeHtml(str) {
    if (!str) return '';
    const div = document.createElement('div');
    div.textContent = str;
    return div.innerHTML;
}

function escapeAttr(str) {
    if (!str) return '';
    return str.replace(/&/g, '&amp;').replace(/'/g, '&#39;').replace(/"/g, '&quot;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
}

async function handleSyncAll() {
    const btn = document.getElementById('sync-all-btn');
    btn.disabled = true;
    btn.textContent = 'Syncing...';
    try {
        const result = await syncAllMembers();
        showToast(result.message, 'success');
        await loadMembers();
    } catch (err) {
        showToast(err.message, 'error');
    } finally {
        btn.disabled = false;
        btn.textContent = 'Sync All';
    }
}

async function handleSync(steamId) {
    try {
        const result = await syncMember(steamId);
        showToast(result.message, 'success');
        await loadMembers();
    } catch (err) {
        showToast(err.message, 'error');
    }
}
