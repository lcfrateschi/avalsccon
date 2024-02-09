package com.sccon.avalgeospatial.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@Entity
public class Pessoa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "nome", nullable = false)
	private String nome;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "data_nascimento", nullable = false)
	private LocalDateTime dataNascimento;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "data_admissao", nullable = false)
	private LocalDateTime dataAdmissao;
	
	
	
	
	
	
}
