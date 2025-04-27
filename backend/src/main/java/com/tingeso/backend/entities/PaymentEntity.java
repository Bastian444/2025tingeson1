package com.tingeso.backend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "payments")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idBooking;
    private int typeForPayment;
    // 1 -> 15000; // 10 vueltas/máx 10 min
    // 2 -> 20000; // 15 vueltas/máx 15 min
    // 3 -> 25000; // 20 vueltas/máx 20 min
    private int quantityForPayment;
    // cantidad de gente que participará en la reserva
    private int totalAmount;

    // RUT DEL PARTICIPANTE
    private String participant1Payment;
    private String participant2Payment;
    private String participant3Payment;
    private String participant4Payment;
    private String participant5Payment;
    private String participant6Payment;
    private String participant7Payment;
    private String participant8Payment;
    private String participant9Payment;
    private String participant10Payment;
    private String participant11Payment;
    private String participant12Payment;
    private String participant13Payment;
    private String participant14Payment;
    private String participant15Payment;

    // MONTO QUE DEBE PAGAR EL PARTICIPANTE
    private float totalParticipant1;
    private float totalParticipant2;
    private float totalParticipant3;
    private float totalParticipant4;
    private float totalParticipant5;
    private float totalParticipant6;
    private float totalParticipant7;
    private float totalParticipant8;
    private float totalParticipant9;
    private float totalParticipant10;
    private float totalParticipant11;
    private float totalParticipant12;
    private float totalParticipant13;
    private float totalParticipant14;
    private float totalParticipant15;

    // Constructor vacío
    public PaymentEntity() {
    }

    // Constructor con parámetros
    public PaymentEntity(Long id, Long idBooking, int typeForPayment, int quantityForPayment, int totalAmount,
                         String participant1Payment, String participant2Payment, String participant3Payment,
                         String participant4Payment, String participant5Payment, String participant6Payment,
                         String participant7Payment, String participant8Payment, String participant9Payment,
                         String participant10Payment, String participant11Payment, String participant12Payment,
                         String participant13Payment, String participant14Payment, String participant15Payment,
                         float totalParticipant1, float totalParticipant2, float totalParticipant3,
                         float totalParticipant4, float totalParticipant5, float totalParticipant6,
                         float totalParticipant7, float totalParticipant8, float totalParticipant9,
                         float totalParticipant10, float totalParticipant11, float totalParticipant12,
                         float totalParticipant13, float totalParticipant14, float totalParticipant15) {
        this.id = id;
        this.idBooking = idBooking;
        this.typeForPayment = typeForPayment;
        this.quantityForPayment = quantityForPayment;
        this.totalAmount = totalAmount;
        this.participant1Payment = participant1Payment;
        this.participant2Payment = participant2Payment;
        this.participant3Payment = participant3Payment;
        this.participant4Payment = participant4Payment;
        this.participant5Payment = participant5Payment;
        this.participant6Payment = participant6Payment;
        this.participant7Payment = participant7Payment;
        this.participant8Payment = participant8Payment;
        this.participant9Payment = participant9Payment;
        this.participant10Payment = participant10Payment;
        this.participant11Payment = participant11Payment;
        this.participant12Payment = participant12Payment;
        this.participant13Payment = participant13Payment;
        this.participant14Payment = participant14Payment;
        this.participant15Payment = participant15Payment;
        this.totalParticipant1 = totalParticipant1;
        this.totalParticipant2 = totalParticipant2;
        this.totalParticipant3 = totalParticipant3;
        this.totalParticipant4 = totalParticipant4;
        this.totalParticipant5 = totalParticipant5;
        this.totalParticipant6 = totalParticipant6;
        this.totalParticipant7 = totalParticipant7;
        this.totalParticipant8 = totalParticipant8;
        this.totalParticipant9 = totalParticipant9;
        this.totalParticipant10 = totalParticipant10;
        this.totalParticipant11 = totalParticipant11;
        this.totalParticipant12 = totalParticipant12;
        this.totalParticipant13 = totalParticipant13;
        this.totalParticipant14 = totalParticipant14;
        this.totalParticipant15 = totalParticipant15;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdBooking() {
        return idBooking;
    }

    public void setIdBooking(Long idBooking) {
        this.idBooking = idBooking;
    }

    public int getTypeForPayment() {
        return typeForPayment;
    }

    public void setTypeForPayment(int typeForPayment) {
        this.typeForPayment = typeForPayment;
    }

    public int getQuantityForPayment() {
        return quantityForPayment;
    }

    public void setQuantityForPayment(int quantityForPayment) {
        this.quantityForPayment = quantityForPayment;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getParticipant1Payment() {
        return participant1Payment;
    }

    public void setParticipant1Payment(String participant1Payment) {
        this.participant1Payment = participant1Payment;
    }

    public String getParticipant2Payment() {
        return participant2Payment;
    }

    public void setParticipant2Payment(String participant2Payment) {
        this.participant2Payment = participant2Payment;
    }

    public String getParticipant3Payment() {
        return participant3Payment;
    }

    public void setParticipant3Payment(String participant3Payment) {
        this.participant3Payment = participant3Payment;
    }

    public String getParticipant4Payment() {
        return participant4Payment;
    }

    public void setParticipant4Payment(String participant4Payment) {
        this.participant4Payment = participant4Payment;
    }

    public String getParticipant5Payment() {
        return participant5Payment;
    }

    public void setParticipant5Payment(String participant5Payment) {
        this.participant5Payment = participant5Payment;
    }

    public String getParticipant6Payment() {
        return participant6Payment;
    }

    public void setParticipant6Payment(String participant6Payment) {
        this.participant6Payment = participant6Payment;
    }

    public String getParticipant7Payment() {
        return participant7Payment;
    }

    public void setParticipant7Payment(String participant7Payment) {
        this.participant7Payment = participant7Payment;
    }

    public String getParticipant8Payment() {
        return participant8Payment;
    }

    public void setParticipant8Payment(String participant8Payment) {
        this.participant8Payment = participant8Payment;
    }

    public String getParticipant9Payment() {
        return participant9Payment;
    }

    public void setParticipant9Payment(String participant9Payment) {
        this.participant9Payment = participant9Payment;
    }

    public String getParticipant10Payment() {
        return participant10Payment;
    }

    public void setParticipant10Payment(String participant10Payment) {
        this.participant10Payment = participant10Payment;
    }

    public String getParticipant11Payment() {
        return participant11Payment;
    }

    public void setParticipant11Payment(String participant11Payment) {
        this.participant11Payment = participant11Payment;
    }

    public String getParticipant12Payment() {
        return participant12Payment;
    }

    public void setParticipant12Payment(String participant12Payment) {
        this.participant12Payment = participant12Payment;
    }

    public String getParticipant13Payment() {
        return participant13Payment;
    }

    public void setParticipant13Payment(String participant13Payment) {
        this.participant13Payment = participant13Payment;
    }

    public String getParticipant14Payment() {
        return participant14Payment;
    }

    public void setParticipant14Payment(String participant14Payment) {
        this.participant14Payment = participant14Payment;
    }

    public String getParticipant15Payment() {
        return participant15Payment;
    }

    public void setParticipant15Payment(String participant15Payment) {
        this.participant15Payment = participant15Payment;
    }

    public float getTotalParticipant1() {
        return totalParticipant1;
    }

    public void setTotalParticipant1(float totalParticipant1) {
        this.totalParticipant1 = totalParticipant1;
    }

    public float getTotalParticipant2() {
        return totalParticipant2;
    }

    public void setTotalParticipant2(float totalParticipant2) {
        this.totalParticipant2 = totalParticipant2;
    }

    public float getTotalParticipant3() {
        return totalParticipant3;
    }

    public void setTotalParticipant3(float totalParticipant3) {
        this.totalParticipant3 = totalParticipant3;
    }

    public float getTotalParticipant4() {
        return totalParticipant4;
    }

    public void setTotalParticipant4(float totalParticipant4) {
        this.totalParticipant4 = totalParticipant4;
    }

    public float getTotalParticipant5() {
        return totalParticipant5;
    }

    public void setTotalParticipant5(float totalParticipant5) {
        this.totalParticipant5 = totalParticipant5;
    }

    public float getTotalParticipant6() {
        return totalParticipant6;
    }

    public void setTotalParticipant6(float totalParticipant6) {
        this.totalParticipant6 = totalParticipant6;
    }

    public float getTotalParticipant7() {
        return totalParticipant7;
    }

    public void setTotalParticipant7(float totalParticipant7) {
        this.totalParticipant7 = totalParticipant7;
    }

    public float getTotalParticipant8() {
        return totalParticipant8;
    }

    public void setTotalParticipant8(float totalParticipant8) {
        this.totalParticipant8 = totalParticipant8;
    }

    public float getTotalParticipant9() {
        return totalParticipant9;
    }

    public void setTotalParticipant9(float totalParticipant9) {
        this.totalParticipant9 = totalParticipant9;
    }

    public float getTotalParticipant10() {
        return totalParticipant10;
    }

    public void setTotalParticipant10(float totalParticipant10) {
        this.totalParticipant10 = totalParticipant10;
    }

    public float getTotalParticipant11() {
        return totalParticipant11;
    }

    public void setTotalParticipant11(float totalParticipant11) {
        this.totalParticipant11 = totalParticipant11;
    }

    public float getTotalParticipant12() {
        return totalParticipant12;
    }

    public void setTotalParticipant12(float totalParticipant12) {
        this.totalParticipant12 = totalParticipant12;
    }

    public float getTotalParticipant13() {
        return totalParticipant13;
    }

    public void setTotalParticipant13(float totalParticipant13) {
        this.totalParticipant13 = totalParticipant13;
    }

    public float getTotalParticipant14() {
        return totalParticipant14;
    }

    public void setTotalParticipant14(float totalParticipant14) {
        this.totalParticipant14 = totalParticipant14;
    }

    public float getTotalParticipant15() {
        return totalParticipant15;
    }

    public void setTotalParticipant15(float totalParticipant15) {
        this.totalParticipant15 = totalParticipant15;
    }
}