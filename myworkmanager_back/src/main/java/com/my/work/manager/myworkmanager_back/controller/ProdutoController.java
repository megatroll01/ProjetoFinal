package com.my.work.manager.myworkmanager_back.controller;

import com.my.work.manager.myworkmanager_back.model.ProdutoObj;
import com.my.work.manager.myworkmanager_back.service.ProdutoService;
import com.my.work.manager.myworkmanager_back.util.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<CustomResponse<List<ProdutoObj>>> listarProdutos() {
        List<ProdutoObj> produtos = produtoService.listarProdutos();
        CustomResponse<List<ProdutoObj>> response = new CustomResponse<>(produtos, HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<ProdutoObj>> buscarProdutoPorId(@PathVariable Long id) {
        Optional<ProdutoObj> produto = produtoService.buscarProdutoPorId(id);
        if (produto.isPresent()) {
            CustomResponse<ProdutoObj> response = new CustomResponse<>(produto.get(), HttpStatus.OK);
            return ResponseEntity.ok(response);
        } else {
            CustomResponse<ProdutoObj> response = new CustomResponse<>(null, HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<CustomResponse<ProdutoObj>> salvarProduto(@RequestBody ProdutoObj produto) {
        ProdutoObj produtoSalvo = produtoService.salvarProduto(produto);
        CustomResponse<ProdutoObj> response = new CustomResponse<>(produtoSalvo, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse<ProdutoObj>> atualizarProduto(@PathVariable Long id, @RequestBody ProdutoObj produtoAtualizado) {
        Optional<ProdutoObj> produtoExistente = produtoService.buscarProdutoPorId(id);
        if (produtoExistente.isPresent()) {
            produtoAtualizado.setId(id);
            ProdutoObj produtoSalvo = produtoService.salvarProduto(produtoAtualizado);
            CustomResponse<ProdutoObj> response = new CustomResponse<>(produtoSalvo, HttpStatus.OK);
            return ResponseEntity.ok(response);
        } else {
            CustomResponse<ProdutoObj> response = new CustomResponse<>(null, HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse<Void>> deletarProduto(@PathVariable Long id) {
        produtoService.deletarProduto(id);
        CustomResponse<Void> response = new CustomResponse<>(null, HttpStatus.NO_CONTENT);
        return ResponseEntity.noContent().build();
    }
}
