package com.javabootcamp.api.municipio.repository;


import com.javabootcamp.api.municipio.model.MunicipioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MunicipioRepository extends JpaRepository<MunicipioModel, Long> {
}
