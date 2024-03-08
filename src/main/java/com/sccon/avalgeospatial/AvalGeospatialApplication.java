package com.sccon.avalgeospatial;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sccon.avalgeospatial.model.Pessoa;

@SpringBootApplication
public class AvalGeospatialApplication {
	
	private static Map<Integer, Pessoa> pessoaMapa = new HashMap<>();

	private static void inicializarPessoaMapa() {
		pessoaMapa.put(1, new Pessoa(1, "Luiz Claudio Frateschi", LocalDateTime.of(1983, 07, 29, 12, 00), LocalDateTime.of(2024, 2, 8, 16, 0)));
		pessoaMapa.put(2, new Pessoa(2, "Jose da Silva", LocalDateTime.of(2000, 4, 6, 9, 30), LocalDateTime.of(2020, 5, 10, 7, 25)));
		pessoaMapa.put(3, new Pessoa(3, "Giovana Ribeiro", LocalDateTime.of(1991, 1, 6, 14, 55), LocalDateTime.of(2019, 11, 1, 19, 20)));
    }
	
	public static Map<Integer, Pessoa> getPessoaMapa() {
        if (pessoaMapa.isEmpty()) {
            inicializarPessoaMapa();
        }
        return pessoaMapa;
    }
	
	public static void main(String[] args) {
		SpringApplication.run(AvalGeospatialApplication.class, args);
		inicializarPessoaMapa();
	}
	
	

}
