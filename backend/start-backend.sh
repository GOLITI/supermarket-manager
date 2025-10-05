#!/bin/bash

# Script de démarrage du backend - Système de Gestion des Stocks

echo "🚀 Démarrage du système de gestion des stocks..."

# Couleurs pour les messages
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Vérifier si PostgreSQL est installé
if ! command -v psql &> /dev/null; then
    echo -e "${RED}❌ PostgreSQL n'est pas installé${NC}"
    echo "Installation requise: sudo apt-get install postgresql"
    exit 1
fi

# Vérifier si PostgreSQL est en cours d'exécution
if ! sudo systemctl is-active --quiet postgresql; then
    echo -e "${YELLOW}⚠️  PostgreSQL n'est pas démarré. Démarrage...${NC}"
    sudo systemctl start postgresql
fi

# Vérifier si la base de données existe
echo "📊 Vérification de la base de données..."
DB_EXISTS=$(sudo -u postgres psql -tAc "SELECT 1 FROM pg_database WHERE datname='supermarket_db'")

if [ "$DB_EXISTS" != "1" ]; then
    echo -e "${YELLOW}⚠️  Base de données non trouvée. Création...${NC}"
    sudo -u postgres psql -c "CREATE DATABASE supermarket_db;"
    echo -e "${GREEN}✅ Base de données créée${NC}"
else
    echo -e "${GREEN}✅ Base de données existante${NC}"
fi

# Nettoyer et compiler
echo "🔨 Compilation du projet..."
./mvnw clean package -DskipTests

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Compilation réussie${NC}"
else
    echo -e "${RED}❌ Erreur de compilation${NC}"
    exit 1
fi

# Démarrer l'application
echo "🚀 Démarrage de l'application..."
echo "📍 L'application sera accessible sur http://localhost:8080"
echo "📚 Documentation API: http://localhost:8080/swagger-ui.html"
echo ""
echo "Pour tester les alertes de stock:"
echo "  curl http://localhost:8080/api/stocks/alertes"
echo ""
echo "Pour générer des commandes automatiques:"
echo "  curl -X POST http://localhost:8080/api/commandes/generer-automatiques"
echo ""

./mvnw spring-boot:run

