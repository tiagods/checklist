package com.prolink.model;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.text.PDFTextStripper;


public class LeitoraPdf {
	
	StringBuilder sb = new StringBuilder();
    private int totalPages;
    
//	public static void main(String[] args){
//		new LeitoraPdf();
//	}
//    public LeitoraPdf(){
//    	File arquivo = new File("C:/Users/Tiago/Desktop/10-2016/0498-EFDCOFINS-RECIBO-10-2016-ORIGINAL.pdf");
//		if(verificarTexto(arquivo, "04.568.084/0001-15"))
//			System.out.println("Conteudo existe");
//    }
    
    	public boolean verificarTexto(File arquivo, String texto){
            PDDocument pdfDocumento = null;
            try{    
                    String extensao = arquivo.getName().substring(arquivo.getName().lastIndexOf(".")+1);
                    if(extensao.equals("pdf")){
                        pdfDocumento = PDDocument.load(arquivo);
                        this.totalPages = pdfDocumento.getNumberOfPages();
                        
                        
                        PDFTextStripper stripper = new PDFTextStripper();
                        sb.append(stripper.getText(pdfDocumento));
                        
                        if(sb.toString().toUpperCase().contains(texto.toUpperCase()))
                            return true;
                    }
                    else
                        return false;
            }catch(IOException e){
                    e.printStackTrace();
            }finally{
                    if(pdfDocumento != null) try{pdfDocumento.close();}catch(IOException e){}
            }
            return false;
    }
	public boolean verificarTexto(File arquivo, String texto,boolean firstPage){
            PDDocument pdfDocumento = null;
            try{    
                    String extensao = arquivo.getName().substring(arquivo.getName().lastIndexOf(".")+1);
                    if(extensao.equals("pdf")){
                        pdfDocumento = PDDocument.load(arquivo);
                        this.totalPages = pdfDocumento.getNumberOfPages();
                        PDFTextStripper stripper = new PDFTextStripper();
                        
                        if(firstPage){
                            stripper.setStartPage(1);
                            stripper.setEndPage(1);
                        }
                        sb.append(stripper.getText(pdfDocumento));
                        
                        if(sb.toString().toUpperCase().contains(texto.toUpperCase()))
                            return true;
                    }
                    else
                        return false;
            }catch(IOException e){
            	//se chegar aqui o arquivo pode
            		System.out.println("Falha ao ler o arquivo: "+arquivo.getName());
                    e.printStackTrace();
            }finally{
                    if(pdfDocumento != null) try{pdfDocumento.close();}catch(IOException e){}
            }
            return false;
    }
    public int totalPages(){
    	return this.totalPages;
    }
    
    public boolean conseguiLer(){
    	return !sb.toString().trim().equals("");
    }
}
