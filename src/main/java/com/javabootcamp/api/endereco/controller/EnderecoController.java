package com.javabootcamp.api.endereco.controller;

import com.javabootcamp.api.bairro.model.BairroModel;
import com.javabootcamp.api.bairro.service.BairroService;
import com.javabootcamp.api.endereco.dto.EnderecoRecordDto;
import com.javabootcamp.api.endereco.dto.EnderecoRecordUpdateDto;
import com.javabootcamp.api.endereco.model.EnderecoModel;
import com.javabootcamp.api.endereco.service.EnderecoService;
import com.javabootcamp.api.error.ErrorDto;
import com.javabootcamp.api.pessoa.model.PessoaModel;
import com.javabootcamp.api.pessoa.service.PessoaService;
import com.javabootcamp.api.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.PersistenceException;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping(value = "/endereco")
@Tag(name = "Endereço")
public class EnderecoController {

    @Autowired
    EnderecoService enderecoService;
    @Autowired
    BairroService bairroService;
    @Autowired
    private PessoaService pessoaService;

    @Operation(summary = "Adicionar novo endereço", method = "POST")
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @NotNull EnderecoRecordDto enderecoRecordDto) {
        // Realiza a validacao dos atributos do Dto.
        var validacaoMensagem = Utils.validarViolacoes(enderecoRecordDto);
        // Verifica se tem violacao e retorna o erro.
        if (validacaoMensagem.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(validacaoMensagem.get(), HttpStatus.BAD_REQUEST.value()));
        }

        try {
            var enderecoModel = new EnderecoModel(enderecoRecordDto);
            Optional<BairroModel> bairroModel = bairroService.findById(enderecoRecordDto.codigoBairro());
            if (bairroModel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Bairro não cadastrado na base de dados informe um id válido", HttpStatus.BAD_REQUEST.value()));
            }
            Optional<PessoaModel> pessoaModel = pessoaService.findById(enderecoRecordDto.codigoPessoa());
            if(pessoaModel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Pessoa não cadastrada na base de dados informe um id válido", HttpStatus.BAD_REQUEST.value()));
            }
            enderecoModel.setCodigoPessoa(pessoaModel.get());
            return ResponseEntity.status(HttpStatus.OK).body(enderecoService.save(enderecoModel));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDto(e.getMessage(), HttpStatus.CONFLICT.value()));
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));        }
    }

    @Operation(summary = "Buscar endereço", method = "GET")
    @GetMapping
    public ResponseEntity<Object> find(
            @RequestParam(required = false) Long codigoEndereco,
            @RequestParam(required = false) Long codigoBairro,
            @RequestParam(required = false) String nomeRua,
            @RequestParam(required = false) String numero,
            @RequestParam(required = false) String complemento,
            @RequestParam(required = false) String cep,
            @RequestParam(required = false) Long codigoPessoa
    ) {
        Object retorno;
        if (codigoEndereco != null) {
            retorno = enderecoService.findById(codigoEndereco).map(endereco -> new EnderecoRecordUpdateDto(
                    endereco.getCodigoEndereco(),
                    endereco.getCodigoBairro().getCodigoBairro(),
                    endereco.getNomeRua(),
                    endereco.getNumero(),
                    endereco.getComplemento(),
                    endereco.getCep(),
                    endereco.getCodigoPessoa().getCodigoPessoa()
            ));
        } else if (codigoBairro ==null && nomeRua ==null && numero == null && complemento == null && cep == null && codigoPessoa == null) {
            retorno = enderecoService.findAll();
        }else{
            retorno = enderecoService.findAll(codigoBairro,nomeRua, numero, complemento, cep ,codigoPessoa);
        }
        return ResponseEntity.status(HttpStatus.OK).body(retorno == null ? Collections.emptyList() : retorno);
    }
    @Operation(summary = "Atualizar endereço", method = "PUT")
    @PutMapping
    public ResponseEntity<Object> update(@RequestBody @NotNull EnderecoRecordUpdateDto enderecoRecordUpdateDto) {
        var validaMensagem = Utils.validarViolacoes(enderecoRecordUpdateDto);
        if (validaMensagem.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(validaMensagem.get(), HttpStatus.BAD_REQUEST.value()));
        }
        try{
            Optional<EnderecoModel> object =enderecoService.findById(enderecoRecordUpdateDto.codigoEndereco());
            if (object.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Endereço não cadastrado na base de dados informe um id válido", HttpStatus.BAD_REQUEST.value()));
            }
            var enderecoModel = new EnderecoModel(enderecoRecordUpdateDto);
            return ResponseEntity.status(HttpStatus.OK).body(enderecoService.save(enderecoModel));
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDto(e.getMessage(), HttpStatus.CONFLICT.value()));
        }
    }
    @Operation(summary = "deletar endereço", method = "DeLETE")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestParam @NotNull Long codigoEndereco) {
        try{
            Optional<EnderecoModel> object =enderecoService.findById(codigoEndereco);
            if (object.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Endereço não cadastrado na base de dados informe um id válido", HttpStatus.BAD_REQUEST.value()));
            }
            return ResponseEntity.status(HttpStatus.OK).body(enderecoService.delete(codigoEndereco));

        }catch (DataIntegrityViolationException e) {
           return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDto(e.getMessage(), HttpStatus.CONFLICT.value()));
        }
    }
}
