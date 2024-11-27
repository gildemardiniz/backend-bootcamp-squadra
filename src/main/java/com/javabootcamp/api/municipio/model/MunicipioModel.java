package com.javabootcamp.api.municipio.model;

import com.javabootcamp.api.municipio.dto.MunicipioRecordDto;
import com.javabootcamp.api.municipio.dto.MunicipioRecordUpdateDto;
import com.javabootcamp.api.uf.model.UfModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@Table(name = "tb_Municipio")
@AllArgsConstructor
@NoArgsConstructor
public class MunicipioModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_municipio_generator")
    @SequenceGenerator(name = "sequence_municipio_generator", sequenceName = "SEQUENCE_MUNICIPIO", allocationSize = 1)
    @Column(name = "codigo_municipio")
    private Long codigoMunicipio;

    @ManyToOne
    @JoinColumn(name = "codigo_uf", nullable = false)
    private UfModel codigoUf = new UfModel();

    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private Integer status;

    public MunicipioModel(Long codigoMunicipio){
        this.codigoMunicipio = codigoMunicipio;
    }

    public MunicipioModel(MunicipioRecordDto dto) {
        this.codigoUf.setCodigoUF(dto.codigoUf());
        this.nome = dto.nome().toUpperCase();
        this.status = dto.status();
    }

    public MunicipioModel(MunicipioRecordUpdateDto dto) {
        this.codigoMunicipio = dto.codigoMunicipio();
        this.codigoUf.setCodigoUF(dto.codigoUf());
        this.nome = dto.nome().toUpperCase();
        this.status = dto.status();
    }

}
