import React from 'react';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

const Unauthorized = () => {
    const navigate = useNavigate();

    return (
        <Container fluid className="bg-light min-vh-100 d-flex align-items-center">
            <Container>
                <Row className="justify-content-center">
                    <Col md={6} lg={4}>
                        <Card className="shadow text-center">
                            <Card.Body className="p-5">
                                <div className="mb-4">
                                    <h1 className="text-danger display-1">403</h1>
                                    <h3 className="text-dark">Accès Refusé</h3>
                                    <p className="text-muted">
                                        Vous n'avez pas les permissions nécessaires pour accéder à cette page.
                                    </p>
                                </div>
                                <Button
                                    variant="primary"
                                    onClick={() => navigate('/dashboard')}
                                >
                                    Retour au Tableau de Bord
                                </Button>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>
        </Container>
    );
};

export default Unauthorized;