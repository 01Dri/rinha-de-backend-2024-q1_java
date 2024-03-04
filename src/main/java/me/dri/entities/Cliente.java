package me.dri.entities;

import java.io.Serializable;

public class Cliente implements Serializable {

    private Integer id;
    private Integer saldoInicial;
    private Integer limite;

    public Cliente() {

    }

    public Cliente(Integer id, Integer saldoInicial, Integer limite) {
        this.id = id;
        this.saldoInicial = saldoInicial;
        this.limite = limite;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(Integer saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public Integer getLimite() {
        return limite;
    }

    public void setLimite(Integer limite) {
        this.limite = limite;
    }
}


