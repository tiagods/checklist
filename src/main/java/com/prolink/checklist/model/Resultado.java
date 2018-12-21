package com.prolink.checklist.model;

import com.prolink.checklist.enuns.Mensageria;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.*;

public class Resultado implements Serializable {
    private Cliente cliente;
    private Map<Path,Mensageria> arquivoNome = new HashMap<>();
    private Map<Path,Mensageria> arquivoConteudo = new HashMap<>();
    private Status status=Status.PENDENTE;
    private String desobrigado;
    private Mensageria mensageria=Mensageria.DEFAULT;

    public Resultado(Cliente cliente){
        this.cliente=cliente;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Map<Path, Mensageria> getArquivoNome() {
        return arquivoNome;
    }
    public Map<Path, Mensageria> getArquivoConteudo() {
        return arquivoConteudo;
    }
    public void putArquivoNome(Path path, Mensageria mensageria){
        this.arquivoNome.put(path,mensageria);
    }
    public void putArquivoConteudo(Path path, Mensageria mensageria){
        this.arquivoConteudo.put(path,mensageria);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDesobrigado() {
        return desobrigado;
    }

    public void setDesobrigado(String desobrigado) {
        this.desobrigado = desobrigado;
    }

    public Mensageria getMensageria() {
        return mensageria;
    }
    public void setMensageria(Mensageria mensageria) {
        this.mensageria = mensageria;
    }

    public enum Status{
        VALIDO,DESOBRIGADO,PENDENTE,ERRO
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resultado resultado = (Resultado) o;
        return Objects.equals(cliente, resultado.cliente) &&
                Objects.equals(arquivoNome, resultado.arquivoNome) &&
                Objects.equals(arquivoConteudo, resultado.arquivoConteudo) &&
                status == resultado.status &&
                Objects.equals(desobrigado, resultado.desobrigado) &&
                mensageria == resultado.mensageria;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cliente, arquivoNome, arquivoConteudo, status, desobrigado, mensageria);
    }
}
