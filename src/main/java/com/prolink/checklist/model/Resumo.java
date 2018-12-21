package com.prolink.checklist.model;

import com.prolink.checklist.enuns.Mensageria;

import java.io.Serializable;

public class Resumo implements Serializable {
    private Mensageria mensageria;
    private int total;
    private boolean intervencao = false;

    public Resumo(Mensageria mensageria, int total){
        this.mensageria=mensageria;
        this.total=total;
        this.intervencao = mensageria.getStatus().equals(Mensageria.Status.ERRO);
    }

    public int getTotal() {
        return total;
    }

    public Mensageria getMensageria() {
        return mensageria;
    }

    public void setMensageria(Mensageria mensageria) {
        this.mensageria = mensageria;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
