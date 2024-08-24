/* eslint-disable */
<template>
 <v-container class="bg-login h-100" fluid fill-height>
    <v-row align="center" justify="center">
      <v-col cols="12" sm="8" md="4">
        <v-card class="elevation-12 mg-t-20">
          <v-card-title class="text-center">
            <h2>Login</h2>
          </v-card-title>
          <v-card-text>
            <v-form @submit.prevent="login">
              <v-text-field v-model="username" label="Usuário"></v-text-field>
              <v-text-field v-model="password" label="Senha" type="password"></v-text-field>
              <v-label class="mg-5 erro" v-if="erro"> Credenciais inválidas!</v-label>
              <br>
              <v-btn class="mg-5" type="submit" color="primary">Entrar</v-btn>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import axiosInstance from '@/plugins/axios';
export default {
  data() {
    return {
      username: '',
      password: '',
      erro: false
    };
  },
  methods: {
    async login() {
  try {
    const response = await axiosInstance.post('/auth/usuario', {
      login: this.username,
      senha: this.password
    });

    if (response.status === 200 && response.data.data) {
      this.erro = false;
      const user = response.data.data;
      localStorage.setItem('token', '123456'); 
      localStorage.setItem('usuario', JSON.stringify(user));

      this.$router.push('/venda');
    } else {
      this.erro = true;
    }
  } catch (error) {
    this.erro = true;
  }
}
  }
};
</script>

<style>
.erro{
  color: red;
}
.mg-5{
  margin: 5px;
}
.mg-t-20{
  margin-top: 20%;
}
.bg-login{
  background-color: rgb(158, 255, 255);
}
.h-100{
  height: 100%;
}
</style>
