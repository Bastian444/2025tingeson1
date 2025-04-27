package com.tingeso.backend.repositories;

import com.tingeso.backend.entities.clientEntity;
import com.tingeso.backend.entities.kartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface kartRepository extends JpaRepository<kartEntity, Long> {
    public clientEntity findByCode(String code);
    public kartEntity findById(long id);
    List<kartEntity> findAll();
}
