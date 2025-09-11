package ar.edu.unq.pdss22025.models.dto;

import lombok.Data;

@Data
public class CrearUsuarioRequest {
    private String email;
    private String password;
    private String nombre;
    private String apellido;
    private String nombreRol;
}
