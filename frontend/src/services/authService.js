import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

const API_URL = 'http://localhost:8080/api/auth';

class AuthService {
    constructor() {
        this.token = localStorage.getItem('token');
        this.setupAxiosInterceptors();
    }

    setupAxiosInterceptors() {
        axios.interceptors.request.use(
            (config) => {
                if (this.token) {
                    config.headers.Authorization = `Bearer ${this.token}`;
                }
                return config;
            },
            (error) => {
                return Promise.reject(error);
            }
        );

        axios.interceptors.response.use(
            (response) => response,
            async (error) => {
                if (error.response?.status === 401) {
                    // Token expiré, tentative de refresh
                    try {
                        const refreshToken = localStorage.getItem('refreshToken');
                        if (refreshToken) {
                            const response = await this.refreshToken(refreshToken);
                            const newToken = response.accessToken;
                            this.setToken(newToken);
                            error.config.headers.Authorization = `Bearer ${newToken}`;
                            return axios(error.config);
                        }
                    } catch (refreshError) {
                        this.logout();
                        window.location.href = '/login';
                    }
                }
                return Promise.reject(error);
            }
        );
    }

    setToken(token) {
        this.token = token;
        if (token) {
            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        } else {
            delete axios.defaults.headers.common['Authorization'];
        }
    }

    async login(username, password) {
        const response = await axios.post(`${API_URL}/login`, {
            username,
            password
        });

        if (response.data.token) {
            this.setToken(response.data.token);
        }

        return response.data;
    }

    async register(userData) {
        const response = await axios.post(`${API_URL}/register`, userData);
        return response.data;
    }

    async refreshToken(refreshToken) {
        const response = await axios.post(`${API_URL}/refresh`, { refreshToken });

        if (response.data.accessToken) {
            this.setToken(response.data.accessToken);
            localStorage.setItem('token', response.data.accessToken);
        }

        return response.data;
    }

    async logout() {
        try {
            await axios.post(`${API_URL}/logout`);
        } catch (error) {
            console.error('Erreur lors de la déconnexion:', error);
        } finally {
            this.setToken(null);
            localStorage.removeItem('token');
            localStorage.removeItem('refreshToken');
        }
    }

    getUserFromToken() {
        if (!this.token) return null;

        try {
            const decoded = jwtDecode(this.token);
            return {
                id: decoded.id,
                username: decoded.sub,
                email: decoded.email,
                firstName: decoded.firstName,
                lastName: decoded.lastName,
                roles: decoded.roles || []
            };
        } catch (error) {
            console.error('Erreur décodage token:', error);
            return null;
        }
    }

    // Ajouter cette méthode dans authService.js
    async registerClient(clientData) {
        const response = await axios.post('http://localhost:8080/api/client/register', clientData);
        return response.data;
    }

    isTokenExpired() {
        if (!this.token) return true;

        try {
            const decoded = jwtDecode(this.token);
            return decoded.exp * 1000 < Date.now();
        } catch (error) {
            return true;
        }
    }
}

export const authService = new AuthService();