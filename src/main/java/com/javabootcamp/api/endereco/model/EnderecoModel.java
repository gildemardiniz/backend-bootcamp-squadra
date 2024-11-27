package com.javabootcamp.api.endereco.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.javabootcamp.api.bairro.model.BairroModel;
import com.javabootcamp.api.endereco.dto.EnderecoRecordDto;
import com.javabootcamp.api.endereco.dto.EnderecoRecordOutDto;
import com.javabootcamp.api.endereco.dto.EnderecoRecordUpdateDto;
import com.javabootcamp.api.pessoa.model.PessoaModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@Table(name = "tb_endereco")
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_endereco_generator")
    @SequenceGenerator(name = "sequence_endereco_generator", sequenceName = "SEQUENCE_ENDERECO", allocationSize = 1)
    @Column(name = "codigo_endereco", nullable = false)
    private Long codigoEndereco;

    @ManyToOne()
    @JoinColumn(name = "codigo_bairro", nullable = false)
    private BairroModel codigoBairro = new BairroModel();

    @Column(nullable = false)
    private String nomeRua;
    @Column(nullable = false)
    private String numero;
    @Column(nullable = false)
    private String complemento;
    @Column(nullable = false)
    private String cep;

    @ManyToOne()
    @NotNull
    @JoinColumn(name = "codigo_pessoa", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private PessoaModel codigoPessoa = new PessoaModel();

    public EnderecoModel(EnderecoRecordDto enderecoRecordDto) {
        this.codigoBairro.setCodigoBairro(enderecoRecordDto.codigoBairro());
        this.nomeRua = enderecoRecordDto.nomeRua().toUpperCase();
        this.numero = enderecoRecordDto.numero().toUpperCase();
        this.complemento = enderecoRecordDto.complemento().toUpperCase();
        this.cep = enderecoRecordDto.cep();
        this.codigoPessoa.setCodigoPessoa(enderecoRecordDto.codigoPessoa());

    }

    public EnderecoModel( EnderecoRecordUpdateDto enderecoRecordUpdateDto) {
        this.codigoEndereco = enderecoRecordUpdateDto.codigoEndereco();
        this.codigoBairro.setCodigoBairro(enderecoRecordUpdateDto.codigoBairro());
        this.nomeRua = enderecoRecordUpdateDto.nomeRua().toUpperCase();
        this.numero = enderecoRecordUpdateDto.numero().toUpperCase();
        this.complemento = enderecoRecordUpdateDto.complemento().toUpperCase();
        this.cep = enderecoRecordUpdateDto.cep();
        this.codigoPessoa.setCodigoPessoa(enderecoRecordUpdateDto.codigoPessoa());
    }

    public EnderecoModel(EnderecoRecordOutDto enderecoRecordOutDto, Long codigoPessoa) {
        this.codigoBairro.setCodigoBairro(enderecoRecordOutDto.codigoBairro());
        this.nomeRua = enderecoRecordOutDto.nomeRua().toUpperCase();
        this.numero = enderecoRecordOutDto.numero().toUpperCase();
        this.complemento = enderecoRecordOutDto.complemento().toUpperCase();
        this.cep = enderecoRecordOutDto.cep();
        this.codigoPessoa.setCodigoPessoa(codigoPessoa);
    }
}

