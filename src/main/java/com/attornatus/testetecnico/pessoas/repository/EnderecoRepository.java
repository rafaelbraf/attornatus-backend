package com.attornatus.testetecnico.pessoas.repository;

import com.attornatus.testetecnico.pessoas.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {

    @Query("SELECT e FROM Endereco e WHERE e.pessoa.id = :idPessoa")
    List<Endereco> findByIdPessoa(@Param("idPessoa") Integer idPessoa);

    @Query("SELECT e FROM Endereco e WHERE e.enderecoPrincipal = true")
    Optional<Endereco> findEnderecoPrincipal();

}
