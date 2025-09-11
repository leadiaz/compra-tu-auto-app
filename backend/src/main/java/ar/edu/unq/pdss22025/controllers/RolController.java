package ar.edu.unq.pdss22025.controllers;

import ar.edu.unq.pdss22025.models.Rol;
import ar.edu.unq.pdss22025.models.dto.CrearRolRequest;
import ar.edu.unq.pdss22025.models.dto.RolResponse;
import ar.edu.unq.pdss22025.services.RolService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rol")
public class RolController {
    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @PostMapping
    public ResponseEntity<?> crearRol(@RequestBody CrearRolRequest request) {
        try {
            Rol rol = rolService.crearRol(request.getNombre());
            RolResponse response = new RolResponse(rol.getId(), rol.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listarRoles() {
        return ResponseEntity.ok(
            rolService.obtenerTodosLosRoles()
                .stream()
                .map(rol -> new RolResponse(rol.getId(), rol.getNombre()))
                .toList()
        );
    }
}
