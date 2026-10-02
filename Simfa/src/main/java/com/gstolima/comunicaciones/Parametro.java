package com.gstolima.comunicaciones;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Parametro - Modelo Realm para la tabla de parámetros de configuración
 * 
 * Estructura:
 * id|nomParametro  |valor1           |valor2|valor3|
 * 
 * @author Global Solutions & Service S.A.S.
 */
public class Parametro extends RealmObject {

    @PrimaryKey
    private int id;
    private String nomParametro;
    private String valor1;
    private String valor2;
    private String valor3;

    public Parametro() {
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomParametro() {
        return nomParametro;
    }

    public void setNomParametro(String nomParametro) {
        this.nomParametro = nomParametro;
    }

    public String getValor1() {
        return valor1 != null ? valor1 : "";
    }

    public void setValor1(String valor1) {
        this.valor1 = valor1;
    }

    public String getValor2() {
        return valor2 != null ? valor2 : "";
    }

    public void setValor2(String valor2) {
        this.valor2 = valor2;
    }

    public String getValor3() {
        return valor3 != null ? valor3 : "";
    }

    public void setValor3(String valor3) {
        this.valor3 = valor3;
    }

    @Override
    public String toString() {
        return "Parametro{" +
                "id=" + id +
                ", nomParametro='" + nomParametro + '\'' +
                ", valor1='" + valor1 + '\'' +
                ", valor2='" + valor2 + '\'' +
                ", valor3='" + valor3 + '\'' +
                '}';
    }
}
