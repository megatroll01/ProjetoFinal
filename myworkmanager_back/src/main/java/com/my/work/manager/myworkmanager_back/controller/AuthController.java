package com.my.work.manager.myworkmanager_back.controller;

import com.my.work.manager.myworkmanager_back.dto.LoginRequest;
import com.my.work.manager.myworkmanager_back.model.FuncionarioObj;
import com.my.work.manager.myworkmanager_back.service.FuncionarioService;
import com.my.work.manager.myworkmanager_back.util.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private FuncionarioService funcionarioService;

    @PostMapping("/auth/usuario")
    public CustomResponse<FuncionarioObj> loginUsuario(@RequestBody LoginRequest loginRequest) {
        boolean authenticated = funcionarioService.autenticarUsuario(loginRequest.getLogin(), loginRequest.getSenha());
        if (authenticated) {
            FuncionarioObj usuario = funcionarioService.obterUsuarioPorLogin(loginRequest.getLogin());
            usuario.setPassword(null);
            return new CustomResponse<>(usuario, HttpStatus.OK);
        } else {
            return new CustomResponse<>(null, HttpStatus.UNAUTHORIZED);
        }
    }
}
