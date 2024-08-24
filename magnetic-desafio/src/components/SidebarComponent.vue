<template>
  <v-app>
    <v-navigation-drawer v-model="drawer" app>
    </v-navigation-drawer>
    <v-app-bar app>
      <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>
      <v-toolbar-title>Minha Aplicação</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn icon @click="cadastroFuncionario">
        <v-icon>mdi-account-plus</v-icon>
      </v-btn>
      <v-btn icon @click="cadastroProdutos">
        <v-icon>mdi-cart-plus</v-icon>
      </v-btn>
      <v-btn icon @click="venda">
        <v-icon>mdi-cash-register</v-icon>
      </v-btn>
      <v-btn icon @click="logout">
        <v-icon>mdi-logout</v-icon>
      </v-btn>
    </v-app-bar>
    <v-main>
    </v-main>
  </v-app>
</template>

<script>
export default {
  data() {
    return {
      drawer: false,
    };
  },
  methods: {
    logout() {
      this.$router.push('/dashboard');
    },
  },
};
</script>