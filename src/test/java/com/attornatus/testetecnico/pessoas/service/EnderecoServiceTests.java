package com.attornatus.testetecnico.pessoas.service;

import com.attornatus.testetecnico.pessoas.controller.EnderecoController;
import com.attornatus.testetecnico.pessoas.model.Endereco;
import com.attornatus.testetecnico.pessoas.model.Pessoa;
import com.attornatus.testetecnico.pessoas.repository.EnderecoRepository;
import com.attornatus.testetecnico.pessoas.repository.PessoaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EnderecoServiceTests {

    @InjectMocks
    private EnderecoService enderecoService;

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private PessoaRepository pessoaRepository;

    @Test
    void testGetEnderecosByIdPessoa() {
        Pessoa pessoa = new Pessoa("Teste", "01/01/01", Collections.emptyList());
        pessoa.setId(1);

        Endereco endereco = new Endereco("Rua Teste", "60000000", "123", "Fortaleza", true);
        endereco.setPessoa(pessoa);
        List<Endereco> enderecosList = List.of(endereco);

        when(enderecoRepository.findByIdPessoa(1)).thenReturn(enderecosList);

        List<Endereco> enderecosListResult = enderecoService.findByIdPessoa(pessoa.getId());

        Assertions.assertEquals(enderecosListResult.isEmpty(), false);
        Assertions.assertEquals(enderecosListResult.get(0), endereco);
        Assertions.assertEquals(enderecosListResult.get(0).getPessoa(), pessoa);

        verify(enderecoRepository).findByIdPessoa(1);
    }

    @Test
    void testInsertEndereco() {
        Pessoa pessoa = new Pessoa("Teste", "01/01/01", Collections.emptyList());
        pessoa.setId(1);

        when(pessoaRepository.findById(1)).thenReturn(Optional.of(pessoa));

        Endereco endereco = new Endereco("Rua Teste", "60000000", "123", "Fortaleza", true);
        endereco.setPessoa(pessoa);

        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);

        Endereco enderecoResult = enderecoService.insert(pessoa.getId(), endereco);

        Assertions.assertEquals(enderecoResult, endereco);
        Assertions.assertEquals(enderecoResult.getPessoa(), pessoa);

        verify(pessoaRepository).findById(1);
        verify(enderecoRepository).save(endereco);
    }

    @Test
    void testInsertNewEnderecoPrincipalWhenExistsOtherEnderecoPrincipal() {
        Pessoa pessoa = new Pessoa("Teste", "01/01/01", Collections.emptyList());
        pessoa.setId(1);

        when(pessoaRepository.findById(1)).thenReturn(Optional.of(pessoa));

        Endereco endereco = new Endereco("Rua Teste", "60000000", "123", "Teste", true);
        endereco.setId(1);
        endereco.setPessoa(pessoa);

        when(enderecoRepository.findEnderecoPrincipal()).thenReturn(Optional.of(endereco));

        Endereco newEndereco = new Endereco("Rua Teste 2", "60000000", "123", "Teste", true);
        endereco.setId(2);
        newEndereco.setPessoa(pessoa);

        when(enderecoRepository.save(any(Endereco.class))).thenReturn(newEndereco);

        Endereco enderecoInserted = enderecoService.insert(1, newEndereco);

        Assertions.assertEquals(endereco.isEnderecoPrincipal(), false);
        Assertions.assertEquals(enderecoInserted.isEnderecoPrincipal(), true);
    }

}
