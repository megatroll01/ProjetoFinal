<template>
  <v-container class="bg-login h-100" fluid fill-height>
    <v-row align="center" justify="center">
      <v-col cols="12" sm="8" md="6">
        <v-card class="elevation-12 mg-t-20">
          <v-card-title class="text-center">
            <h2>Gráfico de Vendas</h2>
          </v-card-title>
          <v-card-text>
            <sales-chart v-if="salesData.funcionario.length && salesData.geral.length" :sales-data="salesData" />
            <v-progress-circular v-else indeterminate color="primary" size="64" class="mx-auto"></v-progress-circular>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import axiosInstance from '@/plugins/axios';
import SalesChart from './grafico/SalesChart.vue';

export default {
  components: {
    SalesChart
  },
  data() {
    return {
      salesData: {
        funcionario: [],
        geral: []
      }
    };
  },
  async created() {
    const user = JSON.parse(localStorage.getItem('usuario'));
    if (user) {
      await this.fetchSalesData(user.login);
    }
  },
  methods: {
    async fetchSalesData(login) {
      try {
        const response = await axiosInstance.get(`/grafico/funcionario/${login}`);
        this.salesData = response.data;
      } catch (error) {
        console.error('Erro ao obter dados de vendas:', error);
      }
    }
  }
};
</script>

<style scoped>
.mg_10 {
  margin-inline: 10%;
}
</style>
