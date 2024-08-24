package com.my.work.manager.myworkmanager_back.service;

import com.my.work.manager.myworkmanager_back.model.ProdutoObj;
import com.my.work.manager.myworkmanager_back.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<ProdutoObj> listarProdutos() {
        return produtoRepository.findAll();
    }

    public Optional<ProdutoObj> buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id);
    }

    public ProdutoObj salvarProduto(ProdutoObj produto) {
        return produtoRepository.save(produto);
    }

    public void deletarProduto(Long id) {
        produtoRepository.deleteById(id);
    }

    public void atualizarEstoque(Long produtoId, int quantidade) {
        ProdutoObj produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (produto.getQuantidadeEstoque() < quantidade) {
            throw new RuntimeException("Quantidade insuficiente em estoque");
        }

        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidade);
        produtoRepository.save(produto);
    }
}
