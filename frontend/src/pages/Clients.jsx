import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { clientService, carteFideliteService } from '../services';
import Card from '../components/common/Card';
import Button from '../components/common/Button';
import Loading from '../components/common/Loading';
import { Users, Plus, Search, UserPlus, CreditCard, Gift } from 'lucide-react';
import toast from 'react-hot-toast';

const Clients = () => {
  const [searchTerm, setSearchTerm] = useState('');

  const { data: clients, isLoading, refetch } = useQuery({
    queryKey: ['clients'],
    queryFn: () => clientService.getAll().then(res => res.data),
  });

  const filteredClients = clients?.filter(client =>
    client.nom?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    client.email?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    client.telephone?.includes(searchTerm)
  );

  const stats = {
    totalClients: clients?.length || 0,
    clientsFideles: clients?.filter(c => c.carteFidelite)?.length || 0,
    totalPoints: clients?.reduce((acc, c) => acc + (c.carteFidelite?.soldePoints || 0), 0) || 0,
  };

  if (isLoading) return <Loading size="lg" />;

  return (
    <div className="space-y-6">
      {/* En-tête */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-800">Gestion des clients</h1>
          <p className="text-gray-500 mt-1">Gérez vos clients et programmes de fidélité</p>
        </div>
        <Button icon={UserPlus}>Nouveau client</Button>
      </div>

      {/* Statistiques */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card className="border-l-4 border-primary-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Total clients</p>
              <p className="text-2xl font-bold text-gray-800">{stats.totalClients}</p>
            </div>
            <Users className="w-12 h-12 text-primary-500" />
          </div>
        </Card>

        <Card className="border-l-4 border-purple-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Cartes fidélité</p>
              <p className="text-2xl font-bold text-purple-600">{stats.clientsFideles}</p>
            </div>
            <CreditCard className="w-12 h-12 text-purple-500" />
          </div>
        </Card>

        <Card className="border-l-4 border-orange-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Points totaux</p>
              <p className="text-2xl font-bold text-orange-600">{stats.totalPoints.toLocaleString('fr-FR')}</p>
            </div>
            <Gift className="w-12 h-12 text-orange-500" />
          </div>
        </Card>
      </div>

      {/* Recherche */}
      <Card>
        <div className="flex items-center gap-4">
          <div className="flex-1 relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
            <input
              type="text"
              placeholder="Rechercher un client (nom, email, téléphone)..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
            />
          </div>
        </div>
      </Card>

      {/* Liste des clients */}
      <Card title="Liste des clients">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b border-gray-200">
                <th className="text-left py-3 px-4 font-semibold text-gray-700">Client</th>
                <th className="text-left py-3 px-4 font-semibold text-gray-700">Contact</th>
                <th className="text-center py-3 px-4 font-semibold text-gray-700">Carte fidélité</th>
                <th className="text-center py-3 px-4 font-semibold text-gray-700">Points</th>
                <th className="text-center py-3 px-4 font-semibold text-gray-700">Total achats</th>
                <th className="text-center py-3 px-4 font-semibold text-gray-700">Statut</th>
                <th className="text-center py-3 px-4 font-semibold text-gray-700">Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredClients?.map((client) => (
                <tr key={client.id} className="border-b border-gray-100 hover:bg-gray-50">
                  <td className="py-3 px-4">
                    <div>
                      <p className="font-medium text-gray-800">{client.nom} {client.prenom}</p>
                      <p className="text-sm text-gray-500">{client.adresse}</p>
                    </div>
                  </td>
                  <td className="py-3 px-4">
                    <div>
                      <p className="text-gray-700">{client.email}</p>
                      <p className="text-sm text-gray-500">{client.telephone}</p>
                    </div>
                  </td>
                  <td className="py-3 px-4 text-center">
                    {client.carteFidelite ? (
                      <div>
                        <p className="font-mono text-sm text-gray-700">{client.carteFidelite.numeroCarte}</p>
                        <span className="px-2 py-1 bg-purple-100 text-purple-700 rounded text-xs">
                          {client.carteFidelite.niveauFidelite}
                        </span>
                      </div>
                    ) : (
                      <span className="text-gray-400">-</span>
                    )}
                  </td>
                  <td className="py-3 px-4 text-center">
                    {client.carteFidelite ? (
                      <span className="font-semibold text-orange-600">
                        {client.carteFidelite.soldePoints} pts
                      </span>
                    ) : (
                      <span className="text-gray-400">-</span>
                    )}
                  </td>
                  <td className="py-3 px-4 text-center">
                    <span className="font-semibold text-gray-800">
                      {(client.totalAchats || 0).toLocaleString('fr-FR')} FCFA
                    </span>
                  </td>
                  <td className="py-3 px-4 text-center">
                    {client.actif ? (
                      <span className="px-3 py-1 bg-green-100 text-green-700 rounded-full text-sm font-medium">
                        Actif
                      </span>
                    ) : (
                      <span className="px-3 py-1 bg-gray-100 text-gray-700 rounded-full text-sm font-medium">
                        Inactif
                      </span>
                    )}
                  </td>
                  <td className="py-3 px-4">
                    <div className="flex items-center justify-center space-x-2">
                      <Button size="sm" variant="outline">Détails</Button>
                      {client.carteFidelite && (
                        <Button size="sm" variant="success" icon={Gift}>Points</Button>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          {(!filteredClients || filteredClients.length === 0) && (
            <div className="text-center py-12">
              <Users className="w-16 h-16 text-gray-300 mx-auto mb-4" />
              <p className="text-gray-500">Aucun client trouvé</p>
            </div>
          )}
        </div>
      </Card>
    </div>
  );
};

export default Clients;

