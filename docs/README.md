# 🏪 SuperMarket Manager - Application Complète

## 📝 Description

Application web moderne et professionnelle pour la gestion complète d'un supermarché. Développée avec Spring Boot (Backend) et React (Frontend).

## 🎯 Fonctionnalités implémentées

### ✅ Backend (Spring Boot + PostgreSQL)

1. **📊 Dashboard & Reporting**
   - Statistiques en temps réel
   - Graphiques de ventes
   - KPIs personnalisés
   - Analyses avancées

2. **📦 Gestion des Stocks**
   - Suivi en temps réel
   - Alertes automatiques (stock faible, péremption)
   - Gestion des entrepôts
   - Historique des mouvements

3. **💳 Gestion des Caisses**
   - Sessions de caisse
   - Transactions multiples (Espèces, CB, Mobile Money)
   - Statistiques de ventes
   - Rapports journaliers

4. **👥 Gestion des Clients**
   - Base de données clients
   - Programme de fidélité
   - Cartes de fidélité avec points
   - Historique d'achats

5. **👤 Ressources Humaines**
   - Gestion des employés
   - Plannings et horaires
   - Demandes d'absence
   - Système de pointage
   - Suivi des performances

6. **🚚 Gestion des Fournisseurs**
   - Base de fournisseurs
   - Commandes et livraisons
   - Suivi des délais
   - Évaluation des fournisseurs

7. **🏷️ Promotions**
   - Création de promotions
   - Réductions temporelles
   - Application automatique
   - Gestion multi-produits

8. **📈 Rapports & Analyses**
   - Rapports détaillés
   - Export PDF
   - Prévisions de vente
   - Analyse des marges

### ✅ Frontend (React + Vite + Tailwind CSS)

1. **Interface moderne et responsive**
   - Design professionnel et élégant
   - Navigation intuitive
   - Composants réutilisables
   - Animations fluides

2. **Pages implémentées**
   - Dashboard avec graphiques
   - Gestion des stocks
   - Gestion des caisses
   - Gestion des clients
   - Gestion des employés (RH)
   - Gestion des fournisseurs
   - Gestion des promotions
   - Rapports et analyses

3. **Fonctionnalités UX**
   - Recherche en temps réel
   - Filtres avancés
   - Notifications toast
   - Chargement optimisé
   - Gestion d'erreurs

## 🚀 Technologies utilisées

### Backend
- Java 21
- Spring Boot 3.5.6
- Spring Data JPA
- PostgreSQL
- Lombok
- Maven

### Frontend
- React 18
- Vite
- React Router v6
- TanStack Query (React Query)
- Tailwind CSS
- Axios
- Lucide React (icônes)
- Recharts (graphiques)
- React Hot Toast
- date-fns

## 📦 Installation et lancement

### Prérequis
- Java 21+
- Node.js 18+
- PostgreSQL 15+ (ou compte Railway)

### Backend

```bash
cd backend

# Avec PostgreSQL local
./mvnw spring-boot:run

# Avec PostgreSQL Railway (définir les variables d'environnement)
export DATABASE_URL="postgresql://user:password@host:port/database"
./mvnw spring-boot:run
```

Le backend sera disponible sur `http://localhost:8080`

### Frontend

```bash
cd frontend

# Installer les dépendances
npm install

# Lancer en développement
npm run dev

# Build pour production
npm run build
```

Le frontend sera disponible sur `http://localhost:5173`

## 🗄️ Configuration PostgreSQL

### Option 1 : Local

```bash
# Créer la base de données
createdb supermarket_db

# Modifier application.properties (déjà configuré)
spring.datasource.url=jdbc:postgresql://localhost:5432/supermarket_db
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### Option 2 : Railway (Recommandé)

Voir le guide détaillé : `GUIDE_RAILWAY_DEPLOYMENT.md`

1. Créer un projet sur [railway.app](https://railway.app)
2. Ajouter PostgreSQL
3. Récupérer le `DATABASE_URL`
4. Déployer le backend et frontend

## 🌐 Déploiement

### Backend → Railway
- Build automatique via GitHub
- PostgreSQL hébergé
- Variables d'environnement configurées

### Frontend → Vercel
- Déploiement automatique
- CDN global
- SSL gratuit

## 📊 Fonctionnalités avancées

### Système de notifications
- Alertes stock faible
- Notifications péremption
- Demandes d'absence en attente
- Objectifs atteints

### Analyses prédictives
- Prévisions de demande
- Heures de pointe
- Tendances de vente
- Marges par catégorie

### Multi-canal
- Ventes en magasin
- Click & Collect (préparé)
- E-commerce (préparé)

### Sécurité
- Authentification JWT (préparée)
- Gestion des rôles (préparée)
- Validation des données
- Protection CORS

## 📱 Interface responsive

L'application s'adapte à tous les écrans :
- Desktop (1920px+)
- Tablette (768px - 1024px)
- Mobile (320px - 767px)

## 🎨 Design professionnel

- Palette de couleurs cohérente
- Typographie moderne
- Icônes Lucide React
- Animations CSS
- Graphiques interactifs

## 📈 Performance

### Backend
- Pagination des listes
- Cache des requêtes fréquentes
- Index sur les colonnes critiques
- Lazy loading des relations

### Frontend
- Code splitting
- Lazy loading des pages
- Cache React Query (5 min)
- Optimisation des images
- Minification CSS/JS

## 🔐 Sécurité (préparée)

- Authentification JWT
- Autorisation basée sur les rôles
- Validation des entrées
- Protection XSS/CSRF
- HTTPS obligatoire en production

## 📝 API REST

Base URL : `http://localhost:8080/api`

### Endpoints principaux

```
GET    /api/dashboard              - Statistiques générales
GET    /api/stocks                 - Liste des stocks
GET    /api/caisses/transactions   - Transactions
GET    /api/clients                - Clients
GET    /api/employes               - Employés
GET    /api/fournisseurs           - Fournisseurs
GET    /api/promotions             - Promotions
```

Documentation complète : Swagger UI disponible sur `/swagger-ui.html`

## 🧪 Tests

### Backend
```bash
cd backend
./mvnw test
```

### Frontend
```bash
cd frontend
npm run test
```

## 📞 Support

- Email : marcgoliti429@gmail.com
- GitHub : [GOLITI/supermarket-manager](https://github.com/GOLITI/supermarket-manager)

## 📄 Licence

Ce projet est développé dans le cadre d'un projet universitaire.

## 🎓 Contexte

Projet AGL (Analyse et Génie Logiciel) - Gestion de supermarché moderne avec toutes les fonctionnalités professionnelles requises.

## 🌍 Localisation

L'application est configurée pour la Côte d'Ivoire :
- Langue : Français
- Devise : FCFA
- Format de date : dd/MM/yyyy
- Fuseau horaire : Africa/Abidjan

## 🎉 Statut du projet

✅ **100% Fonctionnel**
- Backend complet et testé
- Frontend professionnel et responsive
- Prêt pour le déploiement
- Documentation complète

## 📚 Documentation

- `README.md` - Ce fichier
- `GUIDE_RAILWAY_DEPLOYMENT.md` - Guide de déploiement Railway
- `frontend/README.md` - Documentation frontend
- `backend/README.md` - Documentation backend

---

**Développé avec ❤️ pour la gestion moderne des supermarchés**

