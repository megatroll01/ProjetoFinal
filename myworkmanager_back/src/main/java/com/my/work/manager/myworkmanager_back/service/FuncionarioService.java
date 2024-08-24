package com.my.work.manager.myworkmanager_back.service;

import com.my.work.manager.myworkmanager_back.model.FuncionarioObj;
import com.my.work.manager.myworkmanager_back.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    public List<FuncionarioObj> listarUsuarios() {
        return funcionarioRepository.findAll();
    }

    public Optional<FuncionarioObj> buscarUsuarioPorId(Long id) {
        return funcionarioRepository.findById(id);
    }

    public FuncionarioObj salvarUsuario(FuncionarioObj usuario) {
        return funcionarioRepository.save(usuario);
    }

    public void deletarUsuario(Long id) {
        funcionarioRepository.deleteById(id);
    }

    public boolean autenticarUsuario(String login, String password) {
        Optional<FuncionarioObj> usuarioOpt = funcionarioRepository.findByLogin(login);
        return usuarioOpt.isPresent() && usuarioOpt.get().getPassword().equals(password);
    }

    public FuncionarioObj obterUsuarioPorLogin(String login) {
        Optional<FuncionarioObj> usuarioOpt = funcionarioRepository.findByLogin(login);
        return usuarioOpt.orElse(null);
    }
}
