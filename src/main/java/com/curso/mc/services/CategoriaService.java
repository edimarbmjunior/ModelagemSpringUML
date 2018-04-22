package com.curso.mc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.mc.domain.Categoria;
import com.curso.mc.repositories.CategoriaRepository;
import com.curso.mc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;

	public Categoria buscar(Integer idCategoria) {
		Optional<Categoria> obj = repo.findById(idCategoria);
		//return obj.orElse(null);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + idCategoria + ", Tipo: " + Categoria.class.getName()));
	}
}
