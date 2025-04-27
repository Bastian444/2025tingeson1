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

const ReportByQuantity = () => {
    const [payments, setPayments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchPayments = async () => {
            try {
                const response = await axios.get('http://localhost:8100/api/payment/');
                setPayments(Array.isArray(response.data) ? response.data : []);
                setLoading(false);
            } catch (err) {
                setError(err.message);
                setLoading(false);
            }
        };

        fetchPayments();
    }, []);

    // Agrupar pagos por rangos de cantidad de personas
    const groupPaymentsByQuantity = () => {
        const groups = {
            '1-3': { count: 0, totalAmount: 0, min: 1, max: 3 },
            '4-6': { count: 0, totalAmount: 0, min: 4, max: 6 },
            '7-9': { count: 0, totalAmount: 0, min: 7, max: 9 },
            '10+': { count: 0, totalAmount: 0, min: 10, max: Infinity }
        };

        payments.forEach(payment => {
            const quantity = payment.quantityForPayment;

            if (quantity >= 1 && quantity <= 3) {
                groups['1-3'].count += 1;
                groups['1-3'].totalAmount += payment.totalAmount;
            } else if (quantity >= 4 && quantity <= 6) {
                groups['4-6'].count += 1;
                groups['4-6'].totalAmount += payment.totalAmount;
            } else if (quantity >= 7 && quantity <= 9) {
                groups['7-9'].count += 1;
                groups['7-9'].totalAmount += payment.totalAmount;
            } else if (quantity >= 10) {
                groups['10+'].count += 1;
                groups['10+'].totalAmount += payment.totalAmount;
            }
        });

        return groups;
    };

    const groups = groupPaymentsByQuantity();
    const groupKeys = Object.keys(groups);

    if (loading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', p: 3 }}>
                <CircularProgress />
            </Box>
        );
    }

    if (error) {
        return (
            <Box sx={{ p: 3 }}>
                <Typography color="error">Error al cargar los datos: {error}</Typography>
            </Box>
        );
    }

    return (
        <Box sx={{ p: 3 }}>
            <Typography variant="h4" gutterBottom>
                Reporte de Ingresos por Cantidad de Personas
            </Typography>

            <Typography variant="body1" sx={{ mb: 3 }}>
                Este reporte organiza los ingresos según el tamaño de los grupos que realizan reservas,
                brindando información clave sobre cómo las reservas grupales contribuyen al total de ingresos.
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
                <TableContainer component={Paper} className="report-table">
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>Rango de Personas</TableCell>
                                <TableCell align="right">Cantidad de Reservas</TableCell>
                                <TableCell align="right">Total Recaudado</TableCell>
                                <TableCell align="right">Porcentaje del Total</TableCell>
                                <TableCell align="right">Promedio por Reserva</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {groupKeys.map(key => {
                                const group = groups[key];
                                const totalAmount = payments.reduce((sum, payment) => sum + (payment.totalAmount || 0), 0);
                                const percentage = totalAmount > 0 ? (group.totalAmount / totalAmount * 100).toFixed(1) : 0;

                                return (
                                    <TableRow key={key}>
                                        <TableCell>
                                            {key === '10+' ? '10 o más personas' : `${key} personas`}
                                        </TableCell>
                                        <TableCell align="right">{group.count}</TableCell>
                                        <TableCell align="right">${group.totalAmount.toLocaleString()}</TableCell>
                                        <TableCell align="right">{percentage}%</TableCell>
                                        <TableCell align="right">
                                            ${group.count > 0 ? Math.round(group.totalAmount / group.count).toLocaleString() : 0}
                                        </TableCell>
                                    </TableRow>
                                );
                            })}

                            {/* Fila de totales */}
                            <TableRow sx={{ backgroundColor: '#f5f5f5' }}>
                                <TableCell><strong>Total</strong></TableCell>
                                <TableCell align="right"><strong>{payments.length}</strong></TableCell>
                                <TableCell align="right">
                                    <strong>${payments.reduce((sum, payment) => sum + (payment.totalAmount || 0), 0).toLocaleString()}</strong>
                                </TableCell>
                                <TableCell align="right"><strong>100%</strong></TableCell>
                                <TableCell align="right">
                                    <strong>${payments.length > 0 ? Math.round(payments.reduce((sum, payment) => sum + (payment.totalAmount || 0), 0) / payments.length).toLocaleString() : 0}</strong>
                                </TableCell>
                            </TableRow>
                        </TableBody>
                    </Table>
                </TableContainer>
            ) : (
                <Typography sx={{ p: 2 }}>No hay datos de pagos disponibles</Typography>
            )}
        </Box>
    );
};

export default ReportByQuantity;