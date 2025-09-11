package ar.edu.unq.pdss22025.controllers;

import ar.edu.unq.pdss22025.models.Usuario;
import ar.edu.unq.pdss22025.models.dto.CrearUsuarioRequest;
import ar.edu.unq.pdss22025.models.dto.UsuarioResponse;
import ar.edu.unq.pdss22025.services.RolService;
import ar.edu.unq.pdss22025.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody CrearUsuarioRequest request) {
        try {
            Usuario usuario = usuarioService.crearUsuario(
                request.getEmail(),
                request.getPassword(),
                request.getNombre(),
                request.getApellido(),
                request.getNombreRol()
            );
            
            UsuarioResponse response = new UsuarioResponse(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getFechaAlta(),
                usuario.getActivo(),
                usuario.getRol().getNombre()
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        
        List<UsuarioResponse> responses = usuarios.stream()
                .map(usuario -> new UsuarioResponse(
                    usuario.getId(),
                    usuario.getEmail(),
                    usuario.getNombre(),
                    usuario.getApellido(),
                    usuario.getFechaAlta(),
                    usuario.getActivo(),
                    usuario.getRol().getNombre()
                ))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerUsuarioPorId(@PathVariable Long id) {
        return usuarioService.obtenerUsuarioPorId(id)
                .map(usuario -> {
                    UsuarioResponse response = new UsuarioResponse(
                        usuario.getId(),
                        usuario.getEmail(),
                        usuario.getNombre(),
                        usuario.getApellido(),
                        usuario.getFechaAlta(),
                        usuario.getActivo(),
                        usuario.getRol().getNombre()
                    );
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/por-rol/{nombreRol}")
    public ResponseEntity<?> obtenerUsuariosPorRol(@PathVariable String nombreRol) {
        try {
            List<Usuario> usuarios = usuarioService.obtenerUsuariosPorRol(nombreRol);
            
            List<UsuarioResponse> responses = usuarios.stream()
                    .map(usuario -> new UsuarioResponse(
                        usuario.getId(),
                        usuario.getEmail(),
                        usuario.getNombre(),
                        usuario.getApellido(),
                        usuario.getFechaAlta(),
                        usuario.getActivo(),
                        usuario.getRol().getNombre()
                    ))
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(responses);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
