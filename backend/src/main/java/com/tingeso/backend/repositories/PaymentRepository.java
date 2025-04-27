package com.tingeso.backend.repositories;

import com.tingeso.backend.entities.clientEntity;
import com.tingeso.backend.entities.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    // Metodo para buscar por idBooking con query explicita
    @Query("SELECT p FROM PaymentEntity p WHERE p.idBooking = :idBooking")
    Optional<PaymentEntity> findByIdBooking(@Param("idBooking") Long idBooking);






}
