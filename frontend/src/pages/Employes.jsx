import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { employeService, planningService, absenceService } from '../services';
import Card from '../components/common/Card';
import Button from '../components/common/Button';
import Loading from '../components/common/Loading';
import { UserCircle, Plus, Search, Calendar, Clock, AlertCircle } from 'lucide-react';
import { format } from 'date-fns';
import { fr } from 'date-fns/locale';

const Employes = () => {
  const [searchTerm, setSearchTerm] = useState('');
  const [activeTab, setActiveTab] = useState('employes');

  const { data: employes, isLoading } = useQuery({
    queryKey: ['employes'],
    queryFn: () => employeService.getAll().then(res => res.data),
  });

  const { data: absencesEnAttente } = useQuery({
    queryKey: ['absences-en-attente'],
    queryFn: () => absenceService.getEnAttente().then(res => res.data),
  });

  const filteredEmployes = employes?.filter(emp =>
    emp.nom?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    emp.prenom?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    emp.poste?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const stats = {
    totalEmployes: employes?.length || 0,
    employesActifs: employes?.filter(e => e.actif)?.length || 0,
    absencesEnAttente: absencesEnAttente?.length || 0,
  };

  if (isLoading) return <Loading size="lg" />;

  return (
    <div className="space-y-6">
      {/* En-tête */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-800">Gestion des Ressources Humaines</h1>
          <p className="text-gray-500 mt-1">Gérez vos employés, plannings et absences</p>
        </div>
        <Button icon={Plus}>Nouvel employé</Button>
      </div>

      {/* Statistiques */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <Card className="border-l-4 border-primary-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Total employés</p>
              <p className="text-2xl font-bold text-gray-800">{stats.totalEmployes}</p>
            </div>
            <UserCircle className="w-12 h-12 text-primary-500" />
          </div>
        </Card>

        <Card className="border-l-4 border-green-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Employés actifs</p>
              <p className="text-2xl font-bold text-green-600">{stats.employesActifs}</p>
            </div>
            <UserCircle className="w-12 h-12 text-green-500" />
          </div>
        </Card>

        <Card className="border-l-4 border-orange-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Demandes en attente</p>
              <p className="text-2xl font-bold text-orange-600">{stats.absencesEnAttente}</p>
            </div>
            <AlertCircle className="w-12 h-12 text-orange-500" />
          </div>
        </Card>

        <Card className="border-l-4 border-purple-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Présents aujourd'hui</p>
              <p className="text-2xl font-bold text-purple-600">{stats.employesActifs}</p>
            </div>
            <Clock className="w-12 h-12 text-purple-500" />
          </div>
        </Card>
      </div>

      {/* Tabs */}
      <Card>
        <div className="flex border-b border-gray-200">
          <button
            onClick={() => setActiveTab('employes')}
            className={`px-6 py-3 font-medium transition-colors ${
              activeTab === 'employes'
                ? 'text-primary-600 border-b-2 border-primary-600'
                : 'text-gray-500 hover:text-gray-700'
            }`}
          >
            Employés
          </button>
          <button
            onClick={() => setActiveTab('plannings')}
            className={`px-6 py-3 font-medium transition-colors ${
              activeTab === 'plannings'
                ? 'text-primary-600 border-b-2 border-primary-600'
                : 'text-gray-500 hover:text-gray-700'
            }`}
          >
            Plannings
          </button>
          <button
            onClick={() => setActiveTab('absences')}
            className={`px-6 py-3 font-medium transition-colors ${
              activeTab === 'absences'
                ? 'text-primary-600 border-b-2 border-primary-600'
                : 'text-gray-500 hover:text-gray-700'
            }`}
          >
            Absences {absencesEnAttente?.length > 0 && (
              <span className="ml-2 px-2 py-0.5 bg-orange-500 text-white rounded-full text-xs">
                {absencesEnAttente.length}
              </span>
            )}
          </button>
        </div>
      </Card>

      {/* Recherche */}
      {activeTab === 'employes' && (
        <Card>
          <div className="flex items-center gap-4">
            <div className="flex-1 relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
              <input
                type="text"
                placeholder="Rechercher un employé..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
              />
            </div>
          </div>
        </Card>
      )}

      {/* Liste des employés */}
      {activeTab === 'employes' && (
        <Card title="Liste des employés">
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="border-b border-gray-200">
                  <th className="text-left py-3 px-4 font-semibold text-gray-700">Employé</th>
                  <th className="text-left py-3 px-4 font-semibold text-gray-700">Poste</th>
                  <th className="text-center py-3 px-4 font-semibold text-gray-700">Contact</th>
                  <th className="text-center py-3 px-4 font-semibold text-gray-700">Date d'embauche</th>
                  <th className="text-center py-3 px-4 font-semibold text-gray-700">Salaire</th>
                  <th className="text-center py-3 px-4 font-semibold text-gray-700">Statut</th>
                  <th className="text-center py-3 px-4 font-semibold text-gray-700">Actions</th>
                </tr>
              </thead>
              <tbody>
                {filteredEmployes?.map((employe) => (
                  <tr key={employe.id} className="border-b border-gray-100 hover:bg-gray-50">
                    <td className="py-3 px-4">
                      <div className="flex items-center">
                        <div className="w-10 h-10 bg-primary-100 text-primary-600 rounded-full flex items-center justify-center font-bold mr-3">
                          {employe.nom?.charAt(0)}{employe.prenom?.charAt(0)}
                        </div>
                        <div>
                          <p className="font-medium text-gray-800">{employe.nom} {employe.prenom}</p>
                          <p className="text-sm text-gray-500">{employe.email}</p>
                        </div>
                      </div>
                    </td>
                    <td className="py-3 px-4">
                      <span className="px-3 py-1 bg-blue-100 text-blue-700 rounded-full text-sm font-medium">
                        {employe.poste}
                      </span>
                    </td>
                    <td className="py-3 px-4 text-center text-gray-600">{employe.telephone}</td>
                    <td className="py-3 px-4 text-center text-gray-600">
                      {employe.dateEmbauche && format(new Date(employe.dateEmbauche), 'dd/MM/yyyy', { locale: fr })}
                    </td>
                    <td className="py-3 px-4 text-center">
                      <span className="font-semibold text-gray-800">
                        {employe.salaire?.toLocaleString('fr-FR')} FCFA
                      </span>
                    </td>
                    <td className="py-3 px-4 text-center">
                      {employe.actif ? (
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
                        <Button size="sm" variant="outline" icon={Calendar}>Planning</Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </Card>
      )}

      {/* Demandes d'absence */}
      {activeTab === 'absences' && (
        <Card title="Demandes d'absence en attente">
          <div className="space-y-4">
            {absencesEnAttente?.map((absence) => (
              <div key={absence.id} className="p-4 border border-gray-200 rounded-lg hover:shadow-md transition-shadow">
                <div className="flex items-center justify-between">
                  <div className="flex-1">
                    <div className="flex items-center">
                      <div className="w-10 h-10 bg-primary-100 text-primary-600 rounded-full flex items-center justify-center font-bold mr-3">
                        {absence.employe?.nom?.charAt(0)}
                      </div>
                      <div>
                        <p className="font-medium text-gray-800">
                          {absence.employe?.nom} {absence.employe?.prenom}
                        </p>
                        <p className="text-sm text-gray-500">{absence.employe?.poste}</p>
                      </div>
                    </div>
                    <div className="mt-3 ml-13">
                      <p className="text-sm text-gray-700">
                        <span className="font-medium">Type:</span> {absence.typeAbsence}
                      </p>
                      <p className="text-sm text-gray-700">
                        <span className="font-medium">Période:</span> {format(new Date(absence.dateDebut), 'dd/MM/yyyy', { locale: fr })} - {format(new Date(absence.dateFin), 'dd/MM/yyyy', { locale: fr })}
                      </p>
                      {absence.motif && (
                        <p className="text-sm text-gray-700 mt-1">
                          <span className="font-medium">Motif:</span> {absence.motif}
                        </p>
                      )}
                    </div>
                  </div>
                  <div className="flex flex-col space-y-2">
                    <Button size="sm" variant="success">Approuver</Button>
                    <Button size="sm" variant="danger">Rejeter</Button>
                  </div>
                </div>
              </div>
            ))}
            {(!absencesEnAttente || absencesEnAttente.length === 0) && (
              <div className="text-center py-12">
                <AlertCircle className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                <p className="text-gray-500">Aucune demande en attente</p>
              </div>
            )}
          </div>
        </Card>
      )}
    </div>
  );
};

export default Employes;

