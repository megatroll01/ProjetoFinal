package com.my.work.manager.myworkmanager_back.repository;

import com.my.work.manager.myworkmanager_back.model.FuncionarioObj;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionarioRepository extends JpaRepository<FuncionarioObj, Long> {

    Optional<FuncionarioObj> findByLogin(String login);

}
