import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { promotionService } from '../services';
import Card from '../components/common/Card';
import Button from '../components/common/Button';
import Loading from '../components/common/Loading';
import { Tag, Plus, Search, Calendar } from 'lucide-react';
import { format } from 'date-fns';
import { fr } from 'date-fns/locale';

const Promotions = () => {
  const [searchTerm, setSearchTerm] = useState('');

  const { data: promotions, isLoading } = useQuery({
    queryKey: ['promotions'],
    queryFn: () => promotionService.getAll().then(res => res.data),
  });

  const { data: promotionsActives } = useQuery({
    queryKey: ['promotions-actives'],
    queryFn: () => promotionService.getActives().then(res => res.data),
  });

  const filteredPromotions = promotions?.filter(p =>
    p.nom?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    p.description?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  if (isLoading) return <Loading size="lg" />;

  return (
    <div className="space-y-6">
      {/* En-tête */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-800">Gestion des promotions</h1>
          <p className="text-gray-500 mt-1">Créez et gérez vos promotions</p>
        </div>
        <Button icon={Plus}>Nouvelle promotion</Button>
      </div>

      {/* Statistiques */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card className="border-l-4 border-primary-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Total promotions</p>
              <p className="text-2xl font-bold text-gray-800">{promotions?.length || 0}</p>
            </div>
            <Tag className="w-12 h-12 text-primary-500" />
          </div>
        </Card>

        <Card className="border-l-4 border-green-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Promotions actives</p>
              <p className="text-2xl font-bold text-green-600">{promotionsActives?.length || 0}</p>
            </div>
            <Tag className="w-12 h-12 text-green-500" />
          </div>
        </Card>

        <Card className="border-l-4 border-purple-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Réduction moyenne</p>
              <p className="text-2xl font-bold text-purple-600">
                {promotions?.length > 0
                  ? (promotions.reduce((acc, p) => acc + (p.pourcentageReduction || 0), 0) / promotions.length).toFixed(1)
                  : 0}%
              </p>
            </div>
            <Tag className="w-12 h-12 text-purple-500" />
          </div>
        </Card>
      </div>

      {/* Recherche */}
      <Card>
        <div className="relative">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
          <input
            type="text"
            placeholder="Rechercher une promotion..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
          />
        </div>
      </Card>

      {/* Liste des promotions */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredPromotions?.map((promotion) => {
          const isActive = new Date(promotion.dateDebut) <= new Date() && new Date() <= new Date(promotion.dateFin);
          
          return (
            <Card key={promotion.id} className="hover:shadow-lg transition-shadow">
              <div className="space-y-4">
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <h3 className="text-lg font-semibold text-gray-800">{promotion.nom}</h3>
                    <p className="text-sm text-gray-500 mt-1">{promotion.description}</p>
                  </div>
                  {isActive ? (
                    <span className="px-3 py-1 bg-green-100 text-green-700 rounded-full text-sm font-medium">
                      Active
                    </span>
                  ) : (
                    <span className="px-3 py-1 bg-gray-100 text-gray-700 rounded-full text-sm font-medium">
                      Inactive
                    </span>
                  )}
                </div>

                <div className="flex items-center justify-center py-4 bg-gradient-to-r from-primary-500 to-purple-500 rounded-lg">
                  <div className="text-center text-white">
                    <p className="text-4xl font-bold">-{promotion.pourcentageReduction}%</p>
                    <p className="text-sm mt-1">de réduction</p>
                  </div>
                </div>

                <div className="space-y-2 text-sm">
                  <div className="flex items-center text-gray-600">
                    <Calendar className="w-4 h-4 mr-2" />
                    <span>Du {format(new Date(promotion.dateDebut), 'dd/MM/yyyy', { locale: fr })}</span>
                  </div>
                  <div className="flex items-center text-gray-600">
                    <Calendar className="w-4 h-4 mr-2" />
                    <span>Au {format(new Date(promotion.dateFin), 'dd/MM/yyyy', { locale: fr })}</span>
                  </div>
                </div>

                {promotion.produits && promotion.produits.length > 0 && (
                  <div className="pt-3 border-t border-gray-200">
                    <p className="text-sm font-medium text-gray-700">Produits concernés:</p>
                    <div className="mt-2 flex flex-wrap gap-2">
                      {promotion.produits.slice(0, 3).map((produit, index) => (
                        <span key={index} className="px-2 py-1 bg-blue-50 text-blue-700 rounded text-xs">
                          {produit.nom}
                        </span>
                      ))}
                      {promotion.produits.length > 3 && (
                        <span className="px-2 py-1 bg-gray-100 text-gray-600 rounded text-xs">
                          +{promotion.produits.length - 3} autres
                        </span>
                      )}
                    </div>
                  </div>
                )}

                <div className="flex space-x-2 pt-3 border-t border-gray-200">
                  <Button size="sm" variant="outline" className="flex-1">Modifier</Button>
                  <Button size="sm" variant="danger" className="flex-1">Supprimer</Button>
                </div>
              </div>
            </Card>
          );
        })}
      </div>

      {(!filteredPromotions || filteredPromotions.length === 0) && (
        <Card>
          <div className="text-center py-12">
            <Tag className="w-16 h-16 text-gray-300 mx-auto mb-4" />
            <p className="text-gray-500">Aucune promotion trouvée</p>
          </div>
        </Card>
      )}
    </div>
  );
};

export default Promotions;

