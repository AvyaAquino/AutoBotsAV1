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
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.DocumentoAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {
	@Autowired
	private DocumentoRepositorio repositorio;
	@Autowired
	private ClienteRepositorio clienteRepositorio;

	@GetMapping("/{id}")
	public ResponseEntity<Documento> obterDocumento(@PathVariable long id) {
		return repositorio.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/documentos")
	public ResponseEntity<List<Documento>> obterDocumentos() {
		return ResponseEntity.ok(repositorio.findAll());
	}

	@GetMapping("/cliente/{clienteId}")
	public ResponseEntity<List<Documento>> obterDocumentosDoCliente(@PathVariable long clienteId) {
		return clienteRepositorio.findById(clienteId)
				.map(cliente -> ResponseEntity.ok(cliente.getDocumentos()))
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/cadastro/{clienteId}")
	public ResponseEntity<Cliente> cadastrarDocumento(@PathVariable long clienteId,
			@RequestBody Documento documento) {
		Cliente cliente = clienteRepositorio.findById(clienteId).orElse(null);
		if (cliente == null) {
			return ResponseEntity.notFound().build();
		}
		documento.setId(null);
		cliente.getDocumentos().add(documento);
		return ResponseEntity.status(HttpStatus.CREATED).body(clienteRepositorio.save(cliente));
	}

	@PutMapping("/atualizar")
	public ResponseEntity<Documento> atualizarDocumento(@RequestBody Documento atualizacao) {
		if (atualizacao.getId() == null) {
			return ResponseEntity.badRequest().build();
		}
		Documento documento = repositorio.findById(atualizacao.getId()).orElse(null);
		if (documento == null) {
			return ResponseEntity.notFound().build();
		}
		new DocumentoAtualizador().atualizar(documento, atualizacao);
		return ResponseEntity.ok(repositorio.save(documento));
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<Void> excluirDocumento(@RequestBody Documento exclusao) {
		if (exclusao.getId() == null) {
			return ResponseEntity.badRequest().build();
		}
		Cliente cliente = clienteRepositorio.findByDocumentosId(exclusao.getId());
		if (cliente == null) {
			return ResponseEntity.notFound().build();
		}
		cliente.getDocumentos().removeIf(d -> exclusao.getId().equals(d.getId()));
		clienteRepositorio.save(cliente);
		return ResponseEntity.ok().build();
	}
}
