import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import './ViewBookings.css';

const ViewBookings = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [booking, setBooking] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [paymentStatus, setPaymentStatus] = useState(null);

    useEffect(() => {
        const fetchBooking = async () => {
            try {
                const response = await fetch(`http://localhost:8100/api/bookings/${id}`, {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('staffToken')}`
                    }
                });

                if (!response.ok) throw new Error('Error al cargar reserva');

                const data = await response.json();
                setBooking(data);

            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchBooking();
    }, [id]);

    const handleAcceptBooking = async () => {
        try {
            setPaymentStatus('processing');

            const response = await fetch(`http://localhost:8100/api/payment/create-from-booking/${id}`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('staffToken')}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) throw new Error('Error al crear el pago');

            const paymentData = await response.json();
            setPaymentStatus('success');

            // Opcional: redirigir a la página de detalles del pago
            // navigate(`/payments/${paymentData.id}`);

            // O mostrar mensaje de éxito
            alert(`Pago creado exitosamente! Monto total: $${paymentData.totalAmount}`);

        } catch (err) {
            setPaymentStatus('error');
            setError(err.message);
            alert(`Error al crear pago: ${err.message}`);
        }
    };

    // Función para formatear hora
    const formatTime = (hour, minute) => {
        return `${hour?.toString().padStart(2, '0') || '00'}:${minute?.toString().padStart(2, '0') || '00'}`;
    };

    // Función para traducir tipo de reserva
    const getBookingType = (type) => {
        const typeNames = {
            1: '10 vueltas (10 min)',
            2: '15 vueltas (15 min)',
            3: '20 vueltas (20 min)'
        };
        return typeNames[type] || `Tipo ${type}`;
    };

    if (loading) return <div className="loading">Cargando...</div>;
    if (error) return <div className="error">Error: {error}</div>;
    if (!booking) return <div className="not-found">Reserva no encontrada</div>;

    return (
        <div className="view-booking-container">
            <button onClick={() => navigate(-1)} className="back-button">
                ← Volver
            </button>

            <h1>Detalles de Reserva #{booking.id}</h1>

            <div className="booking-details-card">
                <h2>Información Principal</h2>
                <div className="details-grid">
                    <div className="detail-item">
                        <span className="detail-label">Fecha:</span>
                        <span className="detail-value">{booking.date}</span>
                    </div>
                    <div className="detail-item">
                        <span className="detail-label">Hora:</span>
                        <span className="detail-value">{formatTime(booking.timeHour, booking.timeMinute)}</span>
                    </div>
                    <div className="detail-item">
                        <span className="detail-label">Tipo:</span>
                        <span className="detail-value">{getBookingType(booking.type)}</span>
                    </div>
                    <div className="detail-item">
                        <span className="detail-label">Total Personas:</span>
                        <span className="detail-value">{booking.quantity}</span>
                    </div>
                    <div className="detail-item">
                        <span className="detail-label">RUT Reservante:</span>
                        <span className="detail-value">{booking.rutWhoMadeBooking}</span>
                    </div>
                </div>

                <h2>Participantes</h2>
                <div className="participants-grid">
                    {[...Array(15)].map((_, index) => {
                        const participant = booking[`participant${index + 1}`];
                        return participant ? (
                            <div key={index} className="participant-item">
                                <span className="participant-label">Participante {index + 1}:</span>
                                <span className="participant-value">{participant}</span>
                            </div>
                        ) : null;
                    })}
                </div>

                <div className="accept-button-container">
                    <button
                        className="accept-button"
                        onClick={handleAcceptBooking}
                        disabled={paymentStatus === 'processing'}
                    >
                        {paymentStatus === 'processing' ? 'Procesando...' : 'Aceptar'}
                    </button>
                    {paymentStatus === 'success' && (
                        <div className="success-message">Pago creado exitosamente!</div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default ViewBookings;