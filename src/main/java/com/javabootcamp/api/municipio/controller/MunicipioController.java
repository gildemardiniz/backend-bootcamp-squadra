package com.javabootcamp.api.municipio.controller;

import com.javabootcamp.api.error.ErrorDto;
import com.javabootcamp.api.municipio.dto.MunicipioRecordDto;
import com.javabootcamp.api.municipio.dto.MunicipioRecordUpdateDto;
import com.javabootcamp.api.municipio.model.MunicipioModel;
import com.javabootcamp.api.municipio.service.MunicipioService;
import com.javabootcamp.api.uf.model.UfModel;
import com.javabootcamp.api.uf.service.UfService;
import com.javabootcamp.api.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/municipio")
@Tag(name = "Município")
public class MunicipioController {

    @Autowired
    MunicipioService municipioService;
    @Autowired
    private UfService ufService;


    @Operation(summary = "Adicionar novo município", method = "POST")
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @NotNull MunicipioRecordDto municipioRecordDto) {

        // Realiza a validacao dos atributos do Dto.
        var validacaoMensagem = Utils.validarViolacoes(municipioRecordDto);
        // Verifica se tem violacao e retorna o erro.
        if (validacaoMensagem.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(validacaoMensagem.get(), HttpStatus.BAD_REQUEST.value()));
        }

        try {

            List<MunicipioRecordUpdateDto> validacaoMunicipioUf = municipioService.findAll(municipioRecordDto.codigoUf(), municipioRecordDto.nome(), null);
            var municipioModel = new MunicipioModel(municipioRecordDto);
            Optional<UfModel> ufModel = ufService.findById(municipioRecordDto.codigoUf());
            // valida se existe municipip cadastrado para a uf informada.
            if (!validacaoMunicipioUf.isEmpty()) {
                throw new DataIntegrityViolationException("Municipio já cadastrado para essa UF");
            }
            if (ufModel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("UF não cadastrada na base de dados informe um id válido", HttpStatus.BAD_REQUEST.value()));
            }
            return ResponseEntity.status(HttpStatus.OK).body(municipioService.save(municipioModel));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDto(e.getMessage(), HttpStatus.CONFLICT.value()));
        }
    }

    @Operation(summary = "Buscar município", method = "GET")
    @GetMapping
    public ResponseEntity<Object> find(
            @RequestParam(required = false) Long codigoMunicipio,
            @RequestParam(required = false) Long codigoUf,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer status
    ) {

        Object retorno;

        if (codigoMunicipio != null) {
            retorno = municipioService.findById(codigoMunicipio)
                    .map(municipio -> new MunicipioRecordUpdateDto(
                            municipio.getCodigoMunicipio(),
                            municipio.getCodigoUf().getCodigoUF(),
                            municipio.getNome(),
                            municipio.getStatus()));
        } else if (codigoUf == null && nome == null && status == null) {
            retorno = municipioService.findAll();
        } else {
            retorno = municipioService.findAll(codigoUf, nome, status);
        }

        return ResponseEntity.status(HttpStatus.OK).body(retorno == null ? Collections.emptyList() : retorno);
    }

    @Operation(summary = "Atualizar município", method = "PUT")
    @PutMapping()
    public ResponseEntity<Object> update(@RequestBody @NotNull MunicipioRecordUpdateDto municipioRecordUpdateDto) {

        // Realiza a validacao dos atributos do Dto.
        var validaMensagem = Utils.validarViolacoes(municipioRecordUpdateDto);
        // Verifica se tem violacao e retorna o erro.
        if (validaMensagem.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(validaMensagem.get(), HttpStatus.BAD_REQUEST.value()));
        }
        try {
            Optional<MunicipioModel> object = municipioService.findById(municipioRecordUpdateDto.codigoMunicipio());
            if (object.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Município não cadastrado na base de dados informe um id válido", HttpStatus.BAD_REQUEST.value()));
            }
            var municipioModel = new MunicipioModel(municipioRecordUpdateDto);
            return ResponseEntity.status(HttpStatus.OK).body(municipioService.save(municipioModel));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDto(e.getMessage(), HttpStatus.CONFLICT.value()));
        }
    }
}
