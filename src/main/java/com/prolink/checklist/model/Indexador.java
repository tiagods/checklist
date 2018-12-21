package com.prolink.checklist.model;

import java.io.Serializable;
import java.util.Objects;

public class Indexador implements Serializable{
    private int index;
    private String valor;
    private boolean habilitado = true;

    public Indexador(int index, String valor){
        this.index=index;
        this.valor=valor;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return this.valor;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Indexador indexador = (Indexador) o;
        return index == indexador.index &&
                habilitado == indexador.habilitado &&
                Objects.equals(valor, indexador.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, valor, habilitado);
    }
}
