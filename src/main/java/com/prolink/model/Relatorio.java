/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prolink.model;

import java.util.List;

import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Tiago
 */
public class Relatorio {
    private String path; //Caminho base
	private String pathToReportPackage; // Caminho para o package onde estao armazenados os relatorios Jarper
	
	//Recupera os caminhos para que a classe possa encontrar os relatorios
	public Relatorio() {
		this.path = this.getClass().getClassLoader().getResource("").getPath();
		//this.pathToReportPackage = this.path+"br/com/tiagods/utilitarios/";
		this.pathToReportPackage = System.getProperty("user.dir")+"/resources/";
	}	
	//Imprime/gera uma lista de Clientes
	public void imprimir(List<CadastroBean> clientes) {
        try{
        	JasperReport report = JasperCompileManager.compileReport(getPathToReportPackage()+"Cadastro.jrxml");
	        JasperPrint print = JasperFillManager.fillReport(report, null, new JRBeanCollectionDataSource(clientes));
	        //JasperExportManager.exportReportToPdfFile(print, desktop+"/relatorio.pdf");		
            JasperViewer.viewReport(print, false);
            }catch(JRException e){
                JOptionPane.showMessageDialog(null, "Erro ao gerar relatorio \n"+e);
            }
        }
	
	public String getPathToReportPackage() {
		return this.pathToReportPackage;
	}
}
