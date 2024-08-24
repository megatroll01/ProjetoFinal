<template>
  <v-container class="my-5">
    <v-card class="pa-5">
      <v-form @submit.prevent="submitForm">
        <h1 class="text-center">{{ editMode ? 'Editar Produto' : 'Cadastro de Produto' }}</h1>

        <v-text-field
          v-model="nome"
          label="Nome"
          class="mt-4"
        ></v-text-field>

        <v-text-field
          v-model="preco"
          label="Preço"
          @blur="formatarPreco"
          class="mt-4"
        ></v-text-field>

        <v-textarea
          v-model="descricao"
          label="Descrição"
          class="mt-4"
        ></v-textarea>

        <v-text-field
          v-model="quantidadeEstoque"
          label="Quantidade em Estoque"
          type="number"
          class="mt-4"
        ></v-text-field>

        <v-btn
          type="submit"
          color="primary"
          class="mt-4"
          large
          block
        >
          <v-icon left>{{ editMode ? 'mdi-update' : 'mdi-check' }}</v-icon>
          {{ editMode ? 'Atualizar' : 'Cadastrar' }}
        </v-btn>
      </v-form>

      <v-data-table
        :headers="headers"
        :items="listaProdutos"
        item-key="id"
        class="mt-5"
        :items-per-page="5"
        :search="search"
      >
        <template v-slot:[`item.preco`]="{ item }">
          R$ {{ item.preco.toFixed(2).replace('.', ',') }}
        </template>
        <template v-slot:[`item.actions`]="{ item }">
          <v-btn icon @click="editarProduto(item)">
            <v-icon>mdi-pencil</v-icon>
          </v-btn>
          <v-btn icon @click="removerProduto(item.id)">
            <v-icon>mdi-delete</v-icon>
          </v-btn>
        </template>
        <template v-slot:no-data>
          <v-alert type="info" class="mt-4">
            Nenhum produto encontrado
          </v-alert>
        </template>
      </v-data-table>

      <v-alert v-if="mensagemErro" type="error" dismissible class="mt-4">{{ mensagemErro }}</v-alert>
      <v-alert v-if="mensagemSucesso" type="success" dismissible class="mt-4">{{ mensagemSucesso }}</v-alert>
    </v-card>
  </v-container>
</template>

<script>
import axiosInstance from "@/plugins/axios";

export default {
  data() {
    return {
      nome: "",
      preco: "",
      descricao: "",
      quantidadeEstoque: "",
      listaProdutos: [],
      mensagemErro: "",
      mensagemSucesso: "",
      editMode: false,
      produtoEditando: null,
      headers: [
        { text: 'Nome', value: 'nome' },
        { text: 'Preço', value: 'preco' },
        { text: 'Descrição', value: 'descricao' },
        { text: 'Quantidade em Estoque', value: 'quantidadeEstoque' },
        { text: 'Ações', value: 'actions', sortable: false }
      ],
      search: ''
    };
  },
  computed: {
    precoValido() {
      const precoLimpo = this.preco.replace(/[^\d,]/g, '');
      const precoFormatado = parseFloat(precoLimpo.replace(",", "."));
      return !isNaN(precoFormatado);
    },
    quantidadeEstoqueValida() {
      return !isNaN(parseInt(this.quantidadeEstoque)) && parseInt(this.quantidadeEstoque) >= 0;
    },
    formValido() {
      return this.nome && this.precoValido && this.descricao && this.quantidadeEstoqueValida;
    },
  },
  mounted() {
    this.carregarProdutos();
  },
  methods: {
    async submitForm() {
      if (this.formValido) {
        const precoLimpo = this.preco.replace(/[^\d,]/g, '');
        const precoFormatado = parseFloat(precoLimpo.replace(",", "."));
        const dadosProduto = {
          nome: this.nome,
          preco: precoFormatado,
          descricao: this.descricao,
          quantidadeEstoque: parseInt(this.quantidadeEstoque),
        };

        try {
          if (this.editMode) {
            await axiosInstance.put(`/produtos/${this.produtoEditando.id}`, dadosProduto);
            this.mensagemSucesso = "Produto atualizado com sucesso!";
          } else {
            await axiosInstance.post("/produtos", dadosProduto);
            this.mensagemSucesso = "Produto cadastrado com sucesso!";
          }
          this.resetForm();
          this.carregarProdutos();
        } catch (error) {
          this.mensagemErro = this.editMode ? "Erro ao atualizar o produto." : "Erro ao cadastrar o produto.";
          console.error("Erro:", error);
        } finally {
          setTimeout(() => {
            this.mensagemErro = "";
            this.mensagemSucesso = "";
          }, 5000);
        }
      }
    },

    async carregarProdutos() {
      try {
        const response = await axiosInstance.get("/produtos");
        this.listaProdutos = response.data.data;
      } catch (error) {
        this.mensagemErro = "Erro ao carregar produtos.";
        console.error("Erro ao carregar produtos:", error);
        setTimeout(() => {
          this.mensagemErro = "";
        }, 5000);
      }
    },
    async removerProduto(id) {
      try {
        await axiosInstance.delete(`/produtos/${id}`);
        this.carregarProdutos();
        this.mensagemSucesso = "Produto removido com sucesso!";
        setTimeout(() => {
          this.mensagemSucesso = "";
        }, 5000);
      } catch (error) {
        this.mensagemErro = "Erro ao remover o produto.";
        console.error("Erro ao remover o produto:", error);
        setTimeout(() => {
          this.mensagemErro = "";
        }, 5000);
      }
    },
    formatarPreco() {
      let precoNumerico = this.preco.replace(/\D/g, "");
      if (precoNumerico) {
        let precoFormatado = parseFloat(precoNumerico) / 100;
        this.preco = precoFormatado.toLocaleString("pt-BR", {
          style: "currency",
          currency: "BRL",
        });
      }
    },
    editarProduto(produto) {
      this.nome = produto.nome;
      this.preco = produto.preco.toLocaleString("pt-BR", {
        style: "currency",
        currency: "BRL",
      });
      this.descricao = produto.descricao;
      this.quantidadeEstoque = produto.quantidadeEstoque;
      this.produtoEditando = produto;
      this.editMode = true;
    },
    resetForm() {
      this.nome = "";
      this.preco = "";
      this.descricao = "";
      this.quantidadeEstoque = "";
      this.produtoEditando = null;
      this.editMode = false;
    },
  },
};
</script>

<style scoped>
.text-center {
  text-align: center;
}
.mt-4 {
  margin-top: 16px;
}
.mt-5 {
  margin-top: 20px;
}
.pa-5 {
  padding: 20px;
}
.my-5 {
  margin-top: 20px;
  margin-bottom: 20px;
}
</style>
