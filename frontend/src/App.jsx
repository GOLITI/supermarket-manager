import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { Toaster } from 'react-hot-toast';
import Layout from './components/Layout/Layout';
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
      staleTime: 5 * 60 * 1000, // 5 minutes
    },
  },
});

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <Router>
        <Routes>
          <Route path="/" element={<Layout />}>
            <Route index element={<Dashboard />} />
            <Route path="stocks" element={<Stocks />} />
            <Route path="caisses" element={<Caisses />} />
            <Route path="clients" element={<Clients />} />
            <Route path="employes" element={<Employes />} />
            <Route path="fournisseurs" element={<Fournisseurs />} />
            <Route path="promotions" element={<Promotions />} />
            <Route path="rapports" element={<Rapports />} />
          </Route>
        </Routes>
      </Router>
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
    </QueryClientProvider>
  );
}

export default App;

