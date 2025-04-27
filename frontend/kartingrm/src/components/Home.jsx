import React, { useEffect } from 'react';
import './Home.css';

const Home = () => {
    useEffect(() => {
        // Disable scrolling
        document.body.style.overflow = 'hidden';

        return () => {
            // Restore scrolling when component unmounts
            document.body.style.overflow = 'auto';
        };
    }, []);

    return (
        <div className="home-container">
            <h1 className="home-title">Bienvenido a KartingRM!</h1>
            <h2 className="home-subtitle">Inicia Sesión o Regístrate</h2>
            <div className="button-container">
                <div className="button-group">
                    <a href="/client-login" className="client-login">
                        Iniciar sesión Clientes
                    </a>
                    <a href="/client-register" className="client-register">
                        Registrarse Clientes
                    </a>
                </div>
                <div className="button-group">
                    <a href="/staff-login" className="staff-login">
                        Iniciar sesión Personal
                    </a>
                    <a href="/staff-register" className="staff-register">
                        Registrarse Personal
                    </a>
                </div>
            </div>
        </div>
    );
};

export default Home;
