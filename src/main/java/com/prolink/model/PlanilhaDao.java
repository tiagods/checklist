package com.prolink.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JLabel;
import jxl.write.WriteException;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
/*
 * Autor : Tiago Dias
 */

public class PlanilhaDao {
    Iterator linhas = null;
    JLabel label;
    int records = 1;
    int rows = 0;
    
    public boolean lerPlanilha(PlanilhaBean bean, JLabel label, File file){
	InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);

            Workbook workbook = null;
            Sheet sheet;
            
            Arquivo trata = new Arquivo();
            workbook = trata.validarWorkbook(workbook, file,trata.pegarExtensao(file), inputStream);//tratar e validar extensão de arquivo
            
            if(file.getName().equals("Cadastro.xls") || file.getName().equals("Cadastro.xlsx"))
                Biff8EncryptionKey.setCurrentUserPassword(null);
            
            sheet = workbook.getSheetAt (0);//pega a primeira pasta de trabalh
            linhas = sheet.rowIterator();
            rows = sheet.getLastRowNum();
            
            records = 1; 
            
            this.label = label;
            
            Time();
            boolean encerrar = false;
            
            while(linhas.hasNext()){
                if(encerrar)
                	break;
                
                Row linha = (Row) linhas.next();
                
                Iterator<Cell> celulas = linha.cellIterator();//interator para as celulas de cada linha
                
            	while(celulas.hasNext()){
                    Cell celula = celulas.next(); //pega cada cada coluna
                    //sair do loop se o codigo for vazio
                    if(records>1 && celula.getColumnIndex()==0){
                        if(trata.tratarTipo(celula).trim().equals("")){
                            encerrar=true;
                            break;
                        }
                    }
                    trata.enviarValores(celula, bean);
                    
                }
                records++;
            }
            inputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    class Contador implements Runnable{
        @Override
        public void run(){
            try{
            for(int i = 0; i<=rows; i++){
                label.setText("Aguarde...carregando "+i+" linhas de "+rows);
                if(rows<5000)
                	Thread.sleep(1);
            }
            Thread.sleep(5000);
            label.setText("Concluido");
            Thread.sleep(5000);
            label.setText("");
            }catch(InterruptedException e){}
        }
    }
    void Time(){
        Contador contador = new Contador();
        Thread thread = new Thread(contador);
        thread.start();
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void exportToExcel(List<CadastroBean> lista, File arquivo){
        Integer[] larguraColunas = new Integer[]{12,15,40,20,43,43,43};
    	ArrayList<ArrayList> arrayList = new ArrayList<>();
        arrayList.add(new ArrayList());
        arrayList.get(0).add("CODIGO");
        arrayList.get(0).add("STATUS");
        arrayList.get(0).add("NOME");
        arrayList.get(0).add("CNPJ");
        arrayList.get(0).add("STATUS CODIGO");
        arrayList.get(0).add("STATUS CNPJ");
        arrayList.get(0).add("ARQUIVOS EXTRAS");
        
        for(int i = 0 ; i<lista.size(); i++){
        	arrayList.add(new ArrayList());
        	CadastroBean c = lista.get(i);
        	arrayList.get(i+1).add(c.getCodigo());
        	arrayList.get(i+1).add(c.getStatus());
        	arrayList.get(i+1).add(c.getNome());
        	arrayList.get(i+1).add(c.getCnpj());
        	arrayList.get(i+1).add(c.getStatusCodigo());
        	arrayList.get(i+1).add(c.getStatusCnpj());
        	arrayList.get(i+1).add(c.getObservacao());
        }
        
    	ExcelGenerico generico = new ExcelGenerico(arquivo.getAbsolutePath(),arrayList,larguraColunas,0);
    	try {
			generico.gerarExcel();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
