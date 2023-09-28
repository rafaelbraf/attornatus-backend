package com.attornatus.testetecnico.pessoas.service;

import com.attornatus.testetecnico.pessoas.model.Pessoa;
import com.attornatus.testetecnico.pessoas.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    public List<Pessoa> findAll() {
        return pessoaRepository.findAll();
    }

    public Optional<Pessoa> findById(Integer id) {
        return pessoaRepository.findById(id);
    }

    public Pessoa insert(Pessoa pessoa) {
        return pessoaRepository.save(pessoa);
    }

    public void update(Pessoa pessoa) {
        Optional<Pessoa> pessoaToUpdate = pessoaRepository.findById(pessoa.getId());

        if (!pessoaToUpdate.isPresent()) {
            return;
        }

        pessoaRepository.save(pessoa);
    }

}
