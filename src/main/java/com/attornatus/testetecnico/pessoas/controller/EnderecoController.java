package com.attornatus.testetecnico.pessoas.controller;

import com.attornatus.testetecnico.pessoas.model.Endereco;
import com.attornatus.testetecnico.pessoas.service.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/endereco")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @GetMapping("/pessoa/{idPessoa}")
    public ResponseEntity<List<Endereco>> findByIdPessoa(@PathVariable Integer idPessoa) {
        List<Endereco> enderecosDaPessoaList = enderecoService.findByIdPessoa(idPessoa);

        return ResponseEntity.ok().body(enderecosDaPessoaList);
    }

    @PostMapping("/pessoa/{idPessoa}")
    public ResponseEntity<Endereco> insert(@PathVariable Integer idPessoa, @RequestBody Endereco endereco) {
        Endereco enderecoInserted = enderecoService.insert(idPessoa, endereco);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(enderecoInserted.getId())
                .toUri();

        return ResponseEntity.created(uri).body(enderecoInserted);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Endereco endereco) {
        enderecoService.update(endereco);

        return ResponseEntity.noContent().build();
    }

}
