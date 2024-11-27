package com.javabootcamp.api.pessoa.respository;

import com.javabootcamp.api.pessoa.model.PessoaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository  extends JpaRepository<PessoaModel,Long> {

}
