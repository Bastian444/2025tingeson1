package com.tingeso.backend.entities;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "bookings")
public class bookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String date;
    private String status;
    private LocalTime time;
    private int type;
    private int quantity;
    private String rutWhoMadeBooking;

    private String participant1;
    private String participant2;
    private String participant3;
    private String participant4;
    private String participant5;
    private String participant6;
    private String participant7;
    private String participant8;
    private String participant9;
    private String participant10;
    private String participant11;
    private String participant12;
    private String participant13;
    private String participant14;
    private String participant15;

    // Constructor vacío
    public bookingEntity() {}

    // Constructor con todos los campos
    public bookingEntity(Long id, String date, String status, LocalTime time, int type, int quantity,
                         String rutWhoMadeBooking, String participant1, String participant2, String participant3,
                         String participant4, String participant5, String participant6, String participant7,
                         String participant8, String participant9, String participant10, String participant11,
                         String participant12, String participant13, String participant14, String participant15) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.time = time;
        this.type = type;
        this.quantity = quantity;
        this.rutWhoMadeBooking = rutWhoMadeBooking;
        this.participant1 = participant1;
        this.participant2 = participant2;
        this.participant3 = participant3;
        this.participant4 = participant4;
        this.participant5 = participant5;
        this.participant6 = participant6;
        this.participant7 = participant7;
        this.participant8 = participant8;
        this.participant9 = participant9;
        this.participant10 = participant10;
        this.participant11 = participant11;
        this.participant12 = participant12;
        this.participant13 = participant13;
        this.participant14 = participant14;
        this.participant15 = participant15;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }

    public int getType() { return type; }
    public void setType(int type) { this.type = type; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getRutWhoMadeBooking() { return rutWhoMadeBooking; }
    public void setRutWhoMadeBooking(String rutWhoMadeBooking) { this.rutWhoMadeBooking = rutWhoMadeBooking; }

    public String getParticipant1() { return participant1; }
    public void setParticipant1(String participant1) { this.participant1 = participant1; }

    public String getParticipant2() { return participant2; }
    public void setParticipant2(String participant2) { this.participant2 = participant2; }

    public String getParticipant3() { return participant3; }
    public void setParticipant3(String participant3) { this.participant3 = participant3; }

    public String getParticipant4() { return participant4; }
    public void setParticipant4(String participant4) { this.participant4 = participant4; }

    public String getParticipant5() { return participant5; }
    public void setParticipant5(String participant5) { this.participant5 = participant5; }

    public String getParticipant6() { return participant6; }
    public void setParticipant6(String participant6) { this.participant6 = participant6; }

    public String getParticipant7() { return participant7; }
    public void setParticipant7(String participant7) { this.participant7 = participant7; }

    public String getParticipant8() { return participant8; }
    public void setParticipant8(String participant8) { this.participant8 = participant8; }

    public String getParticipant9() { return participant9; }
    public void setParticipant9(String participant9) { this.participant9 = participant9; }

    public String getParticipant10() { return participant10; }
    public void setParticipant10(String participant10) { this.participant10 = participant10; }

    public String getParticipant11() { return participant11; }
    public void setParticipant11(String participant11) { this.participant11 = participant11; }

    public String getParticipant12() { return participant12; }
    public void setParticipant12(String participant12) { this.participant12 = participant12; }

    public String getParticipant13() { return participant13; }
    public void setParticipant13(String participant13) { this.participant13 = participant13; }

    public String getParticipant14() { return participant14; }
    public void setParticipant14(String participant14) { this.participant14 = participant14; }

    public String getParticipant15() { return participant15; }
    public void setParticipant15(String participant15) { this.participant15 = participant15; }
}
