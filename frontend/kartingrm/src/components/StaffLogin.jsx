import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./StaffLogin.css";

const StaffLogin = ({ onLoginSuccess }) => {
    const [formData, setFormData] = useState({
        staffrut: "",
        staffpassword: ""
    });

    const [message, setMessage] = useState("");
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
        try {
            const response = await axios.post("http://localhost:8100/api/staff/login", formData, {
                headers: {
                    "Content-Type": "application/json"
                }
            });

            if (response.status === 200) {
                setMessage("Inicio de sesión exitoso!");
                const staff = response.data;
                if (onLoginSuccess) {
                    onLoginSuccess(staff);
                }
                // Nota: Asegúrate que tu backend devuelva staffname en lugar de name
                navigate("/staff-main", { state: { name: staff.staffname, role: staff.role } });
            }
        } catch (error) {
            setMessage(error.response?.data?.message || "Error al iniciar sesión");
        }
    };

    return (
        <div className="login-container">
            <button onClick={() => navigate("/home")} className="back-button">
                &larr; Volver
            </button>

            <h2 className="login-title">Inicio de Sesión - Personal</h2>
            <p className="login-subtitle">Accede al sistema con tu RUT y contraseña</p>

            {message && <p className={`message ${message.includes("Error") ? "error" : "success"}`}>{message}</p>}

            <form onSubmit={handleSubmit} className="login-form">
                <div className="form-group">
                    <label htmlFor="staffrut">RUT</label>
                    <input
                        type="text"
                        id="staffrut"
                        name="staffrut"
                        value={formData.staffrut}
                        onChange={handleChange}
                        placeholder="12345678-9"
                        required
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="staffpassword">Contraseña</label>
                    <input
                        type="password"
                        id="staffpassword"
                        name="staffpassword"
                        value={formData.staffpassword}
                        onChange={handleChange}
                        required
                    />
                </div>

                <button type="submit" className="login-button staff">Iniciar Sesión</button>
            </form>
        </div>
    );
};

export default StaffLogin;