package com.prolink.checklist.controller;

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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

import com.prolink.checklist.enuns.Mensageria;
import com.prolink.checklist.model.Cliente;
import static com.prolink.checklist.util.ImageView.*;
import com.prolink.checklist.util.ImageView;

public class ControllerImage implements ComponentListener, ActionListener {

	private Map<Path,Mensageria> arquivos;
	private Map<Path,Path> pdfAndImage = new HashMap<>();
	private int registroAtual=1;
	private Rectangle dimensaoAnterior;
	private ImageView view;
	private File dir = new File(System.getProperty("user.dir")+"/"+"convertImages");
	private Cliente cliente;
	
	public void iniciar(ImageView view,Cliente cliente, Map<Path,Mensageria> arquivos){
		this.dimensaoAnterior=view.getBounds();
		this.arquivos=arquivos;
		this.view = view;
		this.cliente = cliente;
		if(!dir.exists()) dir.mkdir();
		else {
			try {
				Iterator<Path> files = Files.list(dir.toPath()).iterator();
				while(files.hasNext()) Files.delete(files.next());
			}catch(IOException e) {}
		}
		processar(arquivos);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "Validar":
			atualizar(Mensageria.VALIDADOMANUALEMNTE);
			if(registroAtual<arquivos.size())
				registroAtual++;
			preencherFormulario();
			break;
		case "Rejeitar":
			atualizar(Mensageria.RECUSADOMANUALMENTE);
			if(registroAtual<arquivos.size()){
				registroAtual++;
				preencherFormulario();
			}
			break;
		case "Sair":
			view.dispose();
			break;
		case "Proximo":
			if(registroAtual<arquivos.size()){
				registroAtual++;
				preencherFormulario();
			}
			break;
		case "Anterior":
			if(registroAtual>1){
				registroAtual--;
				preencherFormulario();
			}
			break;
		case "Ultimo":
			if(registroAtual<arquivos.size()){
				registroAtual=arquivos.size();
				preencherFormulario();
			}
			break;
		case "Primeiro":
			if(registroAtual>1){
				registroAtual=1;
				preencherFormulario();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void componentResized(ComponentEvent e) {
		if(e.getComponent().getBounds()!=dimensaoAnterior){
			Runnable task = () -> {
				if(!arquivos.isEmpty() && !"".equals(txtValue1.getText())) { 
					Path origem = getFile();
					recriarComponents(
							e.getComponent(), 
							getFileImage(origem).toString()
							);
				}
			};
			new Thread(task).start();
		}
	}
	@Override
	public void componentMoved(ComponentEvent e) {}
	@Override
	public void componentShown(ComponentEvent e) {}
	@Override
	public void componentHidden(ComponentEvent e) {}

	private void atualizar(Mensageria status){
		Path origem = pdfAndImage.keySet().stream().collect(Collectors.toList()).get(registroAtual-1);
		arquivos.put(origem, status);
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
	private Path getFile() {
		Path origem = pdfAndImage.keySet().stream().collect(Collectors.toList()).get(registroAtual-1);
		return origem;
	}
	private Path getFileImage(Path origem) {
		Path image = pdfAndImage.get(origem);
		return image;
	}
	private void preencherFormulario(){
		Path origem = getFile();
		Path image = getFileImage(origem);
		Mensageria mensageria = arquivos.get(origem);
		ImageIcon ic2 = receberIcon(image.toString(),pnBody);
		lbIcon.setIcon(ic2);
		txtValue1.setText(""+registroAtual);
		txNome.setText(cliente.getNome());
		txCnpj.setText(cliente.getCnpj());
		txStatus.setText(cliente.getStatus());
		Color color = Color.GREEN;
		if(mensageria.getStatus().equals(Mensageria.Status.ERRO)) color = Color.RED;
		txNome.setBorder(new BevelBorder(BevelBorder.LOWERED, color, color, color, color));
		txCnpj.setBorder(new BevelBorder(BevelBorder.LOWERED, color, color, color, color));
		txStatus.setBorder(new BevelBorder(BevelBorder.LOWERED, color, color,color, color));
	}
	private void processar(Map<Path,Mensageria> arquivos){
		File[] file = dir.listFiles();
		for(File f : file) 
			f.delete();
		arquivos.keySet().forEach(c -> {
			File im = criadorImagem(c.toFile());
			pdfAndImage.put(c, im.toPath());
		});
		txValue2.setText(""+pdfAndImage.size());
		preencherFormulario();
	}
	public ImageIcon receberIcon(String imageName,Component component){
    	ImageIcon ic = new ImageIcon(imageName);
        int lar = ic.getIconWidth();
        int alt = ic.getIconHeight();
        int novaLar = component.getWidth()-20;
        ic.setImage(ic.getImage().getScaledInstance(novaLar, alt/lar*novaLar, 100));
        return ic;
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
}
