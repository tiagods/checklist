package com.prolink.checklist.model;

public enum ObrigacaoTipo {
    GERAL("Geral");
    private String descricao;
    ObrigacaoTipo(String descricao){
        this.descricao=descricao;
    }
    @Override
    public String toString() {
        return this.descricao;
    }
}
