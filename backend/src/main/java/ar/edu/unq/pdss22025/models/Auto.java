package ar.edu.unq.pdss22025.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "AUTO", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"marca", "modelo", "anio_modelo"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "marca", nullable = false)
    private String marca;
    
    @Column(name = "modelo", nullable = false)
    private String modelo;
    
    @Column(name = "anio_modelo", nullable = false)
    private Integer anioModelo;
    
    @OneToMany(mappedBy = "auto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OfertaAuto> ofertas = new ArrayList<>();
}
