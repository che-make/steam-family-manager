async function apiFetch(path, options = {}) {
    const token = getToken();
    const headers = { ...options.headers };
    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }
    if (options.body && !(options.body instanceof FormData)) {
        headers['Content-Type'] = 'application/json';
    }

    const res = await fetch(`${API_BASE_URL}${path}`, { ...options, headers });

    if (res.status === 401 || res.status === 403) {
        clearToken();
        window.location.href = 'index.html';
        throw new Error('Session expired');
    }

    return res;
}

async function getMembers() {
    const res = await apiFetch('/api/family-members');
    if (!res.ok) throw new Error('Failed to load members');
    return res.json();
}

async function addMember(steamId) {
    const res = await apiFetch('/api/family-members', {
        method: 'POST',
        body: JSON.stringify({ steamId })
    });
    if (!res.ok) {
        const err = await res.json().catch(() => null);
        throw new Error(err?.message || 'Failed to add member');
    }
    return res.json();
}

async function deactivateMember(steamId) {
    const res = await apiFetch(`/api/family-members/${steamId}/deactivate`, {
        method: 'PATCH'
    });
    if (!res.ok) throw new Error('Failed to deactivate member');
    return res.json();
}

async function deleteMember(steamId) {
    const res = await apiFetch(`/api/family-members/${steamId}`, {
        method: 'DELETE'
    });
    if (!res.ok) throw new Error('Failed to delete member');
}

async function getMemberGames(steamId) {
    const res = await apiFetch(`/api/family-members/${steamId}/games`);
    if (!res.ok) throw new Error('Failed to load games');
    return res.json();
}

async function syncAllMembers() {
    const res = await apiFetch('/api/family-members/sync', { method: 'POST' });
    if (!res.ok) throw new Error('Failed to sync members');
    return res.json();
}

async function syncMember(steamId) {
    const res = await apiFetch(`/api/family-members/${steamId}/sync`, { method: 'POST' });
    if (!res.ok) throw new Error('Failed to sync member');
    return res.json();
}
