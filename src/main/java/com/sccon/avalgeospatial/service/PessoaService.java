package com.sccon.avalgeospatial.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sccon.avalgeospatial.model.Pessoa;
import com.sccon.avalgeospatial.repository.PessoaRepository;

@Service
public class PessoaService {

	@Autowired
	private PessoaRepository pessoaRepository;

	public Pessoa save(Pessoa pessoa) {

		pessoaRepository.save(pessoa);
		return pessoa;

	}

	public List<Pessoa> readAll() {
		List<Pessoa> all = pessoaRepository.findAll();
		return all;

	}

	public Pessoa readOne(Integer id) {

		return pessoaRepository.findById(id).orElseThrow();

	}
	
	public void delete(Integer id) {
		pessoaRepository.deleteById(id);
	}
}
