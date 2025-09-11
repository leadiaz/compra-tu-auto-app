package ar.edu.unq.pdss22025.services;

import ar.edu.unq.pdss22025.models.Rol;
import ar.edu.unq.pdss22025.models.Usuario;
import ar.edu.unq.pdss22025.repositories.RolRepository;
import ar.edu.unq.pdss22025.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RolRepository rolRepository;
    
    public Usuario crearUsuario(String email, String password, String nombre, String apellido, String nombreRol) {
        // Verificar si el usuario ya existe
        if (usuarioRepository.existsByEmail(email)) {
            throw new RuntimeException("El usuario con email " + email + " ya existe");
        }
        
        // Buscar el rol
        Rol rol = rolRepository.findByNombre(nombreRol)
                .orElseThrow(() -> new RuntimeException("El rol " + nombreRol + " no existe"));
        
        // Crear el usuario
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setFechaAlta(LocalDateTime.now());
        usuario.setActivo(true);
        usuario.setRol(rol);
        
        return usuarioRepository.save(usuario);
    }
    
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }
    
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }
    
    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    public List<Usuario> obtenerUsuariosPorRol(String nombreRol) {
        Rol rol = rolRepository.findByNombre(nombreRol)
                .orElseThrow(() -> new RuntimeException("El rol " + nombreRol + " no existe"));
        return rol.getUsuarios();
    }
}
