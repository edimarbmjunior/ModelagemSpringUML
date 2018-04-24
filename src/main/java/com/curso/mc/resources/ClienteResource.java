package com.curso.mc.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.curso.mc.domain.Cliente;
import com.curso.mc.services.ClienteService;

@RestController
@RequestMapping(value="/clientes")
public class ClienteResource {
	
	@Autowired
	private ClienteService service;
	
	@RequestMapping(value="/{idCliente}",method=RequestMethod.GET)
	public ResponseEntity<?> find(@PathVariable Integer idCliente) {
		
		Cliente obj = service.buscar(idCliente);
		return ResponseEntity.ok().body(obj);

		
		/*
		//insert into categoria (nome) values ('Informática');
		//insert into categoria (nome) values ('Escritório');
		Categoria cat1 = new Categoria(1l, "Informática");
		Categoria cat2 = new Categoria(2l, "Escritório");
		
		List<Categoria> lista = new ArrayList<>();
		lista.add(cat1);
		lista.add(cat2);
		return lista;*/
	}
}
