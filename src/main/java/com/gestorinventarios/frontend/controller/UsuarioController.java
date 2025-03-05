package com.gestorinventarios.frontend.controller;

import com.gestorinventarios.backend.model.Usuario;
import com.gestorinventarios.backend.service.UsuarioService;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public boolean registrarUsuario(String nombre, String email, String password) {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(password);
        try {
            usuarioService.guardarUsuario(nuevoUsuario);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Usuario obtenerUsuarioPorEmail(String email) {
        return usuarioService.obtenerUsuarioPorEmail(email);
    }

    public boolean autenticarUsuario(String email, String password) {
        Usuario usuario = usuarioService.obtenerUsuarioPorEmail(email);
        return usuario != null && usuario.getPassword().equals(password);
    }
}
