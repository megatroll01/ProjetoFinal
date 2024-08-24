// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router';
import Login from '../components/LoginComponent.vue';
import Dashboard from '../components/DashboardComponent.vue';
import CadastroFuncionario from '../components/CadastroFuncionario.vue';
import CadastroProdutos from '../components/CadastroProdutos.vue';
import Venda from '../components/VendaComponent.vue';

const routes = [
  { path: '/', name: 'Login', component: Login },
  { 
    path: '/venda', 
    name: 'Venda', 
    component: Venda, 
    meta: { requiresAuth: true, requiresAccessLevel: [0, 1, 2] } 
  },
  { 
    path: '/dashboard', 
    name: 'Dashboard', 
    component: Dashboard, 
    meta: { requiresAuth: true, requiresAccessLevel: [0] } 
  },
  { 
    path: '/cadastro-funcionario', 
    name: 'CadastroFuncionario', 
    component: CadastroFuncionario, 
    meta: { requiresAuth: true, requiresAccessLevel: [0, 1] } 
  },
  { 
    path: '/cadastro-produtos', 
    name: 'CadastroProdutos', 
    component: CadastroProdutos, 
    meta: { requiresAuth: true, requiresAccessLevel: [0, 1, 2] } 
  },
  { path: '/:pathMatch(.*)*', redirect: '/' } // Redireciona para o login se a rota não for encontrada
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

function usuarioAutenticado() {
  return localStorage.getItem('token') !== null;
}

router.beforeEach((to, from, next) => {
  const usuario = JSON.parse(localStorage.getItem('usuario'));

  if (to.meta.requiresAuth && !usuarioAutenticado()) {
    next({ name: 'Login' });
  } else if (to.meta.requiresAccessLevel) {
    if (!usuario || !to.meta.requiresAccessLevel.includes(usuario.nivelAcesso)) {
      next({ name: 'Login' });
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router;
