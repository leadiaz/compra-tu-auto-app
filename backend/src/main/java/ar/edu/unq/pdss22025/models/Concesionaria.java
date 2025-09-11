package ar.edu.unq.pdss22025.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CONCESIONARIA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Concesionaria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @Column(name = "cuit", unique = true, nullable = false)
    private String cuit;
    
    @Column(name = "telefono")
    private String telefono;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "direccion")
    private String direccion;
    
    @Column(name = "fecha_alta", nullable = false)
    private LocalDateTime fechaAlta;
    
    @Column(name = "activa", nullable = false)
    private Boolean activa = true;
    
    @OneToMany(mappedBy = "concesionaria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OfertaAuto> ofertas = new ArrayList<>();
}
