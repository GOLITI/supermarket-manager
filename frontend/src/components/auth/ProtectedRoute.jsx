import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext.jsx';
import Loading from '../common/Loading.jsx';

const ProtectedRoute = ({ children, roles = [] }) => {
    const { isAuthenticated, user, loading, hasAnyRole } = useAuth();
    const location = useLocation();

    if (loading) {
        return <Loading size="lg" />;
    }

    if (!isAuthenticated) {
        return <Navigate to="/login" state={{ from: location }} replace />;
    }

    if (roles.length > 0 && !hasAnyRole(roles)) {
        return (
            <div className="min-vh-100 d-flex align-items-center justify-content-center">
                <div className="text-center">
                    <div className="text-danger mb-3">
                        <i className="bi bi-shield-exclamation" style={{ fontSize: '4rem' }}></i>
                    </div>
                    <h3 className="text-dark mb-2">Accès refusé</h3>
                    <p className="text-muted mb-4">
                        Vous n'avez pas les permissions nécessaires pour accéder à cette page.
                    </p>
                    <button
                        className="btn btn-primary"
                        onClick={() => window.history.back()}
                    >
                        Retour
                    </button>
                </div>
            </div>
        );
    }

    return children;
};

export default ProtectedRoute;