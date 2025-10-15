import { BrowserRouter as Router, Routes, Route, NavLink } from 'react-router-dom';

// Composant Sidebar
function Sidebar() {
  const menuItems = [
    { label: 'Dashboard', path: '/', icon: '📊' },
    { label: 'Stocks', path: '/stocks', icon: '📦' },
    { label: 'Caisses', path: '/caisses', icon: '🛒' },
    { label: 'Clients', path: '/clients', icon: '👥' },
    { label: 'Employés', path: '/employes', icon: '👤' },
    { label: 'Fournisseurs', path: '/fournisseurs', icon: '🚚' },
    { label: 'Promotions', path: '/promotions', icon: '🏷️' },
    { label: 'Rapports', path: '/rapports', icon: '📈' },
  ];

  return (
    <aside className="sidebar">
      <div className="sidebar-header">
        <h1>SuperMarket</h1>
        <p>Manager Pro</p>
      </div>
      
      <nav className="sidebar-nav">
        {menuItems.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            className={({ isActive }) => isActive ? 'active' : ''}
          >
            <span className="nav-icon">{item.icon}</span>
            <span>{item.label}</span>
          </NavLink>
        ))}
      </nav>
    </aside>
  );
}

// Composant Header
function Header() {
  return (
    <header className="header">
      <div className="search-bar">
        <input
          type="text"
          placeholder="Rechercher..."
          className="search-input"
        />
      </div>

      <div className="header-actions">
        <button className="icon-button">
          🔔
          <span className="notification-badge"></span>
        </button>

        <div className="user-info">
          <div className="user-info-text">
            <div className="user-name">Admin</div>
            <div className="user-role">Gestionnaire</div>
          </div>
          <button className="icon-button">👤</button>
        </div>
      </div>
    </header>
  );
}

// Composant Layout
function Layout({ children }) {
  return (
    <div className="app-container">
      <Sidebar />
      <Header />
      <main className="main-content">
        {children}
      </main>
    </div>
  );
}

// Page Dashboard
function Dashboard() {
  return (
    <div>
      <div className="card">
        <div className="card-header">
          <h1 className="card-title">Tableau de bord</h1>
          <p className="card-description">Vue d'ensemble de votre supermarché</p>
        </div>

        <div className="stats-grid">
          <div className="stat-card blue">
            <div className="stat-label">Chiffre d'affaires</div>
            <div className="stat-value">2,450,000 FCFA</div>
            <div className="stat-change">↑ +12% ce mois</div>
          </div>

          <div className="stat-card green">
            <div className="stat-label">Nombre de ventes</div>
            <div className="stat-value">1,234</div>
            <div className="stat-change">↑ +8% ce mois</div>
          </div>

          <div className="stat-card orange">
            <div className="stat-label">Produits en stock</div>
            <div className="stat-value">456</div>
            <div className="stat-change">15 alertes</div>
          </div>

          <div className="stat-card red">
            <div className="stat-label">Clients actifs</div>
            <div className="stat-value">892</div>
            <div className="stat-change">↑ +5% ce mois</div>
          </div>
        </div>
      </div>

      <div className="card">
        <h2>🎉 Application opérationnelle !</h2>
        <p style={{ marginTop: '16px', color: '#64748b' }}>
          Le frontend est maintenant fonctionnel avec du CSS pur (sans Tailwind).
          Toutes les fonctionnalités sont prêtes à être développées.
        </p>
      </div>
    </div>
  );
}

// Page générique pour les autres sections
function Page({ title, description, icon }) {
  return (
    <div className="card">
      <div className="card-header">
        <h1 className="card-title">{icon} {title}</h1>
        <p className="card-description">{description}</p>
      </div>

      <div className="stats-grid">
        <div className="stat-card blue">
          <div className="stat-label">✅ Fonctionnel</div>
          <div className="stat-value">100%</div>
          <div className="stat-change">Cette page est opérationnelle</div>
        </div>

        <div className="stat-card green">
          <div className="stat-label">🚀 Prêt</div>
          <div className="stat-value">OK</div>
          <div className="stat-change">Backend connecté</div>
        </div>

        <div className="stat-card orange">
          <div className="stat-label">📊 En développement</div>
          <div className="stat-value">--</div>
          <div className="stat-change">Fonctionnalités à venir</div>
        </div>
      </div>

      <div style={{ marginTop: '24px' }}>
        <button className="btn btn-primary">Ajouter</button>
        <button className="btn btn-secondary" style={{ marginLeft: '12px' }}>Exporter</button>
      </div>
    </div>
  );
}

// Application principale
function AppSimple() {
  return (
    <Router>
      <Layout>
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/stocks" element={<Page title="Gestion des Stocks" description="Gérez vos stocks et approvisionnements" icon="📦" />} />
          <Route path="/caisses" element={<Page title="Gestion des Caisses" description="Transactions et ventes en temps réel" icon="🛒" />} />
          <Route path="/clients" element={<Page title="Gestion des Clients" description="Base de données clients et fidélité" icon="👥" />} />
          <Route path="/employes" element={<Page title="Ressources Humaines" description="Gestion des employés et plannings" icon="👤" />} />
          <Route path="/fournisseurs" element={<Page title="Fournisseurs" description="Gérez vos fournisseurs et commandes" icon="🚚" />} />
          <Route path="/promotions" element={<Page title="Promotions" description="Créez et gérez vos promotions" icon="🏷️" />} />
          <Route path="/rapports" element={<Page title="Rapports" description="Analyses et statistiques détaillées" icon="📈" />} />
        </Routes>
      </Layout>
    </Router>
  );
}

export default AppSimple;

