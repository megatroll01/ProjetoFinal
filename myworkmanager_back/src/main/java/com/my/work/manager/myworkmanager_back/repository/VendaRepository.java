package com.my.work.manager.myworkmanager_back.repository;

import com.my.work.manager.myworkmanager_back.model.FuncionarioObj;
import com.my.work.manager.myworkmanager_back.model.VendaObj;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface VendaRepository extends JpaRepository<VendaObj, Long> {

    List<VendaObj> findByFuncionario(FuncionarioObj funcionario);

    List<VendaObj> findByDataVendaBetween(LocalDate startDate, LocalDate endDate);
}
