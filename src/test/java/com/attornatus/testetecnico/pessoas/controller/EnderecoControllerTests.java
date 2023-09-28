package com.attornatus.testetecnico.pessoas.controller;

import com.attornatus.testetecnico.pessoas.model.Endereco;
import com.attornatus.testetecnico.pessoas.model.Pessoa;
import com.attornatus.testetecnico.pessoas.service.EnderecoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
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

import static com.attornatus.testetecnico.pessoas.utils.Utils.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EnderecoControllerTests {

    @InjectMocks
    private EnderecoController enderecoController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnderecoService enderecoService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(enderecoController).build();
    }

    @Test
    void returnOkAndListEnderecosByIdPessoa() throws Exception {
        Pessoa pessoa = new Pessoa("Teste", "01/01/01", Collections.emptyList());
        pessoa.setId(1);

        Endereco endereco = new Endereco("Rua Teste", "60000000", "123", "Fortaleza", true);
        endereco.setPessoa(pessoa);

        List<Endereco> enderecosList = List.of(endereco);

        when(enderecoService.findByIdPessoa(1)).thenReturn(enderecosList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/endereco/pessoa/{idPessa}", 1).contentType(MediaType.APPLICATION_JSON);

        String expected = "[{'logradouro':'Rua Teste','cep':'60000000','numero':'123','cidade':'Fortaleza','enderecoPrincipal':true}]";

        mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expected));
    }

    @Test
    void returnCreatedAndEnderecoSaved() throws Exception {
        Pessoa pessoa = new Pessoa("Teste", "01/01/01", Collections.emptyList());
        pessoa.setId(1);

        Endereco endereco = new Endereco("Rua Teste", "60000000", "123", "Fortaleza", true);
        endereco.setPessoa(pessoa);

        when(enderecoService.insert(anyInt(), any(Endereco.class))).thenReturn(endereco);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/endereco/pessoa/{idPessoa}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(endereco))
                .accept(MediaType.APPLICATION_JSON);

        String expected = "{'logradouro':'Rua Teste','cep':'60000000','numero':'123','cidade':'Fortaleza','enderecoPrincipal':true}";

        mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(expected));
    }

    @Test
    void returnNoContentWhenUpdateEndereco() throws Exception {
        Pessoa pessoa = new Pessoa("Teste", "01/01/01", Collections.emptyList());
        pessoa.setId(1);

        Endereco endereco = new Endereco("Rua Teste", "60000000", "123", "Fortaleza", true);
        endereco.setPessoa(pessoa);

        doNothing().when(enderecoService).update(any(Endereco.class));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/endereco/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(endereco))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(enderecoService).update(any(Endereco.class));
    }

}
