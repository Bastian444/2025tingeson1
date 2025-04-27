package com.tingeso.backend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "karts")
public class kartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String dateLastMaint; /* Maint debido a que se usa como abreviación de Maintenance */
    private String state;

    // Constructor vacío
    public kartEntity() {
    }

    // Constructor con todos los campos
    public kartEntity(Long id, String code, String dateLastMaint, String state) {
        this.id = id;
        this.code = code;
        this.dateLastMaint = dateLastMaint;
        this.state = state;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDateLastMaint() {
        return dateLastMaint;
    }

    public void setDateLastMaint(String dateLastMaint) {
        this.dateLastMaint = dateLastMaint;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    // Método toString()
    @Override
    public String toString() {
        return "kartEntity{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", dateLastMaint='" + dateLastMaint + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    // Métodos equals() y hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof kartEntity)) return false;

        kartEntity that = (kartEntity) o;

        if (!getId().equals(that.getId())) return false;
        if (!getCode().equals(that.getCode())) return false;
        if (!getDateLastMaint().equals(that.getDateLastMaint())) return false;
        return getState().equals(that.getState());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getCode().hashCode();
        result = 31 * result + getDateLastMaint().hashCode();
        result = 31 * result + getState().hashCode();
        return result;
    }
}

/* Atributo state para saber si el kart está reservado
 *
 *  R = Reservado
 *  D = Disponible
 *
 * */