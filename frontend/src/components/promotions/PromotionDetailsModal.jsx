import { useQuery } from '@tanstack/react-query';
import { promotionService } from '../../services';
import Modal from '../common/Modal';
import Button from '../common/Button';
import Loading from '../common/Loading';
import { X, Calendar, Tag, Package } from 'lucide-react';
import { format } from 'date-fns';
import { fr } from 'date-fns/locale';

const PromotionDetailsModal = ({ open, onClose, promotionId }) => {
  const { data: promotion, isLoading } = useQuery({
    queryKey: ['promotion', promotionId],
    queryFn: () => promotionService.getById(promotionId).then(res => res.data),
    enabled: !!promotionId && open,
  });

  const isActive = promotion 
    ? new Date(promotion.dateDebut) <= new Date() && new Date() <= new Date(promotion.dateFin)
    : false;

  return (
    <Modal open={open} onClose={onClose} maxWidth="md">
      <div className="modal-header">
        <h2 className="modal-title">Détails de la promotion</h2>
        <button onClick={onClose} className="modal-close-btn">
          <X size={24} />
        </button>
      </div>

      <div className="modal-body">
        {isLoading ? (
          <div className="loading-container">
            <Loading size="lg" />
          </div>
        ) : promotion ? (
          <div className="promotion-details">
            {/* En-tête avec statut */}
            <div className="details-header">
              <div>
                <h3 className="promotion-name">{promotion.nom}</h3>
                <p className="promotion-code">Code: {promotion.code}</p>
              </div>
              <span className={`statut-badge ${isActive ? 'actif' : 'inactif'}`}>
                {isActive ? 'Active' : 'Inactive'}
              </span>
            </div>

            {/* Réduction */}
            <div className="reduction-card">
              <Tag size={32} color="#8b5cf6" />
              <div>
                <p className="reduction-label">Réduction</p>
                <p className="reduction-value">-{promotion.pourcentageReduction}%</p>
              </div>
            </div>

            {/* Description */}
            {promotion.description && (
              <div className="detail-section">
                <h4 className="section-title">Description</h4>
                <p className="section-content">{promotion.description}</p>
              </div>
            )}

            {/* Période de validité */}
            <div className="detail-section">
              <h4 className="section-title">Période de validité</h4>
              <div className="dates-container">
                <div className="date-item">
                  <Calendar size={16} color="#6b7280" />
                  <span>Du {format(new Date(promotion.dateDebut), 'dd MMMM yyyy', { locale: fr })}</span>
                </div>
                <div className="date-item">
                  <Calendar size={16} color="#6b7280" />
                  <span>Au {format(new Date(promotion.dateFin), 'dd MMMM yyyy', { locale: fr })}</span>
                </div>
              </div>
            </div>

            {/* Produits concernés */}
            {promotion.produits && promotion.produits.length > 0 && (
              <div className="detail-section">
                <h4 className="section-title">
                  <Package size={18} />
                  Produits concernés ({promotion.produits.length})
                </h4>
                <div className="produits-list">
                  {promotion.produits.map((produit, index) => (
                    <div key={index} className="produit-item">
                      <span className="produit-name">{produit.nom}</span>
                      {produit.code && (
                        <span className="produit-code">{produit.code}</span>
                      )}
                    </div>
                  ))}
                </div>
              </div>
            )}

            {/* Informations supplémentaires */}
            <div className="detail-section">
              <h4 className="section-title">Informations</h4>
              <div className="info-grid">
                <div className="info-item">
                  <span className="info-label">État</span>
                  <span className="info-value">{promotion.actif ? 'Activée' : 'Désactivée'}</span>
                </div>
                {promotion.dateCreation && (
                  <div className="info-item">
                    <span className="info-label">Créée le</span>
                    <span className="info-value">
                      {format(new Date(promotion.dateCreation), 'dd/MM/yyyy', { locale: fr })}
                    </span>
                  </div>
                )}
              </div>
            </div>
          </div>
        ) : (
          <p className="error-message">Promotion non trouvée</p>
        )}
      </div>

      <div className="modal-footer">
        <Button variant="outline" onClick={onClose}>
          Fermer
        </Button>
      </div>

      <style jsx>{`
        .modal-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          padding: 1.5rem;
          border-bottom: 1px solid #e5e7eb;
        }

        .modal-title {
          font-size: 1.25rem;
          font-weight: 600;
          color: #1f2937;
          margin: 0;
        }

        .modal-close-btn {
          padding: 0.5rem;
          border: none;
          background: transparent;
          cursor: pointer;
          color: #6b7280;
          transition: color 0.2s;
        }

        .modal-close-btn:hover {
          color: #1f2937;
        }

        .modal-body {
          padding: 1.5rem;
          max-height: 70vh;
          overflow-y: auto;
        }

        .loading-container {
          display: flex;
          justify-content: center;
          padding: 3rem;
        }

        .promotion-details {
          display: flex;
          flex-direction: column;
          gap: 1.5rem;
        }

        .details-header {
          display: flex;
          justify-content: space-between;
          align-items: flex-start;
          padding-bottom: 1rem;
          border-bottom: 2px solid #f3f4f6;
        }

        .promotion-name {
          font-size: 1.5rem;
          font-weight: 700;
          color: #1f2937;
          margin: 0 0 0.5rem 0;
        }

        .promotion-code {
          font-family: 'Courier New', monospace;
          font-size: 0.875rem;
          color: #6b7280;
          margin: 0;
        }

        .statut-badge {
          display: inline-block;
          padding: 0.5rem 1rem;
          border-radius: 9999px;
          font-size: 0.875rem;
          font-weight: 600;
        }

        .statut-badge.actif {
          background-color: #d1fae5;
          color: #065f46;
        }

        .statut-badge.inactif {
          background-color: #f3f4f6;
          color: #374151;
        }

        .reduction-card {
          display: flex;
          align-items: center;
          gap: 1rem;
          padding: 1.5rem;
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          border-radius: 0.75rem;
          color: white;
        }

        .reduction-label {
          font-size: 0.875rem;
          margin: 0 0 0.25rem 0;
          opacity: 0.9;
        }

        .reduction-value {
          font-size: 2rem;
          font-weight: 800;
          margin: 0;
        }

        .detail-section {
          display: flex;
          flex-direction: column;
          gap: 0.75rem;
        }

        .section-title {
          display: flex;
          align-items: center;
          gap: 0.5rem;
          font-size: 1rem;
          font-weight: 600;
          color: #1f2937;
          margin: 0;
        }

        .section-content {
          color: #4b5563;
          line-height: 1.6;
          margin: 0;
        }

        .dates-container {
          display: flex;
          flex-direction: column;
          gap: 0.5rem;
        }

        .date-item {
          display: flex;
          align-items: center;
          gap: 0.5rem;

