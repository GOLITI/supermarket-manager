#!/bin/bash

echo "=========================================="
echo "🚀 Démarrage du projet Supermarket Manager"
echo "=========================================="
echo ""

# Couleurs pour les messages
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Fonction pour arrêter les processus en cas d'erreur
cleanup() {
    echo ""
    echo "${RED}Arrêt des serveurs...${NC}"
    kill $BACKEND_PID $FRONTEND_PID 2>/dev/null
    exit 1
}

trap cleanup SIGINT SIGTERM

# Démarrage du backend
echo "${BLUE}📦 Démarrage du backend Spring Boot...${NC}"
cd backend
./mvnw spring-boot:run > backend.log 2>&1 &
BACKEND_PID=$!
echo "Backend PID: $BACKEND_PID"
echo "Logs backend: backend/backend.log"
echo ""

# Attendre que le backend démarre (maximum 60 secondes)
echo "⏳ Attente du démarrage du backend..."
for i in {1..60}; do
    if grep -q "Started SupermarketManagerBackendApplication" backend.log 2>/dev/null; then
        echo "${GREEN}✅ Backend démarré avec succès sur http://localhost:8080${NC}"
        break
    fi
    sleep 1
    if [ $i -eq 60 ]; then
        echo "${RED}❌ Timeout: Le backend n'a pas démarré en 60 secondes${NC}"
        cat backend.log | tail -20
        cleanup
    fi
done
echo ""

# Démarrage du frontend
echo "${BLUE}🎨 Démarrage du frontend React...${NC}"
cd ../frontend
npm run dev > frontend.log 2>&1 &
FRONTEND_PID=$!
echo "Frontend PID: $FRONTEND_PID"
echo "Logs frontend: frontend/frontend.log"
echo ""

# Attendre que le frontend démarre (maximum 30 secondes)
echo "⏳ Attente du démarrage du frontend..."
for i in {1..30}; do
    if grep -q "Local:.*http://localhost:5173" frontend.log 2>/dev/null; then
        echo "${GREEN}✅ Frontend démarré avec succès sur http://localhost:5173${NC}"
        break
    fi
    sleep 1
    if [ $i -eq 30 ]; then
        echo "${RED}❌ Timeout: Le frontend n'a pas démarré en 30 secondes${NC}"
        cat frontend.log | tail -20
        cleanup
    fi
done
echo ""

# Afficher le résumé
echo "=========================================="
echo "${GREEN}✅ Tous les serveurs sont démarrés !${NC}"
echo "=========================================="
echo ""
echo "📍 URLs:"
echo "   - Frontend: http://localhost:5173"
echo "   - Backend:  http://localhost:8080"
echo ""
echo "📋 PIDs:"
echo "   - Backend:  $BACKEND_PID"
echo "   - Frontend: $FRONTEND_PID"
echo ""
echo "📝 Logs:"
echo "   - Backend:  backend/backend.log"
echo "   - Frontend: frontend/frontend.log"
echo ""
echo "Pour arrêter les serveurs, appuyez sur Ctrl+C"
echo ""

# Garder le script en cours d'exécution
wait $BACKEND_PID $FRONTEND_PID

