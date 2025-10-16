import { useState } from 'react';
import { Bell, User, Search } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';

const Header = () => {
    const { currentUser, logout } = useAuth();
    const [showLogout, setShowLogout] = useState(false);

    return (
        <header className="h-16 bg-white shadow-sm fixed top-0 right-0 left-64 z-10">
            <div className="h-full px-6 flex items-center justify-between">
                {/* Barre de recherche */}
                <div className="flex-1 max-w-xl">
                    <div className="relative">
                        <Search className="w-5 h-5 absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" />
                        <input
                            type="text"
                            placeholder="Rechercher..."
                            className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                    </div>
                </div>
                {/* Section droite */}
                <div className="flex items-center space-x-4">
                    {/* Notifications */}
                    <button className="relative p-2 text-gray-600 hover:bg-gray-100 rounded-lg">
                        <Bell className="w-6 h-6" />
                        <span className="absolute top-1 right-1 w-2 h-2 bg-red-500 rounded-full"></span>
                    </button>

                    {/* Profil utilisateur + Déconnexion */}
                    <div className="flex items-center space-x-3 pl-4 border-l border-gray-200 relative">
                        <div className="text-right">
                            <p className="text-sm font-medium text-gray-700">
                                {currentUser?.username || 'Utilisateur'}
                            </p>
                            <p className="text-xs text-gray-500">
                                {currentUser?.role || 'Rôle'}
                            </p>
                        </div>
                        <button
                            className="p-2 text-gray-600 hover:bg-gray-100 rounded-lg"
                            onClick={() => setShowLogout((v) => !v)}
                        >
                            <User className="w-6 h-6" />
                        </button>
                        {showLogout && (
                            <div className="absolute top-12 right-0 z-20">
                                <button
                                    onClick={logout}
                                    className="px-3 py-1 bg-red-600 text-white rounded hover:bg-red-700 transition text-sm shadow"
                                >
                                    Déconnexion
                                </button>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </header>
    );
};

export default Header;