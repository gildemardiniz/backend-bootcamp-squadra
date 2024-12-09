package com.javabootcamp.api.uf.service;

import com.javabootcamp.api.uf.model.UfModel;
import com.javabootcamp.api.uf.repository.UfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UfService {

    @Autowired
    UfRepository ufRepository;

    public List<UfModel> save(UfModel ufModel) {

        ufRepository.save(ufModel);
        return ufRepository.findAll();
    }

    public List<UfModel> findAll() {
        return ufRepository.findAll();
    }

    public Optional<UfModel> findById(Long id) {
        return ufRepository.findById(id);
    }

    public Object findOne(Long codigoUF, String Sigla, String nome, Integer status) {

        UfModel ufModel = new UfModel(codigoUF, Sigla, nome, status);

        var busca = ufRepository.findOne(
                Example.of(ufModel));
        if (busca.isPresent()) {
            return busca.get();
        }
        return new ArrayList<>();
    }

    public List<UfModel> findAll(Integer status) {
        UfModel ufModel = new UfModel();
        ufModel.setStatus(status);
        return ufRepository.findAll(Example.of(ufModel));
    }

    public List<UfModel> findBySigla(String sigla) {
        UfModel ufModel = new UfModel();
        ufModel.setSigla(sigla.toUpperCase());
        return ufRepository.findAll(Example.of(ufModel));
    }

    public List<UfModel> findByNome(String nome) {
        UfModel ufModel = new UfModel();
        ufModel.setNome(nome.toUpperCase());
        return ufRepository.findAll(Example.of(ufModel));
    }



}
