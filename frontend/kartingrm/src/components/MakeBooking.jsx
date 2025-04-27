import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './MakeBooking.css';

const MakeBooking = () => {
    const navigate = useNavigate();
    const [bookingData, setBookingData] = useState({
        date: '',
        time: '',
        type: 1, // Valor por defecto
        quantity: 1, // Valor por defecto
        rutWhoMadeBooking: '',
        status: 'Pendiente', // Valor por defecto para el nuevo campo
        participants: Array(15).fill('') // Array para los 15 posibles participantes
    });
    const [showParticipants, setShowParticipants] = useState(false);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [error, setError] = useState(null);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setBookingData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleParticipantChange = (index, value) => {
        const newParticipants = [...bookingData.participants];
        newParticipants[index] = value;
        setBookingData(prev => ({
            ...prev,
            participants: newParticipants
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);
        setError(null);

        try {
            // Preparar los datos para enviar al backend
            const participantsData = {};
            for (let i = 0; i < 15; i++) {
                participantsData[`participant${i+1}`] =
                    i < bookingData.quantity ? bookingData.participants[i] || null : null;
            }

            // Combinar fecha y hora
            const dateTime = `${bookingData.date}T${bookingData.time}:00`;

            const bookingPayload = {
                date: dateTime, // Enviamos fecha y hora combinadas
                type: parseInt(bookingData.type),
                quantity: parseInt(bookingData.quantity),
                rutWhoMadeBooking: bookingData.rutWhoMadeBooking,
                status: bookingData.status, // Incluir el status
                ...participantsData
            };

            // Enviar la reserva al backend
            const response = await fetch('http://localhost:8100/api/bookings/', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(bookingPayload)
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Error al crear la reserva');
            }

            const result = await response.json();
            console.log('Reserva creada:', result);
            navigate('/my-bookings', { state: { successMessage: 'Reserva creada exitosamente!' } });
        } catch (err) {
            console.error('Error:', err);
            setError(err.message || 'Ocurrió un error al crear la reserva');
        } finally {
            setIsSubmitting(false);
        }
    };

    const renderParticipantInputs = () => {
        const inputs = [];
        for (let i = 0; i < bookingData.quantity; i++) {
            inputs.push(
                <div key={i} className="input-group">
                    <label htmlFor={`participant-${i}`}>Participante {i + 1} (RUT):</label>
                    <input
                        type="text"
                        id={`participant-${i}`}
                        value={bookingData.participants[i] || ''}
                        onChange={(e) => handleParticipantChange(i, e.target.value)}
                        placeholder="Ingrese RUT (sin puntos ni guión)"
                        required={i < bookingData.quantity}
                    />
                </div>
            );
        }
        return inputs;
    };

    return (
        <div className="make-booking-container">
            <button
                onClick={() => navigate('/client-main')}
                className="back-button"
            >
                ← Volver
            </button>

            <h1>Crear Nueva Reserva</h1>

            {error && <div className="error-message">{error}</div>}

            <form onSubmit={handleSubmit} className="booking-form">
                <div className="input-group">
                    <label htmlFor="date">Fecha:</label>
                    <input
                        type="date"
                        id="date"
                        name="date"
                        value={bookingData.date}
                        onChange={handleChange}
                        min={new Date().toISOString().split('T')[0]} // Fecha mínima hoy
                        required
                    />
                </div>

                <div className="input-group">
                    <label htmlFor="time">Hora:</label>
                    <input
                        type="time"
                        id="time"
                        name="time"
                        min="10:00"
                        max="22:00"
                        value={bookingData.time}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="input-group">
                    <label htmlFor="type">Tipo de Reserva:</label>
                    <select
                        id="type"
                        name="type"
                        value={bookingData.type}
                        onChange={handleChange}
                        required
                    >
                        <option value="1">10 vueltas o máx 10 min</option>
                        <option value="2">15 vueltas o máx 25 min</option>
                        <option value="3">20 vueltas o máx 20 min</option>
                    </select>
                </div>

                <div className="input-group">
                    <label htmlFor="quantity">Cantidad de Personas (1-15):</label>
                    <input
                        type="number"
                        id="quantity"
                        name="quantity"
                        min="1"
                        max="15"
                        value={bookingData.quantity}
                        onChange={(e) => {
                            const value = Math.min(Math.max(parseInt(e.target.value || 1), 1), 15);
                            handleChange({ target: { name: 'quantity', value } });
                        }}
                        required
                    />
                    <button
                        type="button"
                        className="show-participants-btn"
                        onClick={() => setShowParticipants(!showParticipants)}
                    >
                        {showParticipants ? 'Ocultar Participantes' : 'Ingresar Participantes'}
                    </button>
                </div>

                <div className="input-group">
                    <label htmlFor="rutWhoMadeBooking">Tu RUT (Debes ingresarlo también en Participante 1):</label>
                    <input
                        type="text"
                        id="rutWhoMadeBooking"
                        name="rutWhoMadeBooking"
                        value={bookingData.rutWhoMadeBooking}
                        onChange={handleChange}
                        placeholder="Ingrese su RUT (sin puntos ni guión)"
                        required
                    />
                </div>

                {/* Campo oculto para el status */}
                <input type="hidden" name="status" value={bookingData.status} />

                {showParticipants && (
                    <div className="participants-container">
                        <h3>Datos de los Participantes</h3>
                        {renderParticipantInputs()}
                    </div>
                )}

                <div className="form-actions">
                    <button
                        type="submit"
                        className="submit-btn"
                        disabled={isSubmitting}
                    >
                        {isSubmitting ? 'Enviando...' : 'Confirmar Reserva'}
                    </button>
                    <button
                        type="button"
                        className="cancel-btn"
                        onClick={() => navigate('/client-main')}
                        disabled={isSubmitting}
                    >
                        Cancelar
                    </button>
                </div>
            </form>
        </div>
    );
};

export default MakeBooking;