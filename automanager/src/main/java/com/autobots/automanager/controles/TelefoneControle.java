package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.TelefoneAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {
	@Autowired
	private TelefoneRepositorio repositorio;
	@Autowired
	private ClienteRepositorio clienteRepositorio;

	@GetMapping("/{id}")
	public ResponseEntity<Telefone> obterTelefone(@PathVariable long id) {
		return repositorio.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/telefones")
	public ResponseEntity<List<Telefone>> obterTelefones() {
		return ResponseEntity.ok(repositorio.findAll());
	}

	@GetMapping("/cliente/{clienteId}")
	public ResponseEntity<List<Telefone>> obterTelefonesDoCliente(@PathVariable long clienteId) {
		return clienteRepositorio.findById(clienteId)
				.map(cliente -> ResponseEntity.ok(cliente.getTelefones()))
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/cadastro/{clienteId}")
	public ResponseEntity<Cliente> cadastrarTelefone(@PathVariable long clienteId,
			@RequestBody Telefone telefone) {
		Cliente cliente = clienteRepositorio.findById(clienteId).orElse(null);
		if (cliente == null) {
			return ResponseEntity.notFound().build();
		}
		telefone.setId(null);
		cliente.getTelefones().add(telefone);
		return ResponseEntity.status(HttpStatus.CREATED).body(clienteRepositorio.save(cliente));
	}

	@PutMapping("/atualizar")
	public ResponseEntity<Telefone> atualizarTelefone(@RequestBody Telefone atualizacao) {
		if (atualizacao.getId() == null) {
			return ResponseEntity.badRequest().build();
		}
		Telefone telefone = repositorio.findById(atualizacao.getId()).orElse(null);
		if (telefone == null) {
			return ResponseEntity.notFound().build();
		}
		new TelefoneAtualizador().atualizar(telefone, atualizacao);
		return ResponseEntity.ok(repositorio.save(telefone));
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<Void> excluirTelefone(@RequestBody Telefone exclusao) {
		if (exclusao.getId() == null) {
			return ResponseEntity.badRequest().build();
		}
		Cliente cliente = clienteRepositorio.findByTelefonesId(exclusao.getId());
		if (cliente == null) {
			return ResponseEntity.notFound().build();
		}
		cliente.getTelefones().removeIf(t -> exclusao.getId().equals(t.getId()));
		clienteRepositorio.save(cliente);
		return ResponseEntity.ok().build();
	}
}
