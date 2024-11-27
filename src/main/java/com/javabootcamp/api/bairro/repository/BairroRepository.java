package com.javabootcamp.api.bairro.repository;

import com.javabootcamp.api.bairro.model.BairroModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BairroRepository extends JpaRepository<BairroModel,Long> {

}
