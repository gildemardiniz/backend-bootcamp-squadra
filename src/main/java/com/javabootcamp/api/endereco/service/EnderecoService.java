package com.javabootcamp.api.endereco.service;

import com.javabootcamp.api.bairro.model.BairroModel;
import com.javabootcamp.api.bairro.repository.BairroRepository;
import com.javabootcamp.api.endereco.dto.EnderecoRecordUpdateDto;
import com.javabootcamp.api.endereco.model.EnderecoModel;
import com.javabootcamp.api.endereco.repository.EnderecoRepository;
import com.javabootcamp.api.pessoa.model.PessoaModel;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private BairroRepository bairroRepository;


    public Optional<EnderecoModel> findById(Long codigoEndereco) {
        return enderecoRepository.findById(codigoEndereco);
    }

    public List<EnderecoRecordUpdateDto> save(EnderecoModel enderecoModel) {
        enderecoRepository.save(enderecoModel);
        return this.findAll();
    }

    public List<EnderecoRecordUpdateDto> findAll() {
        return enderecoRepository.findAll().stream().map(endereco -> new EnderecoRecordUpdateDto(
                endereco.getCodigoEndereco(),
                endereco.getCodigoBairro().getCodigoBairro(),
                endereco.getNomeRua(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getCep(),
                endereco.getCodigoPessoa().getCodigoPessoa()
        )).toList();
    }

    public List<EnderecoRecordUpdateDto> findAll(Long codigoBairro, String nomeRua, String numero, String complemento, String cep, Long codigoPessoa) {
        EnderecoModel enderecoModel = new EnderecoModel();
        enderecoModel.setCodigoBairro(new BairroModel(codigoBairro));
        enderecoModel.setNomeRua(nomeRua == null ? null : nomeRua.toUpperCase());
        enderecoModel.setNumero(numero == null ? null : numero.toUpperCase());
        enderecoModel.setComplemento(complemento == null ? null : complemento.toUpperCase());
        enderecoModel.setCep(cep == null ? null : complemento.toUpperCase());
        enderecoModel.setCodigoPessoa(new PessoaModel(codigoPessoa));

        return enderecoRepository.findAll(Example.of(enderecoModel)).stream().map(endereco -> new EnderecoRecordUpdateDto(
                endereco.getCodigoEndereco(),
                endereco.getCodigoBairro().getCodigoBairro(),
                endereco.getNomeRua(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getCep(),
                endereco.getCodigoPessoa().getCodigoPessoa()
        )).toList();
    }

    public List<EnderecoRecordUpdateDto> delete( Long codigoEndereco) {
        try{
            enderecoRepository.deleteById(codigoEndereco);
            return this.findAll();
        }catch (Exception e) {
            throw new RuntimeException("Erro ao deletar enderecos: " + codigoEndereco);
        }

    }

    public List<Long> findIdsAndressByPerson(Long codigoPessoa) {
        try{

            return this.findAll(null,null,null,null,null,codigoPessoa).stream().map(endereco -> endereco.codigoEndereco()).toList();
        }catch (Exception e) {
            throw new RuntimeException("Erro ao buscar endereços");
        }
    }
}
