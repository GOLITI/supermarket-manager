import { useQuery } from '@tanstack/react-query';
import { dashboardService } from '../services';
import StatCard from '../components/common/StatCard';
import Card from '../components/common/Card';
import Loading from '../components/common/Loading';
import {
    DollarSign,
    ShoppingCart,
    Package,
    Users,
    TrendingUp,
    AlertTriangle,
    Plus,
    BarChart3,
    Download,
    Settings
} from 'lucide-react';
import './dashboard.css'; // Import du CSS

const Dashboard = () => {
    const { data: stats, isLoading } = useQuery({
        queryKey: ['dashboard-stats'],
        queryFn: async () => {
            return {
                data: {
                    chiffreAffaires: 1250000,
                    nombreVentes: 156,
                    nombreProduits: 2458,
                    nombreClients: 89,
                    produitsStockFaible: 3,
                    produitsPeremptionProche: 7
                }
            };
        }
    });

    if (isLoading) return (
        <div className="dashboard-loading">
            <Loading size="lg" />
        </div>
    );

    return (
        <div className="dashboard-container">
            {/* En-tête stylisé */}
            <div className="dashboard-header">
                <div className="container-fluid">
                    <h1 className="dashboard-title">Tableau de bord</h1>
                    <p className="dashboard-subtitle">Vue d'ensemble de votre supermarché</p>
                </div>
            </div>

            <div className="container-fluid">
                {/* Statistiques principales avec nouveau design */}
                <div className="stats-grid">
                    <div className="stat-card-enhanced blue dashboard-item">
                        <div className="stat-header">
                            <div>
                                <div className="stat-title">Chiffre d'affaires</div>
                                <div className="stat-value-large">
                                    {stats?.data?.chiffreAffaires?.toLocaleString('fr-FR') || 0} FCFA
                                </div>
                                <div className="stat-trend up">
                                    <TrendingUp size={14} />
                                    +12.5%
                                </div>
                            </div>
                            <div className="stat-icon blue">
                                <DollarSign size={24} />
                            </div>
                        </div>
                    </div>

                    <div className="stat-card-enhanced green dashboard-item">
                        <div className="stat-header">
                            <div>
                                <div className="stat-title">Nombre de ventes</div>
                                <div className="stat-value-large">{stats?.data?.nombreVentes || 0}</div>
                                <div className="stat-trend up">
                                    <TrendingUp size={14} />
                                    +8.2%
                                </div>
                            </div>
                            <div className="stat-icon green">
                                <ShoppingCart size={24} />
                            </div>
                        </div>
                    </div>

                    <div className="stat-card-enhanced orange dashboard-item">
                        <div className="stat-header">
                            <div>
                                <div className="stat-title">Produits en stock</div>
                                <div className="stat-value-large">{stats?.data?.nombreProduits || 0}</div>
                            </div>
                            <div className="stat-icon orange">
                                <Package size={24} />
                            </div>
                        </div>
                    </div>

                    <div className="stat-card-enhanced purple dashboard-item">
                        <div className="stat-header">
                            <div>
                                <div className="stat-title">Clients actifs</div>
                                <div className="stat-value-large">{stats?.data?.nombreClients || 0}</div>
                                <div className="stat-trend up">
                                    <TrendingUp size={14} />
                                    +5.1%
                                </div>
                            </div>
                            <div className="stat-icon purple">
                                <Users size={24} />
                            </div>
                        </div>
                    </div>
                </div>

                {/* Alertes stylisées */}
                <div className="alerts-section dashboard-item">
                    <div className="alerts-header">
                        <div>
                            <h2 className="alerts-title">Alertes</h2>
                            <p className="alerts-subtitle">Actions requises</p>
                        </div>
                    </div>

                    <div className="d-flex flex-column gap-3">
                        <div className="alert-card warning">
                            <AlertTriangle className="alert-icon" size={24} />
                            <div className="alert-content">
                                <p className="alert-title">Stock faible</p>
                                <p className="alert-description">
                                    {stats?.data?.produitsStockFaible || 0} produits nécessitent un réapprovisionnement urgent
                                </p>
                            </div>
                        </div>

                        <div className="alert-card danger">
                            <AlertTriangle className="alert-icon" size={24} />
                            <div className="alert-content">
                                <p className="alert-title">Produits périssables</p>
                                <p className="alert-description">
                                    {stats?.data?.produitsPeremptionProche || 0} produits expirent bientôt
                                </p>
                            </div>
                        </div>

                        <div className="alert-card success">
                            <TrendingUp className="alert-icon" size={24} />
                            <div className="alert-content">
                                <p className="alert-title">Performance</p>
                                <p className="alert-description">
                                    Objectif du mois atteint à 78% - Continuez vos efforts !
                                </p>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Actions rapides */}
                <div className="quick-actions dashboard-item">
                    <button className="action-btn">
                        <Plus className="action-icon" />
                        <span className="action-label">Nouvelle Vente</span>
                    </button>
                    <button className="action-btn">
                        <Package className="action-icon" />
                        <span className="action-label">Gérer Stock</span>
                    </button>
                    <button className="action-btn">
                        <BarChart3 className="action-icon" />
                        <span className="action-label">Rapports</span>
                    </button>
                    <button className="action-btn">
                        <Download className="action-icon" />
                        <span className="action-label">Exporter</span>
                    </button>
                </div>
            </div>
        </div>
    );
};

export default Dashboard;