import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './styles.css'
import AppSimple from './AppSimple.jsx'

console.log('🚀 Frontend démarré !');

const rootElement = document.getElementById('root');

if (rootElement) {
  createRoot(rootElement).render(
    <StrictMode>
      <AppSimple />
    </StrictMode>
  )
  console.log('✅ React rendu avec succès');
} else {
  console.error('❌ Élément root non trouvé');
}

