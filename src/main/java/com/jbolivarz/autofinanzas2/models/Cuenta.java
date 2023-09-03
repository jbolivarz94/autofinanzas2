package com.jbolivarz.autofinanzas2.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cuenta {
    private Integer id;
    private Integer idUsuario;
    private String numero;
    private String tipo;
    private Double montoInicial;

    @Override
    public String toString() {
        return "{" +
                "id: " + id +
                ", idUsuario: " + idUsuario +
                ", numero: '" + numero + '\'' +
                ", tipo: '" + tipo + '\'' +
                ", montoInicial: " + montoInicial +
                '}';
    }
}
