import api from '../config/api';

// Dashboard Service
export const dashboardService = {
  getStatistics: (params) => api.get('/api/dashboard', { params }),
  getVentesProduits: (params) => api.get('/api/dashboard/ventes-produits', { params }),
  getProduitsEnBaisse: (params) => api.get('/api/dashboard/produits-baisse', { params }),
  getHeuresPointe: (params) => api.get('/api/dashboard/heures-pointe', { params }),
  getFrequentation: (params) => api.get('/api/dashboard/frequentation', { params }),
  getMargesParCategorie: (params) => api.get('/api/dashboard/marges-categories', { params }),
  getEvolutionProduit: (productId, params) => api.get(`/api/dashboard/evolution-produit/${productId}`, { params }),
};

// Stock Service
export const stockService = {
  getAll: (params) => api.get('/api/stocks', { params }),
  getById: (id) => api.get(`/api/stocks/${id}`),
  getAlertes: () => api.get('/api/stocks/alertes'),
  getProchesPeremption: (jours) => api.get(`/api/stocks/proches-peremption/${jours}`),
  getTotalProduit: (productId) => api.get(`/api/stocks/total/${productId}`),
  ajouterQuantite: (id, quantite) => api.post(`/api/stocks/${id}/ajouter`, null, { params: { quantite } }),
  retirerQuantite: (id, quantite) => api.post(`/api/stocks/${id}/retirer`, null, { params: { quantite } }),
};

// Produit Service
export const produitService = {
  getAll: (params) => api.get('/api/produits', { params }),
  getById: (id) => api.get(`/api/produits/${id}`),
  create: (data) => api.post('/api/produits', data),
  update: (id, data) => api.put(`/api/produits/${id}`, data),
  delete: (id) => api.delete(`/api/produits/${id}`),
  search: (nom) => api.get(`/api/produits/search`, { params: { nom } }),
  getByCategorie: (categorie) => api.get(`/api/produits/categorie/${categorie}`),
};

// Transaction Service
export const transactionService = {
  getAll: (params) => api.get('/api/caisses/transactions', { params }),
  getById: (id) => api.get(`/api/caisses/transactions/${id}`),
  create: (data) => api.post('/api/caisses/transactions', data),
  annuler: (id) => api.post(`/api/caisses/transactions/${id}/annuler`),
  getProduitsLesPlusVendus: (params) => api.get('/api/caisses/transactions/produits-plus-vendus', { params }),
  getChiffreAffaires: (params) => api.get('/api/caisses/transactions/chiffre-affaires', { params }),
};

// Seance Caisse Service
export const seanceCaisseService = {
  getAll: (params) => api.get('/api/caisses/seances', { params }),
  getById: (id) => api.get(`/api/caisses/seances/${id}`),
  ouvrir: (data) => api.post('/api/caisses/seances/ouvrir', data),
  fermer: (id, data) => api.post(`/api/caisses/seances/${id}/fermer`, data),
  getEnCours: () => api.get('/api/caisses/seances/en-cours'),
};

// Client Service
export const clientService = {
  getAll: (params) => api.get('/api/clients', { params }),
  getById: (id) => api.get(`/api/clients/${id}`),
  create: (data) => api.post('/api/clients', data),
  update: (id, data) => api.put(`/api/clients/${id}`, data),
  delete: (id) => api.delete(`/api/clients/${id}`),
  search: (query) => api.get('/api/clients/search', { params: { query } }),
  getHistoriqueAchats: (id) => api.get(`/api/clients/${id}/historique-achats`),
};

// Carte Fidelite Service
export const carteFideliteService = {
  getByClient: (clientId) => api.get(`/api/cartes-fidelite/client/${clientId}`),
  crediterPoints: (id, points) => api.post(`/api/cartes-fidelite/${id}/crediter`, null, { params: { points } }),
  debiterPoints: (id, points) => api.post(`/api/cartes-fidelite/${id}/debiter`, null, { params: { points } }),
  getHistorique: (id) => api.get(`/api/cartes-fidelite/${id}/historique`),
};

// Employe Service
export const employeService = {
  getAll: (params) => api.get('/api/employes', { params }),
  getById: (id) => api.get(`/api/employes/${id}`),
  create: (data) => api.post('/api/employes', data),
  update: (id, data) => api.put(`/api/employes/${id}`, data),
  delete: (id) => api.delete(`/api/employes/${id}`),
  getByPoste: (poste) => api.get(`/api/employes/poste/${poste}`),
  getActifs: () => api.get('/api/employes/actifs'),
};

// Planning Service
export const planningService = {
  getAll: (params) => api.get('/api/plannings', { params }),
  getById: (id) => api.get(`/api/plannings/${id}`),
  create: (data) => api.post('/api/plannings', data),
  update: (id, data) => api.put(`/api/plannings/${id}`, data),
  delete: (id) => api.delete(`/api/plannings/${id}`),
  getByEmploye: (employeId, params) => api.get(`/api/plannings/employe/${employeId}`, { params }),
  getByPeriode: (debut, fin) => api.get('/api/plannings/periode', { params: { debut, fin } }),
};

// Absence Service
export const absenceService = {
  getAll: (params) => api.get('/api/absences', { params }),
  getById: (id) => api.get(`/api/absences/${id}`),
  create: (data) => api.post('/api/absences', data),
  valider: (id, data) => api.post(`/api/absences/${id}/valider`, data),
  getEnAttente: () => api.get('/api/absences/en-attente'),
  getByEmploye: (employeId) => api.get(`/api/absences/employe/${employeId}`),
};

// Pointage Service
export const pointageService = {
  pointer: (data) => api.post('/api/pointages', data),
  getByEmploye: (employeId, params) => api.get(`/api/pointages/employe/${employeId}`, { params }),
  getByDate: (date) => api.get('/api/pointages/date', { params: { date } }),
};

// Fournisseur Service
export const fournisseurService = {
  getAll: (params) => api.get('/api/fournisseurs', { params }),
  getById: (id) => api.get(`/api/fournisseurs/${id}`),
  create: (data) => api.post('/api/fournisseurs', data),
  update: (id, data) => api.put(`/api/fournisseurs/${id}`, data),
  delete: (id) => api.delete(`/api/fournisseurs/${id}`),
  getActifs: () => api.get('/api/fournisseurs/actifs'),
};

// Commande Service
export const commandeService = {
  getAll: (params) => api.get('/api/commandes', { params }),
  getById: (id) => api.get(`/api/commandes/${id}`),
  create: (data) => api.post('/api/commandes', data),
  update: (id, data) => api.put(`/api/commandes/${id}`, data),
  valider: (id) => api.post(`/api/commandes/${id}/valider`),
  annuler: (id) => api.post(`/api/commandes/${id}/annuler`),
  recevoir: (id, data) => api.post(`/api/commandes/${id}/recevoir`, data),
  getEnCours: () => api.get('/api/commandes/en-cours'),
};

// Promotion Service
export const promotionService = {
  getAll: (params) => api.get('/api/promotions', { params }),
  getById: (id) => api.get(`/api/promotions/${id}`),
  create: (data) => api.post('/api/promotions', data),
  update: (id, data) => api.put(`/api/promotions/${id}`, data),
  delete: (id) => api.delete(`/api/promotions/${id}`),
  getActives: () => api.get('/api/promotions/actives'),
  getByProduit: (productId) => api.get(`/api/promotions/produit/${productId}`),
};

