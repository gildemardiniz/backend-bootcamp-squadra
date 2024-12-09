package com.javabootcamp.api.uf.controller;

import com.javabootcamp.api.error.ErrorDto;
import com.javabootcamp.api.uf.dto.UfRecordDto;
import com.javabootcamp.api.uf.dto.UfRecordUpdateDto;
import com.javabootcamp.api.uf.model.UfModel;
import com.javabootcamp.api.uf.service.UfService;
import com.javabootcamp.api.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(value = "/uf")
@Tag(name = "UF")
public class Ufcontroller {


    @Autowired
    UfService ufService;

    @Operation(summary = "Adicionar uma nova Uf", method = "POST")
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody  @NotNull UfRecordDto ufRecordDto) {

        // Realiza a validacao dos atributos do Dto.
        var validacaoMensagem = Utils.validarViolacoes(ufRecordDto);
        // Verifica se tem violacao e retorna o erro.
        if(validacaoMensagem.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(validacaoMensagem.get(), HttpStatus.BAD_REQUEST.value()));
        }
        var ufModel = new UfModel(ufRecordDto);
        try {

            List<UfModel> validasigla = ufService.findBySigla(ufRecordDto.sigla());
            if(!validasigla.isEmpty() ){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDto("Sigla já cadastrada na base de dados", HttpStatus.CONFLICT.value()));
            }
            List<UfModel> validaNome = ufService.findByNome(ufRecordDto.nome());
            if(!validaNome.isEmpty() ){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDto("Nome da UF já cadastrado na base de dados", HttpStatus.CONFLICT.value()));
            }

            return ResponseEntity.status(HttpStatus.OK).body(ufService.save(ufModel));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDto("UF já cadastrada na base de dados", HttpStatus.CONFLICT.value()));
        }

    }

    @Operation(summary = "Buscar Uf", method = "GET")
    @GetMapping
    public ResponseEntity<Object> find(
            @RequestParam(required = false) Long codigoUF,
            @RequestParam(required = false) String sigla,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer status
    ) {
        if(status != null && codigoUF == null && sigla == null && nome == null){
            return ResponseEntity.status(HttpStatus.OK).body(ufService.findAll(status));
        }
        if (codigoUF == null && sigla == null && nome == null) {
            return ResponseEntity.status(HttpStatus.OK).body(ufService.findAll());
        }
        return ResponseEntity.status(HttpStatus.OK).body(ufService.findOne(codigoUF, sigla != null ? sigla.toUpperCase(): null, nome != null ? nome.toUpperCase() : null, status));
    }

    @Operation(summary = "Atualizar Uf", method = "PUT")
    @PutMapping()
    public ResponseEntity<Object> update(@RequestBody @NotNull UfRecordUpdateDto ufRecordUpdateDto) {

        // Realiza a validacao dos atributos do Dto.
        var validacaoMensagem = Utils.validarViolacoes(ufRecordUpdateDto);
        // Verifica se tem violacao e retorna o erro.
        if(validacaoMensagem.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(validacaoMensagem.get(), HttpStatus.BAD_REQUEST.value()));
        }

        Optional<UfModel> object = ufService.findById(ufRecordUpdateDto.codigoUF());
        if (object.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDto("UF não cadastrada na base de dados informe um id válido", HttpStatus.CONFLICT.value()));
        }

        List<UfModel> validasigla = ufService.findBySigla(ufRecordUpdateDto.sigla());

        if(!object.get().getSigla().equals(ufRecordUpdateDto.sigla().toUpperCase()) && !validasigla.isEmpty() ){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDto("Sigla já cadastrada na base de dados", HttpStatus.CONFLICT.value()));
        }

        List<UfModel> validaNome = ufService.findByNome(ufRecordUpdateDto.nome());
        if(!object.get().getNome().equals(ufRecordUpdateDto.nome().toUpperCase()) && !validaNome.isEmpty() ){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDto("Nome da UF já cadastrado na base de dados", HttpStatus.CONFLICT.value()));
        }

        var ufModel = new UfModel(ufRecordUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body(ufService.save(ufModel));
    }

}

