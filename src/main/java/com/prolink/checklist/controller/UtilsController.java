package com.prolink.checklist.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXCheckBox;
import com.prolink.checklist.enuns.Mensageria;
import com.prolink.checklist.model.Cliente;
import com.prolink.checklist.model.Indexador;
import com.prolink.checklist.model.Resultado;
import com.prolink.checklist.util.ExcelGenerico;

import javafx.scene.control.*;
import javafx.scene.control.Alert;
import jxl.write.WriteException;

public class UtilsController {
    public void alert(Alert.AlertType type,String titulo,String assunto, String mensagem){
        Alert alert = new Alert(type);
        alert.setTitle(titulo);
        alert.setHeaderText(assunto);
        alert.setContentText(mensagem);
        if(type != Alert.AlertType.INFORMATION)
            alert.getDialogPane().setMinSize(600,200);
        alert.showAndWait();
    }
    public List<Indexador> getIndexadorDefault(){
        List<Indexador> index = new ArrayList<>();
        index.add(new Indexador(0, "COD"));
        index.add(new Indexador(1, "EMPRESA"));
        index.add(new Indexador(2, "STATUS"));
        index.add(new Indexador(3, "CNPJ"));
        return index;
    }
    
    public void imprimir(TableView<Resultado> tbResultado,JFXCheckBox ckCodigo,JFXCheckBox ckCnpj) {
    	Integer[] larguraColunas = new Integer[]{12,15,20,20,43,43,43,43};
        ArrayList<ArrayList<String>> arrayList = new ArrayList<>();
        ArrayList<String> array1 = new ArrayList<String>();
        array1.add("CODIGO");
        array1.add("STATUS");
        array1.add("CNPJ");
        array1.add("NOME");
        array1.add("CODIGO NO NOME");
        array1.add("VALIDACAO NOME");
        array1.add("CNPJ CONTEUDO");
        array1.add("VALIDACAO CONTEUDO-CNPJ");
        arrayList.add(array1);

        int cabecalhoPosition = arrayList.size()-1;

        for(Resultado resultado : tbResultado.getItems()){
            ArrayList<String> array2 = new ArrayList<>();
            Cliente cliente = resultado.getCliente();
            array2.add(String.valueOf(cliente.getId()));
            array2.add(cliente.getStatus());
            array2.add(cliente.getCnpj());
            array2.add(cliente.getNome());

            array2.add(resultado.getArquivoNome()
                    .keySet()
                    .stream()
                    .map(s->s.getFileName().toString()+"="+resultado.getArquivoNome().get(s).toString())
                    .collect(Collectors.joining("\n")));

            if(resultado.getArquivoNome().isEmpty())
                array2.add(ckCodigo.isSelected()?(resultado.getStatus().equals(Resultado.Status.ERRO)?resultado.getMensageria().toString():Mensageria.ARQUIVONAOENCONTRADO.toString()):Mensageria.NOMEIGNORADO.toString());
            else{
                array2.add(resultado.getArquivoNome()
                        .values()
                        .stream()
                        .map(Mensageria::toString)
                        .collect(Collectors.joining(";")));

            }
            array2.add(resultado.getArquivoConteudo()
                    .keySet()
                    .stream()
                    .map(s->s.getFileName().toString()+"="+resultado.getArquivoConteudo().get(s).toString())
                    .collect(Collectors.joining("\n")));

            if(resultado.getArquivoConteudo().isEmpty())
                array2.add(ckCnpj.isSelected()?(resultado.getStatus().equals(Resultado.Status.ERRO)?resultado.getMensageria().toString():Mensageria.ARQUIVONAOENCONTRADO.toString())
                        :Mensageria.CNPJIGNORADO.toString());
            else{
                array2.add(resultado.getArquivoConteudo()
                        .values()
                        .stream()
                        .map(Mensageria::toString)
                        .collect(Collectors.joining(";")));
            }
            arrayList.add(array2);
        }

        ArrayList<String> a1 = new ArrayList<String>();
        a1.add(Mensageria.VALIDADO.toString());
        a1.add(Mensageria.VALIDADO.getDescricao());

        ArrayList<String> a2 = new ArrayList<String>();
        a2.add(Mensageria.ERROLEITURA.toString());
        a2.add(Mensageria.ERROLEITURA.getDescricao());

        ArrayList<String> a3 = new ArrayList<String>();
        a3.add(Mensageria.LEITURAOCR.toString());
        a3.add(Mensageria.LEITURAOCR.getDescricao());

        ArrayList<String> a4 = new ArrayList<String>();
        a4.add(Mensageria.VALIDADOMANUALEMNTE.toString());
        a4.add(Mensageria.VALIDADOMANUALEMNTE.getDescricao());

        ArrayList<String> a5 = new ArrayList<String>();
        a5.add(Mensageria.ARQUIVOBRANCO.toString());
        a5.add(Mensageria.ARQUIVOBRANCO.getDescricao());

        ArrayList<String> a6 = new ArrayList<String>();
        a6.add(Mensageria.EMPRESAINVALIDA.toString());
        a6.add(Mensageria.EMPRESAINVALIDA.getDescricao());

        ArrayList<String> a7 = new ArrayList<String>();
        a7.add(Mensageria.ARQUIVONAOENCONTRADO.toString());
        a7.add(Mensageria.ARQUIVONAOENCONTRADO.getDescricao());

        ArrayList<String> a8 = new ArrayList<String>();
        a8.add(Mensageria.RECUSADOMANUALMENTE.toString());
        a8.add(Mensageria.RECUSADOMANUALMENTE.getDescricao());

        
        arrayList.add(new ArrayList<String>());

        ArrayList<String> arrayLegenda = new ArrayList<String>();
        arrayLegenda.add("LEGENDAS");
        arrayList.add(arrayLegenda);
        arrayList.add(a1);
        arrayList.add(a2);
        arrayList.add(a3);
        arrayList.add(a4);
        arrayList.add(a5);
        arrayList.add(a6);
        arrayList.add(a7);
        arrayList.add(a8);
        
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        File file = new File(System.getProperty("java.io.tmpdir")+"/file-"+sdf.format(new Date())+".xls");
        ExcelGenerico generico = new ExcelGenerico(file.getAbsolutePath(),arrayList,larguraColunas,cabecalhoPosition);
        try {
            generico.gerarExcel();
            Runnable run = () -> {
                try {
                    Desktop.getDesktop().open(file);
                }catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            };
            new Thread(run).start();
        } catch (WriteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
