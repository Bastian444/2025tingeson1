import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import "./StaffMain.css";

const StaffMain = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const [showReportsDropdown, setShowReportsDropdown] = useState(false);

    // Extraer el nombre del staff desde el estado de navegación
    const staffName = location.state?.name || "Personal";

    return (
        <>
            {/* Navbar */}
            <nav className="navbar">
                <ul className="nav-links">
                    <li onClick={() => navigate("/rack")} className="nav-item">Rack Semanal</li>

                    <li
                        className="nav-item dropdown"
                        onMouseEnter={() => setShowReportsDropdown(true)}
                        onMouseLeave={() => setShowReportsDropdown(false)}
                    >
                        Reportes de Ingresos
                        {showReportsDropdown && (
                            <ul className="dropdown-menu">
                                <li onClick={() => navigate("/report-by-type")}>Por Número de Vueltas o Tiempo Máximo</li>
                                <li onClick={() => navigate("/report-by-quantity")}>Por Número de Personas</li>
                            </ul>
                        )}
                    </li>

                    <li onClick={() => navigate("/list-all-bookings")} className="nav-item">Registro Reservas</li>
                </ul>
            </nav>

            {/* Contenido principal */}
            <div className="main-container">
                <h1 className="welcome-message">Hola, {staffName}!</h1>
                <button className="logout-button" onClick={() => navigate("/")}>
                    Cerrar sesión
                </button>
            </div>
        </>
    );
};

export default StaffMain;