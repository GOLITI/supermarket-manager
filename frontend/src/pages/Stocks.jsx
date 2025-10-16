import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import Card from '../components/common/Card';
import Button from '../components/common/Button';
import Loading from '../components/common/Loading';
import { Package, Plus, AlertCircle, Search, Filter } from 'lucide-react';
import './stocks.css';

const Stocks = () => {
    const [searchTerm, setSearchTerm] = useState('');
    const [showAlertes, setShowAlertes] = useState(false);

    // Données mockées
    const { data: stocks, isLoading } = useQuery({
        queryKey: ['stocks'],
        queryFn: async () => {
            return {
                data: [
                    {
                        id: 1,
                        quantite: 15,
                        seuilAlerte: 20,
                        produit: {
                            nom: "Lait entier",
                            codeEAN: "1234567890123",
                            categorie: "Laitier",
                            prixVente: 1200
                        },
                        entrepot: { nom: "Principal" }
                    },
                    {
                        id: 2,
                        quantite: 45,
                        seuilAlerte: 10,
                        produit: {
                            nom: "Pain de mie",
                            codeEAN: "1234567890124",
                            categorie: "Boulangerie",
                            prixVente: 800
                        },
                        entrepot: { nom: "Principal" }
                    },
                    {
                        id: 3,
                        quantite: 5,
                        seuilAlerte: 15,
                        produit: {
                            nom: "Eau minérale 1L",
                            codeEAN: "1234567890125",
                            categorie: "Boissons",
                            prixVente: 500
                        },
                        entrepot: { nom: "Principal" }
                    }
                ]
            };
        }
    });

    const { data: alertes } = useQuery({
        queryKey: ['stock-alertes'],
        queryFn: async () => {
            return {
                data: stocks?.data?.filter(stock => stock.quantite <= stock.seuilAlerte) || []
            };
        }
    });

    const filteredStocks = stocks?.data?.filter(stock =>
        stock.produit?.nom?.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const displayStocks = showAlertes ? alertes?.data : filteredStocks;

    const handleAddProduct = () => {
        alert('Fonctionnalité "Ajouter un produit" à implémenter');
        // Ici vous pouvez ouvrir un modal ou naviguer vers une autre page
    };

    const handleAddStock = (productName) => {
        const quantite = prompt(`Quantité à ajouter pour ${productName}:`);
        if (quantite && !isNaN(quantite)) {
            alert(`Ajouter ${quantite} unités à ${productName}`);
            // Ici vous pouvez appeler votre API pour mettre à jour le stock
        }
    };

    if (isLoading) return (
        <div className="stocks-loading">
            <Loading size="lg" />
        </div>
    );

    return (
        <div className="stocks-container">
            {/* En-tête stylisé */}
            <div className="stocks-header">
                <div className="container-fluid">
                    <div className="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center">
                        <div className="mb-3 mb-md-0">
                            <h1 className="stocks-title">Gestion des stocks</h1>
                            <p className="stocks-subtitle">Gérez vos stocks et inventaires</p>
                        </div>
                        <Button
                            icon={Plus}
                            className="add-product-btn"
                            onClick={handleAddProduct}
                            type="button"
                        >
                            Ajouter un produit
                        </Button>
                    </div>
                </div>
            </div>

            <div className="container-fluid">
                {/* Statistiques principales */}
                <div className="stats-grid-stocks">
                    <div className="stat-card-stock primary stocks-item">
                        <div className="stat-header-stock">
                            <div>
                                <div className="stat-title-stock">Total produits</div>
                                <div className="stat-value-stock">{stocks?.data?.length || 0}</div>
                            </div>
                            <div className="stat-icon-stock primary">
                                <Package size={24} />
                            </div>
                        </div>
                    </div>

                    <div className="stat-card-stock danger stocks-item">
                        <div className="stat-header-stock">
                            <div>
                                <div className="stat-title-stock">Alertes stock</div>
                                <div className="stat-value-stock">{alertes?.data?.length || 0}</div>
                            </div>
                            <div className="stat-icon-stock danger">
                                <AlertCircle size={24} />
                            </div>
                        </div>
                    </div>

                    <div className="stat-card-stock success stocks-item">
                        <div className="stat-header-stock">
                            <div>
                                <div className="stat-title-stock">Valeur totale</div>
                                <div className="stat-value-stock">
                                    {(stocks?.data?.reduce((acc, s) => acc + (s.quantite * (s.produit?.prixVente || 0)), 0) || 0).toLocaleString('fr-FR')} FCFA
                                </div>
                            </div>
                            <div className="stat-icon-stock success">
                                <Package size={24} />
                            </div>
                        </div>
                    </div>
                </div>

                {/* Filtres et recherche */}
                <div className="filters-section-stock stocks-item">
                    <div className="row g-3 align-items-center">
                        <div className="col-12 col-md-6">
                            <div className="input-group">
                                <span className="input-group-text bg-light">
                                    <Search size={16} />
                                </span>
                                <input
                                    type="text"
                                    className="form-control search-input-stock"
                                    placeholder="Rechercher un produit..."
                                    value={searchTerm}
                                    onChange={(e) => setSearchTerm(e.target.value)}
                                />
                            </div>
                        </div>
                        <div className="col-12 col-md-6">
                            <div className="filter-buttons">
                                <Button
                                    variant={showAlertes ? 'danger' : 'outline'}
                                    icon={AlertCircle}
                                    onClick={() => setShowAlertes(!showAlertes)}
                                    className="filter-btn"
                                    type="button"
                                >
                                    <span className="d-none d-md-inline">
                                        {showAlertes ? 'Tous les stocks' : 'Voir les alertes'}
                                    </span>
                                    <span className="d-md-none">
                                        {showAlertes ? 'Tous' : 'Alertes'}
                                    </span>
                                </Button>
                                <Button
                                    variant="outline"
                                    icon={Filter}
                                    className="filter-btn outline d-none d-md-flex"
                                    type="button"
                                >
                                    Filtrer
                                </Button>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Liste des stocks */}
                <div className="stocks-table-section stocks-item">
                    <div className="table-header-stock">
                        <h2 className="table-title-stock">Liste des stocks</h2>
                    </div>

                    <div className="table-responsive">
                        <table className="table table-stock">
                            <thead>
                            <tr>
                                <th>Produit</th>
                                <th className="d-none d-md-table-cell">Code</th>
                                <th className="text-center">Quantité</th>
                                <th className="text-center d-none d-md-table-cell">Seuil</th>
                                <th className="text-center d-none d-lg-table-cell">Entrepôt</th>
                                <th className="text-center">Statut</th>
                                <th className="text-center">Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            {displayStocks?.map((stock) => (
                                <tr key={stock.id}>
                                    <td>
                                        <div>
                                            <div className="fw-semibold text-dark">{stock.produit?.nom}</div>
                                            <small className="text-muted">{stock.produit?.categorie}</small>
                                            <small className="text-muted d-block d-md-none">{stock.produit?.codeEAN}</small>
                                        </div>
                                    </td>
                                    <td className="d-none d-md-table-cell text-muted">{stock.produit?.codeEAN || 'N/A'}</td>
                                    <td className="text-center">
                                            <span className={`quantity-indicator ${stock.quantite <= stock.seuilAlerte ? 'low' : 'normal'}`}>
                                                {stock.quantite}
                                            </span>
                                    </td>
                                    <td className="text-center text-muted d-none d-md-table-cell">{stock.seuilAlerte}</td>
                                    <td className="text-center text-muted d-none d-lg-table-cell">{stock.entrepot?.nom}</td>
                                    <td className="text-center">
                                        {stock.quantite <= stock.seuilAlerte ? (
                                            <span className="status-badge-stock danger">
                                                    <AlertCircle size={12} />
                                                    Faible
                                                </span>
                                        ) : (
                                            <span className="status-badge-stock success">
                                                    OK
                                                </span>
                                        )}
                                    </td>
                                    <td className="text-center">
                                        <div className="action-buttons-stock">
                                            <Button
                                                size="sm"
                                                variant="primary"
                                                className="btn-action-sm primary"
                                                onClick={() => handleAddStock(stock.produit?.nom)}
                                                type="button"
                                            >
                                                + Stock
                                            </Button>
                                            <Button
                                                size="sm"
                                                variant="outline"
                                                className="btn-action-sm outline d-none d-md-inline-block"
                                                type="button"
                                            >
                                                Détails
                                            </Button>
                                        </div>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                        {(!displayStocks || displayStocks.length === 0) && (
                            <div className="empty-state-stock">
                                <Package className="empty-state-icon-stock" />
                                <h3 className="empty-state-title-stock">Aucun stock trouvé</h3>
                                <p className="empty-state-description-stock">
                                    {searchTerm
                                        ? "Essayez de modifier vos critères de recherche"
                                        : "Commencez par ajouter vos premiers produits"
                                    }
                                </p>
                                <Button
                                    icon={Plus}
                                    variant="primary"
                                    onClick={handleAddProduct}
                                    type="button"
                                >
                                    Ajouter un produit
                                </Button>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Stocks;