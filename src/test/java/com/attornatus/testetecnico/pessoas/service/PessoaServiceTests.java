package com.attornatus.testetecnico.pessoas.service;

import com.attornatus.testetecnico.pessoas.model.Endereco;
import com.attornatus.testetecnico.pessoas.model.Pessoa;
import com.attornatus.testetecnico.pessoas.repository.PessoaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PessoaServiceTests {

    @InjectMocks
    private PessoaService pessoaService;

    @Mock
    private PessoaRepository pessoaRepository;

    @Test
    void testGetAllPessoasWithEmptiesEnderecos() {
        Pessoa pessoa = new Pessoa("Teste", "01/01/01", Collections.emptyList());

        List<Pessoa> pessoasList = List.of(pessoa);

        when(pessoaRepository.findAll()).thenReturn(pessoasList);

        List<Pessoa> pessoaListResult = pessoaService.findAll();

        Assertions.assertEquals(pessoaListResult.isEmpty(), false);
        Assertions.assertEquals(pessoaListResult.get(0).getEnderecos().isEmpty(), true);
        Assertions.assertEquals(pessoaListResult.get(0), pessoasList.get(0));

        verify(pessoaRepository).findAll();
    }

    @Test
    void testGetAllPessoasWithFilledEnderecos() {
        Endereco endereco = new Endereco("Rua Teste", "60000000", "123", "Fortaleza", true);
        List<Endereco> enderecosList = List.of(endereco);

        Pessoa pessoa = new Pessoa("Teste", "01/01/01", enderecosList);
        List<Pessoa> pessoasList = List.of(pessoa);

        when(pessoaRepository.findAll()).thenReturn(pessoasList);

        List<Pessoa> pessoaListResult = pessoaService.findAll();

        Assertions.assertEquals(pessoaListResult.isEmpty(), false);
        Assertions.assertEquals(pessoaListResult.get(0), pessoasList.get(0));
        Assertions.assertEquals(pessoaListResult.get(0).getEnderecos().isEmpty(), false);
        Assertions.assertEquals(pessoaListResult.get(0).getEnderecos(), enderecosList);

        verify(pessoaRepository).findAll();
    }

    @Test
    void testGetPessoaById() {
        Endereco endereco = new Endereco("Rua Teste", "60000000", "123", "Fortaleza", true);
        List<Endereco> enderecosList = List.of(endereco);

        Pessoa pessoa = new Pessoa("Teste", "01/01/01", enderecosList);
        pessoa.setId(1);

        when(pessoaRepository.findById(1)).thenReturn(Optional.of(pessoa));

        Pessoa pessoaFinded = pessoaService.findById(1).get();

        Assertions.assertEquals(pessoaFinded, pessoa);

        verify(pessoaRepository).findById(1);
    }

    @Test
    void testCreatePessoa() {
        Pessoa pessoa = new Pessoa("Teste", "01/01/01", Collections.emptyList());

        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        Pessoa pessoaResult = pessoaService.insert(pessoa);

        Assertions.assertEquals(pessoaResult, pessoa);

        verify(pessoaRepository).save(pessoa);
    }

    @Test
    void testUpdatePessoa() {
        Pessoa pessoa = new Pessoa("Teste", "01/01/01", Collections.emptyList());
        pessoa.setId(1);

        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        pessoaService.insert(pessoa);

        pessoaService.update(pessoa);

        verify(pessoaRepository).findById(1);
        verify(pessoaRepository).save(pessoa);
    }

    @Test
    void testUpdateWhenPessoaIsNotFound() {
        Pessoa pessoa = new Pessoa("Teste", "01/01/01", Collections.emptyList());
        pessoa.setId(1);

        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        pessoaService.update(pessoa);

        verify(pessoaRepository).findById(1);
        verify(pessoaRepository, never()).save(pessoa);
    }

}
