import React, { useState } from 'react';
import { Bell, User, Search, Menu, Settings, LogOut } from 'lucide-react';
import { useAuth } from '../../context/AuthContext.jsx';
import { Dropdown } from 'react-bootstrap';

const Header = ({ onToggleSidebar }) => {
    const [searchQuery, setSearchQuery] = useState('');
    const { user, logout } = useAuth();

    const handleLogout = () => {
        logout();
    };

    const getUserDisplayName = () => {
        if (!user) return '';
        return `${user.firstName} ${user.lastName}`;
    };

    const getUserRoleDisplay = () => {
        if (!user || !user.roles) return '';

        const roleMap = {
            'ROLE_ADMIN': 'Administrateur',
            'ROLE_MANAGER': 'Gestionnaire',
            'ROLE_CASHIER': 'Caissier',
            'ROLE_HR_MANAGER': 'Responsable RH',
            'ROLE_STOCK_MANAGER': 'Responsable Stock',
            'ROLE_CLIENT': 'Client'
        };

        const mainRole = user.roles[0];
        return roleMap[mainRole] || 'Utilisateur';
    };

    return (
        <>
            <nav className="navbar navbar-expand-lg navbar-light bg-white shadow-sm fixed-top border-bottom">
                <div className="container-fluid px-3 px-md-4" style={{ minHeight: '60px' }}>
                    {/* Section gauche - Hamburger */}
                    <div className="d-flex align-items-center">
                        <button
                            className="btn btn-link d-lg-none p-2 me-2 text-dark border-0"
                            type="button"
                            onClick={onToggleSidebar}
                            aria-label="Toggle sidebar"
                        >
                            <Menu size={20} />
                        </button>
                    </div>

                    {/* Barre de recherche centrale - Desktop */}
                    <div className="d-none d-md-flex align-items-center justify-content-center flex-grow-1 mx-4">
                        <div className="input-group" style={{ maxWidth: '500px' }}>
                            <span className="input-group-text bg-light border-end-0 py-2">
                                <Search size={16} className="text-muted" />
                            </span>
                            <input
                                type="text"
                                className="form-control border-start-0 py-2"
                                placeholder="Rechercher produits, clients, commandes..."
                                value={searchQuery}
                                onChange={(e) => setSearchQuery(e.target.value)}
                                aria-label="Rechercher"
                            />
                        </div>
                    </div>

                    {/* Section droite */}
                    <div className="d-flex align-items-center justify-content-end">
                        {/* Notifications */}
                        <Dropdown className="me-2">
                            <Dropdown.Toggle
                                variant="light"
                                className="position-relative p-2 border-0"
                                id="dropdown-notifications"
                            >
                                <Bell size={18} className="text-muted" />
                                <span className="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger"
                                      style={{ fontSize: '0.6rem', padding: '2px 4px', minWidth: '16px' }}>
                                    3
                                </span>
                            </Dropdown.Toggle>

                            <Dropdown.Menu className="shadow border-0" style={{ minWidth: '320px' }}>
                                {/* Contenu des notifications... */}
                            </Dropdown.Menu>
                        </Dropdown>

                        {/* Séparateur */}
                        <div className="vr d-none d-sm-block mx-1 me-2"></div>

                        {/* Profil utilisateur */}
                        <Dropdown>
                            <Dropdown.Toggle
                                variant="light"
                                className="d-flex align-items-center gap-2 border-0 p-1"
                                id="dropdown-profile"
                            >
                                <div className="d-none d-md-block text-start">
                                    <div className="fw-semibold small text-dark">
                                        {getUserDisplayName()}
                                    </div>
                                    <div className="text-muted" style={{ fontSize: '0.7rem' }}>
                                        {getUserRoleDisplay()}
                                    </div>
                                </div>
                                <div className="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center ms-2"
                                     style={{ width: '36px', height: '36px' }}>
                                    <User size={18} />
                                </div>
                            </Dropdown.Toggle>

                            <Dropdown.Menu className="shadow border-0" align="end">
                                <div className="dropdown-header">
                                    <div className="fw-semibold">{getUserDisplayName()}</div>
                                    <small className="text-muted">{user?.email}</small>
                                </div>
                                <Dropdown.Divider />
                                <Dropdown.Item className="d-flex align-items-center">
                                    <User size={16} className="me-2 text-muted" />
                                    Mon profil
                                </Dropdown.Item>
                                <Dropdown.Item className="d-flex align-items-center">
                                    <Settings size={16} className="me-2 text-muted" />
                                    Paramètres
                                </Dropdown.Item>
                                <Dropdown.Divider />
                                <Dropdown.Item
                                    className="d-flex align-items-center text-danger"
                                    onClick={handleLogout}
                                >
                                    <LogOut size={16} className="me-2" />
                                    Déconnexion
                                </Dropdown.Item>
                            </Dropdown.Menu>
                        </Dropdown>
                    </div>
                </div>
            </nav>

            {/* Espace pour le header fixe */}
            <div style={{ height: '60px' }}></div>
        </>
    );
};

export default Header;