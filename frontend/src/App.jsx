import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { Toaster } from 'react-hot-toast';
import { AuthProvider, useAuth } from './context/AuthContext';
import ProtectedRoute from './components/auth/ProtectedRoute';
import Login from './components/auth/Login';
import Layout from './components/Layout/Layout';
import Dashboard from './pages/Dashboard';
import Stocks from './pages/Stocks';
import Caisses from './pages/Caisses';
import Clients from './pages/Clients';
import Employes from './pages/Employes';
import Fournisseurs from './pages/Fournisseurs';
import Promotions from './pages/Promotions';
import Rapports from './pages/Rapports';
import Unauthorized from './pages/Unauthorized';
import Loading from './components/common/Loading';

const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            refetchOnWindowFocus: false,
            retry: 1,
            staleTime: 5 * 60 * 1000,
        },
    },
});

const AppContent = () => {
    const { currentUser, loading } = useAuth();

    if (loading) {
        return <Loading size="lg" />;
    }

    return (
        <Routes>
            {/* Route racine - Login si non connecté */}
            <Route
                path="/"
                element={!currentUser ? <Login /> : <Navigate to="/dashboard" replace />}
            />

            {/* Route publique - Login */}
            <Route
                path="/login"
                element={!currentUser ? <Login /> : <Navigate to="/dashboard" replace />}
            />

            <Route path="/unauthorized" element={<Unauthorized />} />

            {/* Routes protégées */}
            <Route path="/" element={
                <ProtectedRoute>
                    <Layout />
                </ProtectedRoute>
            }>
                <Route index element={<Navigate to="/dashboard" replace />} />
                <Route path="dashboard" element={<Dashboard />} />

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

                <Route path="employes" element={
                    <ProtectedRoute roles={['ROLE_ADMIN', 'ROLE_HR_MANAGER']}>
                        <Employes />
                    </ProtectedRoute>
                } />

                <Route path="clients" element={<Clients />} />
                <Route path="fournisseurs" element={<Fournisseurs />} />
                <Route path="promotions" element={<Promotions />} />
                <Route path="rapports" element={<Rapports />} />
            </Route>

            {/* Redirection par défaut */}
            <Route path="*" element={<Navigate to={currentUser ? "/dashboard" : "/login"} replace />} />
        </Routes>
    );
};

function App() {
    return (
        <QueryClientProvider client={queryClient}>
            <AuthProvider>
                <Router>
                    <AppContent />
                    <Toaster
                        position="top-right"
                        toastOptions={{
                            duration: 3000,
                            style: {
                                background: '#363636',
                                color: '#fff',
                            },
                            success: {
                                duration: 3000,
                                iconTheme: {
                                    primary: '#10b981',
                                    secondary: '#fff',
                                },
                            },
                            error: {
                                duration: 4000,
                                iconTheme: {
                                    primary: '#ef4444',
                                    secondary: '#fff',
                                },
                            },
                        }}
                    />
                </Router>
            </AuthProvider>
        </QueryClientProvider>
    );
}

export default App;