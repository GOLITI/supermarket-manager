import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Intercepteur pour ajouter le token JWT si disponible
api.interceptors.request.use(
    (config) => {
        const user = JSON.parse(localStorage.getItem('user'));
        if (user && user.token) {
            config.headers.Authorization = `Bearer ${user.token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// Intercepteur pour gérer les erreurs et rafraîchir le token
api.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;

        if (error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;

            const user = JSON.parse(localStorage.getItem('user'));

            if (user && user.refreshToken) {
                try {
                    // Tenter de rafraîchir le token
                    const response = await axios.post(`${API_BASE_URL}/api/auth/refresh`, {
                        refreshToken: user.refreshToken
                    });

                    const { accessToken, refreshToken: newRefreshToken } = response.data;

                    // Mettre à jour l'utilisateur dans le localStorage
                    const updatedUser = {
                        ...user,
                        token: accessToken,
                        refreshToken: newRefreshToken || user.refreshToken
                    };
                    localStorage.setItem('user', JSON.stringify(updatedUser));

                    // Retenter la requête originale avec le nouveau token
                    originalRequest.headers.Authorization = `Bearer ${accessToken}`;
                    return api(originalRequest);

                } catch (refreshError) {
                    // Si le refresh échoue, déconnecter l'utilisateur
                    console.error('Erreur de rafraîchissement du token:', refreshError);
                    localStorage.removeItem('user');
                    window.location.href = '/login';
                    return Promise.reject(refreshError);
                }
            } else {
                // Pas de refresh token disponible
                localStorage.removeItem('user');
                window.location.href = '/login';
            }
        }

        // Gérer d'autres erreurs
        if (error.response?.status === 403) {
            window.location.href = '/unauthorized';
        }

        return Promise.reject(error);
    }
);

export default api;