import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./StaffRegister.css";

const StaffRegister = ({ onRegisterSuccess }) => {
    const [formData, setFormData] = useState({
        staffname: "",
        stafflastName: "",
        staffemail: "",
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
            const response = await axios.post("http://localhost:8100/api/staff/", formData, {
                headers: {
                    "Content-Type": "application/json"
                }
            });

            if (response.status === 201) {
                setMessage("Personal registrado exitosamente!");
                setFormData({
                    staffname: "",
                    stafflastName: "",
                    staffemail: "",
                    staffrut: "",
                    staffpassword: ""
                });

                if (onRegisterSuccess) {
                    onRegisterSuccess(response.data);
                }
            }
        } catch (error) {
            setMessage(error.response?.data?.message || "Error al registrar personal");
        }
    };

    return (
        <div className="register-container">
            <button onClick={() => navigate("/home")} className="back-button">
                &larr; Volver
            </button>

            <h2 className="register-title">Registro de Personal</h2>
            <p className="register-subtitle">
                Registra al personal con sus credenciales
            </p>

            {message && <p className={`message ${message.includes("Error") ? "error" : "success"}`}>{message}</p>}

            <form onSubmit={handleSubmit} className="register-form">
                <div className="form-group">
                    <label htmlFor="staffname">Nombre</label>
                    <input
                        type="text"
                        id="staffname"
                        name="staffname"
                        value={formData.staffname}
                        onChange={handleChange}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="stafflastName">Apellido</label>
                    <input
                        type="text"
                        id="stafflastName"
                        name="stafflastName"
                        value={formData.stafflastName}
                        onChange={handleChange}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="staffemail">Email</label>
                    <input
                        type="email"
                        id="staffemail"
                        name="staffemail"
                        value={formData.staffemail}
                        onChange={handleChange}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="staffrut">RUT</label>
                    <input
                        type="text"
                        id="staffrut"
                        name="staffrut"
                        value={formData.staffrut}
                        onChange={handleChange}
                        placeholder="12345678-9"
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
                    />
                </div>

                <button type="submit" className="register-button">
                    Registrar Personal
                </button>
            </form>
        </div>
    );
};

export default StaffRegister;