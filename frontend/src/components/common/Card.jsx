const Card = ({ title, subtitle, children, className = '', actions }) => {
    return (
        <div className={`card custom-card ${className}`}>
            <div className="card-body">
                {(title || subtitle || actions) && (
                    <div className="d-flex justify-content-between align-items-start mb-3">
                        <div>
                            {title && <h5 className="card-title mb-1">{title}</h5>}
                            {subtitle && <p className="card-text text-muted mb-0">{subtitle}</p>}
                        </div>
                        {actions && <div className="d-flex gap-2">{actions}</div>}
                    </div>
                )}
                {children}
            </div>
        </div>
    );
};

export default Card;