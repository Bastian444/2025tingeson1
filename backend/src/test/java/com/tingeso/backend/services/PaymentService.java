package com.tingeso.backend.services;

import com.tingeso.backend.entities.PaymentEntity;
import com.tingeso.backend.entities.bookingEntity;
import com.tingeso.backend.entities.clientEntity;
import com.tingeso.backend.repositories.PaymentRepository;
import com.tingeso.backend.repositories.bookingRepository;
import com.tingeso.backend.repositories.clientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private bookingRepository bookingRepo;

    @Mock
    private clientRepository clientRepo;

    @InjectMocks
    private PaymentService paymentService;

    private bookingEntity testBooking;
    private clientEntity testClient;
    private PaymentEntity testPayment;

    @BeforeEach
    void setUp() throws Exception {
        // Configuración de booking de prueba
        testBooking = new bookingEntity();
        testBooking.setId(1L);
        testBooking.setType(2); // Tipo 2: $20,000
        testBooking.setQuantity(5);
        testBooking.setRutWhoMadeBooking("12345678-9");
        testBooking.setDate("2023-12-15T14:30:00");

        // Configuración de cliente de prueba
        testClient = new clientEntity();
        testClient.setClientrut("12345678-9");
        testClient.setVisits(5); // Cliente frecuente (20% descuento)
        testClient.setBirthdate("1990-05-15");

        // Configuración de pago de prueba
        testPayment = new PaymentEntity();
        testPayment.setId(1L);
        testPayment.setIdBooking(1L);
        testPayment.setTypeForPayment(2);
        testPayment.setQuantityForPayment(5);
        testPayment.setTotalAmount(20000);
    }

    @Test
    void getAllPayments_ShouldReturnAllPayments() {
        // Arrange
        PaymentEntity payment2 = new PaymentEntity();
        payment2.setId(2L);

        List<PaymentEntity> payments = Arrays.asList(testPayment, payment2);
        when(paymentRepository.findAll()).thenReturn(payments);

        // Act
        List<PaymentEntity> result = paymentService.getAllPayments();

        // Assert
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    void savePayment_ShouldSavePayment() {
        // Arrange
        when(paymentRepository.save(testPayment)).thenReturn(testPayment);

        // Act
        PaymentEntity result = paymentService.savePayment(testPayment);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(paymentRepository, times(1)).save(testPayment);
    }

    @Test
    void getPaymentById_ShouldReturnPaymentWhenExists() {
        // Arrange
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));

        // Act
        PaymentEntity result = paymentService.getPaymentById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(paymentRepository, times(1)).findById(1L);
    }

    @Test
    void getPaymentById_ShouldReturnNullWhenNotExists() {
        // Arrange
        when(paymentRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        PaymentEntity result = paymentService.getPaymentById(99L);

        // Assert
        assertNull(result);
        verify(paymentRepository, times(1)).findById(99L);
    }

    @Test
    void updatePayment_ShouldUpdatePayment() {
        // Arrange
        testPayment.setTotalAmount(25000);
        when(paymentRepository.save(testPayment)).thenReturn(testPayment);

        // Act
        PaymentEntity result = paymentService.updatePayment(testPayment);

        // Assert
        assertEquals(25000, result.getTotalAmount());
        verify(paymentRepository, times(1)).save(testPayment);
    }

    @Test
    void deletePayment_ShouldReturnTrueWhenSuccess() throws Exception {
        // Arrange
        doNothing().when(paymentRepository).deleteById(1L);

        // Act
        boolean result = paymentService.deletePayment(1L);

        // Assert
        assertTrue(result);
        verify(paymentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePayment_ShouldThrowExceptionWhenError() {
        // Arrange
        doThrow(new RuntimeException("Database error")).when(paymentRepository).deleteById(1L);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            paymentService.deletePayment(1L);
        });

        assertEquals("Database error", exception.getMessage());
        verify(paymentRepository, times(1)).deleteById(1L);
    }

    @Test
    void calculateFrequentClientDiscount_ShouldReturnCorrectDiscounts() {
        assertEquals(0.30, paymentService.calculateFrequentClientDiscount(7)); // 30%
        assertEquals(0.20, paymentService.calculateFrequentClientDiscount(5)); // 20%
        assertEquals(0.10, paymentService.calculateFrequentClientDiscount(2)); // 10%
        assertEquals(0.0, paymentService.calculateFrequentClientDiscount(1));  // 0%
    }

    @Test
    void calculateGroupDiscount_ShouldReturnCorrectDiscounts() {
        assertEquals(0.30, paymentService.calculateGroupDiscount(12)); // 30%
        assertEquals(0.20, paymentService.calculateGroupDiscount(8));  // 20%
        assertEquals(0.10, paymentService.calculateGroupDiscount(4));  // 10%
        assertEquals(0.0, paymentService.calculateGroupDiscount(2));   // 0%
    }

    @Test
    void determinePrice_ShouldReturnCorrectPrices() {
        assertEquals(15000, paymentService.determinePrice(1));
        assertEquals(20000, paymentService.determinePrice(2));
        assertEquals(25000, paymentService.determinePrice(3));
        assertThrows(IllegalArgumentException.class, () -> paymentService.determinePrice(4));
    }

    @Test
    void calculateBirthdayDiscountSlots_ShouldReturnCorrectSlots() {
        assertEquals(1, paymentService.calculateBirthdayDiscountSlots(4));
        assertEquals(2, paymentService.calculateBirthdayDiscountSlots(8));
        assertEquals(0, paymentService.calculateBirthdayDiscountSlots(2));
    }

    @Test
    void isBirthday_ShouldReturnTrueWhenBirthdayMatches() {
        assertTrue(paymentService.isBirthday("1990-05-15", "2023-05-15T14:30:00"));
        assertFalse(paymentService.isBirthday("1990-05-15", "2023-06-15T14:30:00"));
    }

    @Test
    void createPaymentFromBooking_ShouldCalculateCorrectAmounts() throws Exception {
        // Arrange
        Long bookingId = 1L;

        // Configurar booking de prueba
        bookingEntity booking = new bookingEntity();
        booking.setId(bookingId);
        booking.setType(2); // Tipo 2: $20,000
        booking.setQuantity(5);
        booking.setRutWhoMadeBooking("12345678-9");
        booking.setDate("2023-12-15T14:30:00");

        // Configurar participantes
        Method setParticipant1 = bookingEntity.class.getMethod("setParticipant1", String.class);
        setParticipant1.invoke(booking, "11111111-1"); // Cumpleañero
        Method setParticipant2 = bookingEntity.class.getMethod("setParticipant2", String.class);
        setParticipant2.invoke(booking, "22222222-2");

        // Configurar cliente principal
        clientEntity mainClient = new clientEntity();
        mainClient.setClientrut("12345678-9");
        mainClient.setVisits(5); // Cliente frecuente (20% descuento)
        mainClient.setBirthdate("1990-05-15");

        // Configurar cliente cumpleañero
        clientEntity birthdayClient = new clientEntity();
        birthdayClient.setClientrut("11111111-1");
        birthdayClient.setBirthdate("1990-12-15"); // Mismo día y mes que la reserva

        // Mockear repositorios
        when(bookingRepo.findById(bookingId)).thenReturn(Optional.of(booking));
        when(clientRepo.findByClientrut("12345678-9")).thenReturn(mainClient);
        when(clientRepo.findByClientrut("11111111-1")).thenReturn(birthdayClient);
        when(paymentRepository.save(any(PaymentEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        PaymentEntity result = paymentService.createPaymentFromBooking(bookingId);

        // Assert
        assertNotNull(result, "El pago no debería ser nulo");
        assertEquals(bookingId, result.getIdBooking(), "El ID de reserva no coincide");
        assertEquals(2, result.getTypeForPayment(), "El tipo de pago no coincide");
        assertEquals(5, result.getQuantityForPayment(), "La cantidad de participantes no coincide");

        // Verificar que se aplicaron los descuentos correctamente
        assertTrue(result.getTotalAmount() > 0, "El monto total debe ser mayor que cero");

        // Verificar que se llamó al método save
        verify(paymentRepository, times(1)).save(any(PaymentEntity.class));
    }

    @Test
    void findPaymentById_ShouldReturnPaymentWhenExists() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));
        Optional<PaymentEntity> result = paymentService.findPaymentById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void findPaymentById_ShouldReturnEmptyWhenNotExists() {
        when(paymentRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<PaymentEntity> result = paymentService.findPaymentById(99L);
        assertFalse(result.isPresent());
    }

    @Test
    void createPaymentFromBooking_ShouldThrowWhenBookingNotFound() {
        when(bookingRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> paymentService.createPaymentFromBooking(99L));
    }

    @Test
    void calculateFinalPrice_ShouldApplyBothDiscounts() {
        bookingEntity booking = new bookingEntity();
        booking.setType(2); // $20,000
        booking.setQuantity(8); // 20% group discount

        clientEntity client = new clientEntity();
        client.setVisits(5); // 20% frequent discount

        // $20,000 * 0.8 (group) * 0.8 (frequent) = $12,800
        assertEquals(12800, paymentService.calculateFinalPrice(booking, client));
    }

    @Test
    void applyBirthdayDiscounts_ShouldNotExceedMaxSlots() throws Exception {
        // Configurar booking con fecha en diciembre (para que coincida con el cumpleaños)
        bookingEntity booking = new bookingEntity();
        booking.setQuantity(4); // 1 birthday slot
        booking.setDate("2023-12-15T14:30:00"); // ¡IMPORTANTE! Mismo mes/día que el cumpleaños

        // Configurar 3 participantes que cumplen años
        for (int i = 1; i <= 3; i++) {
            String rut = "11111111-" + i;
            Method setter = bookingEntity.class.getMethod("setParticipant" + i, String.class);
            setter.invoke(booking, rut);
        }

        PaymentEntity payment = new PaymentEntity();

        // Mockear clientRepo para devolver clientes con cumpleaños en diciembre
        when(clientRepo.findByClientrut(anyString())).thenAnswer(invocation -> {
            String rut = invocation.getArgument(0);
            clientEntity client = new clientEntity();
            client.setBirthdate("1990-12-15"); // Mismo día/mes que la reserva
            return client;
        });

        paymentService.applyBirthdayDiscounts(booking, payment, 10000);

        // Verificar que solo 1 participante tiene descuento (50%)
        int discounted = 0;
        for (int i = 1; i <= 3; i++) {
            Method getter = PaymentEntity.class.getMethod("getTotalParticipant" + i);
            Float amount = (Float) getter.invoke(payment);
            if (amount != null && amount == 5000) {
                discounted++;
            }
        }
        assertEquals(1, discounted, "Debería haber exactamente 1 participante con descuento por cumpleaños");
    }

    @Test
    void applyIVA_ShouldCorrectlyApplyTax() throws Exception {
        // Arrange
        testPayment.setTotalAmount(10000);

        // Configurar montos de participantes
        for (int i = 1; i <= 3; i++) {
            Method setAmount = PaymentEntity.class.getMethod("setTotalParticipant" + i, float.class);
            setAmount.invoke(testPayment, 1000.0f);
        }

        // Act
        paymentService.applyIVA(testPayment);

        // Assert
        assertEquals(11900, testPayment.getTotalAmount());
        for (int i = 1; i <= 3; i++) {
            Method getAmount = PaymentEntity.class.getMethod("getTotalParticipant" + i);
            float amount = (float) getAmount.invoke(testPayment);
            assertEquals(1190.0f, amount, 0.01);
        }
    }

    @Test
    void testFindPaymentByBookingIdEmpty() {
        when(paymentRepository.findByIdBooking(anyLong())).thenReturn(Optional.empty());

        Optional<PaymentEntity> result = paymentService.findPaymentByBookingId(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void testIsBirthday_NullBirthdateOrBookingDateTime() {
        boolean result1 = paymentService.isBirthday(null, "2024-04-28T00:00:00");
        boolean result2 = paymentService.isBirthday("2000-01-01T00:00:00", null);

        assertFalse(result1);
        assertFalse(result2);
    }

    @Test
    void testGetParticipantRut_Exception() {
        bookingEntity booking = new bookingEntity(); // Este objeto no tiene los métodos dinámicos.
        String result = paymentService.getParticipantRut(booking, 99); // Llama a getParticipant99 (no existe).

        assertNull(result);
    }

    @Test
    void testSetParticipantPayment_Exception() {
        PaymentEntity payment = new PaymentEntity();

        // Para forzar la excepción, usamos un número inválido.
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            paymentService.setParticipantPayment(payment, 99, "12345678-9", 10000f);
        });

        assertTrue(exception.getMessage().contains("Error al asignar pago al participante"));
    }

    @Test
    void testGetParticipantAmount_Exception() {
        PaymentEntity payment = new PaymentEntity();

        Float result = paymentService.getParticipantAmount(payment, 99); // No existe el método getTotalParticipant99

        assertNull(result);
    }

    @Test
    void testSetParticipantAmount_Exception() {
        PaymentEntity payment = new PaymentEntity();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            paymentService.setParticipantAmount(payment, 99, 15000f);
        });

        assertTrue(exception.getMessage().contains("Error al actualizar monto del participante"));
    }

}