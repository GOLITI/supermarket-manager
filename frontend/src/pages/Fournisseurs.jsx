import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { fournisseurService } from '../services';
import Card from '../components/common/Card';
import Button from '../components/common/Button';
import Loading from '../components/common/Loading';
import { Truck, Plus, Search, Mail, Phone } from 'lucide-react';
import toast from 'react-hot-toast';

const Fournisseurs = () => {
  const [searchTerm, setSearchTerm] = useState('');

  const { data: fournisseurs, isLoading } = useQuery({
    queryKey: ['fournisseurs'],
    queryFn: () => fournisseurService.getAll().then(res => res.data),
  });

  const filteredFournisseurs = fournisseurs?.filter(f =>
    f.nom?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    f.email?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  if (isLoading) return <Loading size="lg" />;

  return (
    <div className="space-y-6">
      {/* En-tête */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-800">Gestion des fournisseurs</h1>
          <p className="text-gray-500 mt-1">Gérez vos fournisseurs et relations</p>
        </div>
        <Button icon={Plus}>Ajouter un fournisseur</Button>
      </div>

      {/* Statistiques */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <Card className="border-l-4 border-primary-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Total fournisseurs</p>
              <p className="text-2xl font-bold text-gray-800">{fournisseurs?.length || 0}</p>
            </div>
            <Truck className="w-12 h-12 text-primary-500" />
          </div>
        </Card>
        <Card className="border-l-4 border-green-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Actifs</p>
              <p className="text-2xl font-bold text-green-600">
                {fournisseurs?.filter(f => f.actif)?.length || 0}
              </p>
            </div>
            <Truck className="w-12 h-12 text-green-500" />
          </div>
        </Card>
        <Card className="border-l-4 border-blue-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Alimentaires</p>
              <p className="text-2xl font-bold text-blue-600">
                {fournisseurs?.filter(f => f.typeFournisseur === 'ALIMENTAIRE')?.length || 0}
              </p>
            </div>
            <Truck className="w-12 h-12 text-blue-500" />
          </div>
        </Card>
        <Card className="border-l-4 border-purple-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Non alimentaires</p>
              <p className="text-2xl font-bold text-purple-600">
                {fournisseurs?.filter(f => f.typeFournisseur === 'NON_ALIMENTAIRE')?.length || 0}
              </p>
            </div>
            <Truck className="w-12 h-12 text-purple-500" />
          </div>
        </Card>
      </div>

      {/* Recherche */}
      <Card>
        <div className="relative">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
          <input
            type="text"
            placeholder="Rechercher un fournisseur..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
          />
        </div>
      </Card>

      {/* Liste des fournisseurs */}
      <Card>
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b border-gray-200">
                <th className="text-left py-3 px-4 font-semibold text-gray-700">Fournisseur</th>
                <th className="text-center py-3 px-4 font-semibold text-gray-700">Type</th>
                <th className="text-center py-3 px-4 font-semibold text-gray-700">Contact</th>
                <th className="text-center py-3 px-4 font-semibold text-gray-700">Délai livraison</th>
                <th className="text-center py-3 px-4 font-semibold text-gray-700">Note</th>
                <th className="text-center py-3 px-4 font-semibold text-gray-700">Statut</th>
                <th className="text-center py-3 px-4 font-semibold text-gray-700">Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredFournisseurs?.map((fournisseur) => (
                <tr key={fournisseur.id} className="border-b border-gray-100 hover:bg-gray-50">
                  <td className="py-3 px-4">
                    <div>
                      <p className="font-medium text-gray-800">{fournisseur.nom}</p>
                      <p className="text-sm text-gray-500">{fournisseur.adresse}</p>
                    </div>
                  </td>
                  <td className="py-3 px-4">
                    <span className="px-3 py-1 bg-blue-100 text-blue-700 rounded-full text-sm">
                      {fournisseur.typeFournisseur}
                    </span>
                  </td>
                  <td className="py-3 px-4 text-center">
                    <div className="space-y-1">
                      <div className="flex items-center justify-center gap-2 text-sm">
                        <Mail className="w-4 h-4 text-gray-400" />
                        <span className="text-gray-700">{fournisseur.email}</span>
                      </div>
                      <div className="flex items-center justify-center gap-2 text-sm">
                        <Phone className="w-4 h-4 text-gray-400" />
                        <span className="text-gray-500">{fournisseur.telephone}</span>
                      </div>
                    </div>
                  </td>
                  <td className="py-3 px-4 text-center text-gray-600">
                    {fournisseur.delaiLivraison} jours
                  </td>
                  <td className="py-3 px-4 text-center">
                    <div className="flex items-center justify-center">
                      <span className="text-yellow-500">★</span>
                      <span className="ml-1 font-semibold">{fournisseur.note || 'N/A'}</span>
                    </div>
                  </td>
                  <td className="py-3 px-4 text-center">
                    {fournisseur.actif ? (
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
                      <Button size="sm" variant="primary">Commander</Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          {(!filteredFournisseurs || filteredFournisseurs.length === 0) && (
            <div className="text-center py-12">
              <Truck className="w-16 h-16 text-gray-300 mx-auto mb-4" />
              <p className="text-gray-500">Aucun fournisseur trouvé</p>
            </div>
          )}
        </div>
      </Card>
    </div>
  );
};

export default Fournisseurs;

