import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { useNavigate, useLocation } from 'react-router-dom';
import { Eye, EyeOff, LogIn } from 'lucide-react';

const Login = () => {
    const [formData, setFormData] = useState({
        username: '',
        password: ''
    });
    const [showPassword, setShowPassword] = useState(false);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const { login } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();

    const from = location.state?.from?.pathname || '/dashboard';

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
        setError('');
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        const result = await login(formData.username, formData.password);

        if (result.success) {
            navigate(from, { replace: true });
        } else {
            setError(result.error);
        }

        setLoading(false);
    };

    return (
        <div className="min-vh-100 d-flex align-items-center justify-content-center bg-light">
            <div className="container">
                <div className="row justify-content-center">
                    <div className="col-md-6 col-lg-4">
                        <div className="card shadow-lg border-0">
                            <div className="card-body p-5">
                                {/* En-tête */}
                                <div className="text-center mb-4">
                                    <div className="bg-primary rounded-circle p-3 d-inline-flex mb-3">
                                        <LogIn size={32} className="text-white" />
                                    </div>
                                    <h2 className="card-title fw-bold text-dark">Connexion</h2>
                                    <p className="text-muted">Accédez à votre espace de gestion</p>
                                </div>

                                {/* Formulaire */}
                                <form onSubmit={handleSubmit}>
                                    {error && (
                                        <div className="alert alert-danger d-flex align-items-center" role="alert">
                                            <i className="bi bi-exclamation-triangle-fill me-2"></i>
                                            {error}
                                        </div>
                                    )}

                                    <div className="mb-3">
                                        <label htmlFor="username" className="form-label fw-semibold">
                                            Nom d'utilisateur
                                        </label>
                                        <input
                                            type="text"
                                            className="form-control form-control-lg"
                                            id="username"
                                            name="username"
                                            value={formData.username}
                                            onChange={handleChange}
                                            placeholder="Entrez votre nom d'utilisateur"
                                            required
                                            disabled={loading}
                                        />
                                    </div>

                                    <div className="mb-4">
                                        <label htmlFor="password" className="form-label fw-semibold">
                                            Mot de passe
                                        </label>
                                        <div className="position-relative">
                                            <input
                                                type={showPassword ? 'text' : 'password'}
                                                className="form-control form-control-lg"
                                                id="password"
                                                name="password"
                                                value={formData.password}
                                                onChange={handleChange}
                                                placeholder="Entrez votre mot de passe"
                                                required
                                                disabled={loading}
                                            />
                                            <button
                                                type="button"
                                                className="btn btn-link position-absolute end-0 top-50 translate-middle-y text-muted"
                                                onClick={() => setShowPassword(!showPassword)}
                                            >
                                                {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
                                            </button>
                                        </div>
                                    </div>

                                    <button
                                        type="submit"
                                        className="btn btn-primary btn-lg w-100 py-3 fw-semibold"
                                        disabled={loading}
                                    >
                                        {loading ? (
                                            <>
                                                <span className="spinner-border spinner-border-sm me-2" role="status"></span>
                                                Connexion...
                                            </>
                                        ) : (
                                            'Se connecter'
                                        )}
                                    </button>
                                </form>


                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Login;