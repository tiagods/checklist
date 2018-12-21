package com.prolink.checklist.util;

import com.prolink.checklist.enuns.Mensageria;
import com.prolink.checklist.model.Cliente;
import com.prolink.checklist.model.Resultado;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LeitoraPDF implements InterfacePDF {
    private Path path;
    @Override
    public PDDocument abrirPFD(Path path) throws IOException{
        this.path=path;
        return PDDocument.load(path.toFile());
    }

    @Override
    public void buscarCNPJ(int page, PDDocument document, TableView<Resultado> tbResultados, Label txProgresso,Resultado sugestao, Set<Path> erroLeitura, String cnpjIgnore) throws IOException{
        String text = getPdfText(document,page);
        if(text.trim().equals("")) {
            erroLeitura.add(this.path);
            Mensageria mensageria = Mensageria.ERROLEITURA;
            text = buscarPorTesseract();
            Set<Resultado> set = buscarCnpjNoConteudo(Mensageria.LEITURAOCR,tbResultados,text,cnpjIgnore,txProgresso);
            System.out.println(text);
            System.out.println("Validação manual = "+text.contains("07.728.201/0001—59"));
            System.out.println("Validação Lista Tamanho = "+set.size());

            if(text.trim().equals(""))
                mensageria = Mensageria.ARQUIVOBRANCO;
            if(sugestao!=null){
                Optional <Resultado> result = tbResultados.getItems().stream().filter(c->c.getCliente()==sugestao.getCliente()).findAny();
                if(result.isPresent()) {
                    result.get().putArquivoConteudo(this.path, set.isEmpty()?mensageria:Mensageria.EMPRESAINVALIDA);
                    setRowInTable(tbResultados, result.get());
                    setTxProgresso(txProgresso, "ERRO DE LEITURA: " + this.path.getFileName().toString());
                    System.out.println("ERRO DE LEITURA: " + this.path.getFileName().toString()+"-Cliente="+result.get().getCliente());
                }
            }
        }
        else{
            buscarCnpjNoConteudo(Mensageria.VALIDADO,tbResultados,text,cnpjIgnore,txProgresso);
        }
    }
    private Set<Resultado> buscarCnpjNoConteudo(Mensageria mensageria, TableView<Resultado> tbResultados, String text, String cnpjIgnore, Label txProgresso){
        Set<Resultado> achados = new HashSet<>();
        for (Resultado resultado : tbResultados.getItems()) {
            Cliente cli = resultado.getCliente();
            String cnpjMargemOCR = cli.getCnpj().replace("-","—");
            if(cli.isCnpjValido() && text.contains(cli.getCnpj()) ||
                    mensageria.equals(Mensageria.LEITURAOCR) && cli.isCnpjValido() &&text.contains(cnpjMargemOCR)){
                if(validarCNPJ(cnpjIgnore) && text.contains(cnpjIgnore) && cli.getCnpj().equals(cnpjIgnore)){
                    setTxProgresso(txProgresso, "ARQUIVO="+this.path.getFileName().toString()+"+IGNORANDO CNPJ ENCONTRADO= "+cnpjIgnore);
                }
                else{
                    resultado.putArquivoConteudo(this.path,mensageria);
                    setRowInTable(tbResultados, resultado);
                    setTxProgresso(txProgresso,
                            mensageria.equals(Mensageria.LEITURAOCR)?"VALIDAÇÃO C0M INTELIGENCIA ARTIFICIAL="+this.path.getFileName().toString()+"-CLIENTE="+cli.toString():"VALIDAÇÃO DE CONTEUDO= "+this.path.getFileName().toString()+"-CLIENTE="+cli.toString());
                    achados.add(resultado);
                }
            }
        }
        if(achados.isEmpty()) setTxProgresso(txProgresso, "NENHUM CNPJ DA RELAÇÃO FOI IDENTIFICADO NO ARQUIVO="+this.path.getFileName().toString());
        return achados;
    }

    private String buscarPorTesseract() throws IOException{
        ITesseract instance = new Tesseract1();
        instance.setLanguage("eng");
        instance.setDatapath("tessdata"); // path to tessdata directory
        try {
            return instance.doOCR(this.path.toFile());
        } catch (TesseractException e) {
            return "";
        }
    }

    private String getPdfText(PDDocument document,int page) throws IOException{
        PDFTextStripper stripper = new PDFTextStripper();
        if(page!=-1) {
            stripper.setStartPage(page);
            stripper.setEndPage(page);
        }
        return stripper.getText(document);
    }

    @Override
    public PDPageTree pageTree(PDDocument document){
        return document.getPages();
    }

    @Override
    public void fechar(PDDocument document) throws IOException{
        document.close();
    }
    private void setRowInTable(TableView<Resultado> tbResultado, Resultado r){
        Platform.runLater(() -> {
            tbResultado.getSelectionModel().select(r);
            int index = tbResultado.getItems().indexOf(r);
            tbResultado.scrollTo(index);
            tbResultado.requestFocus();
        });
    }
    private void setTxProgresso(Label txProgresso, String text){
        Platform.runLater(() ->txProgresso.setText(text));
    }
    boolean validarCNPJ(String cnpj){
        String cnpjFormat = "(^\\d{2}\\d{3}\\d{3}\\d{4}\\d{2}$)";
        cnpj = cnpj.replace(".","").replace("/","").replace("-","");
        Matcher matcher = Pattern.compile(cnpjFormat).matcher(cnpj);
        return matcher.find();
    }

    @Override
    public boolean validarExtensao(Path path) {
        String nome = path.getFileName().toString();
        String extensao = nome.substring(nome.lastIndexOf(".")+1);
        return extensao.toLowerCase().equals("pdf");
    }

    @Override
    public boolean verificarArquivoImagem(PDDocument document) throws IOException{
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);
        return text.trim().equals("");
    }


}
