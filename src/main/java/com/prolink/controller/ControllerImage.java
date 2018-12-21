package com.prolink.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import com.prolink.model.CadastroBean;
import com.prolink.view.ImageView;

import static com.prolink.view.ImageView.*;

public class ControllerImage implements ComponentListener, ActionListener {

	boolean editarUm;
	JTable table;
	List<CadastroBean> lista;
	int registro, registroAtual=1;
	Rectangle dimensaoAnterior;
	ImageView view;
	String pastaDosArquivos;
	File dir = new File(System.getProperty("user.dir")+"/"+"convertImages");
	
	public void iniciar(ImageView view,List<CadastroBean> lista,JTable table,boolean editarUm, int registro, String pastaDosArquivos){
		this.table=table;
		this.editarUm=editarUm;
		this.registro=registro;
		dimensaoAnterior=view.getBounds();
		this.view = view;
		this.pastaDosArquivos=pastaDosArquivos;
		if(!dir.exists())
			dir.mkdir();
		processar(lista);
	}
	private void processar(List<CadastroBean> lista){
		File[] file = dir.listFiles();
		for(File f : file) 
			f.delete();
		int contador = 0;
		this.lista = new ArrayList<>();
		if(registro==-1){
			for(int i =0; i<lista.size();i++){
				CadastroBean cb = lista.get(i);
		        if("Erro na Leitura do Arquivo PDF".equals(cb.getStatusCnpj())){
		        	contador++;
		        	File im = criadorImagem(new File(pastaDosArquivos+"/"+cb.getStatusCodigo()));
		        	cb.setImage(im);
		        	this.lista.add(cb);
		        }
			}
		}
		else{
			registroAtual=registro+1;
		}
		txValue2.setText(""+contador);
		preencherFormulario(this.lista.get(registroAtual-1), registroAtual);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "Validar":
			atualizar("Validado Manualmente");
			if(registroAtual<lista.size()){
				registroAtual++;
			}
			preencherFormulario(lista.get(registroAtual-1),registroAtual);
			break;
		case "Rejeitar":
			atualizar("Rejeitado Manualmente");
			if(registroAtual<lista.size()){
				registroAtual++;
				preencherFormulario(lista.get(registroAtual-1),registroAtual);
			}
			
			break;
		case "Sair":
			view.dispose();
			break;
		case "Proximo":
			if(registroAtual<lista.size()){
				registroAtual++;
				preencherFormulario(lista.get(registroAtual-1),registroAtual);
			}
			break;
		case "Anterior":
			if(registroAtual>1){
				registroAtual--;
				preencherFormulario(lista.get(registroAtual-1),registroAtual);
			}
			break;
		case "Ultimo":
			if(registroAtual<lista.size()){
				registroAtual=lista.size();
				preencherFormulario(lista.get(registroAtual-1),registroAtual);
			}
			break;
		case "Primeiro":
			if(registroAtual>1){
				registroAtual=1;
				preencherFormulario(lista.get(registroAtual-1),registroAtual);
			}
			break;
		default:
			break;
		}
	}
	private void atualizar(String status){
		CadastroBean b = lista.get(registroAtual-1);
		b.setStatusCnpj(status);
		lista.set(registroAtual-1, b);
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		model.setValueAt(b.getStatusCnpj(), b.getLocalizacao(), 5);
		model.setValueAt("", b.getLocalizacao(), 7);
	}
	
	private void preencherFormulario(CadastroBean bean, int valor){
		ImageIcon ic2 = receberIcon(bean.getImage().getAbsolutePath(),pnBody);
		lbIcon.setIcon(ic2);
		txtValue1.setText(""+valor);
		txNome.setText(bean.getNome());
		txCnpj.setText(bean.getCnpj());
		txStatus.setText(bean.getStatus());
		Color color = Color.BLUE;
		if("Erro na Leitura do Arquivo PDF".equals(bean.getStatusCnpj()) || "Rejeitado Manualmente".equals(bean.getStatusCnpj()))
			color = Color.RED;
		txNome.setBorder(new BevelBorder(BevelBorder.LOWERED, color, color, color, color));
		txCnpj.setBorder(new BevelBorder(BevelBorder.LOWERED, color, color, color, color));
		txStatus.setBorder(new BevelBorder(BevelBorder.LOWERED, color, color,color, color));
	}
	
	
	@Override
	public void componentResized(ComponentEvent e) {
		if(e.getComponent().getBounds()!=dimensaoAnterior){
			Runnable task = () -> {
				if(!lista.isEmpty() && !"".equals(txtValue1.getText())) recriarComponents(e.getComponent(), lista.get(registroAtual-1).getImage().getAbsolutePath());
			};
			new Thread(task).start();
		}
	}

	@Override
	public void componentMoved(ComponentEvent e) {

	}

	@Override
	public void componentShown(ComponentEvent e) {

	}

	@Override
	public void componentHidden(ComponentEvent e) {

	}
	public void recriarComponents(Component e,String image){
		//pnAuxiliar.setBounds(10, 13, 317, 180);
		pnBody.setBounds(343, 13, e.getWidth()-380, e.getHeight()-63);
		ImageIcon ic2 = receberIcon(image,pnBody);
		lbIcon.setIcon(ic2);
		int altScroll = ic2.getIconHeight()+20<=pnBody.getHeight()?ic2.getIconHeight():pnBody.getHeight()-40;
		scrollPane.setBounds(10,10, pnBody.getWidth()-20,altScroll);
		JDialog dialog = (JDialog)e;
		dimensaoAnterior = dialog.getBounds();
		dialog.repaint();	
	}
	
	public File criadorImagem(File arquivoPdf){
		try {
			PDDocument doc = PDDocument.load(new FileInputStream(arquivoPdf));
			PDFRenderer pdfRenderer = new PDFRenderer(doc);
			BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 200,ImageType.RGB);
			File file = new File(dir.getAbsolutePath()+"/"+arquivoPdf.getName().replace(".pdf", "")+".jpg");
			ImageIOUtil.writeImage(bim, file.getAbsolutePath(), 200);
			doc.close();
			return file;
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Imagem não encontrado!\n"+e);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, "Imagem não encontrado!\n"+ex);
		}
		return null;
//        BufferedImage[] bf = new BufferedImage[doc.getNumberOfPages()];
//        for(int page = 0; page<doc.getNumberOfPages(); page++){
//      	  BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 200,ImageType.RGB);
//      	  ImageIOUtil.writeImage(bim, "pdfimage"+(page+1)+".jpg", 200);
//        }
  	  
	}
	public ImageIcon receberIcon(String imageName,Component component){
    	ImageIcon ic = new ImageIcon(imageName);
        int lar = ic.getIconWidth();
        int alt = ic.getIconHeight();
        int novaLar = component.getWidth()-20;
        ic.setImage(ic.getImage().getScaledInstance(novaLar, alt/lar*novaLar, 100));
        return ic;
    }
}
