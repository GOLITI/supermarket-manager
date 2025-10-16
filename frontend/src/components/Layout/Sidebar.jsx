import React from 'react';
import { NavLink } from 'react-router-dom';
import {
    LayoutDashboard,
    Package,
    ShoppingCart,
    Users,
    UserCircle,
    Truck,
    Tag,
    BarChart3,
    X,
    Home
} from 'lucide-react';

const Sidebar = ({ isOpen, onClose }) => {
    const menuItems = [
        { icon: LayoutDashboard, label: 'Tableau de bord', path: '/dashboard', badge: null },
        { icon: Package, label: 'Gestion des stocks', path: '/stocks', badge: '3' },
        { icon: ShoppingCart, label: 'Caisses & Ventes', path: '/caisses', badge: null },
        { icon: Users, label: 'Clients', path: '/clients', badge: '12' },
        { icon: UserCircle, label: 'Employés', path: '/employes', badge: null },
        { icon: Truck, label: 'Fournisseurs', path: '/fournisseurs', badge: '2' },
        { icon: Tag, label: 'Promotions', path: '/promotions', badge: '5' },
        { icon: BarChart3, label: 'Rapports & Analytics', path: '/rapports', badge: null },
    ];

    return (
        <>
            {/* Overlay pour mobile */}
            {isOpen && (
                <div
                    className="position-fixed top-0 start-0 w-100 h-100 bg-dark bg-opacity-50 d-lg-none"
                    style={{ zIndex: 1040 }}
                    onClick={onClose}
                />
            )}

            {/* Sidebar */}
            <aside
                className={`bg-white shadow-lg position-fixed top-0 start-0 h-100 d-flex flex-column ${
                    isOpen ? 'translate-x-0' : 'translate-x-100'
                }`}
                style={{
                    width: '280px',
                    zIndex: 1045,
                    transition: 'transform 0.3s ease-in-out',
                    transform: isOpen ? 'translateX(0)' : 'translateX(-100%)',
                }}
            >
                {/* En-tête avec logo */}
                <div className="p-4 border-bottom bg-primary text-white">
                    <div className="d-flex justify-content-between align-items-center">
                        <div className="d-flex align-items-center">
                            <div className="bg-white text-primary rounded-circle d-flex align-items-center justify-content-center me-3"
                                 style={{ width: '40px', height: '40px' }}>
                                <Home size={20} />
                            </div>
                            <div>
                                <h1 className="h5 mb-0 fw-bold">SuperMarket Pro</h1>
                                <p className="small mb-0 opacity-75">Gestion Complète</p>
                            </div>
                        </div>
                        <button
                            className="btn btn-link p-0 text-white d-lg-none"
                            onClick={onClose}
                            aria-label="Fermer le menu"
                        >
                            <X size={20} />
                        </button>
                    </div>
                </div>

                {/* Navigation */}
                <nav className="flex-grow-1 py-3">
                    <div className="px-3 mb-3">
                        <small className="text-uppercase text-muted fw-semibold" style={{ fontSize: '0.7rem' }}>
                            Navigation Principale
                        </small>
                    </div>

                    {menuItems.map((item) => (
                        <NavLink
                            key={item.path}
                            to={item.path}
                            onClick={onClose}
                            className={({ isActive }) =>
                                `d-flex align-items-center px-3 py-2 mx-2 mb-1 text-decoration-none rounded ${
                                    isActive
                                        ? 'bg-primary text-white shadow-sm'
                                        : 'text-dark hover-bg-light'
                                }`
                            }
                            style={{
                                transition: 'all 0.2s ease'
                            }}
                        >
                            <item.icon className="me-3" size={18} />
                            <span className="flex-grow-1">{item.label}</span>
                            {item.badge && (
                                <span className={`badge rounded-pill ${
                                    item.path === '/stocks' ? 'bg-danger' : 'bg-primary'
                                }`} style={{ fontSize: '0.6rem' }}>
                                    {item.badge}
                                </span>
                            )}
                        </NavLink>
                    ))}
                </nav>

                {/* Footer de la sidebar */}
                <div className="p-3 border-top bg-light">
                    <div className="text-center">
                        <div className="bg-primary bg-opacity-10 text-primary rounded p-2 mb-2">
                            <small className="fw-semibold">Version 2.1.0</small>
                        </div>
                        <small className="text-muted d-block">
                            © 2024 SuperMarket Pro
                        </small>
                    </div>
                </div>
            </aside>

            {/* Styles CSS additionnels */}
            <style>{`
                .hover-bg-light:hover {
                    background-color: rgba(0, 0, 0, 0.04) !important;
                }
                
                @media (min-width: 992px) {
                    aside {
                        transform: translateX(0) !important;
                    }
                }
            `}</style>
        </>
    );
};

export default Sidebar;