package com.javabootcamp.api.pessoa.model;

import com.javabootcamp.api.endereco.model.EnderecoModel;
import com.javabootcamp.api.pessoa.dto.PessoaRecordDto;
import com.javabootcamp.api.pessoa.dto.PessoaRecordOutputDto;
import com.javabootcamp.api.pessoa.dto.PessoaRecordUpdateDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "tb_pessoa")
@AllArgsConstructor
@NoArgsConstructor
public class PessoaModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_pessoa_generator")
    @SequenceGenerator(name = "sequence_pessoa_generator", sequenceName = "SEQUENCE_PESSOA", allocationSize = 1)
    @Column(name = "codigo_pessoa")
    private Long codigoPessoa;

    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String sobrenome;
    @Column(nullable = false)
    private Integer idade;
    @Column(nullable = false)
    private String login;
    @Column(nullable = false)
    private String senha;
    @Column(nullable = false)
    private Integer status;

    @OneToMany(mappedBy = "codigoPessoa", fetch = FetchType.EAGER)
    private List<EnderecoModel> enderecos = new ArrayList<>();

    public PessoaModel(PessoaRecordDto pessoaRecordDto) {
        this.nome = pessoaRecordDto.nome().toUpperCase();
        this.sobrenome = pessoaRecordDto.sobrenome().toUpperCase();
        this.idade = pessoaRecordDto.idade();
        this.login = pessoaRecordDto.login().toUpperCase();
        this.senha = pessoaRecordDto.senha();
        this.status = pessoaRecordDto.status();
        this.enderecos = new ArrayList<>();
    }

    public PessoaModel(Long codigoPessoa) {
        this.codigoPessoa = codigoPessoa;
    }

    public PessoaModel(@NotNull PessoaRecordUpdateDto pessoaRecordUpdateDto) {
        this.codigoPessoa = pessoaRecordUpdateDto.codigoPessoa();
        this.nome = pessoaRecordUpdateDto.nome().toUpperCase();
        this.sobrenome = pessoaRecordUpdateDto.sobrenome().toUpperCase();
        this.idade = pessoaRecordUpdateDto.idade();
        this.login = pessoaRecordUpdateDto.login().toUpperCase();
        this.senha = pessoaRecordUpdateDto.senha();
        this.status = pessoaRecordUpdateDto.status();
        this.enderecos = new ArrayList<>();
    }
}
