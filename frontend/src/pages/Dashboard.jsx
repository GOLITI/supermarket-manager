import { useQuery } from '@tanstack/react-query';
import { dashboardService } from '../services';
import StatCard from '../components/common/StatCard';
import Card from '../components/common/Card';
import Loading from '../components/common/Loading';
import { 
  DollarSign, 
  ShoppingCart, 
  Package, 
  Users,
  TrendingUp,
  AlertTriangle
} from 'lucide-react';
import { LineChart, Line, BarChart, Bar, PieChart, Pie, Cell, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { format } from 'date-fns';
import { fr } from 'date-fns/locale';

const Dashboard = () => {
  const today = new Date();
  const params = {
    dateDebut: format(today, 'yyyy-MM-dd'),
    dateFin: format(today, 'yyyy-MM-dd'),
  };

  const { data: stats, isLoading } = useQuery({
    queryKey: ['dashboard-stats', params],
    queryFn: () => dashboardService.getStatistics(params).then(res => res.data),
  });

  const { data: ventesProduits } = useQuery({
    queryKey: ['ventes-produits'],
    queryFn: () => dashboardService.getVentesProduits(params).then(res => res.data),
  });

  const { data: heuresPointe } = useQuery({
    queryKey: ['heures-pointe'],
    queryFn: () => dashboardService.getHeuresPointe(params).then(res => res.data),
  });

  const { data: margesCategories } = useQuery({
    queryKey: ['marges-categories'],
    queryFn: () => dashboardService.getMargesParCategorie(params).then(res => res.data),
  });

  if (isLoading) return <Loading size="lg" />;

  const COLORS = ['#0ea5e9', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6'];

  return (
    <div className="space-y-6">
      {/* En-tête */}
      <div>
        <h1 className="text-3xl font-bold text-gray-800">Tableau de bord</h1>
        <p className="text-gray-500 mt-1">Vue d'ensemble de votre supermarché</p>
      </div>

      {/* Statistiques principales */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <StatCard
          title="Chiffre d'affaires"
          value={`${stats?.chiffreAffaires?.toLocaleString('fr-FR') || 0} FCFA`}
          icon={DollarSign}
          color="success"
          trend="up"
          trendValue="+12.5%"
        />
        <StatCard
          title="Nombre de ventes"
          value={stats?.nombreVentes || 0}
          icon={ShoppingCart}
          color="primary"
          trend="up"
          trendValue="+8.2%"
        />
        <StatCard
          title="Produits en stock"
          value={stats?.nombreProduits || 0}
          icon={Package}
          color="warning"
        />
        <StatCard
          title="Clients actifs"
          value={stats?.nombreClients || 0}
          icon={Users}
          color="primary"
          trend="up"
          trendValue="+5.1%"
        />
      </div>

      {/* Graphiques */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Produits les plus vendus */}
        <Card title="Produits les plus vendus" subtitle="Top 5 du jour">
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={ventesProduits?.slice(0, 5) || []}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="nom" angle={-45} textAnchor="end" height={100} />
              <YAxis />
              <Tooltip />
              <Bar dataKey="quantite" fill="#0ea5e9" />
            </BarChart>
          </ResponsiveContainer>
        </Card>

        {/* Heures de pointe */}
        <Card title="Heures de pointe" subtitle="Affluence par heure">
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={heuresPointe || []}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="heure" />
              <YAxis />
              <Tooltip />
              <Line type="monotone" dataKey="nombreVentes" stroke="#0ea5e9" strokeWidth={2} />
            </LineChart>
          </ResponsiveContainer>
        </Card>

        {/* Marges par catégorie */}
        <Card title="Marges par catégorie" subtitle="Répartition des bénéfices">
          <ResponsiveContainer width="100%" height={300}>
            <PieChart>
              <Pie
                data={margesCategories || []}
                cx="50%"
                cy="50%"
                labelLine={false}
                label={({ nom, pourcentage }) => `${nom} (${pourcentage?.toFixed(1)}%)`}
                outerRadius={80}
                fill="#8884d8"
                dataKey="marge"
              >
                {(margesCategories || []).map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                ))}
              </Pie>
              <Tooltip />
            </PieChart>
          </ResponsiveContainer>
        </Card>

        {/* Alertes */}
        <Card title="Alertes" subtitle="Actions requises">
          <div className="space-y-3">
            <div className="flex items-start p-4 bg-red-50 rounded-lg">
              <AlertTriangle className="w-5 h-5 text-red-600 mt-0.5 mr-3" />
              <div>
                <p className="font-medium text-red-800">Stock faible</p>
                <p className="text-sm text-red-600">{stats?.produitsStockFaible || 0} produits nécessitent un réapprovisionnement</p>
              </div>
            </div>
            <div className="flex items-start p-4 bg-yellow-50 rounded-lg">
              <AlertTriangle className="w-5 h-5 text-yellow-600 mt-0.5 mr-3" />
              <div>
                <p className="font-medium text-yellow-800">Produits périssables</p>
                <p className="text-sm text-yellow-600">{stats?.produitsPeremptionProche || 0} produits expirent bientôt</p>
              </div>
            </div>
            <div className="flex items-start p-4 bg-blue-50 rounded-lg">
              <TrendingUp className="w-5 h-5 text-blue-600 mt-0.5 mr-3" />
              <div>
                <p className="font-medium text-blue-800">Performance</p>
                <p className="text-sm text-blue-600">Objectif du mois atteint à 78%</p>
              </div>
            </div>
          </div>
        </Card>
      </div>
    </div>
  );
};

export default Dashboard;

