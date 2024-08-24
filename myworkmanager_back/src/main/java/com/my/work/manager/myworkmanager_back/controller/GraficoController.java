package com.my.work.manager.myworkmanager_back.controller;

import com.my.work.manager.myworkmanager_back.model.FuncionarioObj;
import com.my.work.manager.myworkmanager_back.model.VendaObj;
import com.my.work.manager.myworkmanager_back.repository.FuncionarioRepository;
import com.my.work.manager.myworkmanager_back.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/grafico")
public class GraficoController {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @GetMapping("/funcionario/{login}")
public Map<String, List<VendaPorMes>> obterVendasFuncionario(@PathVariable String login) {
    Optional<FuncionarioObj> funcionarioOpt = funcionarioRepository.findByLogin(login);
    if (funcionarioOpt.isPresent()) {
        FuncionarioObj funcionario = funcionarioOpt.get();
        
        // Dados de venda do usuário
        List<VendaObj> vendasFuncionario = vendaRepository.findByFuncionario(funcionario);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        List<VendaPorMes> vendasFuncionarioPorMes = vendasFuncionario.stream()
                .collect(Collectors.groupingBy(venda -> venda.getDataVenda().format(formatter)))
                .entrySet().stream()
                .map(entry -> new VendaPorMes(entry.getKey(), entry.getValue().stream()
                        .mapToDouble(VendaObj::getTotal)
                        .sum()))
                .collect(Collectors.toList());

        LocalDate startDate = LocalDate.now().minusMonths(12);
        LocalDate endDate = LocalDate.now();
        List<VendaObj> vendasGeral = vendaRepository.findByDataVendaBetween(startDate, endDate);
        List<VendaPorMes> vendasGeralPorMes = vendasGeral.stream()
                .collect(Collectors.groupingBy(venda -> venda.getDataVenda().format(formatter)))
                .entrySet().stream()
                .map(entry -> new VendaPorMes(entry.getKey(), entry.getValue().stream()
                        .mapToDouble(VendaObj::getTotal)
                        .sum()))
                .collect(Collectors.toList());

        return Map.of(
                "funcionario", vendasFuncionarioPorMes,
                "geral", vendasGeralPorMes
        );
    }
    return Map.of("funcionario", List.of(), "geral", List.of());
}


    public static class VendaPorMes {
        private String mes;
        private Double total;

        public VendaPorMes(String mes, Double total) {
            this.mes = mes;
            this.total = total;
        }

        public String getMes() {
            return mes;
        }

        public void setMes(String mes) {
            this.mes = mes;
        }

        public Double getTotal() {
            return total;
        }

        public void setTotal(Double total) {
            this.total = total;
        }
    }
}
