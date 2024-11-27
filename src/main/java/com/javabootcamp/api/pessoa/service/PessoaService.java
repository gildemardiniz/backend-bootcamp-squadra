package com.javabootcamp.api.pessoa.service;

import com.javabootcamp.api.endereco.repository.EnderecoRepository;
import com.javabootcamp.api.pessoa.dto.PessoaRecordOutputDto;
import com.javabootcamp.api.pessoa.model.PessoaModel;
import com.javabootcamp.api.pessoa.respository.PessoaRepository;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {

    @Autowired
    PessoaRepository pessoaRepository;
    @Autowired
    EnderecoRepository enderecoRepository;


    public List<PessoaRecordOutputDto> findAll() {
        return pessoaRepository.findAll().stream().map(pessoa -> new PessoaRecordOutputDto(
                pessoa.getCodigoPessoa(),
                pessoa.getNome(),
                pessoa.getSobrenome(),
                pessoa.getIdade(),
                pessoa.getLogin(),
                pessoa.getSenha(),
                pessoa.getStatus(),
                new ArrayList<>()

        )).toList();

    }

    public Optional<PessoaModel> findById(Long codigoPessoa) {
        return pessoaRepository.findById(codigoPessoa);
    }

    public List<PessoaRecordOutputDto> findAll(String login, Integer status, String nome) {
        PessoaModel pessoaModel = new PessoaModel();
        pessoaModel.setLogin(login == null ? null : login.toUpperCase());
        pessoaModel.setStatus(status);
        pessoaModel.setNome(nome == null ? null : nome.toUpperCase());


        return pessoaRepository.findAll(Example.of(pessoaModel)).stream().map(pessoa -> new PessoaRecordOutputDto(
                pessoa.getCodigoPessoa(),
                pessoa.getNome(),
                pessoa.getSobrenome(),
                pessoa.getIdade(),
                pessoa.getLogin(),
                pessoa.getSenha(),
                pessoa.getStatus(),
                new ArrayList<>()
        )).toList();

    }

    public PessoaModel save(PessoaModel pessoaModel) {
        return pessoaRepository.save(pessoaModel);
    }

    public void desativarPessoa(long codigoPessoa) {
        Optional<PessoaModel> pessoaModel = pessoaRepository.findById(codigoPessoa);
        if (pessoaModel.isPresent()) {
            pessoaModel.get().setStatus(2);
            pessoaRepository.save(pessoaModel.get());
        } else {
            throw new PersistenceException("Pessoa não cadastrada na base de dados");
        }
    }
}
