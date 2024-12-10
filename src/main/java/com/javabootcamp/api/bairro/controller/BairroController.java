package com.javabootcamp.api.bairro.controller;

import com.javabootcamp.api.bairro.dto.BairroRecordDto;
import com.javabootcamp.api.bairro.dto.BairroRecordUpdateDto;
import com.javabootcamp.api.bairro.model.BairroModel;
import com.javabootcamp.api.bairro.service.BairroService;
import com.javabootcamp.api.error.ErrorDto;
import com.javabootcamp.api.municipio.model.MunicipioModel;
import com.javabootcamp.api.municipio.service.MunicipioService;
import com.javabootcamp.api.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/bairro")
@Tag(name = "Bairro")
public class BairroController {

    @Autowired
    BairroService bairroService;
    @Autowired
    MunicipioService municipioService;

    @Operation(summary = "Adicionar novo bairro", method = "POST")
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @NotNull BairroRecordDto bairroRecordDto) {

        // Realiza a validacao dos atributos do Dto.b
        var validacaoMensagem = Utils.validarViolacoes(bairroRecordDto);
        // Verifica se tem violacao e retorna o erro.
        if (validacaoMensagem.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(validacaoMensagem.get(), HttpStatus.BAD_REQUEST.value()));
        }

        try {

            Optional<MunicipioModel> municipioModel = municipioService.findById(bairroRecordDto.codigoMunicipio());
            var bairroModel = new BairroModel(bairroRecordDto);
            List<BairroRecordUpdateDto> validacaoBairroMunicipio = bairroService.findAll(bairroRecordDto.codigoMunicipio(), bairroRecordDto.nome(), null);

            if (municipioModel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Município não cadastrado na base de dados informe um id válido", HttpStatus.BAD_REQUEST.value()));
            }
            if (!validacaoBairroMunicipio.isEmpty()) {
                throw new DataIntegrityViolationException("Bairro já cadastrado para esse municipio");
            }

            return ResponseEntity.status(HttpStatus.OK).body(bairroService.save(bairroModel));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDto(e.getMessage(), HttpStatus.CONFLICT.value()));
        }

    }

    @Operation(summary = "Buscar bairro", method = "GET")
    @GetMapping
    public ResponseEntity<Object> find(
            @RequestParam(required = false) Long codigoBairro,
            @RequestParam(required = false) Long codigoMunicipio,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer status) {

        Object retorno;
        if (codigoBairro != null && codigoMunicipio == null  && nome == null && status == null ) {
            retorno = bairroService.findById(codigoBairro).map(bairro -> new BairroRecordUpdateDto(
                    bairro.getCodigoBairro(),
                    bairro.getCodigoMunicipio().getCodigoMunicipio(),
                    bairro.getNome(),
                    bairro.getStatus()
            )).orElse(null);
        } else if (codigoMunicipio == null && nome == null && status == null) {
            retorno = bairroService.findAll();
        }else{
            retorno = bairroService.findAll(codigoMunicipio, nome, status);
        }
        return ResponseEntity.status(HttpStatus.OK).body(retorno == null ? Collections.emptyList() : retorno);
    }

    @Operation(summary = "Atualizar bairro", method = "PUT")
    @PutMapping()
    public ResponseEntity<Object> update(@RequestBody  @NotNull BairroRecordUpdateDto bairroRecordUpdateDto) {
        var validaMensagem = Utils.validarViolacoes(bairroRecordUpdateDto);
        if (validaMensagem.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(validaMensagem.get(), HttpStatus.BAD_REQUEST.value()));
        }

        try{
            Optional<BairroModel> object = bairroService.findById(bairroRecordUpdateDto.codigoBairro());
            if (object.isPresent()) {
                var bairroModel = new BairroModel(bairroRecordUpdateDto);
                return ResponseEntity.status(HttpStatus.OK).body(bairroService.save(bairroModel));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Bairro não cadastrado na base de dados informe um id válido", HttpStatus.BAD_REQUEST.value()));
            }
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDto(e.getMessage(), HttpStatus.CONFLICT.value()));
        }
    }
}
