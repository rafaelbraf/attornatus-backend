package com.attornatus.testetecnico.pessoas.service;

import com.attornatus.testetecnico.pessoas.model.Endereco;
import com.attornatus.testetecnico.pessoas.model.Pessoa;
import com.attornatus.testetecnico.pessoas.repository.EnderecoRepository;
import com.attornatus.testetecnico.pessoas.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    public List<Endereco> findByIdPessoa(Integer idPessoa) {
        return enderecoRepository.findByIdPessoa(idPessoa);
    }

    public Endereco insert(Integer idPessoa, Endereco endereco) {
        Pessoa pessoa = pessoaRepository.findById(idPessoa).orElseThrow();

        endereco.setPessoa(pessoa);

        if (endereco.isEnderecoPrincipal()) {
            Optional<Endereco> enderecoPrincipalOptional = enderecoRepository.findEnderecoPrincipal();

            if (enderecoPrincipalOptional.isPresent()) {
                Endereco enderecoPrincipal = enderecoPrincipalOptional.get();
                enderecoPrincipal.setEnderecoPrincipal(false);

                enderecoRepository.save(enderecoPrincipal);
            }
        }

        return enderecoRepository.save(endereco);
    }

    public void update(Endereco endereco) {
        Optional<Endereco> enderecoToUpdate = enderecoRepository.findById(endereco.getId());

        if (!enderecoToUpdate.isPresent()) {
            return;
        }

        Endereco currentEndereco = enderecoToUpdate.get();

        endereco.setPessoa(currentEndereco.getPessoa());

        if (endereco.isEnderecoPrincipal() && !currentEndereco.isEnderecoPrincipal()) {
            Optional<Endereco> enderecoPrincipalOptional = enderecoRepository.findEnderecoPrincipal();

            if (enderecoPrincipalOptional.isPresent()) {
                Endereco enderecoPrincipal = enderecoPrincipalOptional.get();
                enderecoPrincipal.setEnderecoPrincipal(false);

                enderecoRepository.save(enderecoPrincipal);
            }
        }

        enderecoRepository.save(endereco);
    }

}
