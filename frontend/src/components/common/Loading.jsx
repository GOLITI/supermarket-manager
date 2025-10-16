const Loading = ({ size = 'md' }) => {
    const sizes = {
        sm: 'spinner-border-sm',
        md: '',
        lg: 'spinner-border-lg'
    };

    return (
        <div className="d-flex justify-content-center align-items-center p-4">
            <div className={`spinner-border text-primary ${sizes[size]}`} role="status">
                <span className="visually-hidden">Chargement...</span>
            </div>
        </div>
    );
};

export default Loading;