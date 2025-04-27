import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './MyBookings.css';

const MyBookings = () => {
    const navigate = useNavigate();
    const [bookings, setBookings] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Función para obtener y validar el RUT del localStorage
    const getClientRutFromStorage = () => {
        const clientRut = localStorage.getItem('clientRut');
        if (!clientRut || clientRut.trim() === '') {
            throw new Error('No se encontró el RUT del cliente en la sesión');
        }
        return clientRut;
    };

    useEffect(() => {
        const fetchBookings = async () => {
            try {
                // 1. Obtener el RUT del cliente desde localStorage
                const clientRut = getClientRutFromStorage();

                // 2. Hacer la petición al backend (usando el mismo valor como 'rut')
                const response = await fetch(`http://localhost:8100/api/bookings/rut/${clientRut}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });

                if (!response.ok) {
                    const errorData = await response.json();
                    throw new Error(errorData.message || 'Error al cargar reservas');
                }

                const data = await response.json();
                setBookings(data);

            } catch (err) {
                console.error('Error al obtener reservas:', err);
                setError(err.message);

                // Redirigir al login si no hay RUT o hay error de autenticación
                if (err.message.includes('No se encontró') || err.message.includes('autenticación')) {
                    localStorage.removeItem('clientRut'); // Limpiar el RUT inválido
                    navigate('/login', { replace: true });
                }
            } finally {
                setLoading(false);
            }
        };

        fetchBookings();
    }, [navigate]);

    // Formatear hora para mostrar en formato HH:MM
    const formatTime = (hour, minute) => {
        const formattedHour = hour?.toString().padStart(2, '0') || '00';
        const formattedMinute = minute?.toString().padStart(2, '0') || '00';
        return `${formattedHour}:${formattedMinute}`;
    };

    // Traducir el tipo de reserva a texto legible
    const getBookingType = (type) => {
        const typeNames = {
            1: '10 vueltas o máx 10 min',
            2: '15 vueltas o máx 15 min',
            3: '20 vueltas o máx 20 min'
        };
        return typeNames[type] || `Tipo ${type}`;
    };

    // Mostrar estado de carga
    if (loading) {
        return (
            <div className="loading-container">
                <div className="loading-spinner"></div>
                <p>Cargando tus reservas...</p>
            </div>
        );
    }

    // Mostrar errores
    if (error) {
        return (
            <div className="error-container">
                <h2>Error</h2>
                <p className="error-message">{error}</p>
                <button
                    onClick={() => window.location.reload()}
                    className="retry-button"
                >
                    Reintentar
                </button>
                <button
                    onClick={() => navigate('/client-main')}
                    className="back-button"
                >
                    Volver al inicio
                </button>
            </div>
        );
    }

    // Renderizado principal
    return (
        <div className="my-bookings-container">
            <div className="header-section">
                <button
                    onClick={() => navigate('/client-main')}
                    className="back-button"
                >
                    ← Volver al menú principal
                </button>
                <h1 className="main-title">Mis Reservas</h1>
            </div>

            {bookings.length === 0 ? (
                <div className="no-bookings">
                    <p>No tienes reservas registradas.</p>
                    <button
                        onClick={() => navigate('/make-booking')}
                        className="new-booking-button"
                    >
                        Crear nueva reserva
                    </button>
                </div>
            ) : (
                <div className="bookings-content">
                    <div className="bookings-table-container">
                        <table className="bookings-table">
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Fecha</th>
                                <th>Hora</th>
                                <th>Tipo</th>
                                <th>Personas</th>
                                <th>Estado</th>
                                <th>Acciones</th>
                            </tr>
                            </thead>
                            <tbody>
                            {bookings.map((booking) => (
                                <tr key={booking.id} className="booking-row">
                                    <td>{booking.id}</td>
                                    <td>{booking.date}</td>
                                    <td>{formatTime(booking.timeHour, booking.timeMinute)}</td>
                                    <td>{getBookingType(booking.type)}</td>
                                    <td>{booking.quantity}</td>
                                    <td className={`status ${booking.status || 'confirmed'}`}>
                                        {booking.status || 'Confirmada'}
                                    </td>
                                    <td>
                                        <button
                                            onClick={() => navigate(`/see-payment/${booking.id}`)}
                                            className="payment-details-button"
                                        >
                                            Ver detalles de pago
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>

                    <div className="actions-section">
                        <button
                            onClick={() => navigate('/make-booking')}
                            className="action-button primary"
                        >
                            Nueva Reserva
                        </button>
                        <button
                            onClick={() => window.print()}
                            className="action-button secondary"
                        >
                            Imprimir
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default MyBookings;