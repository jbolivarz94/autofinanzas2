package com.jbolivarz.autofinanzas2.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaccion {
    private Integer id;
    private Integer idCuenta;
    private Integer idCategoria;
    private LocalDate fecha;
    private String descripcion;
    private Double monto;

    @Override
    public String toString() {
        return "{" +
                "id: " + id +
                ", idCuenta: " + idCuenta +
                ", idCategoria: " + idCategoria +
                ", fecha: " + fecha +
                ", descripcion: '" + descripcion + '\'' +
                ", monto: " + monto +
                '}';
    }
}
