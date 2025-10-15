import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { stockService, produitService } from '../services';
import Card from '../components/common/Card';
import Button from '../components/common/Button';
import Loading from '../components/common/Loading';
import { Package, Plus, AlertCircle, Search, Filter } from 'lucide-react';
import toast from 'react-hot-toast';

const Stocks = () => {
  const [searchTerm, setSearchTerm] = useState('');
  const [showAlertes, setShowAlertes] = useState(false);

  const { data: stocks, isLoading, refetch } = useQuery({
    queryKey: ['stocks'],
    queryFn: () => stockService.getAll().then(res => res.data),
  });

  const { data: alertes } = useQuery({
    queryKey: ['stock-alertes'],
    queryFn: () => stockService.getAlertes().then(res => res.data),
  });

  const handleAjouterStock = async (stockId, quantite) => {
    try {
      await stockService.ajouterQuantite(stockId, quantite);
      toast.success('Stock mis à jour avec succès');
      refetch();
    } catch (error) {
      toast.error('Erreur lors de la mise à jour du stock');
    }
  };

  const filteredStocks = stocks?.filter(stock =>
    stock.produit?.nom?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const displayStocks = showAlertes ? alertes : filteredStocks;

  if (isLoading) return <Loading size="lg" />;

  return (
    <div className="space-y-6">
      {/* En-tête */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-800">Gestion des stocks</h1>
          <p className="text-gray-500 mt-1">Gérez vos stocks et inventaires</p>
        </div>
        <Button icon={Plus}>Ajouter un produit</Button>
      </div>

      {/* Statistiques rapides */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card className="border-l-4 border-primary-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Total produits</p>
              <p className="text-2xl font-bold text-gray-800">{stocks?.length || 0}</p>
            </div>
            <Package className="w-12 h-12 text-primary-500" />
          </div>
        </Card>
        <Card className="border-l-4 border-red-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Alertes stock</p>
              <p className="text-2xl font-bold text-red-600">{alertes?.length || 0}</p>
            </div>
            <AlertCircle className="w-12 h-12 text-red-500" />
          </div>
        </Card>
        <Card className="border-l-4 border-green-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Valeur totale</p>
              <p className="text-2xl font-bold text-green-600">
                {(stocks?.reduce((acc, s) => acc + (s.quantite * (s.produit?.prixVente || 0)), 0) || 0).toLocaleString('fr-FR')} FCFA
              </p>
            </div>
            <Package className="w-12 h-12 text-green-500" />
          </div>
        </Card>
      </div>

      {/* Filtres et recherche */}
      <Card>
        <div className="flex items-center justify-between gap-4">
          <div className="flex-1 relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
            <input
              type="text"
              placeholder="Rechercher un produit..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
            />
          </div>
          <Button 
            variant={showAlertes ? 'danger' : 'outline'}
            icon={AlertCircle}
            onClick={() => setShowAlertes(!showAlertes)}
          >
            {showAlertes ? 'Tous les stocks' : 'Alertes'}
          </Button>
          <Button variant="outline" icon={Filter}>Filtrer</Button>
        </div>
      </Card>

      {/* Liste des stocks */}
      <Card title="Liste des stocks">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b border-gray-200">
                <th className="text-left py-3 px-4 font-semibold text-gray-700">Produit</th>
                <th className="text-left py-3 px-4 font-semibold text-gray-700">Code</th>
                <th className="text-center py-3 px-4 font-semibold text-gray-700">Quantité</th>
                <th className="text-center py-3 px-4 font-semibold text-gray-700">Seuil alerte</th>
                <th className="text-center py-3 px-4 font-semibold text-gray-700">Entrepôt</th>
                <th className="text-center py-3 px-4 font-semibold text-gray-700">Statut</th>
                <th className="text-center py-3 px-4 font-semibold text-gray-700">Actions</th>
              </tr>
            </thead>
            <tbody>
              {displayStocks?.map((stock) => (
                <tr key={stock.id} className="border-b border-gray-100 hover:bg-gray-50">
                  <td className="py-3 px-4">
                    <div>
                      <p className="font-medium text-gray-800">{stock.produit?.nom}</p>
                      <p className="text-sm text-gray-500">{stock.produit?.categorie}</p>
                    </div>
                  </td>
                  <td className="py-3 px-4 text-gray-600">{stock.produit?.codeEAN || 'N/A'}</td>
                  <td className="py-3 px-4 text-center">
                    <span className={`font-semibold ${stock.quantite <= stock.seuilAlerte ? 'text-red-600' : 'text-gray-800'}`}>
                      {stock.quantite}
                    </span>
                  </td>
                  <td className="py-3 px-4 text-center text-gray-600">{stock.seuilAlerte}</td>
                  <td className="py-3 px-4 text-center text-gray-600">{stock.entrepot?.nom}</td>
                  <td className="py-3 px-4 text-center">
                    {stock.quantite <= stock.seuilAlerte ? (
                      <span className="px-3 py-1 bg-red-100 text-red-700 rounded-full text-sm font-medium">
                        Stock faible
                      </span>
                    ) : (
                      <span className="px-3 py-1 bg-green-100 text-green-700 rounded-full text-sm font-medium">
                        En stock
                      </span>
                    )}
                  </td>
                  <td className="py-3 px-4">
                    <div className="flex items-center justify-center space-x-2">
                      <Button 
                        size="sm" 
                        variant="success"
                        onClick={() => {
                          const quantite = prompt('Quantité à ajouter:');
                          if (quantite) handleAjouterStock(stock.id, parseInt(quantite));
                        }}
                      >
                        + Stock
                      </Button>
                      <Button size="sm" variant="outline">Détails</Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          {(!displayStocks || displayStocks.length === 0) && (
            <div className="text-center py-12">
              <Package className="w-16 h-16 text-gray-300 mx-auto mb-4" />
              <p className="text-gray-500">Aucun stock trouvé</p>
            </div>
          )}
        </div>
      </Card>
    </div>
  );
};

export default Stocks;

