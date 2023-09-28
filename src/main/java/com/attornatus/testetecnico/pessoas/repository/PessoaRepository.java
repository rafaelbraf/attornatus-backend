package com.attornatus.testetecnico.pessoas.repository;

import com.attornatus.testetecnico.pessoas.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Integer> {}
