import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'

// Application de test simple
function TestApp() {
  return (
    <div style={{ 
      minHeight: '100vh', 
      display: 'flex', 
      alignItems: 'center', 
      justifyContent: 'center',
      background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
      fontFamily: 'Arial, sans-serif'
    }}>
      <div style={{
        background: 'white',
        padding: '40px',
        borderRadius: '20px',
        boxShadow: '0 20px 60px rgba(0,0,0,0.3)',
        maxWidth: '600px',
        textAlign: 'center'
      }}>
        <h1 style={{ color: '#667eea', fontSize: '48px', margin: '0 0 20px 0' }}>
          🎉 Ça marche !
        </h1>
        <h2 style={{ color: '#333', fontWeight: 'normal' }}>
          Supermarket Manager
        </h2>
        <p style={{ color: '#666', fontSize: '18px', lineHeight: '1.6' }}>
          Le frontend React est maintenant opérationnel !
        </p>
        
        <div style={{ 
          marginTop: '30px', 
          padding: '20px', 
          background: '#f0f9ff',
          borderRadius: '10px'
        }}>
          <h3 style={{ color: '#0284c7', marginTop: 0 }}>✅ Tests réussis :</h3>
          <ul style={{ textAlign: 'left', color: '#333' }}>
            <li>✓ React chargé correctement</li>
            <li>✓ Vite serveur fonctionnel</li>
            <li>✓ CSS appliqué avec succès</li>
            <li>✓ Prêt pour le développement</li>
          </ul>
        </div>

        <div style={{ marginTop: '30px' }}>
          <a 
            href="/test.html" 
            style={{
              display: 'inline-block',
              background: '#0ea5e9',
              color: 'white',
              padding: '12px 24px',
              borderRadius: '8px',
              textDecoration: 'none',
              fontWeight: 'bold',
              marginRight: '10px'
            }}
          >
            🧪 Tester le Backend
          </a>
          <button 
            onClick={() => window.location.reload()}
            style={{
              background: '#6366f1',
              color: 'white',
              padding: '12px 24px',
              borderRadius: '8px',
              border: 'none',
              fontWeight: 'bold',
              cursor: 'pointer'
            }}
          >
            🔄 Actualiser
          </button>
        </div>
      </div>
    </div>
  );
}

console.log('🚀 Frontend démarré avec succès !');

const rootElement = document.getElementById('root');

if (rootElement) {
  createRoot(rootElement).render(
    <StrictMode>
      <TestApp />
    </StrictMode>
  )
  console.log('✅ React rendu avec succès');
} else {
  console.error('❌ Élément root non trouvé');
  document.body.innerHTML = '<div style="padding: 50px; text-align: center;"><h1>❌ Erreur: Élément root non trouvé</h1></div>';
}

