package me.dri.entities;

import java.io.Serializable;
import java.util.Date;

public class Transacao implements Serializable {


    private Integer valor;

    private String tipo;
    private String descricao;
    private Date realizadaEm;

    public Transacao(Integer valor, String tipo, String descricao, Date realizadaEm) {
        this.valor = valor;
        this.tipo = tipo;
        this.descricao = descricao;
        this.realizadaEm = realizadaEm;
    }


    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getRealizadaEm() {
        return realizadaEm;
    }

    public void setRealizadaEm(Date realizadaEm) {
        this.realizadaEm = realizadaEm;
    }

    @Override
    public String toString() {
        return "Transacao{" +
                ", valor=" + valor +
                ", tipo='" + tipo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", realizadaEm=" + realizadaEm +
                '}';
    }
}

