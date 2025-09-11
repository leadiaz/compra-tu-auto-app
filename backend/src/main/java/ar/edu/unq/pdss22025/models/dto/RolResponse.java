package ar.edu.unq.pdss22025.models.dto;

import lombok.Data;

@Data
public class RolResponse {
    private Long id;
    private String nombre;
    
    public RolResponse(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
}
