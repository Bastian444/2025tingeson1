import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import './SeePaymentDetails.css';

const SeePaymentDetails = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [bookingData, setBookingData] = useState(null);
    const [paymentData, setPaymentData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchPaymentAndBookingDetails = async () => {
            try {
                setLoading(true);
                setError(null);

                // 1. Obtener el pago por idBooking
                const paymentResponse = await fetch(`http://localhost:8100/api/payment/by-booking/${id}`, {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('clientToken')}`,
                        'Content-Type': 'application/json',
                        'Accept': 'application/json'
                    }
                });

                // Verificar si la respuesta está vacía
                const paymentText = await paymentResponse.text();
                if (!paymentText) {
                    throw new Error('No se encontró ningún pago asociado a esta reserva');
                }

                const payment = JSON.parse(paymentText);

                if (!paymentResponse.ok) {
                    throw new Error(payment.message || 'Error al obtener los datos del pago');
                }

                setPaymentData(payment);

                // 2. Obtener los datos de la reserva asociada
                if (!payment.idBooking) {
                    throw new Error('El pago no tiene una reserva asociada');
                }

                const bookingResponse = await fetch(`http://localhost:8100/api/bookings/${payment.idBooking}`, {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('clientToken')}`,
                        'Content-Type': 'application/json',
                        'Accept': 'application/json'
                    }
                });

                const bookingText = await bookingResponse.text();
                if (!bookingText) {
                    throw new Error('No se encontraron datos para la reserva');
                }

                const booking = JSON.parse(bookingText);

                if (!bookingResponse.ok) {
                    throw new Error(booking.message || 'Error al obtener los datos de la reserva');
                }

                setBookingData(booking);

            } catch (err) {
                setError(err.message || 'Ocurrió un error al cargar los datos');
                console.error("Error detallado:", err);
            } finally {
                setLoading(false);
            }
        };

        fetchPaymentAndBookingDetails();
    }, [id]);

    // Función para formatear los participantes desde el pago
    const formatParticipantsFromPayment = (payment) => {
        if (!payment) return [];

        const participants = [];

        // Iterar sobre todos los posibles participantes (1-15)
        for (let i = 1; i <= 15; i++) {
            const rut = payment[`participant${i}Payment`];
            const amount = payment[`totalParticipant${i}`];

            if (rut && amount > 0) {
                participants.push({
                    rut: rut,
                    amount: amount
                });
            }
        }

        return participants;
    };

    // Función para traducir tipo de reserva
    const getBookingType = (type) => {
        const types = {
            1: '10 vueltas o máx 10 min',
            2: '15 vueltas o máx 15 min',
            3: '20 vueltas o máx 20 min'
        };
        return types[type] || `Tipo ${type}`;
    };

    // Función para formatear hora
    const formatTime = (time) => {
        if (!time) return '';
        const timeParts = time.split(':');
        return `${timeParts[0].padStart(2, '0')}:${timeParts[1].padStart(2, '0')}`;
    };

    if (loading) {
        return (
            <div className="loading-container">
                <div className="loading-spinner"></div>
                <p>Cargando detalles de pago...</p>
            </div>
        );
    }

    if (error) {
        return (
            <div className="error-container">
                <p className="error-message">{error}</p>
                <button onClick={() => navigate(-1)} className="back-button">
                    Volver atrás
                </button>
            </div>
        );
    }

    if (!bookingData || !paymentData) {
        return (
            <div className="error-container">
                <p className="error-message">No se encontraron datos para este pago</p>
                <button onClick={() => navigate(-1)} className="back-button">
                    Volver atrás
                </button>
            </div>
        );
    }

    // Preparar los datos para mostrar
    const participants = formatParticipantsFromPayment(paymentData);
    const totalAmount = paymentData.totalAmount;

    return (
        <div className="payment-details-container">
            <button onClick={() => navigate(-1)} className="back-button">
                ← Volver
            </button>

            <h1>Detalles de Pago - Reserva #{bookingData.id}</h1>

            <div className="payment-section">
                <h2>Información de la Reserva</h2>
                <div className="booking-info">
                    <div className="info-row">
                        <span className="info-label">Código de reserva:</span>
                        <span className="info-value">{bookingData.id}</span>
                    </div>
                    <div className="info-row">
                        <span className="info-label">Fecha y hora:</span>
                        <span className="info-value">{bookingData.date} a las {formatTime(bookingData.time)}</span>
                    </div>
                    <div className="info-row">
                        <span className="info-label">Tipo de reserva:</span>
                        <span className="info-value">{getBookingType(bookingData.type)}</span>
                    </div>
                    <div className="info-row">
                        <span className="info-label">Personas incluidas:</span>
                        <span className="info-value">{bookingData.quantity}</span>
                    </div>
                    <div className="info-row">
                        <span className="info-label">Reservado por (RUT):</span>
                        <span className="info-value">{bookingData.rutWhoMadeBooking}</span>
                    </div>
                </div>
            </div>

            <div className="payment-section">
                <h2>Detalle de Pago por Integrante</h2>
                <div className="table-container">
                    <table className="participants-table">
                        <thead>
                        <tr>
                            <th>RUT</th>
                            <th>Total a Pagar</th>
                        </tr>
                        </thead>
                        <tbody>
                        {participants.map((participant, index) => (
                            <tr key={index}>
                                <td>{participant.rut}</td>
                                <td>${participant.amount.toLocaleString()}</td>
                            </tr>
                        ))}
                        </tbody>
                        <tfoot>
                        <tr className="total-row">
                            <td>Total General</td>
                            <td>${totalAmount.toLocaleString()}</td>
                        </tr>
                        </tfoot>
                    </table>
                </div>
            </div>

            <div className="payment-section">
                <h2>Información del Pago</h2>
                <div className="payment-info">
                    <div className="info-row">
                        <span className="info-label">ID del Pago:</span>
                        <span className="info-value">{paymentData.id}</span>
                    </div>
                    <div className="info-row">
                        <span className="info-label">Total Pagado:</span>
                        <span className="info-value">${paymentData.totalAmount.toLocaleString()}</span>
                    </div>
                    <div className="info-row">
                        <span className="info-label">Tipo de Pago:</span>
                        <span className="info-value">{getBookingType(paymentData.typeForPayment)}</span>
                    </div>
                    <div className="info-row">
                        <span className="info-label">Estado del Pago:</span>
                        <span className="info-value">{paymentData.status || 'Completado'}</span>
                    </div>
                    <div className="info-row">
                        <span className="info-label">Fecha de Pago:</span>
                        <span className="info-value">{paymentData.paymentDate || 'No especificada'}</span>
                    </div>
                </div>
            </div>

            <div className="actions-section">
                <button onClick={() => window.print()} className="print-button">
                    Imprimir Comprobante
                </button>
            </div>
        </div>
    );
};

export default SeePaymentDetails;