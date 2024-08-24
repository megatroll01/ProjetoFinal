<template>
  <v-form class="mg-20" @submit.prevent="submitForm">
    <v-row>
      <v-col cols="12" class="text-center">
        <h1 class="mb-4">Vendas</h1>
      </v-col>
    </v-row>

    <v-alert type="error" v-if="semProduto">Produto não selecionado!</v-alert>
    
    <v-divider></v-divider>

    <v-list class="produto-list mt-4" v-if="produtos && produtos.length">
      <v-list-item v-for="(produto, index) in produtos" :key="produto.id" class="mb-2">
        <v-card>
          <v-card-title>{{ produto.nome }}</v-card-title>
          <v-card-subtitle>Preço: {{ produto.preco.toFixed(2) }}</v-card-subtitle>
          <v-card-subtitle>Estoque: {{ produto.quantidadeEstoque }}</v-card-subtitle>

          <v-card-actions>
            <v-btn @click="adicionarProduto(index)" color="primary" outlined>
              <v-icon left>mdi-plus</v-icon>Adicionar
            </v-btn>
            <v-btn @click="removerProduto(index)" :disabled="!produto.quantidadeSelecionada" outlined>
              <v-icon left>mdi-minus</v-icon>Remover
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-list-item>
    </v-list>

    <v-alert type="error" class="mt-4" v-if="semVenda">Nenhum produto na venda!</v-alert>

    <v-list v-if="listaProdutos && listaProdutos.length" class="mt-4">
      <v-list-item v-for="(produto, index) in listaProdutos" :key="index">
        <v-list-item-content class="d-flex align-center">
          <v-list-item-title>{{ produto.produto }}</v-list-item-title>
          <v-list-item-subtitle class="mg_sub">Quantidade: {{ produto.quantidade }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
    </v-list>

    <v-divider></v-divider>

    <v-card class="mt-4 pa-3">
      <v-card-title>Total: {{ valorTotal.toFixed(2) }}</v-card-title>
    </v-card>

    <v-row class="mt-4">
      <v-col cols="6">
        <v-btn :disabled="semProduto || listaProdutos.length === 0" type="submit" color="primary" block>
          Cadastrar
        </v-btn>
      </v-col>
      <v-col cols="6">
        <v-btn @click="abrirModalVendas" color="secondary" block>
          Carregar Vendas
        </v-btn>
      </v-col>
    </v-row>

    <v-dialog v-model="mostrarModalVendas" max-width="800px">
      <v-card>
        <v-card-title class="text-h5">Histórico de Vendas</v-card-title>
        <v-card-text class="modal-body">
          <v-list v-if="vendas && vendas.length">
            <v-list-item-group v-for="(venda) in vendas" :key="venda.id" class="mb-3">
              <v-list-item>
                <v-list-item-content>
                  <v-list-item-title>{{ venda.id }} - {{ new Date(venda.dataVenda).toLocaleDateString() }}</v-list-item-title>
                  <v-list-item-subtitle>Total: {{ venda.total.toFixed(2) }}</v-list-item-subtitle>
                  <v-list dense>
                    <v-list-item-group>
                      <v-list-item v-for="(item) in venda.itens" :key="item.id">
                        <v-list-item-content>
                          <v-list-item-title>{{ item.produto.nome }}</v-list-item-title>
                          <v-list-item-subtitle>
                            Quantidade: {{ item.quantidade }} | Preço Unitário: {{ item.precoUnitario.toFixed(2) }}
                          </v-list-item-subtitle>
                        </v-list-item-content>
                      </v-list-item>
                    </v-list-item-group>
                  </v-list>
                </v-list-item-content>
              </v-list-item>
            </v-list-item-group>
          </v-list>
          <v-alert v-else type="info">Nenhuma venda encontrada.</v-alert>
        </v-card-text>
        <v-card-actions>
          <v-btn color="primary" @click="fecharModalVendas">Fechar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-form>
</template>

<script>
import axiosInstance from '@/plugins/axios';

export default {
  data() {
    return {
      produtos: [],
      listaProdutos: [],
      semProduto: false,
      semVenda: false,
      vendas: [],
      mostrarModalVendas: false,
      valorTotal: 0,
    };
  },
  methods: {
    async submitForm() {
      this.semVenda = this.listaProdutos.length === 0;

      if (!this.semVenda) {
        const dadosVenda = {
          dataVenda: new Date().toISOString().split('T')[0],
          total: parseFloat(this.valorTotal.toFixed(2)),
          itens: this.listaProdutos.map(item => ({
            produto: { id: this.produtos.find(p => p.nome === item.produto).id },
            quantidade: item.quantidade,
            precoUnitario: parseFloat(item.precoUnitario.toFixed(2)),
          })),
          funcionario: { id: JSON.parse(localStorage.getItem('usuario')).id },
        };

        try {
          await axiosInstance.post('/vendas', dadosVenda);
          this.listaProdutos = [];
          this.valorTotal = 0;
          await this.atualizarEstoque();
        } catch (error) {
          console.error('Erro ao cadastrar venda:', error);
        }
      }
    },
    async carregarVendas() {
      try {
        const response = await axiosInstance.get('/vendas');
        this.vendas = response.data.data;
      } catch (error) {
        console.error('Erro ao carregar vendas:', error);
      }
    },
    abrirModalVendas() {
      this.carregarVendas();
      this.mostrarModalVendas = true;
    },
    fecharModalVendas() {
      this.mostrarModalVendas = false;
    },
    adicionarProduto(index) {
      const produto = this.produtos[index];

      if (produto.quantidadeEstoque > 0) {
        produto.quantidadeEstoque--;
        const itemVenda = this.listaProdutos.find(p => p.produto === produto.nome);

        if (itemVenda) {
          itemVenda.quantidade++;
        } else {
          this.listaProdutos.push({
            produto: produto.nome,
            quantidade: 1,
            precoUnitario: produto.preco,
          });
        }
        this.calcularTotal();
      } else {
        alert('Quantidade não disponível em estoque!');
      }
    },
    removerProduto(index) {
      const produto = this.produtos[index];
      const itemVenda = this.listaProdutos.find(p => p.produto === produto.nome);

      if (itemVenda) {
        if (itemVenda.quantidade > 1) {
          itemVenda.quantidade--;
        } else {
          this.listaProdutos = this.listaProdutos.filter(p => p.produto !== produto.nome);
        }
        produto.quantidadeEstoque++;
        this.calcularTotal();
      }
    },
    calcularTotal() {
      this.valorTotal = this.listaProdutos.reduce(
        (total, produto) => total + produto.quantidade * produto.precoUnitario,
        0
      );
      this.valorTotal = parseFloat(this.valorTotal.toFixed(2));
    },
    async atualizarEstoque() {
      for (const item of this.listaProdutos) {
        const produto = this.produtos.find(p => p.nome === item.produto);
        if (produto) {
          await axiosInstance.put(`/produtos/${produto.id}`, {
            ...produto,
            quantidadeEstoque: produto.quantidadeEstoque,
          });
        }
      }
    },
  },
  async created() {
    try {
      const response = await axiosInstance.get('/produtos');
      this.produtos = response.data.data.map(produto => ({
        nome: produto.nome,
        preco: produto.preco,
        quantidadeEstoque: produto.quantidadeEstoque,
        id: produto.id,
      }));
    } catch (error) {
      console.error('Erro ao carregar produtos:', error);
    }
  },
};
</script>

<style scoped>
.erro {
  color: red;
}
.mg-20 {
  margin: 20px;
}
.produto-list {
  max-height: 300px;
  overflow-y: auto;
}
.mg_sub {
  margin-left: 20px;
}
.total {
  font-size: 1.2em;
  font-weight: bold;
}
.modal-body {
  max-height: 400px;
  overflow-y: auto;
}
</style>
