import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./ClientLogin.css";

const ClientLogin = ({ onLoginSuccess }) => {
    const [formData, setFormData] = useState({
        clientrut: "",  // Cambiado de rut a clientrut
        clientpassword: ""  // Cambiado de password a clientpassword
    });

    const [message, setMessage] = useState("");
    const [isSubmitting, setIsSubmitting] = useState(false);
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);
        setMessage("");

        try {
            const response = await axios.post(
                "http://localhost:8100/api/clients/login",
                formData,  // Ahora envía clientrut y clientpassword
                {
                    headers: {
                        "Content-Type": "application/json"
                    }
                }
            );

            if (response.status === 200) {
                setMessage("¡Inicio de sesión exitoso!");
                const user = response.data;

                // Guardar en localStorage con el nuevo formato
                localStorage.setItem('clientRut', user.clientrut);  // Usar clientrut del usuario autenticado
                localStorage.setItem('clientName', user.clientname);

                if (onLoginSuccess) {
                    onLoginSuccess(user);
                }

                navigate("/client-main", {
                    state: {
                        name: user.clientname,  // Cambiado de name a clientname
                        rut: user.clientrut     // Cambiado de rut a clientrut
                    }
                });
            }
        } catch (error) {
            setMessage(error.response?.data?.message || "Credenciales incorrectas. Intente nuevamente.");
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="login-container">
            <button onClick={() => navigate("/home")} className="back-button">
                &larr; Volver
            </button>

            <h2 className="login-title">Iniciar Sesión - Clientes</h2>
            <p className="login-subtitle">Accede a tu cuenta con tu RUT y contraseña</p>

            {message && (
                <div className={`message ${message.includes("Error") || message.includes("incorrectas") ? "error" : "success"}`}>
                    {message}
                </div>
            )}

            <form onSubmit={handleSubmit} className="login-form">
                <div className="form-group">
                    <label htmlFor="clientrut">RUT</label>
                    <input
                        type="text"
                        id="clientrut"
                        name="clientrut"
                        value={formData.clientrut}
                        onChange={handleChange}
                        placeholder="12345678-9"
                        required
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="clientpassword">Contraseña</label>
                    <input
                        type="password"
                        id="clientpassword"
                        name="clientpassword"
                        value={formData.clientpassword}
                        onChange={handleChange}
                        required
                    />
                </div>

                <button
                    type="submit"
                    className="login-button"
                    disabled={isSubmitting}
                >
                    {isSubmitting ? "Iniciando sesión..." : "Iniciar Sesión"}
                </button>
            </form>
        </div>
    );
};

export default ClientLogin;