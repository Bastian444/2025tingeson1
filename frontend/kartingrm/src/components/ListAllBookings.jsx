import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './ListAllBookings.css';

const ListAllBookings = () => {
    const navigate = useNavigate();
    const [bookings, setBookings] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Fetch todas las reservas
    useEffect(() => {
        const fetchAllBookings = async () => {
            try {
                const response = await fetch('http://localhost:8100/api/bookings/', {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${localStorage.getItem('staffToken')}`
                    }
                });

                if (!response.ok) {
                    throw new Error('Error al cargar reservas');
                }

                const data = await response.json();
                setBookings(data);

            } catch (err) {
                console.error('Error:', err);
                setError(err.message);
                if (err.message.includes('401')) {
                    navigate('/staff-login');
                }
            } finally {
                setLoading(false);
            }
        };

        fetchAllBookings();
    }, [navigate]);

    // Formatear hora (HH:MM)
    const formatTime = (hour, minute) => {
        return `${hour?.toString().padStart(2, '0') || '00'}:${minute?.toString().padStart(2, '0') || '00'}`;
    };

    // Traducir tipo de reserva
    const getBookingType = (type) => {
        const typeNames = {
            1: '10 vueltas (10 min)',
            2: '15 vueltas (15 min)',
            3: '20 vueltas (20 min)'
        };
        return typeNames[type] || `Tipo ${type}`;
    };

    // Cancelar reserva (FUNCIÓN CORREGIDA)
    const handleCancelBooking = async (bookingId) => {
        try {
            const response = await fetch(`http://localhost:8100/api/bookings/${bookingId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('staffToken')}`
                }
            });

            if (!response.ok) {
                throw new Error('Error al cancelar reserva');
            }

            setBookings(bookings.filter(booking => booking.id !== bookingId));

        } catch (err) {
            console.error('Error al cancelar:', err);
            setError(err.message);
        }
    };

    if (loading) {
        return <div className="loading">Cargando reservas...</div>;
    }

    if (error) {
        return (
            <div className="error">
                <p>Error: {error}</p>
                <button onClick={() => window.location.reload()}>Reintentar</button>
            </div>
        );
    }

    return (
        <div className="list-all-bookings">
            <button
                onClick={() => navigate(-1)}
                className="back-button"
            >
                ← Volver
            </button>

            <h1>Gestión de Reservas (Staff)</h1>

            <table>
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
                    <tr key={booking.id}>
                        <td>{booking.id}</td>
                        <td>{booking.date}</td>
                        <td>{formatTime(booking.timeHour, booking.timeMinute)}</td>
                        <td>{getBookingType(booking.type)}</td>
                        <td>{booking.quantity}</td>
                        <td className={`status-${booking.status}`}>
                            {booking.status || 'Confirmada'}
                        </td>
                        <td className="actions-cell">
                            <div className="button-group">
                                <button
                                    onClick={() => navigate(`/view-bookings/${booking.id}`)}
                                    className="details-btn"
                                >
                                    Detalles
                                </button>
                                <button
                                    onClick={() => handleCancelBooking(booking.id)}
                                    disabled={booking.status === 'Cancelled'}
                                    className="cancel-btn"
                                >
                                    Cancelar
                                </button>
                            </div>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default ListAllBookings;