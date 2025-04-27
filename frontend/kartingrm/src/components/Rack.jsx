import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './Rack.css';

const Rack = () => {
    const navigate = useNavigate();
    const [bookings, setBookings] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [currentWeek, setCurrentWeek] = useState(new Date());

    useEffect(() => {
        const fetchBookings = async () => {
            try {
                const response = await fetch('http://localhost:8100/api/bookings/', {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('staffToken')}`
                    }
                });

                if (!response.ok) throw new Error('Error al cargar reservas');

                const data = await response.json();
                setBookings(data);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchBookings();
    }, []);

    const getWeekDays = () => {
        const days = [];
        const startDate = new Date(currentWeek);
        startDate.setDate(startDate.getDate() - startDate.getDay() + 1);

        for (let i = 0; i < 7; i++) {
            const day = new Date(startDate);
            day.setDate(day.getDate() + i);
            days.push(day);
        }

        return days;
    };

    const formatDate = (date) => {
        return date.toLocaleDateString('es-CL', { day: '2-digit', month: '2-digit' });
    };

    const extractTimeFromISO = (isoString) => {
        if (!isoString) return null;
        try {
            // Extrae "HH:MM:SS" de "YYYY-MM-DDTHH:MM:SS"
            const timePart = isoString.split('T')[1];
            return timePart.substring(0, 5); // Retorna solo "HH:MM"
        } catch (e) {
            return null;
        }
    };

    const getBookingsForDay = (day) => {
        const dayStr = day.toISOString().split('T')[0];
        return bookings.filter(booking => {
            try {
                if (!booking.date) return false;
                const bookingDate = new Date(booking.date);
                return bookingDate.toISOString().split('T')[0] === dayStr;
            } catch (e) {
                console.error("Error procesando fecha:", booking.date);
                return false;
            }
        }).sort((a, b) => {
            // Ordenar por hora extraída del campo date
            const timeA = extractTimeFromISO(a.date) || "";
            const timeB = extractTimeFromISO(b.date) || "";
            return timeA.localeCompare(timeB);
        });
    };

    const prevWeek = () => {
        const newWeek = new Date(currentWeek);
        newWeek.setDate(newWeek.getDate() - 7);
        setCurrentWeek(newWeek);
    };

    const nextWeek = () => {
        const newWeek = new Date(currentWeek);
        newWeek.setDate(newWeek.getDate() + 7);
        setCurrentWeek(newWeek);
    };

    if (loading) return <div className="loading">Cargando reservas...</div>;
    if (error) return <div className="error">Error: {error}</div>;

    return (
        <div className="rack-container">
            <button onClick={() => navigate(-1)} className="back-button">
                ← Volver
            </button>

            <div className="week-navigation">
                <button onClick={prevWeek} className="nav-button">← Semana anterior</button>
                <h2>Semana del {formatDate(getWeekDays()[0])} al {formatDate(getWeekDays()[6])}</h2>
                <button onClick={nextWeek} className="nav-button">Siguiente semana →</button>
            </div>

            <div className="week-grid">
                {getWeekDays().map((day, index) => {
                    const dayBookings = getBookingsForDay(day);
                    const dayName = day.toLocaleDateString('es-CL', { weekday: 'short' });

                    return (
                        <div key={index} className="day-column">
                            <div className="day-header">
                                <span className="day-name">{dayName}</span>
                                <span className="day-date">{formatDate(day)}</span>
                            </div>

                            <div className="bookings-container">
                                {dayBookings.length > 0 ? (
                                    dayBookings.map(booking => {
                                        const bookingTime = extractTimeFromISO(booking.date);
                                        return (
                                            <div key={booking.id} className="booking-card">
                                                <div className="booking-time">
                                                    {bookingTime || "Sin hora"}
                                                </div>
                                                <div className="booking-details">
                                                    <span className="booking-type">
                                                        {booking.type === 1 ? '10 min/vueltas' :
                                                            booking.type === 2 ? '15 min/vueltas' :
                                                                booking.type === 3 ? '20 min/vueltas' : `Tipo ${booking.type}`}
                                                    </span>
                                                    <span className="booking-participants">
                                                        Participantes: {booking.quantity}
                                                    </span>
                                                    <span className="booking-rut">
                                                        RUT: {booking.rutWhoMadeBooking}
                                                    </span>
                                                </div>
                                            </div>
                                        );
                                    })
                                ) : (
                                    <div className="no-bookings">Sin reservas</div>
                                )}
                            </div>
                        </div>
                    );
                })}
            </div>
        </div>
    );
};

export default Rack;