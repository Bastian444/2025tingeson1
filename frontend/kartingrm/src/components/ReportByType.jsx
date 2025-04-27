import React, { useState, useEffect } from 'react';
import {
    Box,
    Typography,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    Grid,
    Card,
    CardContent,
    CircularProgress
} from '@mui/material';
import axios from 'axios';
import './ReportByType.css';

const ReportByType = () => {
    // Estados para los pagos
    const [payments, setPayments] = useState([]);
    const [loadingPayments, setLoadingPayments] = useState(true);
    const [paymentError, setPaymentError] = useState(null);

    // Estados para las reservas (bookings)
    const [bookings, setBookings] = useState([]);
    const [loadingBookings, setLoadingBookings] = useState(true);
    const [bookingError, setBookingError] = useState(null);

    // Cargar datos de pagos
    useEffect(() => {
        const fetchPayments = async () => {
            try {
                const response = await axios.get('http://localhost:8100/api/payment/');
                setPayments(Array.isArray(response.data) ? response.data : []);
                setLoadingPayments(false);
            } catch (err) {
                setPaymentError(err.message);
                setLoadingPayments(false);
            }
        };

        fetchPayments();
    }, []);

    // Cargar datos de reservas
    useEffect(() => {
        const fetchBookings = async () => {
            try {
                const response = await axios.get('http://localhost:8100/api/bookings/');
                setBookings(Array.isArray(response.data) ? response.data : []);
                setLoadingBookings(false);
            } catch (err) {
                setBookingError(err.message);
                setLoadingBookings(false);
            }
        };

        fetchBookings();
    }, []);

    // Agrupar pagos por tipo
    const groupPaymentsByType = () => {
        const groups = {};

        payments.forEach(payment => {
            const key = payment.typeForPayment;

            if (!groups[key]) {
                groups[key] = {
                    count: 0,
                    totalAmount: 0,
                    type: payment.typeForPayment
                };
            }

            groups[key].count += 1;
            groups[key].totalAmount += payment.totalAmount;
        });

        return groups;
    };

    // Agrupar reservas por mes y tipo
    const groupBookingsByMonthAndType = () => {
        const months = [
            'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
            'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
        ];

        const result = {};

        bookings.forEach(booking => {
            if (!booking.date) return;

            // Extraer mes y año de la fecha (formato: '2025-04-30T16:30:00')
            const date = new Date(booking.date);
            const month = date.getMonth(); // 0-11
            const year = date.getFullYear();
            const monthYear = `${months[month]} ${year}`;

            if (!result[monthYear]) {
                result[monthYear] = {
                    month: month + 1, // Para ordenar
                    year,
                    types: {
                        1: { count: 0, label: '10 vueltas/máx 10 min' },
                        2: { count: 0, label: '15 vueltas/máx 15 min' },
                        3: { count: 0, label: '20 vueltas/máx 20 min' }
                    }
                };
            }

            // Incrementar el contador para el tipo de reserva
            if (booking.type && result[monthYear].types[booking.type]) {
                result[monthYear].types[booking.type].count += 1;
            }
        });

        // Ordenar por año y mes
        return Object.entries(result)
            .sort((a, b) => {
                if (a[1].year !== b[1].year) return b[1].year - a[1].year;
                return b[1].month - a[1].month;
            })
            .reduce((acc, [key, value]) => {
                acc[key] = value;
                return acc;
            }, {});
    };

    const getTypeDescription = (type) => {
        switch(type) {
            case 1: return '10 vueltas/máx 10 min ($15,000)';
            case 2: return '15 vueltas/máx 15 min ($20,000)';
            case 3: return '20 vueltas/máx 20 min ($25,000)';
            default: return `Tipo ${type}`;
        }
    };

    const paymentGroups = groupPaymentsByType();
    const paymentGroupKeys = Object.keys(paymentGroups).sort();
    const bookingGroups = groupBookingsByMonthAndType();
    const bookingGroupKeys = Object.keys(bookingGroups);

    if (loadingPayments || loadingBookings) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', p: 3 }}>
                <CircularProgress />
            </Box>
        );
    }

    if (paymentError || bookingError) {
        return (
            <Box sx={{ p: 3 }}>
                {paymentError && <Typography color="error">Error en pagos: {paymentError}</Typography>}
                {bookingError && <Typography color="error">Error en reservas: {bookingError}</Typography>}
            </Box>
        );
    }

    return (
        <Box sx={{ p: 3 }}>
            <Typography variant="h4" gutterBottom>
                Reporte de Ingresos y Reservas por Tipo
            </Typography>

            {/* Sección de Pagos */}
            <Typography variant="h5" sx={{ mt: 3, mb: 2 }}>
                Estadísticas de Pagos por Tipo de Tarifa
            </Typography>
            <Grid container spacing={3} sx={{ mb: 3 }}>
                <Grid item xs={12} md={6}>
                    <Card className="summary-card">
                        <CardContent className="summary-card-content">
                            <Typography className="summary-card-title" variant="h6">Total de Ingresos</Typography>
                            <Typography className="summary-card-value" variant="h4">
                                ${payments.reduce((sum, payment) => sum + (payment.totalAmount || 0), 0).toLocaleString()}
                            </Typography>
                        </CardContent>
                    </Card>
                </Grid>
                <Grid item xs={12} md={6}>
                    <Card className="summary-card">
                        <CardContent className="summary-card-content">
                            <Typography className="summary-card-title" variant="h6">Total de Reservas</Typography>
                            <Typography className="summary-card-value" variant="h4">{payments.length}</Typography>
                        </CardContent>
                    </Card>
                </Grid>
            </Grid>

            {payments.length > 0 ? (
                <TableContainer component={Paper} className="report-table" sx={{ mb: 4 }}>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>Tipo de Tarifa</TableCell>
                                <TableCell align="right">Cantidad de Reservas</TableCell>
                                <TableCell align="right">Total Recaudado</TableCell>
                                <TableCell align="right">Promedio por Reserva</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {paymentGroupKeys.map(key => (
                                <TableRow key={key}>
                                    <TableCell>{getTypeDescription(paymentGroups[key].type)}</TableCell>
                                    <TableCell align="right">{paymentGroups[key].count}</TableCell>
                                    <TableCell align="right">${paymentGroups[key].totalAmount.toLocaleString()}</TableCell>
                                    <TableCell align="right">
                                        ${Math.round(paymentGroups[key].totalAmount / paymentGroups[key].count).toLocaleString()}
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            ) : (
                <Typography sx={{ p: 2 }}>No hay datos de pagos disponibles</Typography>
            )}

            {/* Sección de Reservas por Mes */}
            <Typography variant="h5" sx={{ mt: 3, mb: 2 }}>
                Estadísticas de Reservas por Mes
            </Typography>

            {bookings.length > 0 ? (
                <TableContainer component={Paper} className="report-table">
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>Mes</TableCell>
                                <TableCell align="right">10 vueltas/máx 10 min</TableCell>
                                <TableCell align="right">15 vueltas/máx 15 min</TableCell>
                                <TableCell align="right">20 vueltas/máx 20 min</TableCell>
                                <TableCell align="right">Total</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {bookingGroupKeys.map(monthYear => {
                                const monthData = bookingGroups[monthYear];
                                const total = Object.values(monthData.types).reduce((sum, type) => sum + type.count, 0);

                                return (
                                    <TableRow key={monthYear}>
                                        <TableCell>{monthYear}</TableCell>
                                        <TableCell align="right">{monthData.types[1].count}</TableCell>
                                        <TableCell align="right">{monthData.types[2].count}</TableCell>
                                        <TableCell align="right">{monthData.types[3].count}</TableCell>
                                        <TableCell align="right">{total}</TableCell>
                                    </TableRow>
                                );
                            })}
                        </TableBody>
                    </Table>
                </TableContainer>
            ) : (
                <Typography sx={{ p: 2 }}>No hay datos de reservas disponibles</Typography>
            )}
        </Box>
    );
};

export default ReportByType;