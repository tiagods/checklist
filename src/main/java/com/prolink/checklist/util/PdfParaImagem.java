package com.prolink.checklist.util;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;


public class PdfParaImagem {
	public Path criarImagem(Path origem,Path destino) throws FileNotFoundException, IOException {
	    PDDocument doc = PDDocument.load(new FileInputStream(origem.toFile()));
        PDFRenderer pdfRenderer = new PDFRenderer(doc);
        BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 300,ImageType.RGB);
    	ImageIOUtil.writeImage(bim, destino.toString(), 300);
    	 /*
        BufferedImage[] bf = new BufferedImage[doc.getNumberOfPages()];
        for(int page = 0; page<doc.getNumberOfPages(); page++){
      	  BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 200,ImageType.RGB);
      	  ImageIOUtil.writeImage(bim, "pdfimage"+(page+1)+".jpg", 200);
        }
        */
    	doc.close();
    	return destino;
	}
}

