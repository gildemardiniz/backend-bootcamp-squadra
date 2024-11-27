package com.javabootcamp.api.uf.repository;


import com.javabootcamp.api.uf.model.UfModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UfRepository extends JpaRepository<UfModel,Long> {

}
