package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.entidades.Cliente;

public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {
	Cliente findByDocumentosId(Long documentoId);

	Cliente findByTelefonesId(Long telefoneId);

	Cliente findByEnderecoId(Long enderecoId);
}
