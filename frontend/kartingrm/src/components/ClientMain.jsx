import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import "./ClientMain.css";

const ClientMain = () => {
    const location = useLocation();
    const navigate = useNavigate();

    // Extraer el nombre del usuario desde el estado de navegación
    const userName = location.state?.name || "Usuario";

    return (
        <>
            {/* Navbar */}
            <nav className="navbar">
                <ul className="nav-links">
                    <li onClick={() => navigate("/make-booking")} className="nav-item">Hacer una reserva</li>
                    <li onClick={() => navigate("/my-bookings")} className="nav-item">Mis reservas</li>
                </ul>
            </nav>

            {/* Contenido principal */}
            <div className="main-container">
                <h1 className="welcome-message">Hola, {userName}!</h1>
                <button className="logout-button" onClick={() => navigate("/")}>Cerrar sesión</button>
            </div>
        </>
    );
};

export default ClientMain;
