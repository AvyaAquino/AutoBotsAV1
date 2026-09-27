# Autobots

## Como executar

Pré-requisitos: Java 17+ e Maven. Se não tiver o Maven instalado, use o wrapper do projeto: `.\mvnw.cmd` no Windows ou `./mvnw` no Linux/macOS.

```bash
cd automanager
mvn spring-boot:run
```

A API irá rodar em `http://localhost:8080`.

## Endpoints

| Entidade  | Listar                      | Buscar                | Cadastrar                               | Atualizar (PUT)        | Excluir (DELETE)      |
|-----------|-----------------------------|-----------------------|-----------------------------------------|------------------------|-----------------------|
| Cliente   | GET `/cliente/clientes`     | GET `/cliente/{id}`   | POST `/cliente/cadastro`                | `/cliente/atualizar`   | `/cliente/excluir`    |
| Documento | GET `/documento/documentos` | GET `/documento/{id}` | POST `/documento/cadastro/{clienteId}`  | `/documento/atualizar` | `/documento/excluir`  |
| Telefone  | GET `/telefone/telefones`   | GET `/telefone/{id}`  | POST `/telefone/cadastro/{clienteId}`   | `/telefone/atualizar`  | `/telefone/excluir`   |
| Endereço  | GET `/endereco/enderecos`   | GET `/endereco/{id}`  | POST `/endereco/cadastro/{clienteId}`   | `/endereco/atualizar`  | `/endereco/excluir`   |
