import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { Toaster } from 'react-hot-toast';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/auth/ProtectedRoute';
import Layout from './components/Layout/Layout';
import Login from './components/auth/Login';
import Dashboard from './pages/Dashboard';
import Stocks from './pages/Stocks';
import Caisses from './pages/Caisses';
import Clients from './pages/Clients';
import Employes from './pages/Employes';
import Fournisseurs from './pages/Fournisseurs';
import Promotions from './pages/Promotions';
import Rapports from './pages/Rapports';

const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            refetchOnWindowFocus: false,
            retry: 1,
            staleTime: 5 * 60 * 1000,
        },
    },
});

// Pages temporaires simples
const TempPage = ({ title }) => (
    <div>
        <div className="page-header">
            <h1 className="page-title">{title}</h1>
            <p className="page-subtitle">Page en cours de développement</p>
        </div>
        <div className="alert alert-info">
            <strong>Information :</strong> Cette fonctionnalité sera disponible prochainement.
        </div>
    </div>
);

function App() {
    return (
        <QueryClientProvider client={queryClient}>
            <AuthProvider>
                <Router>
                    <Routes>


                        <Route path="/login" element={<Login />} />
                        {/* Routes protégées */}
                        <Route path="/" element={
                            <ProtectedRoute>
                                <Layout />
                            </ProtectedRoute>
                        }>
                            <Route index element={<Navigate to="/dashboard" replace />} />
                            <Route path="dashboard" element={
                                <ProtectedRoute roles={['ROLE_ADMIN', 'ROLE_MANAGER']}>
                                    <Dashboard />
                                </ProtectedRoute>
                            } />
                            <Route path="stocks" element={
                                <ProtectedRoute roles={['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STOCK_MANAGER']}>
                                    <Stocks />
                                </ProtectedRoute>
                            } />
                            <Route path="caisses" element={
                                <ProtectedRoute roles={['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_CASHIER']}>
                                    <Caisses />
                                </ProtectedRoute>
                            } />
                            <Route path="clients" element={
                                <ProtectedRoute roles={['ROLE_ADMIN', 'ROLE_MANAGER']}>
                                    <TempPage title="Gestion des Clients" />
                                </ProtectedRoute>
                            } />
                            <Route path="employes" element={
                                <ProtectedRoute roles={['ROLE_ADMIN', 'ROLE_HR_MANAGER']}>
                                    <TempPage title="Gestion des Employés" />
                                </ProtectedRoute>
                            } />
                            <Route path="fournisseurs" element={
                                <ProtectedRoute roles={['ROLE_ADMIN', 'ROLE_MANAGER']}>
                                    <TempPage title="Fournisseurs" />
                                </ProtectedRoute>
                            } />
                            <Route path="promotions" element={
                                <ProtectedRoute roles={['ROLE_ADMIN', 'ROLE_MANAGER']}>
                                    <TempPage title="Promotions" />
                                </ProtectedRoute>
                            } />
                            <Route path="rapports" element={
                                <ProtectedRoute roles={['ROLE_ADMIN', 'ROLE_MANAGER']}>
                                    <TempPage title="Rapports & Analytics" />
                                </ProtectedRoute>
                            } />
                        </Route>

                        {/* Route 404 */}
                        <Route path="*" element={<Navigate to="/dashboard" replace />} />
                    </Routes>
                    <Toaster
                        position="top-right"
                        toastOptions={{
                            duration: 4000,
                            style: {
                                background: '#363636',
                                color: '#fff',
                                borderRadius: '8px',
                                fontSize: '14px'
                            },
                        }}
                    />
                </Router>
            </AuthProvider>
        </QueryClientProvider>
    );
}

export default App;