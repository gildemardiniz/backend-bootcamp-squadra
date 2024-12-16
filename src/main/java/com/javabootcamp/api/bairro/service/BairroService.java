package com.javabootcamp.api.bairro.service;

import com.javabootcamp.api.bairro.dto.BairroRecordUpdateDto;
import com.javabootcamp.api.bairro.model.BairroModel;
import com.javabootcamp.api.bairro.repository.BairroRepository;
import com.javabootcamp.api.municipio.model.MunicipioModel;
import com.javabootcamp.api.uf.model.UfModel;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BairroService {

    @Autowired
    private BairroRepository bairroRepository;


    public List<BairroRecordUpdateDto> save(BairroModel bairroModel) {
        // Salva no banco
        bairroRepository.save(bairroModel);
        //Retorna todos os objetos
        return this.findAll();
    }
    public List<BairroRecordUpdateDto> findAll(Long codigoMunicipio, String nome, Integer status) {

        BairroModel bairroModel = new BairroModel();
        bairroModel.setCodigoMunicipio(new MunicipioModel(codigoMunicipio));
        bairroModel.setNome(nome == null ? null : nome.toUpperCase());
        bairroModel.setStatus(status);

        return bairroRepository.findAll(Example.of(bairroModel)).stream().map(bairro -> new BairroRecordUpdateDto(
                bairro.getCodigoBairro(),
                bairro.getCodigoMunicipio().getCodigoMunicipio(),
                bairro.getNome(),
                bairro.getStatus()

        )).toList();
    }
    public List<BairroRecordUpdateDto> findAll(){
        return bairroRepository.findAll().stream().map(bairro -> new BairroRecordUpdateDto(
                bairro.getCodigoBairro(),
                bairro.getCodigoMunicipio().getCodigoMunicipio(),
                bairro.getNome(),
                bairro.getStatus()
        )).toList();
    }
    public Optional<BairroModel> findById(Long codigoBairro) {
        return bairroRepository.findById(codigoBairro);
    }
    public void desativarBairro(long codigoUf) {
        Optional<BairroModel> bairroModel = bairroRepository.findById(codigoUf);
        if (bairroModel.isPresent()) {
            bairroModel.get().setStatus(2);
            bairroRepository.save(bairroModel.get());
        } else {
            throw new PersistenceException("Bairro não cadastrado na base de dados");
        }
    }
}
