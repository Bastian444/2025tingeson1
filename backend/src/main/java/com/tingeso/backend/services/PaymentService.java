package com.tingeso.backend.services;

import com.tingeso.backend.entities.bookingEntity;
import com.tingeso.backend.entities.PaymentEntity;
import com.tingeso.backend.entities.clientEntity;
import com.tingeso.backend.repositories.PaymentRepository;
import com.tingeso.backend.repositories.bookingRepository;
import com.tingeso.backend.repositories.clientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;


@Service
public class PaymentService {
    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    bookingRepository bookingRepo;

    @Autowired
    clientRepository clientRepo;

    public Optional<PaymentEntity> findPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    public Optional<PaymentEntity> findPaymentByBookingId(Long idBooking) {
        return paymentRepository.findByIdBooking(idBooking);
    }

    // Métodos públicos del servicio
    public List<PaymentEntity> getAllPayments() {
        return paymentRepository.findAll();
    }

    public PaymentEntity savePayment(PaymentEntity payment) {
        return paymentRepository.save(payment);
    }

    public PaymentEntity getPaymentById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    public PaymentEntity updatePayment(PaymentEntity payment) {
        return paymentRepository.save(payment);
    }

    public boolean deletePayment(Long id) throws Exception {
        try {
            paymentRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void calculateTotalAmount(PaymentEntity payment) {
        float total = 0f;

        // Sumamos todos los totalParticipantX que no sean cero
        if (payment.getTotalParticipant1() > 0) total += payment.getTotalParticipant1();
        if (payment.getTotalParticipant2() > 0) total += payment.getTotalParticipant2();
        if (payment.getTotalParticipant3() > 0) total += payment.getTotalParticipant3();
        if (payment.getTotalParticipant4() > 0) total += payment.getTotalParticipant4();
        if (payment.getTotalParticipant5() > 0) total += payment.getTotalParticipant5();
        if (payment.getTotalParticipant6() > 0) total += payment.getTotalParticipant6();
        if (payment.getTotalParticipant7() > 0) total += payment.getTotalParticipant7();
        if (payment.getTotalParticipant8() > 0) total += payment.getTotalParticipant8();
        if (payment.getTotalParticipant9() > 0) total += payment.getTotalParticipant9();
        if (payment.getTotalParticipant10() > 0) total += payment.getTotalParticipant10();
        if (payment.getTotalParticipant11() > 0) total += payment.getTotalParticipant11();
        if (payment.getTotalParticipant12() > 0) total += payment.getTotalParticipant12();
        if (payment.getTotalParticipant13() > 0) total += payment.getTotalParticipant13();
        if (payment.getTotalParticipant14() > 0) total += payment.getTotalParticipant14();
        if (payment.getTotalParticipant15() > 0) total += payment.getTotalParticipant15();

        payment.setTotalAmount(Math.round(total));
    }

    // Métodos de cálculo de descuentos (ahora accesibles a nivel de paquete)
    double calculateFrequentClientDiscount(int visits) {
        if (visits >= 7) return 0.30;
        if (visits >= 5) return 0.20;
        if (visits >= 2) return 0.10;
        return 0.0;
    }

    double calculateGroupDiscount(int participantCount) {
        if (participantCount >= 11 && participantCount <= 15) return 0.30;
        if (participantCount >= 6 && participantCount <= 10) return 0.20;
        if (participantCount >= 3 && participantCount <= 5) return 0.10;
        return 0.0;
    }

    int determinePrice(int type) {
        return switch (type) {
            case 1 -> 15000;
            case 2 -> 20000;
            case 3 -> 25000;
            default -> throw new IllegalArgumentException("Tipo de reserva inválido");
        };
    }

    int calculateBirthdayDiscountSlots(int participantCount) {
        if (participantCount >= 3 && participantCount <= 5) return 1;
        if (participantCount >= 6 && participantCount <= 10) return 2;
        return 0;
    }

    boolean isBirthday(String birthdate, String bookingDateTime) {
        if (birthdate == null || bookingDateTime == null) {
            return false;
        }

        try {
            // Eliminar cualquier parte de tiempo y espacios en blanco
            birthdate = birthdate.split("T")[0].trim();
            bookingDateTime = bookingDateTime.split("T")[0].trim();

            // Extraer mes y día de ambas fechas
            String[] birthParts = birthdate.split("-");
            String birthMonthDay = birthParts[1] + "-" + birthParts[2].substring(0, 2); // Asegurar solo día

            String[] bookingParts = bookingDateTime.split("-");
            String bookingMonthDay = bookingParts[1] + "-" + bookingParts[2].substring(0, 2);

            return birthMonthDay.equals(bookingMonthDay);
        } catch (Exception e) {
            System.err.println("Error al comparar fechas: " + e.getMessage());
            return false;
        }
    }

    // Metodo principal para crear pagos
    @Transactional
    public PaymentEntity createPaymentFromBooking(Long bookingId) {
        // 1. Validación y obtención de datos básicos
        bookingEntity booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada para ID: " + bookingId));

        clientEntity client = clientRepo.findByClientrut(booking.getRutWhoMadeBooking());
        if (client == null) {
            throw new RuntimeException("Cliente no encontrado con RUT: " + booking.getRutWhoMadeBooking());
        }

        // 2. Creación del pago base
        PaymentEntity payment = new PaymentEntity();
        payment.setIdBooking(booking.getId());
        payment.setTypeForPayment(booking.getType());
        payment.setQuantityForPayment(booking.getQuantity());

        // 3. Cálculo de precios base con descuentos de grupo y cliente frecuente
        double basePrice = determinePrice(booking.getType());
        double discountedPrice = basePrice * (1 - calculateGroupDiscount(booking.getQuantity()))
                * (1 - calculateFrequentClientDiscount(client.getVisits()));

        // 4. Aplicar descuentos por cumpleaños
        applyBirthdayDiscounts(booking, payment, discountedPrice);

        // 5. Aplicar IVA a cada participante individualmente
        applyIVA(payment);

        // 6. Calcular el totalAmount sumando todos los totalParticipantX (que ya incluyen IVA)
        calculateTotalAmount(payment);

        // 7. Guardar y retornar el pago
        return paymentRepository.save(payment);
    }

    // Métodos auxiliares (accesibles a nivel de paquete)
    double calculateFinalPrice(bookingEntity booking, clientEntity client) {
        int basePrice = determinePrice(booking.getType());
        double groupDiscount = calculateGroupDiscount(booking.getQuantity());
        double frequentDiscount = calculateFrequentClientDiscount(client.getVisits());
        return basePrice * (1 - groupDiscount) * (1 - frequentDiscount);
    }

    void applyBirthdayDiscounts(bookingEntity booking, PaymentEntity payment, double basePrice) {
        int birthdayDiscountSlots = calculateBirthdayDiscountSlots(booking.getQuantity());
        int birthdayDiscountsApplied = 0;
        double totalAmount = 0;

        // Primero verificar el cliente que hizo la reserva (tiene prioridad)
        clientEntity bookingClient = clientRepo.findByClientrut(booking.getRutWhoMadeBooking());
        boolean isBookingClientBirthday = bookingClient != null &&
                isBirthday(bookingClient.getBirthdate(), booking.getDate());

        if (isBookingClientBirthday && birthdayDiscountSlots > 0) {
            setParticipantPayment(payment, 1, booking.getRutWhoMadeBooking(), (float) (basePrice * 0.5));
            birthdayDiscountsApplied++;
        } else {
            setParticipantPayment(payment, 1, booking.getRutWhoMadeBooking(), (float) basePrice);
        }

        // Luego verificar otros participantes
        for (int i = 2; i <= 15; i++) {
            String participantRut = getParticipantRut(booking, i);
            if (participantRut == null || participantRut.isEmpty()) {
                setParticipantPayment(payment, i, null, 0);
                continue;
            }

            double finalPrice = basePrice;

            if (birthdayDiscountsApplied < birthdayDiscountSlots) {
                clientEntity participantClient = clientRepo.findByClientrut(participantRut);
                if (participantClient != null && isBirthday(participantClient.getBirthdate(), booking.getDate())) {
                    finalPrice = basePrice * 0.5;
                    birthdayDiscountsApplied++;
                }
            }

            setParticipantPayment(payment, i, participantRut, (float) finalPrice);
            totalAmount += finalPrice;
        }

        payment.setTotalAmount((int) Math.round(totalAmount));
    }

    String getParticipantRut(bookingEntity booking, int number) {
        try {
            Method getter = booking.getClass().getMethod("getParticipant" + number);
            return (String) getter.invoke(booking);
        } catch (Exception e) {
            return null;
        }
    }

    void setParticipantPayment(PaymentEntity payment, int number, String rut, float amount) {
        try {
            Method setRut = payment.getClass().getMethod("setParticipant" + number + "Payment", String.class);
            Method setAmount = payment.getClass().getMethod("setTotalParticipant" + number, float.class);
            setRut.invoke(payment, rut);
            setAmount.invoke(payment, amount);
        } catch (Exception e) {
            throw new RuntimeException("Error al asignar pago al participante " + number, e);
        }
    }

    void applyIVA(PaymentEntity payment) {
        double iva = payment.getTotalAmount() * 0.19;
        for (int i = 1; i <= 15; i++) {
            Float currentAmount = getParticipantAmount(payment, i);
            if (currentAmount != null && currentAmount > 0) {
                setParticipantAmount(payment, i, currentAmount * 1.19f);
            }
        }
        payment.setTotalAmount((int) Math.round(payment.getTotalAmount() * 1.19));
    }

    Float getParticipantAmount(PaymentEntity payment, int number) {
        try {
            Method getter = payment.getClass().getMethod("getTotalParticipant" + number);
            return (Float) getter.invoke(payment);
        } catch (Exception e) {
            return null;
        }
    }

    void setParticipantAmount(PaymentEntity payment, int number, float amount) {
        try {
            Method setter = payment.getClass().getMethod("setTotalParticipant" + number, float.class);
            setter.invoke(payment, amount);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar monto del participante " + number, e);
        }
    }




}