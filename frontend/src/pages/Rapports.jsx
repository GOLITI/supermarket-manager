import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { dashboardService, transactionService, stockService } from '../services';
import Card from '../components/common/Card';
import Button from '../components/common/Button';
import Loading from '../components/common/Loading';
import { FileText, Download, Calendar, TrendingUp, DollarSign, Package } from 'lucide-react';
import { LineChart, Line, BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { format, subDays } from 'date-fns';
import { fr } from 'date-fns/locale';

const Rapports = () => {
  const [dateDebut, setDateDebut] = useState(format(subDays(new Date(), 30), 'yyyy-MM-dd'));
  const [dateFin, setDateFin] = useState(format(new Date(), 'yyyy-MM-dd'));

  const { data: stats, isLoading } = useQuery({
    queryKey: ['rapport-stats', dateDebut, dateFin],
    queryFn: () => dashboardService.getStatistics({ dateDebut, dateFin }).then(res => res.data),
  });

  const { data: ventesProduits } = useQuery({
    queryKey: ['rapport-ventes-produits', dateDebut, dateFin],
    queryFn: () => dashboardService.getVentesProduits({ dateDebut, dateFin }).then(res => res.data),
  });

  const { data: chiffreAffaires } = useQuery({
    queryKey: ['rapport-ca', dateDebut, dateFin],
    queryFn: () => transactionService.getChiffreAffaires({ dateDebut, dateFin }).then(res => res.data),
  });

  if (isLoading) return <Loading size="lg" />;

  return (
    <div className="space-y-6">
      {/* En-tête */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-800">Rapports et analyses</h1>
          <p className="text-gray-500 mt-1">Visualisez et exportez vos rapports</p>
        </div>
        <Button icon={Download} variant="success">Exporter PDF</Button>
      </div>

      {/* Filtres de période */}
      <Card>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Date début</label>
            <input
              type="date"
              value={dateDebut}
              onChange={(e) => setDateDebut(e.target.value)}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Date fin</label>
            <input
              type="date"
              value={dateFin}
              onChange={(e) => setDateFin(e.target.value)}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
            />
          </div>
          <div className="flex items-end">
            <Button className="w-full">Actualiser</Button>
          </div>
        </div>
      </Card>

      {/* KPIs */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <Card className="border-l-4 border-green-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Chiffre d'affaires</p>
              <p className="text-2xl font-bold text-green-600">
                {(chiffreAffaires || 0).toLocaleString('fr-FR')} FCFA
              </p>
            </div>
            <DollarSign className="w-12 h-12 text-green-500" />
          </div>
        </Card>

        <Card className="border-l-4 border-blue-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Nombre de ventes</p>
              <p className="text-2xl font-bold text-blue-600">{stats?.nombreVentes || 0}</p>
            </div>
            <TrendingUp className="w-12 h-12 text-blue-500" />
          </div>
        </Card>

        <Card className="border-l-4 border-purple-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Panier moyen</p>
              <p className="text-2xl font-bold text-purple-600">
                {stats?.panierMoyen?.toLocaleString('fr-FR') || 0} FCFA
              </p>
            </div>
            <DollarSign className="w-12 h-12 text-purple-500" />
          </div>
        </Card>

        <Card className="border-l-4 border-orange-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Produits vendus</p>
              <p className="text-2xl font-bold text-orange-600">{stats?.totalProduitsVendus || 0}</p>
            </div>
            <Package className="w-12 h-12 text-orange-500" />
          </div>
        </Card>
      </div>

      {/* Graphiques */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card title="Top 10 des produits les plus vendus">
          <ResponsiveContainer width="100%" height={400}>
            <BarChart data={ventesProduits?.slice(0, 10) || []}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="nom" angle={-45} textAnchor="end" height={150} />
              <YAxis />
              <Tooltip />
              <Legend />
              <Bar dataKey="quantite" fill="#0ea5e9" name="Quantité vendue" />
            </BarChart>
          </ResponsiveContainer>
        </Card>

        <Card title="Évolution du chiffre d'affaires">
          <ResponsiveContainer width="100%" height={400}>
            <LineChart data={stats?.evolutionCA || []}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="date" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Line type="monotone" dataKey="montant" stroke="#10b981" strokeWidth={2} name="CA journalier" />
            </LineChart>
          </ResponsiveContainer>
        </Card>
      </div>

      {/* Rapports disponibles */}
      <Card title="Rapports disponibles">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div className="p-4 border-2 border-gray-200 rounded-lg hover:border-primary-500 transition-colors cursor-pointer">
            <FileText className="w-10 h-10 text-primary-500 mb-3" />
            <h3 className="font-semibold text-gray-800">Rapport des ventes</h3>
            <p className="text-sm text-gray-500 mt-1">Détail complet des transactions</p>
            <Button size="sm" variant="outline" className="mt-4 w-full" icon={Download}>
              Télécharger
            </Button>
          </div>

          <div className="p-4 border-2 border-gray-200 rounded-lg hover:border-primary-500 transition-colors cursor-pointer">
            <FileText className="w-10 h-10 text-primary-500 mb-3" />
            <h3 className="font-semibold text-gray-800">Rapport des stocks</h3>
            <p className="text-sm text-gray-500 mt-1">État des stocks et alertes</p>
            <Button size="sm" variant="outline" className="mt-4 w-full" icon={Download}>
              Télécharger
            </Button>
          </div>

          <div className="p-4 border-2 border-gray-200 rounded-lg hover:border-primary-500 transition-colors cursor-pointer">
            <FileText className="w-10 h-10 text-primary-500 mb-3" />
            <h3 className="font-semibold text-gray-800">Rapport financier</h3>
            <p className="text-sm text-gray-500 mt-1">Bilan comptable et marges</p>
            <Button size="sm" variant="outline" className="mt-4 w-full" icon={Download}>
              Télécharger
            </Button>
          </div>

          <div className="p-4 border-2 border-gray-200 rounded-lg hover:border-primary-500 transition-colors cursor-pointer">
            <FileText className="w-10 h-10 text-primary-500 mb-3" />
            <h3 className="font-semibold text-gray-800">Rapport clients</h3>
            <p className="text-sm text-gray-500 mt-1">Analyse de la fidélité</p>
            <Button size="sm" variant="outline" className="mt-4 w-full" icon={Download}>
              Télécharger
            </Button>
          </div>

          <div className="p-4 border-2 border-gray-200 rounded-lg hover:border-primary-500 transition-colors cursor-pointer">
            <FileText className="w-10 h-10 text-primary-500 mb-3" />
            <h3 className="font-semibold text-gray-800">Rapport RH</h3>
            <p className="text-sm text-gray-500 mt-1">Présences et performances</p>
            <Button size="sm" variant="outline" className="mt-4 w-full" icon={Download}>
              Télécharger
            </Button>
          </div>

          <div className="p-4 border-2 border-gray-200 rounded-lg hover:border-primary-500 transition-colors cursor-pointer">
            <FileText className="w-10 h-10 text-primary-500 mb-3" />
            <h3 className="font-semibold text-gray-800">Rapport fournisseurs</h3>
            <p className="text-sm text-gray-500 mt-1">Commandes et livraisons</p>
            <Button size="sm" variant="outline" className="mt-4 w-full" icon={Download}>
              Télécharger
            </Button>
          </div>
        </div>
      </Card>
    </div>
  );
};

export default Rapports;

