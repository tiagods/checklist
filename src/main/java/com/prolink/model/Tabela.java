/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prolink.model;

import static com.prolink.view.Menu.tbPrincipal;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.prolink.view.ImageView;
import com.prolink.view.Menu;

/**
 *
 * @author Tiago
 */
public class Tabela {
    @SuppressWarnings({ "unchecked", "rawtypes", "serial" })
	public void preencherTabela(Menu menu, JTable table, ArrayList lista){
        Object[] nomeColunas = new Object[] {
        		"Codigo", "Status", "Nome", "CNPJ", "Status Codigo", "Status CNPJ", "Observa\u00E7\u00E3o", "Valida\u00E7\u00E3o Manual"
        };
    	limparTabela(table);
        DefaultTableModel tbm = new DefaultTableModel(nomeColunas,0){
    		boolean[] canEdit = new boolean[]{
    				false,false,false,false,false,false,false,true
    		};
    		@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit [columnIndex];
			}
    	};
        int linha =0;
        while(linha<lista.size()){
            tbm.addRow(new Object[1]);
            Iterator<String> iterator = ((ArrayList)lista.get(linha)).iterator();
            int coluna = 0;
            
            boolean achei = false;
            String coluna7="";
            while(iterator.hasNext()){
            	String valor = iterator.next();
            	if(coluna==4){//se coluna for 4 retornará verdadeiro para a proxima coluna
            		if(!valor.equals("Não Existe")){
            			achei = true;
            		}
            	}//enviar novo valor para a coluna de acordo com o criterio recebido
            	if(coluna==5 && achei==true){
            		if(valor.equals("Não Existe")){
            			valor="Erro na Leitura do Arquivo PDF";
            			coluna7="Nec.Intervenção Manual";
            		}
            	}
            	tbm.setValueAt(valor, linha, coluna);
                coluna++;
            }
            if(coluna==7){
            	tbm.setValueAt(coluna7, linha, coluna);
            }
            linha++;
        }
        table.setModel(tbm);
        table.setRowHeight(30);
        if (table.getColumnModel().getColumnCount() > 0) {
        	table.getColumnModel().getColumn(0).setPreferredWidth(40);
        	table.getColumnModel().getColumn(1).setPreferredWidth(40);
        	table.getColumnModel().getColumn(2).setMaxWidth(150);
        	table.getColumnModel().getColumn(3).setPreferredWidth(100);
        	table.getColumnModel().getColumn(7).setMinWidth(100);
        }
        
//        JButton button = new ButtonColumn(table, 7).getButton();
//        button.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
////				ImageView dialog = new ImageView(menu,pegarDadosTabela(table),table,true,table.getSelectedRow(),pastaDosArquivos);
////            	dialog.setVisible(true);
//			}
//		});
        TableCellRenderer tcr1 = new Colorir();
        TableColumn column1 = table.getColumnModel().getColumn(4);
        column1.setCellRenderer(tcr1);

        TableCellRenderer tcr2 = new Colorir();
        TableColumn column2 = table.getColumnModel().getColumn(5);
        column2.setCellRenderer(tcr2);
        
    }
    public int pegarNumeroDeLinhas(JTable table){
        DefaultTableModel tbm = (DefaultTableModel)table.getModel();
        return tbm.getRowCount();
    }
    public TreeSet<String> pegarValores(JTable table){
        DefaultTableModel tbm = (DefaultTableModel)table.getModel();
        int i=0;
        TreeSet<String> mapa = new TreeSet<>();
        while(i<tbm.getRowCount()){
            mapa.add((String)tbm.getValueAt(i, 0));
            i++;
        }
        return mapa;
    }
    
    public void carregaTodos(JTable table, Iterator iterator){
        table.getColumnModel().getColumn(0);
        DefaultTableModel tbm = (DefaultTableModel)table.getModel();
        limparTabela(table);
        int i = 0;
        do{
            tbm.addRow(new String[1]);
            tbm.setValueAt(iterator.next(), i, 0);
            i++;
        }while(iterator.hasNext());
}
public void limparTabela(JTable table){
    DefaultTableModel tbm = (DefaultTableModel)table.getModel();
    for(int i=tbm.getRowCount()-1; i>=0; i--){
        tbm.removeRow(i);
    }
}
public void addTudoOrRemoveTudo(JTable tabela1, JTable tabela2){
    DefaultTableModel tb1 = (DefaultTableModel)tabela1.getModel();
    DefaultTableModel tb2 = (DefaultTableModel)tabela2.getModel();
   
    while(tb1.getRowCount()>0){
        int linha = tb2.getRowCount();

        for(int i=0; i<tb1.getRowCount(); i++){
            String codigo = (String) tb1.getValueAt(i, 0);
            
            tb2.addRow(new Object[1]);
            tb2.setValueAt(codigo, linha, 0);
            linha++;

            tb1.removeRow(i);
        }
    }
}
public void addUmOrRemoveUm(JTable tabela1, JTable tabela2, String action, String tabela){
    DefaultTableModel tb1 = (DefaultTableModel)tabela1.getModel();
    DefaultTableModel tb2 = (DefaultTableModel)tabela2.getModel();
    
    if(tb1.getRowCount()>0){
        int linha = tabela1.getSelectedRow();
        if(linha!=-1){
            String codigo = (String) tabela1.getValueAt(linha, 0);
            
            int linhaAux = tabela2.getRowCount();
            tb2.addRow(new Object[1]);
            tb2.setValueAt(codigo, linhaAux, 0);
            tb1.removeRow(linha);
        }
        else
            JOptionPane.showMessageDialog(null, "Selecione um valor da "+tabela+" para "+action+"!");
    }
    else
        JOptionPane.showMessageDialog(null, "Não há valores na tabela para "+action+"!");
}
public List<CadastroBean> pegarDadosTabela(JTable tabela){
    DefaultTableModel tb = (DefaultTableModel)tabela.getModel();
    List<CadastroBean> lista = new ArrayList();
    
    for(int i =0; i<tb.getRowCount(); i++){
        CadastroBean cb = new CadastroBean();
        cb.setCodigo((String)tb.getValueAt(i, 0));
        cb.setStatus((String)tb.getValueAt(i, 1));
        cb.setNome((String)tb.getValueAt(i, 2));
        cb.setCnpj((String)tb.getValueAt(i, 3));
        cb.setStatusCodigo((String)tb.getValueAt(i, 4));
        cb.setStatusCnpj((String)tb.getValueAt(i, 5));
        cb.setObservacao((String)tb.getValueAt(i, 6));
        cb.setLocalizacao(i);
        lista.add(cb);
    }
    return lista;
}

public class Colorir extends JLabel implements TableCellRenderer{
    public Colorir(){
        this.setOpaque(true);
    }
    @Override
    public Component getTableCellRendererComponent(
        JTable table, 
        Object value, boolean isSelected, boolean hasFocus,
           int row, int column){

        if(value.toString().equals("Não Existe") || value.toString().equals("Rejeitado Manualmente") ){
            setBackground(Color.RED);
            setForeground(Color.WHITE);
        }
        else if(value.toString().equals("CNPJ Inválido")){
        	setBackground(Color.YELLOW);
            setForeground(Color.WHITE);
        }
        else if(value.toString().equals("Erro na Leitura do Arquivo PDF")){
        	setBackground(Color.ORANGE);
            setForeground(Color.WHITE);
        }
        else{
            setBackground(Color.GREEN);
            setForeground(Color.WHITE);
//setBackground(table.getBackground());		
        }
        setText(value.toString());
        return this;   	
    }
  
  public void validate() {}
  public void revalidate() {}
  protected void firePropertyChange(String propertyName,
     Object oldValue, Object newValue) {}
  public void firePropertyChange(String propertyName,
     boolean oldValue, boolean newValue) {}
}
}
