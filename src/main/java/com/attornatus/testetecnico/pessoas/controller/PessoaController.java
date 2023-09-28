package com.attornatus.testetecnico.pessoas.controller;

import com.attornatus.testetecnico.pessoas.model.Pessoa;
import com.attornatus.testetecnico.pessoas.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    @GetMapping("/")
    private ResponseEntity<List<Pessoa>> findAll() {
        List<Pessoa> listPessoas = pessoaService.findAll();

        return ResponseEntity.ok().body(listPessoas);
    }

    @GetMapping("/{id}")
    private ResponseEntity<Optional<Pessoa>> findById(@PathVariable Integer id) {
        Optional<Pessoa> pessoa = pessoaService.findById(id);

        return ResponseEntity.ok().body(pessoa);
    }

    @PostMapping("/")
    private ResponseEntity<Pessoa> insert(@RequestBody Pessoa pessoa) {
        Pessoa pessoaInserted = pessoaService.insert(pessoa);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(pessoaInserted.getId())
                .toUri();

        return ResponseEntity.created(uri).body(pessoaInserted);
    }

    @PutMapping("/")
    private ResponseEntity<Void> update(@RequestBody Pessoa pessoa) {
        pessoaService.update(pessoa);

        return ResponseEntity.noContent().build();
    }

}
