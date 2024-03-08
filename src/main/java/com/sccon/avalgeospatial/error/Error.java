package com.sccon.avalgeospatial.error;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class Error {

	@JsonIgnore
	private HttpStatus httpStatus;
	
	int codigo;
	
	private String descricao;
	
	private String message;

	public Error(HttpStatus httpStatus, String message) {
		this.codigo = httpStatus.value();
		this.descricao = httpStatus.getReasonPhrase();
		this.message = message;
	}

}
