/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prolink.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Tiago
 */
public class Arquivo {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    
    public String pegarExtensao(File file){
        int pos = file.toString().lastIndexOf(".");
        return file.toString().substring(pos + 1);
    }
    public int campoSelecionado(String campo){
        switch(campo){//retorna o numero das colunas, nesse caso defini como 5 colunas
            case "A":
                return 0;
            case "B":
                return 1;
            case "C":
                return 2;
            case "D":
                return 3;
            case "E":
                return 4;
            case "F":
                return 5;
            case "G":
                return 6;
            case "H":
                return 7;
            case "I":
                return 8;
            case "J":
                return 9;
            case "K":
                return 10;
            case "L":
                return 11;
            case "M":
                return 12;
            case "N":
                return 13;
            case "O":
                return 14;
            case "P":
                return 15;
            case "Q":
                return 16;
            case "R":
                return 17;
            case "S":
                return 18;
            case "T":
                return 19;
            case "U":
                return 20;
            case "V":
                return 21;
            case "W":
                return 22;
            case "X":
                return 23;
            case "Y":
                return 24;
            case "Z":
                return 25;
        }
        return -1;
    }
    
    public boolean validarArquivo(File file){
        String extensao = pegarExtensao(file);
        if(!extensao.equals("xls") && !extensao.equals("xlsx")){
            JOptionPane.showMessageDialog(null,"Arquivo invalido, por favor informe um arquivo correto!\n"
                    + "Formatos aceitos {xls, xlsx}");
            return false;
        }
        else return true;
    }
    
    public Workbook validarWorkbook(Workbook workbook, File file, String ext, InputStream inputStream){
        switch (ext) {
            case "xls":
                try{
                    if(file.getName().equals("Cadastro.xls")){//formula para abrir arquivo com senha
                        Biff8EncryptionKey.setCurrentUserPassword("PLKCONTRATOS");
                        NPOIFSFileSystem fs = new NPOIFSFileSystem(file, true);
                        return workbook = new HSSFWorkbook(fs.getRoot(), true);
                    }
                    else{
                        POIFSFileSystem fileSystem = new POIFSFileSystem(inputStream);
                        return workbook = new HSSFWorkbook(fileSystem);
                    }
                }catch(IOException e){
                    JOptionPane.showMessageDialog(null, e);
                    return null;
                }
            case "xlsx":
                try{
                    return workbook = new XSSFWorkbook(inputStream);
                }catch(IOException e){
                    JOptionPane.showMessageDialog(null, e);
                    return null;
                }  
            default:
                JOptionPane.showMessageDialog(null, "Formato de arquivo não permitido!");
                return null;
        }
    }
    //enviar valor recebidos de cada celula da planilha para um conjunto de listas no bean
    public void enviarValores(Cell celula, PlanilhaBean bean){
        switch(celula.getColumnIndex()){//retorna o numero das colunas, nesse caso defini como 5 colunas
            case 0:
                String valor = tratarTipo(celula);
                switch(valor.length()){
                    case 1:
                        valor = "000"+valor;
                        break;
                    case 2:
                        valor = "00"+valor;
                        break;
                    case 3:
                        valor = "0"+valor;
                        break;
                }
                bean.setC0(valor);
                break;
            case 1:
                bean.setC1(tratarTipo(celula));
                break;
            case 2:
                bean.setC2(tratarTipo(celula));
                break;
            case 3:
                bean.setC3(tratarTipo(celula));
                break;
            case 4:
                bean.setC4(tratarTipo(celula));
                break;
            case 5:
                bean.setC5(tratarTipo(celula));
                break;
            case 6:
                bean.setC6(tratarTipo(celula));
                break;
            case 7:
                bean.setC7(tratarTipo(celula));
                break;
            case 8:
                bean.setC8(tratarTipo(celula));
                break;
            case 9:
                bean.setC9(tratarTipo(celula));
                break;
            case 10:
                bean.setC10(tratarTipo(celula));
                break;
            case 11:
                bean.setC11(tratarTipo(celula));
                break;
            case 12:
                bean.setC12(tratarTipo(celula));
                break;
            case 13:
                bean.setC13(tratarTipo(celula));
                break;
            case 14:
                bean.setC14(tratarTipo(celula));
                break;
            case 15:
                bean.setC15(tratarTipo(celula));
                break;
            case 16:
                bean.setC16(tratarTipo(celula));
                break;
            case 17:
                bean.setC17(tratarTipo(celula));
                break;
            case 18:
                bean.setC18(tratarTipo(celula));
                break;
            case 19:
                bean.setC19(tratarTipo(celula));
                break;
            case 20:
                bean.setC20(tratarTipo(celula));
                break;
            case 21:
                bean.setC21(tratarTipo(celula));
                break;
            case 22:
                bean.setC22(tratarTipo(celula));
                break;
            case 23:
                bean.setC23(tratarTipo(celula));
                break;
            case 24:
                bean.setC24(tratarTipo(celula));
                break;
            case 25:
                bean.setC25(tratarTipo(celula));
                break;
            default:
                break;
        }
    }
    
    //trata o tipo de celula da planilha e retorna como string  
    public String tratarTipo(Cell celula){ //metodo usado para tratar as celulas
        switch (celula.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                if(DateUtil.isCellDateFormatted(celula))
                    return sdf.format(celula.getDateCellValue());//campo do tipo data formatando no ato
                else{
                    return String.valueOf((long)celula.getNumericCellValue());//campo do tipo numerico, convertendo double para long
                }
            case Cell.CELL_TYPE_STRING:
                    return String.valueOf(celula.getStringCellValue());//conteudo do tipo texto
            case Cell.CELL_TYPE_BOOLEAN:    
                return "";//conteudo do tipo booleano
            case Cell.CELL_TYPE_BLANK:
                return "";//em branco
            default:
                return "";
        }
    }
    
    
    public List<File> pegarArquivos(String caminho, boolean filtrar, String extensao){
        List<File> lista = new ArrayList<>();
        File diretorio = new File(caminho);
        File[] files = diretorio.listFiles();
        for(File f : files){
            if(f.isDirectory()){
                carregar(f, lista, filtrar, extensao);
            }
            else{
                adicionar(f, lista, filtrar, extensao);
            }
        }
        return lista;
    }
    private void carregar(File diretorio, List<File> lista, boolean filtrar, String extensao){
        File[] files = diretorio.listFiles();
        for(File f : files){
            if(f.isDirectory()){
                carregar(f, lista, filtrar, extensao);
            }
            else{
                adicionar(f, lista, filtrar, extensao);
            }
        }
    }
    private void adicionar(File f, List<File> lista, boolean filtrar, String extensao){
        if(filtrar==true){
            if(pegarExtensao(f).equals(extensao))
                lista.add(f);
        }
        else
            lista.add(f);
    }
    public boolean gravarTxtInconsistencias(Set<File> file, String arquivo){
    	File f = new File(arquivo);
    	if(f.exists())
    		f.delete();
			try{
	    		f.createNewFile();
	    		StringBuilder builder = new StringBuilder();
	    		builder.append("Arquivos incompativeis, não foi possivel realizar a leitura do conteudo, necessário intervenção manual"+System.getProperty("line.separator"));
	    		for(File arq : file){
	    			builder.append(arq.getAbsolutePath());
	    			builder.append(System.getProperty("line.separator"));
	    		}
	    		FileWriter writer = new FileWriter(f, true);
	    		writer.write(builder.toString());
	    		writer.close();
	    		return true;
    		}catch(IOException e){
    			return false;
    		}
    }
    public void copiarArquivosInconsistentes(Set<File> file, File destino) throws IOException{
    	if (!destino.exists())
    		destino.mkdir();
    	else{
    		File[] lista = destino.listFiles();
    		for(File f : lista)
    			f.delete();
    	}
    	for(File arquivo : file){ 
	    	 File fileFinal = new File(destino+"/"+arquivo.getName());
	        	 Path pathI = Paths.get(arquivo.getAbsolutePath());
	             Path pathO = Paths.get(fileFinal.getAbsolutePath());
	             Files.copy(pathI, pathO, StandardCopyOption.REPLACE_EXISTING);
	    }
    }
}
