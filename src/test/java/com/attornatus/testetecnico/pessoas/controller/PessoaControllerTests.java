package com.attornatus.testetecnico.pessoas.controller;

import com.attornatus.testetecnico.pessoas.model.Endereco;
import com.attornatus.testetecnico.pessoas.model.Pessoa;
import com.attornatus.testetecnico.pessoas.service.PessoaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.attornatus.testetecnico.pessoas.utils.Utils.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PessoaControllerTests {

    @InjectMocks
    private PessoaController pessoaController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PessoaService pessoaService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(pessoaController).build();
    }

    @Test
    void returnOkAndListPessoasWithoutEnderecos() throws Exception {
        Pessoa pessoa = new Pessoa("Teste", "01/01/01", Collections.emptyList());

        List<Pessoa> pessoasList = List.of(pessoa);

        when(pessoaService.findAll()).thenReturn(pessoasList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pessoas/").contentType(MediaType.APPLICATION_JSON);

        String expected = "[{'nome':'Teste','dataNascimento':'01/01/01','enderecos':[]}]";

        mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expected));
    }

    @Test
    void returnOkAndListPessoasWithEnderecos() throws Exception {
        Endereco endereco = new Endereco("Rua Teste", "60000000", "123", "Fortaleza", true);
        Pessoa pessoa = new Pessoa("Teste", "01/01/01", List.of(endereco));

        List<Pessoa> pessoasList = List.of(pessoa);

        when(pessoaService.findAll()).thenReturn(pessoasList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pessoas/").contentType(MediaType.APPLICATION_JSON);

        String expected = "[{'nome':'Teste','dataNascimento':'01/01/01','enderecos':[{'logradouro':'Rua Teste','cep':'60000000','numero':'123','cidade':'Fortaleza','enderecoPrincipal':true}]}]";

        mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expected));
    }

    @Test
    void returnOkAndPessoaById() throws Exception {
        Pessoa pessoa = new Pessoa("Teste", "01/01/01", Collections.emptyList());
        pessoa.setId(1);

        when(pessoaService.findById(1)).thenReturn(Optional.of(pessoa));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pessoas/{idPessoa}", 1).contentType(MediaType.APPLICATION_JSON);

        String expected = "{'id':1,'nome':'Teste','dataNascimento':'01/01/01','enderecos':[]}";

        mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expected));
    }

    @Test
    void returnCreatedAndPessoaSaved() throws Exception {
        Pessoa pessoa = new Pessoa("Teste", "01/01/01", Collections.emptyList());
        pessoa.setId(1);

        when(pessoaService.insert(any(Pessoa.class))).thenReturn(pessoa);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/pessoas/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(pessoa))
                .accept(MediaType.APPLICATION_JSON);

        String expected = "{'id':1,'nome':'Teste','dataNascimento':'01/01/01','enderecos':[]}";

        mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(expected));
    }

    @Test
    void returnNoContentWhenUpdatePessoa() throws Exception {
        Pessoa pessoa = new Pessoa("Teste", "01/01/01", Collections.emptyList());
        pessoa.setId(1);

        doNothing().when(pessoaService).update(any(Pessoa.class));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/pessoas/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(pessoa))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(pessoaService).update(any(Pessoa.class));
    }

}
