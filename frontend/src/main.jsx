import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import 'bootstrap/dist/css/bootstrap.min.css'
import './custom-bootstrap.css'
import AppBootstrap from './AppBootstrap.jsx'

console.log('🚀 Frontend avec Bootstrap démarré !');

const rootElement = document.getElementById('root');

if (rootElement) {
  createRoot(rootElement).render(
    <StrictMode>
      <AppBootstrap />
    </StrictMode>
  )
  console.log('✅ Application Bootstrap rendue avec succès');
} else {
  console.error('❌ Élément root non trouvé');
}

