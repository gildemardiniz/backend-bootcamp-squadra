package com.javabootcamp.api.pessoa.controller;

import com.javabootcamp.api.bairro.dto.BairroRecordUpdateDto;
import com.javabootcamp.api.bairro.model.BairroModel;
import com.javabootcamp.api.bairro.service.BairroService;
import com.javabootcamp.api.endereco.dto.EnderecoRecordOutDto;
import com.javabootcamp.api.endereco.dto.EnderecoRecordUpdateDto;
import com.javabootcamp.api.endereco.model.EnderecoModel;
import com.javabootcamp.api.endereco.service.EnderecoService;
import com.javabootcamp.api.error.ErrorDto;
import com.javabootcamp.api.pessoa.dto.PessoaRecordDto;
import com.javabootcamp.api.pessoa.dto.PessoaRecordOutputDto;
import com.javabootcamp.api.pessoa.dto.PessoaRecordUpdateDto;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/pessoa")
@Tag(name = "Pessoa")
public class PessoaController {

    @Autowired
    PessoaService pessoaService;
    @Autowired
    private EnderecoService enderecoService;
    @Autowired
    private BairroService bairroService;


    @Operation(summary = "Adicionar nova pessoa ", method = "POST")
    @PostMapping
    ResponseEntity<Object> save(@RequestBody @NotNull PessoaRecordDto pessoaRecordDto) {
        var validacaoMensagem = Utils.validarViolacoes(pessoaRecordDto);
        if (validacaoMensagem.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(validacaoMensagem.get(), HttpStatus.BAD_REQUEST.value()));
        }
        try {

            Object retorno = null;

            // Valida se Nome contém numero.
            if (pessoaRecordDto.nome().matches("^[0-9]*$")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Formato inválido para o campo nome", HttpStatus.BAD_REQUEST.value()));
            }
            // Valida se sobrenome contém numero.
            if (pessoaRecordDto.sobrenome().matches("^[0-9]*$")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Formato inválido para o campo sobrenome", HttpStatus.BAD_REQUEST.value()));
            }

            // valida de enderecos esta vazio
            if (pessoaRecordDto.enderecos().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("O endereço está vazio, informe no minimo um endereço", HttpStatus.BAD_REQUEST.value()));
            }

            // Valida se codigoBairro existe.
            List<Long> codigosBairrosCadastrados = bairroService.findAll().stream().map(BairroRecordUpdateDto::codigoBairro).toList();
            List<Long> codigosBairroPessoaDto = pessoaRecordDto.enderecos().stream().map(dto->dto.codigoBairro()).toList();
            List<Long> bairrosNaoCadastrados = codigosBairroPessoaDto.stream().filter(codigoBairro->!codigosBairrosCadastrados.contains(codigoBairro)).toList();
            if (!bairrosNaoCadastrados.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Bairros " + bairrosNaoCadastrados.toString() + " não estão cadastrados na base de dados", HttpStatus.BAD_REQUEST.value()));
            }

            // valida ser cep contem letras.
            List<EnderecoRecordOutDto> buscaCep = pessoaRecordDto.enderecos().stream().filter(endereco -> endereco.cep().matches("^[a-zA-Z0-9]*[a-zA-Z]+[A-Za-z0-9]*$")).toList();
            if (!buscaCep.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Formato inválido para o campo CEP, informe somente numeros", HttpStatus.BAD_REQUEST.value()));
            }
            List<PessoaRecordOutputDto> validaLogin = pessoaService.findAll(pessoaRecordDto.login().toUpperCase(), null, null);
            if (!validaLogin.isEmpty()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDto("Login já cadastrado na base de dados", HttpStatus.CONFLICT.value()));
            }

            var pessoaModel = new PessoaModel(pessoaRecordDto);
            PessoaModel pessoa = pessoaService.save(pessoaModel);

            if (pessoa != null) {
                pessoaRecordDto.enderecos().stream().map(endereco -> new EnderecoModel(endereco, pessoa.getCodigoPessoa())).forEach(
                        enderecoModel -> enderecoService.save(enderecoModel)
                );
                retorno = pessoaService.findAll();
            }
            return ResponseEntity.status(HttpStatus.OK).body(retorno);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDto(e.getMessage(), HttpStatus.CONFLICT.value()));
        }
    }

    @Operation(summary = "Buscar pessoa", method = "GET")
    @GetMapping
    public ResponseEntity<Object> find(
            @RequestParam(required = false) Long codigoPessoa,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String login,
            @RequestParam(required = false) Integer status
    ) {

        Object retorno;

        //Valida se nome contem numeros na busca
        if (nome != null) { // Valida se nome contaim numero.
            if (nome.matches("^[0-9]*$")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Valor inválido, informe no nome um texto ", HttpStatus.BAD_REQUEST.value()));
            }
        }
        //Valida Valores 1 e dois pra status
        if (status != null) {
            if (status != 1 && status != 2) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Valor inválido, informe no status 1 para ativo ou 2 para inativo", HttpStatus.BAD_REQUEST.value()));
            }
        }

        if (codigoPessoa != null && nome == null && login == null && status == null) {
            retorno = pessoaService.findById(codigoPessoa).orElse(null);
        } else if (login == null && status == null && nome == null) {
            retorno = pessoaService.findAll();
        } else {
            retorno = pessoaService.findAll(login, status, nome);
        }

        return ResponseEntity.status(HttpStatus.OK).body(retorno == null ? Collections.emptyList() : retorno);

    }

    @Operation(summary = "Atualizar pessoa", method = "PUT")
    @PutMapping
    public ResponseEntity<Object> update(@RequestBody @NotNull PessoaRecordUpdateDto pessoaRecordUpdateDto) {
        // validacão de paramentros
        var validacaoMensagem = Utils.validarViolacoes(pessoaRecordUpdateDto);
        if (validacaoMensagem.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(validacaoMensagem.get(), HttpStatus.BAD_REQUEST.value()));
        }

        try {

            // Valida se Nome contaim numero.
            if (pessoaRecordUpdateDto.nome().matches("^[0-9]*$")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Formato inválido para o campo nome", HttpStatus.BAD_REQUEST.value()));
            }
            // Valida se sobrenome contaim numero.
            if (pessoaRecordUpdateDto.sobrenome().matches("^[0-9]*$")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Formato inválido para o campo sobrenome", HttpStatus.BAD_REQUEST.value()));
            }
            // valida ser cep contem letras.
            List<EnderecoRecordUpdateDto> buscaCep = pessoaRecordUpdateDto.enderecos().stream().filter(endereco -> endereco.cep().matches("^[a-zA-Z0-9]*[a-zA-Z]+[A-Za-z0-9]*$")).toList();
            if (!buscaCep.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Formato inválido para o campo CEP, informe somente numeros", HttpStatus.BAD_REQUEST.value()));
            }
            // Valida se pessoa ja existe
            Optional<PessoaModel> validaCodigoPessoa = pessoaService.findById(pessoaRecordUpdateDto.codigoPessoa());
            if (validaCodigoPessoa.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Pessoa não cadastrada na base de dados, informe um id válido", HttpStatus.BAD_REQUEST.value()));
            }

            // valida de enderecos esta vazio
            if (pessoaRecordUpdateDto.enderecos().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("O endereço está vazio, informe no minimo um endereço", HttpStatus.BAD_REQUEST.value()));
            }
            //valida se existe login cadastrada
            Optional<PessoaModel> buscaLogin = pessoaService.findByLogin(pessoaRecordUpdateDto.login());

            if (buscaLogin.isPresent() && !buscaLogin.get().getCodigoPessoa().equals(pessoaRecordUpdateDto.codigoPessoa())  ) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Login já cadastrado na base de dados", HttpStatus.BAD_REQUEST.value()));
            }

            // Valida se codigoBairro existe.
            List<Long> codigosBairrosCadastrados = bairroService.findAll().stream().map(BairroRecordUpdateDto::codigoBairro).toList();
            List<Long> codigosBairroPessoaDto = pessoaRecordUpdateDto.enderecos().stream().map(dto->dto.codigoBairro()).toList();
            List<Long> bairrosNaoCadastrados = codigosBairroPessoaDto.stream().filter(codigoBairro->!codigosBairrosCadastrados.contains(codigoBairro)).toList();
            if (!bairrosNaoCadastrados.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Bairros " + bairrosNaoCadastrados.toString() + " não estão cadastrados na base de dados", HttpStatus.BAD_REQUEST.value()));
            }
            //deletar endereços não enviados
            List<Long> codigosEnderecoPessoaBanco = enderecoService.findIdsAndressByPerson(validaCodigoPessoa.get().getCodigoPessoa());
            List<Long> codigosEnderecoPessoaDto = pessoaRecordUpdateDto.enderecos() != null ? pessoaRecordUpdateDto.enderecos().stream().map(endereco -> endereco.codigoEndereco()).toList() : new ArrayList<>();
            List<Long> codigoEnderecoDelete = codigosEnderecoPessoaBanco.stream().filter(endereco -> !codigosEnderecoPessoaDto.contains(endereco)).toList();
            codigoEnderecoDelete.forEach(endereco -> enderecoService.delete(endereco));

            //salva e atualiza enderecos
            if (pessoaRecordUpdateDto.enderecos() != null) {
                pessoaRecordUpdateDto.enderecos().stream().map(endereco -> new EnderecoModel(endereco)).forEach(
                        enderecoModel -> enderecoService.save(enderecoModel));
            }
            // salva pessoa
            var pessoaModel = new PessoaModel(pessoaRecordUpdateDto);
            pessoaService.save(pessoaModel);
            return ResponseEntity.status(HttpStatus.OK).body(pessoaService.findAll());
        } catch (PersistenceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Falha ao atualiar pessoa", HttpStatus.BAD_REQUEST.value()));
        }
    }

    @Operation(summary = "Desativar pessoa", method = "DELETE")
    @DeleteMapping
    public ResponseEntity<Object> delete(long codigoPessoa) {
        try {
            pessoaService.desativarPessoa(codigoPessoa);
            return ResponseEntity.status(HttpStatus.OK).body(new ErrorDto("Pessoa desativada com sucesso ", HttpStatus.OK.value()));
        } catch (PersistenceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDto(e.getMessage(), HttpStatus.CONFLICT.value()));
        }

    }
}

