import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { Spinner, Container } from 'react-bootstrap';

const ProtectedRoute = ({ children, roles = [] }) => {
    const { currentUser, loading } = useAuth();

    if (loading) {
        return (
            <Container className="d-flex justify-content-center align-items-center min-vh-100">
                <Spinner animation="border" role="status" variant="primary">
                    <span className="visually-hidden">Chargement...</span>
                </Spinner>
            </Container>
        );
    }

    if (!currentUser) {
        return <Navigate to="/login" replace />;
    }

    if (roles.length > 0 && !roles.some(role => currentUser.roles?.includes(role))) {
        return <Navigate to="/unauthorized" replace />;
    }

    return children;
};

export default ProtectedRoute;