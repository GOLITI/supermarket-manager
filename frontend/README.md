# SuperMarket Manager - Frontend

## 🎨 Interface moderne et professionnelle

Application React moderne pour la gestion complète d'un supermarché, avec une interface utilisateur intuitive et élégante.

## 🚀 Technologies utilisées

- **React 18** - Framework JavaScript
- **Vite** - Build tool ultra-rapide
- **React Router v6** - Routing
- **TanStack Query** (React Query) - Gestion des données serveur
- **Tailwind CSS** - Framework CSS utilitaire
- **Lucide React** - Icônes modernes
- **Recharts** - Graphiques et visualisations
- **Axios** - Client HTTP
- **React Hot Toast** - Notifications
- **date-fns** - Gestion des dates

## 📦 Installation

```bash
# Installer les dépendances
npm install

# Lancer en développement
npm run dev

# Build pour la production
npm run build

# Prévisualiser le build
npm run preview
```

## 🔧 Configuration

Créez un fichier `.env` à la racine du projet frontend :

```env
VITE_API_URL=http://localhost:8080
```

Pour Railway ou production, modifiez l'URL de l'API.

## 📁 Structure du projet

```
frontend/
├── src/
│   ├── components/
│   │   ├── Layout/
│   │   │   ├── Layout.jsx
│   │   │   ├── Sidebar.jsx
│   │   │   └── Header.jsx
│   │   └── common/
│   │       ├── Card.jsx
│   │       ├── Button.jsx
│   │       ├── Loading.jsx
│   │       └── StatCard.jsx
│   ├── pages/
│   │   ├── Dashboard.jsx
│   │   ├── Stocks.jsx
│   │   ├── Caisses.jsx
│   │   ├── Clients.jsx
│   │   ├── Employes.jsx
│   │   ├── Fournisseurs.jsx
│   │   ├── Promotions.jsx
│   │   └── Rapports.jsx
│   ├── services/
│   │   └── index.js
│   ├── config/
│   │   └── api.js
│   ├── App.jsx
│   ├── main.jsx
│   └── index.css
├── .env
├── .env.example
├── tailwind.config.js
├── postcss.config.js
├── vite.config.js
└── package.json
```

## 🎯 Fonctionnalités

### 📊 Dashboard
- Vue d'ensemble avec KPIs
- Graphiques de ventes
- Alertes en temps réel
- Statistiques journalières

### 📦 Gestion des stocks
- Suivi des stocks en temps réel
- Alertes de stock faible
- Gestion des entrées/sorties
- Produits proches de la péremption

### 💳 Caisses
- Transactions en temps réel
- Statistiques de ventes
- Gestion des sessions de caisse
- Différents modes de paiement

### 👥 Clients
- Base de données clients
- Programme de fidélité
- Historique d'achats
- Gestion des points

### 👤 Employés (RH)
- Gestion des employés
- Plannings
- Demandes d'absence
- Pointages

### 🚚 Fournisseurs
- Base de fournisseurs
- Gestion des commandes
- Suivi des livraisons

### 🏷️ Promotions
- Création de promotions
- Gestion des réductions
- Planification temporelle

### 📈 Rapports
- Rapports détaillés
- Graphiques d'analyse
- Export PDF
- KPIs personnalisés

## 🎨 Design

L'interface utilise une palette de couleurs moderne et professionnelle :
- **Primary** : Bleu (#0ea5e9)
- **Success** : Vert (#10b981)
- **Warning** : Orange (#f59e0b)
- **Danger** : Rouge (#ef4444)

## 🌐 Déploiement

### Vercel (Recommandé pour React)

```bash
npm run build
# Déployez le dossier dist/
```

### Railway

Ajoutez un fichier `railway.toml` :

```toml
[build]
builder = "nixpacks"

[deploy]
startCommand = "npm run dev -- --host 0.0.0.0 --port $PORT"
```

## 📝 Notes

- L'application est entièrement responsive
- Tous les composants sont réutilisables
- Les appels API sont gérés avec React Query pour un cache optimisé
- Les notifications sont gérées avec React Hot Toast
- L'application supporte le français (locale fr)

## 🔐 Sécurité

- Les tokens JWT sont stockés dans le localStorage
- Les intercepteurs Axios gèrent l'authentification
- Redirection automatique en cas de session expirée

## 📞 Support

Pour toute question ou problème, contactez l'équipe de développement.

