import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import Layout from './components/Layout/Layout';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 1,
      staleTime: 5 * 60 * 1000,
    },
  },
});

// Page de test simple pour chaque route
function SimplePage({ title, description }) {
  return (
    <div className="p-6">
      <div className="bg-white rounded-lg shadow-md p-8">
        <h1 className="text-3xl font-bold text-gray-800 mb-4">{title}</h1>
        <p className="text-gray-600 mb-6">{description}</p>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div className="bg-blue-50 p-4 rounded-lg">
            <h3 className="font-semibold text-blue-800 mb-2">✅ Fonctionnel</h3>
            <p className="text-sm text-blue-600">Cette page est opérationnelle</p>
          </div>
          <div className="bg-green-50 p-4 rounded-lg">
            <h3 className="font-semibold text-green-800 mb-2">🚀 Prêt</h3>
            <p className="text-sm text-green-600">Backend connecté</p>
          </div>
          <div className="bg-purple-50 p-4 rounded-lg">
            <h3 className="font-semibold text-purple-800 mb-2">📊 En développement</h3>
            <p className="text-sm text-purple-600">Fonctionnalités à venir</p>
          </div>
        </div>
      </div>
    </div>
  );
}

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <Router>
        <Routes>
          <Route path="/" element={<Layout />}>
            <Route index element={<SimplePage title="Dashboard" description="Tableau de bord - Vue d'ensemble de votre supermarché" />} />
            <Route path="stocks" element={<SimplePage title="Gestion des Stocks" description="Gérez vos stocks et approvisionnements" />} />
            <Route path="caisses" element={<SimplePage title="Gestion des Caisses" description="Transactions et ventes en temps réel" />} />
            <Route path="clients" element={<SimplePage title="Gestion des Clients" description="Base de données clients et fidélité" />} />
            <Route path="employes" element={<SimplePage title="Ressources Humaines" description="Gestion des employés et plannings" />} />
            <Route path="fournisseurs" element={<SimplePage title="Fournisseurs" description="Gérez vos fournisseurs et commandes" />} />
            <Route path="promotions" element={<SimplePage title="Promotions" description="Créez et gérez vos promotions" />} />
            <Route path="rapports" element={<SimplePage title="Rapports" description="Analyses et statistiques détaillées" />} />
          </Route>
        </Routes>
      </Router>
    </QueryClientProvider>
  );
}

export default App;

