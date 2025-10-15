#!/bin/bash

echo "=========================================="
echo "  Démarrage Supermarket Manager"
echo "=========================================="
echo ""

# Démarrer le backend en arrière-plan
echo "🚀 Démarrage du backend Spring Boot..."
cd /home/goliti/PROJETS/AGLPROJET/supermarket-manager/backend
./mvnw spring-boot:run > backend.log 2>&1 &
BACKEND_PID=$!
echo "✅ Backend démarré (PID: $BACKEND_PID)"
echo "   Logs disponibles dans: backend/backend.log"
echo "   URL: http://localhost:8080"
echo ""

# Attendre que le backend soit prêt
echo "⏳ Attente du démarrage du backend (30 secondes)..."
sleep 30

# Démarrer le frontend en arrière-plan
echo "🚀 Démarrage du frontend React..."
cd /home/goliti/PROJETS/AGLPROJET/supermarket-manager/frontend
npm run dev > frontend.log 2>&1 &
FRONTEND_PID=$!
echo "✅ Frontend démarré (PID: $FRONTEND_PID)"
echo "   Logs disponibles dans: frontend/frontend.log"
echo "   URL: http://localhost:5173"
echo ""

echo "=========================================="
echo "✨ Application prête !"
echo "=========================================="
echo ""
echo "📊 Backend:  http://localhost:8080"
echo "🖥️  Frontend: http://localhost:5173"
echo ""
echo "Pour arrêter les serveurs:"
echo "  kill $BACKEND_PID $FRONTEND_PID"
echo ""
echo "Ou utilisez: pkill -f 'spring-boot:run' && pkill -f 'vite'"
echo ""

