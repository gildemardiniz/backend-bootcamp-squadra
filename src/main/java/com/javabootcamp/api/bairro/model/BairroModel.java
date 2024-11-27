package com.javabootcamp.api.bairro.model;


import com.javabootcamp.api.bairro.dto.BairroRecordDto;
import com.javabootcamp.api.bairro.dto.BairroRecordUpdateDto;
import com.javabootcamp.api.municipio.model.MunicipioModel;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@Table(name = "tb_Bairro")
@AllArgsConstructor
@NoArgsConstructor
public class BairroModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_bairro_generator")
    @SequenceGenerator(name = "sequence_bairro_generator", sequenceName = "SEQUENCE_BAIRRO", allocationSize = 1)
    @Column(name = "codigo_bairro")
    private Long codigoBairro;

    @ManyToOne
    @JoinColumn(name = "codigo_municipio", nullable = false)
    private MunicipioModel codigoMunicipio = new MunicipioModel();
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private Integer status;

    public BairroModel(BairroRecordDto bairroRecordDto) {
        this.codigoMunicipio.setCodigoMunicipio(bairroRecordDto.codigoMunicipio());
        this.nome = bairroRecordDto.nome().toUpperCase();
        this.status = bairroRecordDto.status();
    }

    public BairroModel(BairroRecordUpdateDto bairroRecordUpdateDto) {
        this.codigoBairro = bairroRecordUpdateDto.codigoBairro();
        this.codigoMunicipio.setCodigoMunicipio(bairroRecordUpdateDto.codigoMunicipio());
        this.nome = bairroRecordUpdateDto.nome().toUpperCase();
        this.status = bairroRecordUpdateDto.status();
    }

    public BairroModel(Long codigoBairro) {
        this.codigoBairro = codigoBairro;
    }
}
