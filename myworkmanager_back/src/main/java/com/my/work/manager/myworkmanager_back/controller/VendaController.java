package com.my.work.manager.myworkmanager_back.controller;

import com.my.work.manager.myworkmanager_back.model.ItemVendaObj;
import com.my.work.manager.myworkmanager_back.model.VendaObj;
import com.my.work.manager.myworkmanager_back.service.ProdutoService;
import com.my.work.manager.myworkmanager_back.service.VendaService;
import com.my.work.manager.myworkmanager_back.util.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @Autowired 
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<CustomResponse<List<VendaObj>>> listarVendas() {
        List<VendaObj> vendas = vendaService.listarVendas();
        CustomResponse<List<VendaObj>> response = new CustomResponse<>(vendas, HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<VendaObj>> buscarVendaPorId(@PathVariable Long id) {
        Optional<VendaObj> venda = vendaService.buscarVendaPorId(id);
        if (venda.isPresent()) {
            CustomResponse<VendaObj> response = new CustomResponse<>(venda.get(), HttpStatus.OK);
            return ResponseEntity.ok(response);
        } else {
            CustomResponse<VendaObj> response = new CustomResponse<>(null, HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<CustomResponse<VendaObj>> salvarVenda(@RequestBody VendaObj venda) {
        VendaObj vendaSalva = vendaService.salvarVenda(venda);
        for (ItemVendaObj item : venda.getItens()) {
            produtoService.atualizarEstoque(item.getProduto().getId(), item.getQuantidade());
        }
        CustomResponse<VendaObj> response = new CustomResponse<>(vendaSalva, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse<VendaObj>> atualizarVenda(@PathVariable Long id,
            @RequestBody VendaObj vendaAtualizada) {
        Optional<VendaObj> vendaExistente = vendaService.buscarVendaPorId(id);
        if (vendaExistente.isPresent()) {
            vendaAtualizada.setId(id);
            VendaObj vendaSalva = vendaService.salvarVenda(vendaAtualizada);
            CustomResponse<VendaObj> response = new CustomResponse<>(vendaSalva, HttpStatus.OK);
            return ResponseEntity.ok(response);
        } else {
            CustomResponse<VendaObj> response = new CustomResponse<>(null, HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse<Void>> deletarVenda(@PathVariable Long id) {
        vendaService.deletarVenda(id);
        CustomResponse<Void> response = new CustomResponse<>(null, HttpStatus.NO_CONTENT);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/relatorio")
    public ResponseEntity<CustomResponse<List<VendaObj>>> gerarRelatorioPorDatas(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate) {
        List<VendaObj> vendas = vendaService.gerarRelatorioPorDatas(startDate, endDate);
        CustomResponse<List<VendaObj>> response = new CustomResponse<>(vendas, HttpStatus.OK);
        return ResponseEntity.ok(response);
    }
}
