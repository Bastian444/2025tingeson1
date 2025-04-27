import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './ClientRegister.css';

const ClientRegister = ({ onLoginSuccess }) => {
    const [formData, setFormData] = useState({
        clientname: '',
        clientlastname: '',
        clientemail: '',
        clientrut: '',
        clientpassword: '',
        birthdate: '',
        visits: 0
    });

    const [message, setMessage] = useState('');
    const [isSuccess, setIsSuccess] = useState(false);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);
        setMessage('');

        try {
            const response = await axios.post('http://localhost:8100/api/clients/', {
                ...formData
            }, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (response.status === 201) {
                setIsSuccess(true);
                setMessage('¡Registro exitoso! Redirigiendo a página de login...');

                // Limpiar formulario
                setFormData({
                    clientname: '',
                    clientlastname: '',
                    clientemail: '',
                    clientrut: '',
                    clientpassword: '',
                    birthdate: '',
                    visits: 0
                });

                // Redirigir después de 2 segundos
                setTimeout(() => {
                    navigate('/client-login');
                }, 2000);
            }
        } catch (error) {
            setIsSuccess(false);
            const errorMessage = error.response?.data?.message ||
                error.response?.data?.error ||
                'Error al registrar cliente. Por favor intenta nuevamente.';
            setMessage(errorMessage);
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="register-container">
            <button onClick={() => navigate('/home')} className="back-button">
                &larr; Volver
            </button>

            <h2 className="register-title">Registro de Cliente</h2>
            <p className="register-subtitle">
                Crea tu cuenta para acceder a nuestros servicios
            </p>

            {message && (
                <div className={`message ${isSuccess ? 'success' : 'error'}`}>
                    {message}
                    {isSuccess && <div className="loading-spinner"></div>}
                </div>
            )}

            <form onSubmit={handleSubmit} className="register-form">
                <div className="form-group">
                    <label htmlFor="clientname">Nombre</label>
                    <input
                        type="text"
                        id="clientname"
                        name="clientname"
                        value={formData.clientname}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="clientlastname">Apellido</label>
                    <input
                        type="text"
                        id="clientlastname"
                        name="clientlastname"
                        value={formData.clientlastname}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="clientemail">Email</label>
                    <input
                        type="email"
                        id="clientemail"
                        name="clientemail"
                        value={formData.clientemail}
                        onChange={handleChange}
                        required
                    />
                </div>

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
                        minLength="6"
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="birthdate">Fecha de Nacimiento</label>
                    <input
                        type="date"
                        id="birthdate"
                        name="birthdate"
                        value={formData.birthdate}
                        onChange={handleChange}
                        required
                    />
                </div>

                <button
                    type="submit"
                    className="register-button"
                    disabled={isSubmitting}
                >
                    {isSubmitting ? 'Registrando...' : 'Registrarse'}
                </button>
            </form>
        </div>
    );
};

export default ClientRegister;