package ar.edu.unq.pdss22025.models.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsuarioResponse {
    private Long id;
    private String email;
    private String nombre;
    private String apellido;
    private LocalDateTime fechaAlta;
    private Boolean activo;
    private String rolNombre;
    
    public UsuarioResponse(Long id, String email, String nombre, String apellido, 
                          LocalDateTime fechaAlta, Boolean activo, String rolNombre) {
        this.id = id;
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaAlta = fechaAlta;
        this.activo = activo;
        this.rolNombre = rolNombre;
    }
}
