package com.prolink.view;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;


public class PdfParaImagem {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws FileNotFoundException, IOException {
          File input = new File("teste.pdf");
          PDDocument doc = PDDocument.load(new FileInputStream(input));
          PDFRenderer pdfRenderer = new PDFRenderer(doc);
          BufferedImage[] bf = new BufferedImage[doc.getNumberOfPages()];
          for(int page = 0; page<doc.getNumberOfPages(); page++){
        	  BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 200,ImageType.RGB);
        	  ImageIOUtil.writeImage(bim, "pdfimage"+(page+1)+".jpg", 200);
          }
		  doc.close();
          new PdfParaImagem();
    }
    public PdfParaImagem(){
          JDialog jdialog = new JDialog();
          jdialog.setSize(700,700);
          jdialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
          
          JPanel panel = new JPanel();
          panel.setLayout(null);
          jdialog.add(panel);
          
          ImageIcon ic = receberIcon(jdialog);
          JLabel label = new JLabel(ic);
          JScrollPane scroll = new JScrollPane();
          scroll.setBounds(10,10, ic.getIconWidth(),jdialog.getHeight());
          scroll.setViewportView(label);
          panel.add(scroll);
          jdialog.addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				ImageIcon ic2 = receberIcon(jdialog);
				int altScroll = ic2.getIconHeight()+20<=jdialog.getHeight()?ic2.getIconHeight():jdialog.getHeight()-20;
				label.setIcon(ic2);
				scroll.setBounds(10,10, ic2.getIconWidth(),altScroll);  
				jdialog.repaint();
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});
          jdialog.setLocationRelativeTo(null);
          jdialog.setVisible(true);
    }
    public ImageIcon receberIcon(JDialog jdialog){
    	ImageIcon ic = new ImageIcon("pdfimage1.png");
        int lar = ic.getIconWidth();
        int alt = ic.getIconHeight();
        int novaLar = jdialog.getWidth()-40;
        ic.setImage(ic.getImage().getScaledInstance(novaLar, alt/lar*novaLar, 100));
        return ic;
    }
}

