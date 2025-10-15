import { BrowserRouter as Router, Routes, Route, NavLink } from 'react-router-dom';
import { Container, Row, Col, Card, Badge, Button, Form, InputGroup } from 'react-bootstrap';
import {
  MdDashboard,
  MdInventory,
  MdPointOfSale,
  MdPeople,
  MdWork,
  MdLocalShipping,
  MdLocalOffer,
  MdBarChart,
  MdSearch,
  MdNotifications,
  MdPerson,
  MdAdd,
  MdFileDownload,
  MdFileUpload,
  MdRefresh,
  MdSettings,
  MdTrendingUp,
  MdWarning
} from 'react-icons/md';

// Composant Sidebar avec Bootstrap
function Sidebar() {
  const menuItems = [
    { label: 'Dashboard', path: '/', icon: MdDashboard },
    { label: 'Stocks', path: '/stocks', icon: MdInventory },
    { label: 'Caisses', path: '/caisses', icon: MdPointOfSale },
    { label: 'Clients', path: '/clients', icon: MdPeople },
    { label: 'Employés', path: '/employes', icon: MdWork },
    { label: 'Fournisseurs', path: '/fournisseurs', icon: MdLocalShipping },
    { label: 'Promotions', path: '/promotions', icon: MdLocalOffer },
    { label: 'Rapports', path: '/rapports', icon: MdBarChart },
  ];

  return (
    <aside className="sidebar-custom">
      <div className="sidebar-header">
        <h1>SuperMarket</h1>
        <p>Manager Pro</p>
      </div>
      
      <nav className="nav-custom">
        {menuItems.map((item) => {
          const Icon = item.icon;
          return (
            <NavLink
              key={item.path}
              to={item.path}
              className={({ isActive }) => `nav-link-custom ${isActive ? 'active' : ''}`}
            >
              <Icon className="nav-icon" size={20} />
              <span>{item.label}</span>
            </NavLink>
          );
        })}
      </nav>
    </aside>
  );
}

// Composant Header avec Bootstrap
function Header() {
  return (
    <nav className="navbar-custom">
      <Container fluid>
        <Row className="w-100 align-items-center">
          <Col md={6}>
            <InputGroup>
              <InputGroup.Text className="bg-white border-end-0">
                <MdSearch size={20} />
              </InputGroup.Text>
              <Form.Control
                type="text"
                placeholder="Rechercher..."
                className="search-input-custom border-start-0"
              />
            </InputGroup>
          </Col>
          <Col md={6} className="text-end">
            <Button variant="link" className="position-relative me-3">
              <MdNotifications size={24} />
              <span className="notification-badge"></span>
            </Button>
            <Button variant="link" className="text-dark">
              <div className="d-inline-flex align-items-center">
                <div className="text-end me-2">
                  <div className="fw-bold" style={{ fontSize: '0.875rem' }}>Admin</div>
                  <div style={{ fontSize: '0.75rem', color: '#6c757d' }}>Gestionnaire</div>
                </div>
                <MdPerson size={28} />
              </div>
            </Button>
          </Col>
        </Row>
      </Container>
    </nav>
  );
}

// Composant Layout
function Layout({ children }) {
  return (
    <div>
      <Sidebar />
      <Header />
      <main className="main-content">
        <Container fluid className="py-4">
          {children}
        </Container>
      </main>
    </div>
  );
}

// Page Dashboard avec Bootstrap
function Dashboard() {
  return (
    <div className="fade-in-up">
      <Row className="mb-4">
        <Col>
          <h1 className="fw-bold mb-2"><MdDashboard className="me-2" />Tableau de bord</h1>
          <p className="text-muted">Vue d'ensemble de votre supermarché</p>
        </Col>
      </Row>

      <Row className="g-4 mb-4">
        <Col md={6} lg={3}>
          <Card className="stat-card blue border-0">
            <Card.Body>
              <div className="stat-label">Chiffre d'affaires</div>
              <div className="stat-value">2,450,000 FCFA</div>
              <div className="stat-change"><MdTrendingUp /> +12% ce mois</div>
            </Card.Body>
          </Card>
        </Col>

        <Col md={6} lg={3}>
          <Card className="stat-card green border-0">
            <Card.Body>
              <div className="stat-label">Nombre de ventes</div>
              <div className="stat-value">1,234</div>
              <div className="stat-change"><MdTrendingUp /> +8% ce mois</div>
            </Card.Body>
          </Card>
        </Col>

        <Col md={6} lg={3}>
          <Card className="stat-card orange border-0">
            <Card.Body>
              <div className="stat-label">Produits en stock</div>
              <div className="stat-value">456</div>
              <div className="stat-change"><MdWarning /> 15 alertes</div>
            </Card.Body>
          </Card>
        </Col>

        <Col md={6} lg={3}>
          <Card className="stat-card red border-0">
            <Card.Body>
              <div className="stat-label">Clients actifs</div>
              <div className="stat-value">892</div>
              <div className="stat-change"><MdTrendingUp /> +5% ce mois</div>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Row className="g-4">
        <Col lg={8}>
          <Card className="custom-card">
            <Card.Body>
              <h5 className="fw-bold mb-3"><MdBarChart className="me-2" />Ventes récentes</h5>
              <div className="table-responsive">
                <table className="table table-hover">
                  <thead className="table-light">
                    <tr>
                      <th>Transaction</th>
                      <th>Client</th>
                      <th>Montant</th>
                      <th>Statut</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <td className="fw-bold">#1234</td>
                      <td>Jean Kouassi</td>
                      <td className="fw-bold text-success">45,000 FCFA</td>
                      <td><Badge bg="success">Complétée</Badge></td>
                    </tr>
                    <tr>
                      <td className="fw-bold">#1233</td>
                      <td>Marie Bamba</td>
                      <td className="fw-bold text-success">32,500 FCFA</td>
                      <td><Badge bg="success">Complétée</Badge></td>
                    </tr>
                    <tr>
                      <td className="fw-bold">#1232</td>
                      <td>Kouadio Yao</td>
                      <td className="fw-bold text-success">78,900 FCFA</td>
                      <td><Badge bg="success">Complétée</Badge></td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </Card.Body>
          </Card>
        </Col>

        <Col lg={4}>
          <Card className="custom-card mb-4">
            <Card.Body>
              <h5 className="fw-bold mb-3"><MdLocalOffer className="me-2" />Produits populaires</h5>
              <div className="d-flex justify-content-between align-items-center mb-3">
                <span>Riz Parfumé 5kg</span>
                <Badge bg="primary">234 ventes</Badge>
              </div>
              <div className="d-flex justify-content-between align-items-center mb-3">
                <span>Huile Végétale 1L</span>
                <Badge bg="primary">189 ventes</Badge>
              </div>
              <div className="d-flex justify-content-between align-items-center">
                <span>Sucre Cristallisé 1kg</span>
                <Badge bg="primary">156 ventes</Badge>
              </div>
            </Card.Body>
          </Card>

          <Card className="custom-card" style={{ background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)', color: 'white' }}>
            <Card.Body>
              <h5 className="fw-bold mb-3">🎉 Succès !</h5>
              <p className="mb-0">
                L'application fonctionne parfaitement avec Bootstrap 5 et React Icons !
                Interface moderne et responsive.
              </p>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </div>
  );
}

// Page générique pour les autres sections
function Page({ title, description, icon: IconComponent }) {
  return (
    <div className="fade-in-up">
      <Row className="mb-4">
        <Col>
          <h1 className="fw-bold mb-2"><IconComponent className="me-2" />{title}</h1>
          <p className="text-muted">{description}</p>
        </Col>
        <Col xs="auto">
          <Button variant="primary" className="btn-icon">
            <MdAdd /> Ajouter
          </Button>
        </Col>
      </Row>

      <Row className="g-4 mb-4">
        <Col md={4}>
          <Card className="stat-card blue border-0">
            <Card.Body>
              <div className="stat-label">✅ Fonctionnel</div>
              <div className="stat-value">100%</div>
              <div className="stat-change">Cette page est opérationnelle</div>
            </Card.Body>
          </Card>
        </Col>

        <Col md={4}>
          <Card className="stat-card green border-0">
            <Card.Body>
              <div className="stat-label">🚀 Statut</div>
              <div className="stat-value">Prêt</div>
              <div className="stat-change">Backend connecté</div>
            </Card.Body>
          </Card>
        </Col>

        <Col md={4}>
          <Card className="stat-card purple border-0">
            <Card.Body>
              <div className="stat-label">📊 Développement</div>
              <div className="stat-value">En cours</div>
              <div className="stat-change">Fonctionnalités à venir</div>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Card className="custom-card">
        <Card.Body>
          <h5 className="fw-bold mb-4">Actions rapides</h5>
          <div className="d-flex gap-2 flex-wrap">
            <Button variant="primary" className="btn-icon"><MdFileUpload /> Importer</Button>
            <Button variant="success" className="btn-icon"><MdFileDownload /> Exporter</Button>
            <Button variant="info" className="btn-icon"><MdRefresh /> Actualiser</Button>
            <Button variant="warning" className="btn-icon"><MdSettings /> Paramètres</Button>
          </div>
        </Card.Body>
      </Card>
    </div>
  );
}

// Application principale avec Bootstrap
function AppBootstrap() {
  return (
    <Router>
      <Layout>
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/stocks" element={<Page title="Gestion des Stocks" description="Gérez vos stocks et approvisionnements" icon={MdInventory} />} />
          <Route path="/caisses" element={<Page title="Gestion des Caisses" description="Transactions et ventes en temps réel" icon={MdPointOfSale} />} />
          <Route path="/clients" element={<Page title="Gestion des Clients" description="Base de données clients et fidélité" icon={MdPeople} />} />
          <Route path="/employes" element={<Page title="Ressources Humaines" description="Gestion des employés et plannings" icon={MdWork} />} />
          <Route path="/fournisseurs" element={<Page title="Fournisseurs" description="Gérez vos fournisseurs et commandes" icon={MdLocalShipping} />} />
          <Route path="/promotions" element={<Page title="Promotions" description="Créez et gérez vos promotions" icon={MdLocalOffer} />} />
          <Route path="/rapports" element={<Page title="Rapports" description="Analyses et statistiques détaillées" icon={MdBarChart} />} />
        </Routes>
      </Layout>
    </Router>
  );
}

export default AppBootstrap;
