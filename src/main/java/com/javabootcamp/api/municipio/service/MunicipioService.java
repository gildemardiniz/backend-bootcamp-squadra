package com.javabootcamp.api.municipio.service;

import com.javabootcamp.api.municipio.dto.MunicipioRecordUpdateDto;
import com.javabootcamp.api.municipio.model.MunicipioModel;
import com.javabootcamp.api.uf.model.UfModel;
import com.javabootcamp.api.municipio.repository.MunicipioRepository;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MunicipioService {

    @Autowired
    MunicipioRepository municipioRepository;

    public List<MunicipioRecordUpdateDto> findAll() {

        return municipioRepository.findAll().stream().map(municipio -> new MunicipioRecordUpdateDto(
                municipio.getCodigoMunicipio(),
                municipio.getCodigoUf().getCodigoUF(),
                municipio.getNome(),
                municipio.getStatus()
        )).toList();
    }


    public List<MunicipioRecordUpdateDto> save(MunicipioModel municipioModel) {
        //salva
        municipioRepository.save(municipioModel);
        //retorna
        return this.findAll();
    }

    public Optional<MunicipioModel> findById(Long codigoMunicipio) {
        return municipioRepository.findById(codigoMunicipio);
    }

    public List<MunicipioRecordUpdateDto> findAll(Long codigoUf, String nome, Integer status) {

        MunicipioModel municipioModel = new MunicipioModel();
        municipioModel.setCodigoUf(new UfModel(codigoUf));
        municipioModel.setNome(nome == null ? null : nome.toUpperCase());
        municipioModel.setStatus(status);

        return municipioRepository.findAll(Example.of(municipioModel)).stream().map(municipio -> new MunicipioRecordUpdateDto(
                municipio.getCodigoMunicipio(),
                municipio.getCodigoUf().getCodigoUF(),
                municipio.getNome(),
                municipio.getStatus()
        )).toList();
    }
    public void desativarMunicipio(long codigoMunicipio) {
        Optional<MunicipioModel> municipioModel = municipioRepository.findById(codigoMunicipio);
        if (municipioModel.isPresent()) {
            municipioModel.get().setStatus(2);
            municipioRepository.save(municipioModel.get());
        } else {
            throw new PersistenceException("Município não cadastrado na base de dados");
        }
    }
}
