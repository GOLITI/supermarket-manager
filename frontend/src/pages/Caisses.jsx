import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { transactionService, seanceCaisseService } from '../services';
import Card from '../components/common/Card';
import Button from '../components/common/Button';
import Loading from '../components/common/Loading';
import { CreditCard, DollarSign, ShoppingBag, Plus, Eye } from 'lucide-react';
import { format } from 'date-fns';
import { fr } from 'date-fns/locale';
import toast from 'react-hot-toast';

const Caisses = () => {
  const [selectedDate, setSelectedDate] = useState(format(new Date(), 'yyyy-MM-dd'));

  const { data: transactions, isLoading } = useQuery({
    queryKey: ['transactions', selectedDate],
    queryFn: () => transactionService.getAll({ date: selectedDate }).then(res => res.data),
  });

  const { data: seancesEnCours } = useQuery({
    queryKey: ['seances-en-cours'],
    queryFn: () => seanceCaisseService.getEnCours().then(res => res.data),
  });

  const { data: chiffreAffaires } = useQuery({
    queryKey: ['chiffre-affaires', selectedDate],
    queryFn: () => transactionService.getChiffreAffaires({ date: selectedDate }).then(res => res.data),
  });

  const stats = {
    totalVentes: transactions?.length || 0,
    chiffreAffaires: chiffreAffaires || 0,
    panierMoyen: transactions?.length > 0 ? (chiffreAffaires / transactions.length) : 0,
    caisseEnCours: seancesEnCours?.length || 0,
  };

  if (isLoading) return <Loading size="lg" />;

  return (
    <div className="space-y-6">
      {/* En-tête */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-800">Gestion des caisses</h1>
          <p className="text-gray-500 mt-1">Suivez vos ventes et transactions</p>
        </div>
        <Button icon={Plus}>Nouvelle vente</Button>
      </div>

      {/* Statistiques */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <Card className="border-l-4 border-blue-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Ventes du jour</p>
              <p className="text-2xl font-bold text-gray-800">{stats.totalVentes}</p>
            </div>
            <ShoppingBag className="w-12 h-12 text-blue-500" />
          </div>
        </Card>

        <Card className="border-l-4 border-green-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Chiffre d'affaires</p>
              <p className="text-2xl font-bold text-green-600">
                {stats.chiffreAffaires.toLocaleString('fr-FR')} FCFA
              </p>
            </div>
            <DollarSign className="w-12 h-12 text-green-500" />
          </div>
        </Card>

        <Card className="border-l-4 border-purple-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Panier moyen</p>
              <p className="text-2xl font-bold text-purple-600">
                {stats.panierMoyen.toLocaleString('fr-FR')} FCFA
              </p>
            </div>
            <CreditCard className="w-12 h-12 text-purple-500" />
          </div>
        </Card>

        <Card className="border-l-4 border-orange-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Caisses ouvertes</p>
              <p className="text-2xl font-bold text-orange-600">{stats.caisseEnCours}</p>
            </div>
            <CreditCard className="w-12 h-12 text-orange-500" />
          </div>
        </Card>
      </div>

      {/* Filtres */}
      <Card>
        <div className="flex items-center gap-4">
          <div className="flex-1">
            <label className="block text-sm font-medium text-gray-700 mb-2">Date</label>
            <input
              type="date"
              value={selectedDate}
              onChange={(e) => setSelectedDate(e.target.value)}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
            />
          </div>
          <div className="flex-1">
            <label className="block text-sm font-medium text-gray-700 mb-2">Caisse</label>
            <select className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500">
              <option>Toutes les caisses</option>
              {seancesEnCours?.map(seance => (
                <option key={seance.id} value={seance.id}>Caisse {seance.numeroRang}</option>
              ))}
            </select>
          </div>
          <div className="flex-1">
            <label className="block text-sm font-medium text-gray-700 mb-2">Mode de paiement</label>
            <select className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500">
              <option>Tous</option>
              <option>ESPECES</option>
              <option>CARTE_BANCAIRE</option>
              <option>MOBILE_MONEY</option>
            </select>
          </div>
        </div>
      </Card>

      {/* Liste des transactions */}
      <Card title="Transactions récentes">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b border-gray-200">
                <th className="text-left py-3 px-4 font-semibold text-gray-700">N° Transaction</th>
                <th className="text-left py-3 px-4 font-semibold text-gray-700">Date/Heure</th>
                <th className="text-left py-3 px-4 font-semibold text-gray-700">Client</th>
                <th className="text-center py-3 px-4 font-semibold text-gray-700">Articles</th>
                <th className="text-center py-3 px-4 font-semibold text-gray-700">Montant</th>
                <th className="text-center py-3 px-4 font-semibold text-gray-700">Paiement</th>
                <th className="text-center py-3 px-4 font-semibold text-gray-700">Statut</th>
                <th className="text-center py-3 px-4 font-semibold text-gray-700">Actions</th>
              </tr>
            </thead>
            <tbody>
              {transactions?.map((transaction) => (
                <tr key={transaction.id} className="border-b border-gray-100 hover:bg-gray-50">
                  <td className="py-3 px-4 font-mono text-sm text-gray-600">
                    #{transaction.numeroTransaction}
                  </td>
                  <td className="py-3 px-4 text-gray-600">
                    {format(new Date(transaction.dateHeure), 'dd/MM/yyyy HH:mm', { locale: fr })}
                  </td>
                  <td className="py-3 px-4">
                    <div>
                      <p className="font-medium text-gray-800">
                        {transaction.client ? transaction.client.nom : 'Client anonyme'}
                      </p>
                      {transaction.client?.carteFidelite && (
                        <p className="text-sm text-primary-600">Carte fidélité</p>
                      )}
                    </div>
                  </td>
                  <td className="py-3 px-4 text-center text-gray-600">
                    {transaction.lignesTransaction?.length || 0}
                  </td>
                  <td className="py-3 px-4 text-center">
                    <span className="font-semibold text-gray-800">
                      {transaction.montantTotal.toLocaleString('fr-FR')} FCFA
                    </span>
                  </td>
                  <td className="py-3 px-4 text-center">
                    <span className="px-2 py-1 bg-gray-100 text-gray-700 rounded text-sm">
                      {transaction.modePaiement}
                    </span>
                  </td>
                  <td className="py-3 px-4 text-center">
                    {transaction.statut === 'COMPLETEE' ? (
                      <span className="px-3 py-1 bg-green-100 text-green-700 rounded-full text-sm font-medium">
                        Complétée
                      </span>
                    ) : transaction.statut === 'ANNULEE' ? (
                      <span className="px-3 py-1 bg-red-100 text-red-700 rounded-full text-sm font-medium">
                        Annulée
                      </span>
                    ) : (
                      <span className="px-3 py-1 bg-yellow-100 text-yellow-700 rounded-full text-sm font-medium">
                        En cours
                      </span>
                    )}
                  </td>
                  <td className="py-3 px-4">
                    <div className="flex items-center justify-center space-x-2">
                      <Button size="sm" variant="outline" icon={Eye}>Détails</Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          {(!transactions || transactions.length === 0) && (
            <div className="text-center py-12">
              <ShoppingBag className="w-16 h-16 text-gray-300 mx-auto mb-4" />
              <p className="text-gray-500">Aucune transaction trouvée</p>
            </div>
          )}
        </div>
      </Card>
    </div>
  );
};

export default Caisses;

