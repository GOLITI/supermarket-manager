import React, { createContext, useState, useEffect, useContext } from 'react';
import AuthService from '../services/authService';

const AuthContext = createContext();

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};

export const AuthProvider = ({ children }) => {
    const [currentUser, setCurrentUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const user = AuthService.getCurrentUser();
        if (user) {
            setCurrentUser(user);
        }
        setLoading(false);
    }, []);

    const login = async (username, password) => {
        try {
            const userData = await AuthService.login(username, password);
            setCurrentUser(userData);
            return userData;
        } catch (error) {
            throw error;
        }
    };

    const logout = () => {
        AuthService.logout();
        setCurrentUser(null);
    };

    const register = async (userData) => {
        try {
            const response = await AuthService.register(userData);
            return response;
        } catch (error) {
            throw error;
        }
    };

    const hasRole = (role) => {
        return currentUser?.roles?.includes(role);
    };

    const value = {
        currentUser,
        login,
        logout,
        register,
        hasRole,
        loading
    };

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
};