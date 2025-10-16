import React, { useState, useEffect } from 'react';
import { Outlet } from 'react-router-dom';
import Sidebar from './Sidebar';
import Header from './Header';

const Layout = () => {
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);
    const [isDesktop, setIsDesktop] = useState(window.innerWidth >= 992);

    useEffect(() => {
        const handleResize = () => {
            const desktop = window.innerWidth >= 992;
            setIsDesktop(desktop);

            // Sur desktop, la sidebar est toujours ouverte
            // Sur mobile, elle est fermée par défaut
            if (desktop) {
                setIsSidebarOpen(true);
            } else {
                setIsSidebarOpen(false);
            }
        };

        // Initialiser l'état au chargement
        handleResize();

        window.addEventListener('resize', handleResize);

        return () => window.removeEventListener('resize', handleResize);
    }, []);

    const toggleSidebar = () => {
        setIsSidebarOpen(prev => !prev);
    };

    const closeSidebar = () => {
        if (!isDesktop) {
            setIsSidebarOpen(false);
        }
    };

    // Overlay pour mobile quand la sidebar est ouverte
    const overlayStyle = {
        display: !isDesktop && isSidebarOpen ? 'block' : 'none',
        position: 'fixed',
        top: 0,
        left: 0,
        right: 0,
        bottom: 0,
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
        zIndex: 1040,
        transition: 'opacity 0.3s ease-in-out'
    };

    return (
        <div className="min-vh-100 bg-light position-relative">
            {/* Overlay pour mobile */}
            <div
                className="offcanvas-backdrop fade"
                style={overlayStyle}
                onClick={closeSidebar}
            />

            <Sidebar isOpen={isSidebarOpen} onClose={closeSidebar} />
            <Header onToggleSidebar={toggleSidebar} />

            {/* Contenu principal avec gestion responsive */}
            <main
                className="pt-2"
                style={{
                    marginLeft: isDesktop && isSidebarOpen ? '280px' : '0',
                    transition: 'margin-left 0.3s ease-in-out',
                    minHeight: 'calc(100vh - 60px)',
                    paddingTop: '0.5rem'
                }}
            >
                <div className="container-fluid px-3 px-md-4 py-3">
                    <Outlet />
                </div>
            </main>
        </div>
    );
};

export default Layout;