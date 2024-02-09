package com.sccon.avalgeospatial.api.controller;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sccon.avalgeospatial.AvalGeospatialApplication;
import com.sccon.avalgeospatial.model.Pessoa;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "SCCONAvalGeospatial", description = "API")
@RestController
public class PessoaController {

	@Operation(
            summary = "List all persons",
            description = "List all persons and their data from map")
	@GetMapping("/person")
	public ResponseEntity<List<Pessoa>> getAllPersons() {
		Map<Integer, Pessoa> mapaPessoas = AvalGeospatialApplication.getPessoaMapa();
		List<Pessoa> pessoasOrdenadas = new ArrayList<>(mapaPessoas.values());
		pessoasOrdenadas.sort((p1, p2) -> p1.getNome().compareTo(p2.getNome()));
		return ResponseEntity.ok(pessoasOrdenadas);
	}

	@Operation(
            summary = "Add person",
            description = "Add person entitie and their data from map")
	@PostMapping("/person")
	public ResponseEntity<?> addPerson(@RequestBody Pessoa pessoa) {
		Map<Integer, Pessoa> mapaPessoas = AvalGeospatialApplication.getPessoaMapa();
		List<Pessoa> pessoas = new ArrayList<>(mapaPessoas.values());
		if (pessoa.getId() == null) {
			int maiorId = 0;
			for (Pessoa p : pessoas) {
				if (p.getId() > maiorId) {
					maiorId = p.getId();
				}
			}
			pessoa.setId(++maiorId);
		} else if (mapaPessoas.containsKey(pessoa.getId())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Pessoa com id " + pessoa.getId() + " ja existe");
		}
		mapaPessoas.put(pessoa.getId(), pessoa);
		return ResponseEntity.ok().build();
	}

	@Operation(
            summary = "Delete person by id",
            description = "Delete person by id parameter from map")
	@DeleteMapping("/person/{id}")
	public ResponseEntity<?> deletePerson(@PathVariable Integer id) {
		Map<Integer, Pessoa> mapaPessoas = AvalGeospatialApplication.getPessoaMapa();
		if (!mapaPessoas.containsKey(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa com id " + id + " nao encontrada");
		}
		mapaPessoas.remove(id);
		return ResponseEntity.ok().build();
	}

	@Operation(
            summary = "Update person by id",
            description = "Update person by id of map")
	@PutMapping("/person/{id}")
	public ResponseEntity<?> updatePerson(@PathVariable Integer id, @RequestBody Pessoa pessoa) {
		Map<Integer, Pessoa> mapaPessoas = AvalGeospatialApplication.getPessoaMapa();
		if (!mapaPessoas.containsKey(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa com id " + id + " nao encontrada.");
		}
		pessoa.setId(id);
		mapaPessoas.put(id, pessoa);
		return ResponseEntity.ok().build();
	}

	@Operation(
            summary = "Update person attribute",
            description = "Update person attribute sepparately of map")
	@PatchMapping("/person/{id}")
	public ResponseEntity<?> updatePersonAttribute(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
		Map<Integer, Pessoa> mapaPessoas = AvalGeospatialApplication.getPessoaMapa();
		if (!mapaPessoas.containsKey(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa com id " + id + " nao encontrada");
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		Pessoa pessoa = mapaPessoas.get(id);
		updates.forEach((key, value) -> {
			switch (key) {
			case "nome":
				pessoa.setNome((String) value);
				break;
			case "dataNascimento":
				pessoa.setDataNascimento(LocalDateTime.parse((String) value, formatter));
				break;
			case "dataAdmissao":
				pessoa.setDataAdmissao(LocalDateTime.parse((String) value, formatter));
				break;
			default:
				// Handle invalid attribute
			}
		});
		mapaPessoas.put(id, pessoa);
		return ResponseEntity.ok().build();
	}

	@Operation(
            summary = "List person by id",
            description = "List person by id from map")
	@GetMapping("/person/{id}")
	public ResponseEntity<?> getPersonById(@PathVariable Integer id) {
		Map<Integer, Pessoa> mapaPessoas = AvalGeospatialApplication.getPessoaMapa();
		if (!mapaPessoas.containsKey(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa com id " + id + " nao encontrada");
		}
		return ResponseEntity.ok(mapaPessoas.get(id));
	}

	@Operation(
            summary = "Get person age by id",
            description = "Get age person by id that receives a output format parameter, which returns the current age of a person in complete according to the passed parameter days|months|years from map")
	@GetMapping("/person/{id}/age")
	public ResponseEntity<?> getPersonAge(@PathVariable Integer id, @RequestParam String output) {
		Map<Integer, Pessoa> mapaPessoas = AvalGeospatialApplication.getPessoaMapa();
		if (!mapaPessoas.containsKey(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa com id " + id + " nao encontrada");
		}
		Pessoa pessoa = mapaPessoas.get(id);
		LocalDate now = LocalDate.now();
		Period period = Period.between(pessoa.getDataNascimento().toLocalDate(), now);
		int years = period.getYears();
		int months = (int) period.toTotalMonths();
		// int days = period.getDays();
		int totalDays = period.getDays(); // Dias restantes após a contagem de anos e meses
		LocalDate dateWithoutDays = pessoa.getDataNascimento().toLocalDate().plusYears(years).plusMonths(months);
		int remainingDays = (int) ChronoUnit.DAYS.between(dateWithoutDays, now);

		int days = (totalDays + remainingDays) * -1;

		switch (output) {
		case "years":
			return ResponseEntity.ok(years);
		case "months":
			return ResponseEntity.ok(months);
		case "days":
			return ResponseEntity.ok(days);
		default:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato de saida invalido");
		}
	}

	@Operation(
            summary = "Get person salary by id",
            description = "Get salary person by id that receives a output format parameter, which returns the current salary of a person in complete according to the passed parameter min|full from map")
	@GetMapping("/person/{id}/salary")
	public ResponseEntity<?> getPersonSalary(@PathVariable Integer id, @RequestParam String output) {

		Map<Integer, Pessoa> mapaPessoas = AvalGeospatialApplication.getPessoaMapa();
		if (!mapaPessoas.containsKey(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa com id " + id + " nao encontrada");
		}

		double salarioBase = 1558.00;
		double salarioAnualPosAumento = salarioBase * 0.18 + 500.00;
		Pessoa pessoa = mapaPessoas.get(id);
		LocalDate now = LocalDate.now();
		int anosTotais = Period.between(pessoa.getDataNascimento().toLocalDate(), now).getYears();
		System.out.println(anosTotais);
		double salarioAtual = salarioBase;

		switch (output) {

		case "full":
			for (int i = 0; i < anosTotais; i++) {
				salarioAtual += salarioAnualPosAumento;
			}
			return ResponseEntity.ok(NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(salarioAtual));

		case "min":
			double salarioMinimo = 1302.00;
			double totalEmSalariosMinimos = salarioAtual / salarioMinimo;
			for (int i = 0; i < anosTotais; i++) {
				totalEmSalariosMinimos += salarioAnualPosAumento / salarioMinimo;
			}
			return ResponseEntity.ok(String.format("%.2f", Math.ceil(totalEmSalariosMinimos * 100) / 100) + " Salarios Minimos");

		default:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato de saida invalido");
		}
	}

}
