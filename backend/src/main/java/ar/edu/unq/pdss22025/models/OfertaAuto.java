package ar.edu.unq.pdss22025.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "OFERTA_AUTO",
       uniqueConstraints = @UniqueConstraint(columnNames = {"concesionaria_id", "auto_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfertaAuto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concesionaria_id", nullable = false)
    private Concesionaria concesionaria;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auto_id", nullable = false)
    private Auto auto;
    
    @Column(name = "stock", nullable = false)
    private Integer stock;
    
    @Column(name = "precio_actual", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioActual;
    
    @Column(name = "moneda", nullable = false)
    private String moneda;
    
}
