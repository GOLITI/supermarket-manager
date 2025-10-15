# 🚀 Guide de configuration PostgreSQL Railway pour SuperMarket Manager

## 📋 Étape 1 : Créer un compte Railway

1. Allez sur [railway.app](https://railway.app)
2. Cliquez sur **"Start a New Project"**
3. Connectez-vous avec GitHub

## 🗄️ Étape 2 : Créer une base de données PostgreSQL

1. Dans votre projet Railway, cliquez sur **"+ New"**
2. Sélectionnez **"Database"**
3. Choisissez **"Add PostgreSQL"**
4. Railway va automatiquement provisionner votre base de données

## 🔑 Étape 3 : Récupérer les informations de connexion

1. Cliquez sur votre service PostgreSQL
2. Allez dans l'onglet **"Variables"**
3. Vous verrez les variables suivantes :
   - `DATABASE_URL` - URL complète de connexion
   - `PGHOST` - Hôte
   - `PGPORT` - Port
   - `PGUSER` - Utilisateur
   - `PGPASSWORD` - Mot de passe
   - `PGDATABASE` - Nom de la base

## ⚙️ Étape 4 : Configurer le Backend

### Option A : Utiliser DATABASE_URL (Recommandé)

Railway fournit une variable `DATABASE_URL` au format :
```
postgresql://user:password@host:port/database
```

Dans votre `application.properties` (déjà configuré) :
```properties
spring.datasource.url=${DATABASE_URL:jdbc:postgresql://localhost:5432/supermarket_db}
```

### Option B : Utiliser les variables séparées

```properties
spring.datasource.url=jdbc:postgresql://${PGHOST}:${PGPORT}/${PGDATABASE}
spring.datasource.username=${PGUSER}
spring.datasource.password=${PGPASSWORD}
```

## 🚀 Étape 5 : Déployer le Backend sur Railway

### Méthode 1 : Via GitHub (Recommandé)

1. Poussez votre code sur GitHub :
```bash
cd /home/goliti/PROJETS/AGLPROJET/supermarket-manager
git add .
git commit -m "feat: Application complète avec frontend React"
git push origin main
```

2. Dans Railway :
   - Cliquez sur **"+ New"**
   - Sélectionnez **"GitHub Repo"**
   - Choisissez votre repository `supermarket-manager`
   - Railway détectera automatiquement votre projet Spring Boot

3. Configurez le build :
   - Root Directory : `/backend`
   - Build Command : `./mvnw clean package -DskipTests`
   - Start Command : `java -jar target/*.jar`

4. Ajoutez les variables d'environnement :
   - Cliquez sur votre service backend
   - Allez dans **"Variables"**
   - Ajoutez :
     - `DATABASE_URL` : Copiez depuis votre service PostgreSQL (Railway le fait automatiquement si les services sont liés)
     - `CORS_ORIGINS` : URL de votre frontend (ex: `https://votre-app.vercel.app`)

### Méthode 2 : Via CLI Railway

```bash
# Installer Railway CLI
npm i -g @railway/cli

# Se connecter
railway login

# Initialiser le projet
cd backend
railway init

# Lier à votre projet Railway
railway link

# Déployer
railway up
```

## 🌐 Étape 6 : Déployer le Frontend

### Option A : Vercel (Recommandé pour React)

1. Allez sur [vercel.com](https://vercel.com)
2. Cliquez sur **"New Project"**
3. Importez votre repository GitHub
4. Configurez :
   - **Root Directory** : `frontend`
   - **Framework Preset** : Vite
   - **Build Command** : `npm run build`
   - **Output Directory** : `dist`

5. Ajoutez la variable d'environnement :
   - `VITE_API_URL` : URL de votre backend Railway (ex: `https://votre-backend.railway.app`)

6. Déployez !

### Option B : Railway

```bash
cd frontend
railway init
railway up
```

Ajoutez dans les variables :
- `VITE_API_URL` : URL de votre backend

## 🔗 Étape 7 : Lier les services (Railway)

1. Dans Railway, sélectionnez votre service backend
2. Allez dans **"Settings"**
3. Dans **"Service Variables"**, Railway devrait automatiquement proposer de lier la base de données
4. Si ce n'est pas le cas, copiez manuellement le `DATABASE_URL` depuis PostgreSQL

## ✅ Étape 8 : Vérifier que tout fonctionne

### Tester le backend :
```bash
curl https://votre-backend.railway.app/api/dashboard
```

### Tester le frontend :
Ouvrez votre URL Vercel dans le navigateur et vérifiez que :
- Le dashboard s'affiche
- Les données sont chargées depuis le backend
- Aucune erreur CORS

## 🛠️ Commandes utiles Railway CLI

```bash
# Voir les logs
railway logs

# Ouvrir le dashboard
railway open

# Voir les variables
railway variables

# Redéployer
railway up --detach
```

## 🔧 Dépannage

### Erreur CORS
Vérifiez que `CORS_ORIGINS` dans le backend inclut l'URL de votre frontend.

### Erreur de connexion à la base
1. Vérifiez que `DATABASE_URL` est bien configuré
2. Vérifiez que les services sont liés dans Railway
3. Regardez les logs : `railway logs`

### Le frontend ne communique pas avec le backend
1. Vérifiez `VITE_API_URL` dans Vercel
2. Vérifiez que le backend est bien déployé et accessible
3. Ouvrez la console du navigateur pour voir les erreurs

## 💡 Configuration pour la Côte d'Ivoire

Pour utiliser la locale française (déjà configurée dans le code) :

**Backend** (application.properties) :
```properties
spring.jackson.locale=fr_FR
spring.jackson.time-zone=Africa/Abidjan
```

**Frontend** (déjà configuré avec date-fns) :
```javascript
import { fr } from 'date-fns/locale';
format(date, 'dd/MM/yyyy', { locale: fr })
```

## 📊 Monitoring

Railway offre :
- Métriques CPU/RAM
- Logs en temps réel
- Alertes
- Déploiements automatiques sur push Git

## 💰 Coûts

- **PostgreSQL** : Gratuit pour 500MB, puis ~$5/mois
- **Backend** : $5/mois pour le plan Hobby
- **Frontend sur Vercel** : Gratuit pour les projets personnels

## 🎉 Vous êtes prêt !

Votre application SuperMarket Manager est maintenant déployée sur Railway avec PostgreSQL et accessible depuis n'importe où !

URL Backend : `https://votre-backend.railway.app`
URL Frontend : `https://votre-app.vercel.app`

