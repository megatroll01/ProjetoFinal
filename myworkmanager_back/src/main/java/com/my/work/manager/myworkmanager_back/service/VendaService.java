package com.my.work.manager.myworkmanager_back.service;

import com.my.work.manager.myworkmanager_back.model.VendaObj;
import com.my.work.manager.myworkmanager_back.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    public List<VendaObj> listarVendas() {
        return vendaRepository.findAll();
    }

    public Optional<VendaObj> buscarVendaPorId(Long id) {
        return vendaRepository.findById(id);
    }

    public VendaObj salvarVenda(VendaObj venda) {
        return vendaRepository.save(venda);
    }

    public void deletarVenda(Long id) {
        vendaRepository.deleteById(id);
    }

    public List<VendaObj> gerarRelatorioPorDatas(LocalDate startDate, LocalDate endDate) {
        return vendaRepository.findByDataVendaBetween(startDate, endDate);
    }
}
