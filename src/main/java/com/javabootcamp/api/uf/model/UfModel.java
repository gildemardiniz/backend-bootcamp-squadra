package com.javabootcamp.api.uf.model;

import com.javabootcamp.api.uf.dto.UfRecordDto;
import com.javabootcamp.api.uf.dto.UfRecordUpdateDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Entity
@Data
@Table(name = "tb_uf", uniqueConstraints = {@UniqueConstraint(columnNames = {"sigla", "nome"})})
@AllArgsConstructor
@NoArgsConstructor
public class UfModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_uf_generator")
    @SequenceGenerator(name = "sequence_uf_generator", sequenceName = "SEQUENCE_UF", allocationSize = 1)
    @Column(name = "codigo_uf")
    private Long codigoUF;

    @Column(unique = true, nullable = false)
    private String sigla;

    @Column(unique = true, nullable = false)
    private String nome;
    private Integer status;

    public UfModel(Long codigoUF) {
        this.codigoUF = codigoUF;
    }

    public UfModel (UfRecordDto dto){
        this.sigla = dto.sigla().toUpperCase();
        this.nome = dto.nome().toUpperCase();
        this.status = dto.status();
    }

    public UfModel(UfRecordUpdateDto dto) {
        this.codigoUF = dto.codigoUF();
        this.sigla = dto.sigla().toUpperCase();
        this.nome = dto.nome().toUpperCase();
        this.status = dto.status();
    }
}
