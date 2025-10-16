import React, { createContext, useState, useContext, useEffect } from 'react';
import { authService } from '../services/authService';

const AuthContext = createContext();

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [token, setToken] = useState(localStorage.getItem('token'));

    useEffect(() => {
        if (token) {
            authService.setToken(token);
            getCurrentUser();
        } else {
            setLoading(false);
        }
    }, [token]);

    const getCurrentUser = async () => {
        try {
            // Vous pouvez implémenter un endpoint /me pour récupérer l'utilisateur courant
            // Pour l'instant, on utilise les infos du token
            const userData = authService.getUserFromToken();
            setUser(userData);
        } catch (error) {
            console.error('Erreur récupération utilisateur:', error);
            logout();
        } finally {
            setLoading(false);
        }
    };

    const login = async (username, password) => {
        try {
            const response = await authService.login(username, password);
            const { token, refreshToken, ...userData } = response;

            localStorage.setItem('token', token);
            localStorage.setItem('refreshToken', refreshToken);
            setToken(token);
            setUser(userData);

            return { success: true, user: userData };
        } catch (error) {
            return {
                success: false,
                error: error.response?.data?.message || 'Erreur de connexion'
            };
        }
    };

    const logout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('refreshToken');
        setToken(null);
        setUser(null);
        authService.setToken(null);
    };

    const hasRole = (role) => {
        if (!user || !user.roles) return false;
        return user.roles.includes(role);
    };

    const hasAnyRole = (roles) => {
        if (!user || !user.roles) return false;
        return roles.some(role => user.roles.includes(role));
    };

    const value = {
        user,
        loading,
        login,
        logout,
        hasRole,
        hasAnyRole,
        isAuthenticated: !!user
    };

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
};