package com.my.work.manager.myworkmanager_back.controller;

import com.my.work.manager.myworkmanager_back.model.FuncionarioObj;
import com.my.work.manager.myworkmanager_back.service.FuncionarioService;
import com.my.work.manager.myworkmanager_back.util.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @GetMapping
    public ResponseEntity<CustomResponse<List<FuncionarioObj>>> listarUsuarios() {
        List<FuncionarioObj> usuarios = funcionarioService.listarUsuarios();
        CustomResponse<List<FuncionarioObj>> response = new CustomResponse<>(usuarios, HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<FuncionarioObj>> buscarUsuarioPorId(@PathVariable Long id) {
        Optional<FuncionarioObj> usuario = funcionarioService.buscarUsuarioPorId(id);
        if (usuario.isPresent()) {
            CustomResponse<FuncionarioObj> response = new CustomResponse<>(usuario.get(), HttpStatus.OK);
            return ResponseEntity.ok(response);
        } else {
            CustomResponse<FuncionarioObj> response = new CustomResponse<>(null, HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<CustomResponse<FuncionarioObj>> salvarUsuario(@RequestBody FuncionarioObj usuario) {
        FuncionarioObj usuarioSalvo = funcionarioService.salvarUsuario(usuario);
        CustomResponse<FuncionarioObj> response = new CustomResponse<>(usuarioSalvo, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse<FuncionarioObj>> atualizarUsuario(@PathVariable Long id, @RequestBody FuncionarioObj usuarioAtualizado) {
        Optional<FuncionarioObj> usuarioExistente = funcionarioService.buscarUsuarioPorId(id);
        if (usuarioExistente.isPresent()) {
            usuarioAtualizado.setId(id); // Assegure que o ID do usuário atualizado é o mesmo que o ID do path
            FuncionarioObj usuarioSalvo = funcionarioService.salvarUsuario(usuarioAtualizado); // Ou use um método de atualização específico, se disponível
            CustomResponse<FuncionarioObj> response = new CustomResponse<>(usuarioSalvo, HttpStatus.OK);
            return ResponseEntity.ok(response);
        } else {
            CustomResponse<FuncionarioObj> response = new CustomResponse<>(null, HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse<Void>> deletarUsuario(@PathVariable Long id) {
        funcionarioService.deletarUsuario(id);
        CustomResponse<Void> response = new CustomResponse<>(null, HttpStatus.NO_CONTENT);
        return ResponseEntity.noContent().build();
    }
}
