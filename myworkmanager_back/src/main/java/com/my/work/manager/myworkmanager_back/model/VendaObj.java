package com.my.work.manager.myworkmanager_back.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "Venda_TB")
public class VendaObj implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_venda", nullable = false)
    private LocalDate dataVenda;

    @Column(name = "total", nullable = false)
    private Double total;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "venda_id")
    private List<ItemVendaObj> itens;

    @ManyToOne
    @JoinColumn(name = "funcionario_id", nullable = false)
    private FuncionarioObj funcionario;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public List<ItemVendaObj> getItens() {
        return itens;
    }

    public void setItens(List<ItemVendaObj> itens) {
        this.itens = itens;
    }

    public FuncionarioObj getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioObj funcionario) {
        this.funcionario = funcionario;
    }
}
