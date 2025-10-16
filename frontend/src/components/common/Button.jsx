import React from 'react';

const Button = ({
                    children,
                    icon: Icon,
                    variant = 'primary',
                    size = 'md',
                    onClick,
                    disabled = false,
                    className = '',
                    type = 'button'
                }) => {
    const variants = {
        primary: 'btn-primary',
        secondary: 'btn-secondary',
        success: 'btn-success',
        danger: 'btn-danger',
        warning: 'btn-warning',
        info: 'btn-info',
        light: 'btn-light',
        dark: 'btn-dark',
        outline: 'btn-outline-primary'
    };

    const sizes = {
        sm: 'btn-sm',
        md: '',
        lg: 'btn-lg'
    };

    return (
        <button
            type={type}
            className={`btn ${variants[variant]} ${sizes[size]} ${Icon ? 'btn-icon' : ''} ${className}`}
            onClick={onClick}
            disabled={disabled}
        >
            {Icon && <Icon size={16} className="me-2" />}
            {children}
        </button>
    );
};

export default Button;