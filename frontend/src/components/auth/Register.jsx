import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Eye, EyeOff, UserPlus, CheckCircle } from 'lucide-react';
import { authService } from '../services/authService';

const Register = () => {
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        firstName: '',
        lastName: '',
        phoneNumber: '',
        address: '',
        city: '',
        postalCode: ''
    });
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
        setError('');
        setSuccess('');
    };

    const validateForm = () => {
        if (formData.password !== formData.confirmPassword) {
            setError('Les mots de passe ne correspondent pas');
            return false;
        }

        if (formData.password.length < 6) {
            setError('Le mot de passe doit contenir au moins 6 caractères');
            return false;
        }

        return true;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');
        setSuccess('');

        if (!validateForm()) {
            setLoading(false);
            return;
        }

        try {
            const { confirmPassword, ...clientData } = formData;

            await authService.registerClient(clientData);

            setSuccess('Inscription réussie ! Vous pouvez maintenant vous connecter.');

            // Redirection automatique après 3 secondes
            setTimeout(() => {
                navigate('/login');
            }, 3000);

        } catch (error) {
            setError(error.response?.data?.message || 'Erreur lors de l\'inscription');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-vh-100 d-flex align-items-center justify-content-center bg-light py-4">
            <div className="container">
                <div className="row justify-content-center">
                    <div className="col-md-8 col-lg-6">
                        <div className="card shadow-lg border-0">
                            <div className="card-body p-4 p-md-5">
                                {/* En-tête */}
                                <div className="text-center mb-4">
                                    <div className="bg-success rounded-circle p-3 d-inline-flex mb-3">
                                        <UserPlus size={32} className="text-white" />
                                    </div>
                                    <h2 className="card-title fw-bold text-dark">Créer un compte client</h2>
                                    <p className="text-muted">Rejoignez notre programme de fidélité</p>
                                </div>

                                {/* Formulaire */}
                                <form onSubmit={handleSubmit}>
                                    {error && (
                                        <div className="alert alert-danger d-flex align-items-center" role="alert">
                                            <i className="bi bi-exclamation-triangle-fill me-2"></i>
                                            {error}
                                        </div>
                                    )}

                                    {success && (
                                        <div className="alert alert-success d-flex align-items-center" role="alert">
                                            <CheckCircle size={20} className="me-2" />
                                            {success}
                                        </div>
                                    )}

                                    <div className="row">
                                        {/* Informations personnelles */}
                                        <div className="col-md-6">
                                            <h6 className="text-muted mb-3">Informations personnelles</h6>

                                            <div className="mb-3">
                                                <label htmlFor="firstName" className="form-label fw-semibold">
                                                    Prénom *
                                                </label>
                                                <input
                                                    type="text"
                                                    className="form-control"
                                                    id="firstName"
                                                    name="firstName"
                                                    value={formData.firstName}
                                                    onChange={handleChange}
                                                    required
                                                    disabled={loading}
                                                />
                                            </div>

                                            <div className="mb-3">
                                                <label htmlFor="lastName" className="form-label fw-semibold">
                                                    Nom *
                                                </label>
                                                <input
                                                    type="text"
                                                    className="form-control"
                                                    id="lastName"
                                                    name="lastName"
                                                    value={formData.lastName}
                                                    onChange={handleChange}
                                                    required
                                                    disabled={loading}
                                                />
                                            </div>

                                            <div className="mb-3">
                                                <label htmlFor="phoneNumber" className="form-label fw-semibold">
                                                    Téléphone
                                                </label>
                                                <input
                                                    type="tel"
                                                    className="form-control"
                                                    id="phoneNumber"
                                                    name="phoneNumber"
                                                    value={formData.phoneNumber}
                                                    onChange={handleChange}
                                                    disabled={loading}
                                                />
                                            </div>
                                        </div>

                                        {/* Informations de connexion */}
                                        <div className="col-md-6">
                                            <h6 className="text-muted mb-3">Informations de connexion</h6>

                                            <div className="mb-3">
                                                <label htmlFor="username" className="form-label fw-semibold">
                                                    Nom d'utilisateur *
                                                </label>
                                                <input
                                                    type="text"
                                                    className="form-control"
                                                    id="username"
                                                    name="username"
                                                    value={formData.username}
                                                    onChange={handleChange}
                                                    required
                                                    disabled={loading}
                                                />
                                            </div>

                                            <div className="mb-3">
                                                <label htmlFor="email" className="form-label fw-semibold">
                                                    Email *
                                                </label>
                                                <input
                                                    type="email"
                                                    className="form-control"
                                                    id="email"
                                                    name="email"
                                                    value={formData.email}
                                                    onChange={handleChange}
                                                    required
                                                    disabled={loading}
                                                />
                                            </div>

                                            <div className="mb-3">
                                                <label htmlFor="password" className="form-label fw-semibold">
                                                    Mot de passe *
                                                </label>
                                                <div className="position-relative">
                                                    <input
                                                        type={showPassword ? 'text' : 'password'}
                                                        className="form-control"
                                                        id="password"
                                                        name="password"
                                                        value={formData.password}
                                                        onChange={handleChange}
                                                        required
                                                        disabled={loading}
                                                    />
                                                    <button
                                                        type="button"
                                                        className="btn btn-link position-absolute end-0 top-50 translate-middle-y text-muted"
                                                        onClick={() => setShowPassword(!showPassword)}
                                                    >
                                                        {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                                                    </button>
                                                </div>
                                                <small className="text-muted">Minimum 6 caractères</small>
                                            </div>

                                            <div className="mb-4">
                                                <label htmlFor="confirmPassword" className="form-label fw-semibold">
                                                    Confirmer le mot de passe *
                                                </label>
                                                <div className="position-relative">
                                                    <input
                                                        type={showConfirmPassword ? 'text' : 'password'}
                                                        className="form-control"
                                                        id="confirmPassword"
                                                        name="confirmPassword"
                                                        value={formData.confirmPassword}
                                                        onChange={handleChange}
                                                        required
                                                        disabled={loading}
                                                    />
                                                    <button
                                                        type="button"
                                                        className="btn btn-link position-absolute end-0 top-50 translate-middle-y text-muted"
                                                        onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                                                    >
                                                        {showConfirmPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    {/* Adresse */}
                                    <div className="row mt-2">
                                        <div className="col-12">
                                            <h6 className="text-muted mb-3">Adresse (optionnel)</h6>
                                            <div className="row">
                                                <div className="col-md-8 mb-3">
                                                    <label htmlFor="address" className="form-label fw-semibold">
                                                        Adresse
                                                    </label>
                                                    <input
                                                        type="text"
                                                        className="form-control"
                                                        id="address"
                                                        name="address"
                                                        value={formData.address}
                                                        onChange={handleChange}
                                                        disabled={loading}
                                                    />
                                                </div>
                                                <div className="col-md-4 mb-3">
                                                    <label htmlFor="postalCode" className="form-label fw-semibold">
                                                        Code postal
                                                    </label>
                                                    <input
                                                        type="text"
                                                        className="form-control"
                                                        id="postalCode"
                                                        name="postalCode"
                                                        value={formData.postalCode}
                                                        onChange={handleChange}
                                                        disabled={loading}
                                                    />
                                                </div>
                                                <div className="col-12 mb-4">
                                                    <label htmlFor="city" className="form-label fw-semibold">
                                                        Ville
                                                    </label>
                                                    <input
                                                        type="text"
                                                        className="form-control"
                                                        id="city"
                                                        name="city"
                                                        value={formData.city}
                                                        onChange={handleChange}
                                                        disabled={loading}
                                                    />
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <button
                                        type="submit"
                                        className="btn btn-success btn-lg w-100 py-3 fw-semibold"
                                        disabled={loading}
                                    >
                                        {loading ? (
                                            <>
                                                <span className="spinner-border spinner-border-sm me-2" role="status"></span>
                                                Inscription...
                                            </>
                                        ) : (
                                            'Créer mon compte'
                                        )}
                                    </button>

                                    <div className="text-center mt-3">
                                        <small className="text-muted">
                                            Déjà un compte ?{' '}
                                            <Link to="/login" className="text-primary text-decoration-none fw-semibold">
                                                Se connecter
                                            </Link>
                                        </small>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Register;