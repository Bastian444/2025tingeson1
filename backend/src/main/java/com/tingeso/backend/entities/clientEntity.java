package com.tingeso.backend.entities;


import jakarta.persistence.*;

@Entity
@Table(name = "clients")
public class clientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String clientname;
    private String clientlastname;
    private String clientemail;
    private String clientrut;
    private String clientpassword;
    private int visits;
    private String birthdate;

    public clientEntity() {}

    public clientEntity(Long id, String clientname, String clientlastname, String clientemail,
                        String clientrut, String clientpassword, int visits, String birthdate) {
        this.id = id;
        this.clientname = clientname;
        this.clientlastname = clientlastname;
        this.clientemail = clientemail;
        this.clientrut = clientrut;
        this.clientpassword = clientpassword;
        this.visits = visits;
        this.birthdate = birthdate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientname() {
        return clientname;
    }

    public void setClientname(String clientname) {
        this.clientname = clientname;
    }

    public String getClientlastname() {
        return clientlastname;
    }

    public void setClientlastname(String clientlastname) {
        this.clientlastname = clientlastname;
    }

    public String getClientemail() {
        return clientemail;
    }

    public void setClientemail(String clientemail) {
        this.clientemail = clientemail;
    }

    public String getClientrut() {
        return clientrut;
    }

    public void setClientrut(String clientrut) {
        this.clientrut = clientrut;
    }

    public String getClientpassword() {
        return clientpassword;
    }

    public void setClientpassword(String clientpassword) {
        this.clientpassword = clientpassword;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }
}
