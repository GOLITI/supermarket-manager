import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { Container, Row, Col, Card, Form, Button, Alert } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            await login(username, password);
            navigate('/dashboard');
        } catch (err) {
            setError(err.message || 'Identifiants incorrects');
        } finally {
            setLoading(false);
        }
    };

    return (
        <Container fluid className="bg-light min-vh-100 d-flex align-items-center">
            <Container>
                <Row className="justify-content-center">
                    <Col md={6} lg={4}>
                        <Card className="shadow">
                            <Card.Body className="p-4">
                                <div className="text-center mb-4">
                                    <h2 className="text-primary">Supermarket Manager</h2>
                                    <p className="text-muted">Connectez-vous à votre compte</p>
                                </div>

                                {error && <Alert variant="danger">{error}</Alert>}

                                <Form onSubmit={handleSubmit}>
                                    <Form.Group className="mb-3">
                                        <Form.Label>Nom d'utilisateur</Form.Label>
                                        <Form.Control
                                            type="text"
                                            value={username}
                                            onChange={(e) => setUsername(e.target.value)}
                                            required
                                            placeholder="Entrez votre nom d'utilisateur"
                                        />
                                    </Form.Group>

                                    <Form.Group className="mb-3">
                                        <Form.Label>Mot de passe</Form.Label>
                                        <Form.Control
                                            type="password"
                                            value={password}
                                            onChange={(e) => setPassword(e.target.value)}
                                            required
                                            placeholder="Entrez votre mot de passe"
                                        />
                                    </Form.Group>

                                    <Button
                                        variant="primary"
                                        type="submit"
                                        className="w-100"
                                        disabled={loading}
                                    >
                                        {loading ? 'Connexion...' : 'Se connecter'}
                                    </Button>
                                </Form>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>
        </Container>
    );
};

export default Login;