import React from 'react';
import { Link } from 'react-router-dom';
import { ShoppingCart, Users, BarChart3, Award } from 'lucide-react';

const Home = () => {
    return (
        <div className="min-vh-100 bg-light">
            {/* Hero Section */}
            <div className="bg-primary text-white py-5">
                <div className="container">
                    <div className="row align-items-center">
                        <div className="col-lg-6">
                            <h1 className="display-4 fw-bold mb-4">
                                Supermarché Manager
                            </h1>
                            <p className="lead mb-4">
                                Solution complète de gestion pour votre supermarché.
                                Optimisez vos stocks, gérez vos ventes et fidélisez vos clients.
                            </p>
                            <div className="d-flex gap-3 flex-wrap">
                                <Link to="/login" className="btn btn-light btn-lg px-4 py-2 fw-semibold">
                                    Espace Professionnel
                                </Link>
                                <Link to="/register" className="btn btn-outline-light btn-lg px-4 py-2 fw-semibold">
                                    Devenir Client
                                </Link>
                            </div>
                        </div>
                        <div className="col-lg-6 text-center">
                            <ShoppingCart size={200} className="text-white opacity-75" />
                        </div>
                    </div>
                </div>
            </div>

            {/* Features Section */}
            <div className="py-5">
                <div className="container">
                    <div className="row text-center mb-5">
                        <div className="col-12">
                            <h2 className="fw-bold text-dark mb-3">Pourquoi nous choisir ?</h2>
                            <p className="text-muted lead">Une solution adaptée à tous vos besoins</p>
                        </div>
                    </div>
                    <div className="row g-4">
                        <div className="col-md-6 col-lg-3">
                            <div className="card border-0 shadow-sm h-100">
                                <div className="card-body text-center p-4">
                                    <div className="bg-primary bg-opacity-10 rounded-circle p-3 d-inline-flex mb-3">
                                        <BarChart3 size={32} className="text-primary" />
                                    </div>
                                    <h5 className="fw-bold">Gestion Optimisée</h5>
                                    <p className="text-muted">
                                        Suivi en temps réel des stocks et analyse des performances.
                                    </p>
                                </div>
                            </div>
                        </div>
                        <div className="col-md-6 col-lg-3">
                            <div className="card border-0 shadow-sm h-100">
                                <div className="card-body text-center p-4">
                                    <div className="bg-success bg-opacity-10 rounded-circle p-3 d-inline-flex mb-3">
                                        <Users size={32} className="text-success" />
                                    </div>
                                    <h5 className="fw-bold">Fidélisation Client</h5>
                                    <p className="text-muted">
                                        Programme de fidélité et promotions personnalisées.
                                    </p>
                                </div>
                            </div>
                        </div>
                        <div className="col-md-6 col-lg-3">
                            <div className="card border-0 shadow-sm h-100">
                                <div className="card-body text-center p-4">
                                    <div className="bg-warning bg-opacity-10 rounded-circle p-3 d-inline-flex mb-3">
                                        <Award size={32} className="text-warning" />
                                    </div>
                                    <h5 className="fw-bold">Rapports Détaillés</h5>
                                    <p className="text-muted">
                                        Tableaux de bord complets pour une prise de décision éclairée.
                                    </p>
                                </div>
                            </div>
                        </div>
                        <div className="col-md-6 col-lg-3">
                            <div className="card border-0 shadow-sm h-100">
                                <div className="card-body text-center p-4">
                                    <div className="bg-info bg-opacity-10 rounded-circle p-3 d-inline-flex mb-3">
                                        <ShoppingCart size={32} className="text-info" />
                                    </div>
                                    <h5 className="fw-bold">Ventes Intelligentes</h5>
                                    <p className="text-muted">
                                        Gestion des caisses et suivi des performances commerciales.
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Home;