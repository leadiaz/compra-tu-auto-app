package ar.edu.unq.pdss22025.services;

import ar.edu.unq.pdss22025.models.Rol;
import ar.edu.unq.pdss22025.repositories.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolService {
    
    @Autowired
    private RolRepository rolRepository;
    
    public Rol crearRol(String nombre) {
        if (rolRepository.existsByNombre(nombre)) {
            throw new RuntimeException("El rol " + nombre + " ya existe");
        }
        
        Rol rol = new Rol();
        rol.setNombre(nombre);
        
        return rolRepository.save(rol);
    }
    
    public List<Rol> obtenerTodosLosRoles() {
        return rolRepository.findAll();
    }
    
    public Optional<Rol> obtenerRolPorId(Long id) {
        return rolRepository.findById(id);
    }
    
    public Optional<Rol> obtenerRolPorNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }
}
