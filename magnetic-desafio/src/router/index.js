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
    meta: { requiresAuth: true, requiresAccessLevel: [0,1,2] } 
  },
  { 
    path: '/cadastro-funcionario', 
    name: 'CadastroFuncionario', 
    component: CadastroFuncionario, 
    meta: { requiresAuth: true, requiresAccessLevel: [0, 1,2] } 
  },
  { 
    path: '/cadastro-produtos', 
    name: 'CadastroProdutos', 
    component: CadastroProdutos, 
    meta: { requiresAuth: true, requiresAccessLevel: [0, 1, 2] } 
  },
  { path: '/:pathMatch(.*)*', redirect: '/' }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

function usuarioAutenticado() {
  const token = localStorage.getItem('token');
  return token !== null && token !== '';
}

function recuperarUsuario() {
  const usuario = localStorage.getItem('usuario');
  return usuario ? JSON.parse(usuario) : null;
}

router.beforeEach((to, from, next) => {
  const usuario = recuperarUsuario();

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
