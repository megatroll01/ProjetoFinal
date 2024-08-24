<template>
  <v-container class="pa-4">
    <!-- Formulário para Cadastro/Atualização de Funcionário -->
    <v-form ref="form" @submit.prevent="isEditing ? updateFuncionario() : submitForm()">
      <v-row>
        <v-col cols="12">
          <h1 class="text-center">{{ isEditing ? 'Editar Funcionário' : 'Cadastro de Funcionário' }}</h1>
        </v-col>

        <v-col cols="12" md="6">
          <v-text-field
            v-model="nome"
            label="Nome"
            :rules="[rules.required]"
            clearable
          />
        </v-col>

        <v-col cols="12" md="6">
          <v-text-field
            v-model="email"
            label="Email"
            :rules="[rules.required, rules.email]"
            clearable
          />
        </v-col>

        <v-col cols="12" md="6">
          <v-select
            v-model="cargo"
            :items="cargos"
            label="Cargo"
            :rules="[rules.required]"
            clearable
          />
        </v-col>

        <v-col cols="12" md="6">
          <v-text-field
            v-model="cpf"
            label="CPF"
            :rules="[rules.required]"
            clearable
          />
        </v-col>

        <v-col cols="12" md="6">
          <v-text-field
            v-model="telefone"
            label="Telefone"
            :rules="[rules.required]"
            clearable
          />
        </v-col>

        <v-col cols="12" md="6">
          <v-text-field
            v-model="login"
            label="Login"
            :rules="[rules.required]"
            clearable
          />
        </v-col>

        <v-col cols="12" md="6">
          <v-text-field
            v-model="password"
            label="Senha"
            type="password"
            :rules="[rules.required]"
            clearable
          />
        </v-col>

        <v-col cols="12">
          <v-btn
            type="submit"
            color="primary"
            :disabled="!permitidoCadastrar"
            class="mr-2"
          >
            {{ isEditing ? 'Atualizar' : 'Cadastrar' }}
          </v-btn>

          <v-btn
            color="secondary"
            @click="cancelarEdicao"
            v-if="isEditing"
          >
            Cancelar
          </v-btn>
        </v-col>
      </v-row>
    </v-form>

    <!-- Tabela de Funcionários -->
    <v-data-table
      v-if="listaFuncionarios.length"
      :headers="headers"
      :items="listaFuncionarios"
      item-key="id"
      class="mt-4"
      :items-per-page="itemsPerPage"
      :footer-props="{
        'items-per-page-options': [5, 10, 15, 20],
        'show-first-last-page': true,
      }"
    >
      <template v-slot:item="{ item }">
        <tr>
          <td>{{ item.nome }}</td>
          <td>{{ item.email }}</td>
          <td>{{ getCargoLabel(item.nivelAcesso) }}</td>
          <td>{{ item.cpf }}</td>
          <td>{{ item.telefone }}</td>
          <td>{{ item.login }}</td>
          <td>
            <v-btn @click="editFuncionario(item)" icon>
              <v-icon>mdi-pencil</v-icon>
            </v-btn>
            <v-btn
              @click="removerFuncionario(item)"
              icon
              :disabled="!podeRemover(item)"
            >
              <v-icon>mdi-delete</v-icon>
            </v-btn>
          </td>
        </tr>
      </template>
      <template v-slot:no-data>
        <v-alert type="info" :value="true">
          Nenhum funcionário encontrado.
        </v-alert>
      </template>
    </v-data-table>
  </v-container>
</template>

<script>
import axiosInstance from '@/plugins/axios';

export default {
  data() {
    return {
      nome: '',
      email: '',
      cargo: '',
      cpf: '',
      telefone: '',
      login: '',
      password: '',
      cargos: ['Gerente', 'Vendedor'],
      listaFuncionarios: [],
      usuarioLogado: null,
      isEditing: false,
      funcionarioEditandoId: null,
      rules: {
        required: value => !!value || 'Campo obrigatório',
        email: value => /.+@.+\..+/.test(value) || 'E-mail inválido',
      },
      headers: [
        { text: 'Nome', value: 'nome' },
        { text: 'Email', value: 'email' },
        { text: 'Cargo', value: 'cargo' },
        { text: 'CPF', value: 'cpf' },
        { text: 'Telefone', value: 'telefone' },
        { text: 'Login', value: 'login' },
        { text: 'Ações', value: 'actions', sortable: false },
      ],
      itemsPerPage: 10,
    };
  },
  computed: {
    permitidoCadastrar() {
      return this.usuarioLogado && [0, 1].includes(this.usuarioLogado.nivelAcesso);
    }
  },
  methods: {
    async submitForm() {
      if (this.$refs.form.validate() && this.permitidoCadastrar) {
        const cargoValue = this.cargo === 'Gerente' ? 1 : 2;
        const dadosFuncionario = {
          nome: this.nome,
          email: this.email,
          nivelAcesso: cargoValue,
          cpf: this.cpf,
          telefone: this.telefone,
          login: this.login,
          password: this.password,
        };
        try {
          const response = await axiosInstance.post('/usuarios', dadosFuncionario);
          this.listaFuncionarios.push(response.data.data);
          this.resetForm();
        } catch (error) {
          console.error('Erro ao cadastrar funcionário:', error.response ? error.response.data : error.message);
        }
      }
    },
    async updateFuncionario() {
      if (this.$refs.form.validate() && this.permitidoCadastrar) {
        const cargoValue = this.cargo === 'Gerente' ? 1 : 2;
        const dadosFuncionario = {
          nome: this.nome,
          email: this.email,
          nivelAcesso: cargoValue,
          cpf: this.cpf,
          telefone: this.telefone,
          login: this.login,
          password: this.password,
        };
        try {
          await axiosInstance.put(`/usuarios/${this.funcionarioEditandoId}`, dadosFuncionario);
          this.listaFuncionarios = this.listaFuncionarios.map(funcionario =>
            funcionario.id === this.funcionarioEditandoId ? { ...funcionario, ...dadosFuncionario } : funcionario
          );
          this.resetForm();
        } catch (error) {
          console.error('Erro ao atualizar funcionário:', error.response ? error.response.data : error.message);
        }
      }
    },
    async carregarFuncionarios() {
      try {
        const response = await axiosInstance.get('/usuarios');
        this.listaFuncionarios = response.data.data;
      } catch (error) {
        console.error('Erro ao carregar funcionários:', error.response ? error.response.data : error.message);
      }
    },
    podeRemover(funcionario) {
      if (this.usuarioLogado.nivelAcesso === 1) {
        return funcionario.cargo !== 0 && funcionario.login !== this.usuarioLogado.login;
      } else if (this.usuarioLogado.nivelAcesso === 0) {
        return funcionario.login !== this.usuarioLogado.login;
      }
      return false;
    },
    async removerFuncionario(funcionario) {
      if (this.podeRemover(funcionario)) {
        try {
          await axiosInstance.delete(`/usuarios/${funcionario.id}`);
          this.listaFuncionarios = this.listaFuncionarios.filter(f => f.id !== funcionario.id);
        } catch (error) {
          console.error('Erro ao remover funcionário:', error.response ? error.response.data : error.message);
        }
      }
    },
    editFuncionario(funcionario) {
      this.nome = funcionario.nome;
      this.email = funcionario.email;
      this.cargo = funcionario.nivelAcesso === 1 ? 'Gerente' : 'Vendedor';
      this.cpf = funcionario.cpf;
      this.telefone = funcionario.telefone;
      this.login = funcionario.login;
      this.password = funcionario.password;
      this.funcionarioEditandoId = funcionario.id;
      this.isEditing = true;
    },
    resetForm() {
      this.nome = '';
      this.email = '';
      this.cargo = '';
      this.cpf = '';
      this.telefone = '';
      this.login = '';
      this.password = '';
      this.isEditing = false;
      this.funcionarioEditandoId = null;
      this.$refs.form.reset();
    },
    cancelarEdicao() {
      this.resetForm();
    },
    async verificarUsuarioLogado() {
      this.usuarioLogado = JSON.parse(localStorage.getItem('usuario'));
    },
    getCargoLabel(cargoValue) {
      switch (cargoValue) {
        case 1:
          return 'Gerente';
        case 2:
          return 'Vendedor';
        default:
          return 'MASTER';
      }
    }
  },
  mounted() {
    this.verificarUsuarioLogado();
    this.carregarFuncionarios();
  }
};
</script>

<style scoped>
.pa-4 {
  padding: 16px;
}
.text-center {
  text-align: center;
}
</style>
