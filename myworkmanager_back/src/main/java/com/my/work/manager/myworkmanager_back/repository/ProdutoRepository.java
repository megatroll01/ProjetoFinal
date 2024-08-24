package com.my.work.manager.myworkmanager_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.my.work.manager.myworkmanager_back.model.ProdutoObj;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoObj, Long> {
}
