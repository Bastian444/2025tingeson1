package com.tingeso.backend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "staff")
public class staffEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String staffname;
    private String stafflastName;
    private String staffemail;
    private String staffrut;
    private String staffpassword;

    public staffEntity() {}

    public staffEntity(Long id, String staffname, String stafflastName, String staffemail,
                       String staffrut, String staffpassword) {
        this.id = id;
        this.staffname = staffname;
        this.stafflastName = stafflastName;
        this.staffemail = staffemail;
        this.staffrut = staffrut;
        this.staffpassword = staffpassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStaffname() {
        return staffname;
    }

    public void setStaffname(String staffname) {
        this.staffname = staffname;
    }

    public String getStafflastName() {
        return stafflastName;
    }

    public void setStafflastName(String stafflastName) {
        this.stafflastName = stafflastName;
    }

    public String getStaffemail() {
        return staffemail;
    }

    public void setStaffemail(String staffemail) {
        this.staffemail = staffemail;
    }

    public String getStaffrut() {
        return staffrut;
    }

    public void setStaffrut(String staffrut) {
        this.staffrut = staffrut;
    }

    public String getStaffpassword() {
        return staffpassword;
    }

    public void setStaffpassword(String staffpassword) {
        this.staffpassword = staffpassword;
    }
}
