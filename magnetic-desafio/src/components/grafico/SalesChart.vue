<template>
  <v-container class="bg-white my-5">
    <v-card class="pa-5 bg-white">
      <v-card-text>
        <div class="chart-container" v-if="chartData">
          <Line :data="chartData" :options="chartOptions" />
        </div>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
import { Line } from 'vue-chartjs';
import { Chart as ChartJS, Title, Tooltip, Legend, LineElement, PointElement, CategoryScale, LinearScale } from 'chart.js';

ChartJS.register(Title, Tooltip, Legend, LineElement, PointElement, CategoryScale, LinearScale);

export default {
  name: 'SalesChart',
  components: { Line },
  props: {
    salesData: {
      type: Object,
      default: () => ({
        funcionario: [],
        geral: []
      })
    }
  },
  computed: {
    chartData() {
      const labels = [...new Set([...this.salesData.funcionario.map(item => item.mes), ...this.salesData.geral.map(item => item.mes)])];
      return {
        labels,
        datasets: [
          {
            label: 'Venda do Usuário',
            backgroundColor: '#f87979',
            borderColor: '#f87979',
            data: labels.map(label => {
              const item = this.salesData.funcionario.find(data => data.mes === label);
              return item ? item.total : 0;
            }),
            fill: false
          },
          {
            label: 'Venda Geral',
            backgroundColor: '#7c9aff',
            borderColor: '#7c9aff',
            data: labels.map(label => {
              const item = this.salesData.geral.find(data => data.mes === label);
              return item ? item.total : 0;
            }),
            fill: false
          }
        ]
      };
    },
    chartOptions() {
      return {
        responsive: true,
        plugins: {
          legend: {
            position: 'top'
          },
          tooltip: {
            callbacks: {
              label: function(tooltipItem) {
                return `Vendas: ${tooltipItem.raw}`;
              }
            }
          }
        },
        scales: {
          x: {
            beginAtZero: true
          },
          y: {
            beginAtZero: true
          }
        }
      };
    }
  }
};
</script>

<style scoped>
.chart-container {
  width: 100%;
  height: 400px;
}
</style>
