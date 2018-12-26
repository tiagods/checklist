package com.prolink.checklist.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.prolink.checklist.enuns.Mensageria;
import com.prolink.checklist.model.Cliente;
import com.prolink.checklist.util.PdfParaImagem;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import jxl.format.Border;

public class ImageController implements Initializable{
	 	@FXML
	    private JFXTextField txNome;

	    @FXML
	    private JFXTextField txStatus;

	    @FXML
	    private JFXTextField txCnpj;

	    @FXML
	    private JFXTextArea txResultado;

	    @FXML
	    private JFXButton btValidar;

	    @FXML
	    private JFXButton btRejeitar;

	    @FXML
	    private JFXButton btSair;

	    @FXML
	    private JFXTextArea txSobre;

	    @FXML
	    private ScrollPane scroll;

	    @FXML
	    private ImageView imageView;
	    
	    @FXML
	    private AnchorPane anchorPane;
	    
	    @FXML
	    private VBox vBoxLeft;
	    
	    @FXML
	    private JFXTextField txValor1,txValor2;
	    
	    @FXML
	    private JFXButton btPrimeiro,btAnterior,btProximo,btUltimo;
	    
	    private Stage stage;
	    
	    private Cliente cliente;
	    private Map<Path,Mensageria> arquivos;
	    private Map<Path,Path> pdfAndImage = new HashMap<>();
		private int registroAtual=1;
		private File dir = new File(System.getProperty("user.dir")+"/"+"convertImages");
		private PdfParaImagem pdfToImage = new PdfParaImagem();
		
		public enum Etapa{
			PRIMEIRO,ANTERIOR,PROXIMO,ULTIMO
		}
		
	    public ImageController(Stage stage, Cliente cliente, Map<Path, Mensageria> arquivos) {
	    	this.cliente=cliente;
	    	this.arquivos=arquivos;
	    	this.stage=stage;
		}
	    @Override
	    public void initialize(URL location, ResourceBundle resources) {
	    	if(!dir.exists()) dir.mkdir();
			else {
				try {
					Iterator<Path> files = Files.list(dir.toPath()).iterator();
					while(files.hasNext()) Files.delete(files.next());
				}catch(IOException e) {}
			}
			processar(arquivos);
			combos();
	    }
	    private void ajustarImagem() {
	    	scroll.setMinWidth(stage.getWidth()-vBoxLeft.getWidth());
	    	scroll.setMinViewportWidth(scroll.getWidth()-20);
	    	double novaLargura = scroll.getWidth();
			double novaAltura = imageView.getFitHeight()/imageView.getFitWidth()*novaLargura;
			imageView.setFitHeight(novaAltura);
			imageView.setFitWidth(novaLargura);
	    }
	    private void atualizar(Mensageria status){
			Path origem = pdfAndImage.keySet().stream().collect(Collectors.toList()).get(registroAtual-1);
			arquivos.put(origem, status);
			preencherFormulario();
		}
	    private void combos() {
	    	ChangeListener<Number> event = ((obs,oldValue,newValue)->ajustarImagem());
	    	stage.widthProperty().addListener(event);
	    	stage.heightProperty().addListener(event);
	    	ajustarImagem();
	    	
	    	btPrimeiro.setOnAction(e->proximoOuAnterior(Etapa.PRIMEIRO));
	    	btAnterior.setOnAction(e->proximoOuAnterior(Etapa.ANTERIOR));
	    	btProximo.setOnAction(e->proximoOuAnterior(Etapa.ULTIMO));
	    	btUltimo.setOnAction(e->proximoOuAnterior(Etapa.ULTIMO));
	    	
	    	btSair.setOnAction(e->stage.close());
	    	btRejeitar.setOnAction(e->{
	    		atualizar(Mensageria.RECUSADOMANUALMENTE);
	    		proximoOuAnterior(Etapa.PROXIMO);
	    	});
	    	btValidar.setOnAction(e->{
	    		atualizar(Mensageria.VALIDADOMANUALEMNTE);
				proximoOuAnterior(Etapa.PROXIMO);
	    	});
	    }
	    private Path getFile() {
			Path origem = pdfAndImage.keySet().stream().collect(Collectors.toList()).get(registroAtual-1);
			return origem;
		}
		private Path getFileImage(Path origem) {
			Path image = pdfAndImage.get(origem);
			return image;
		}
		private void preencherFormulario() {
			Path origem = getFile();
			Path image = getFileImage(origem);
			Mensageria mensageria = arquivos.get(origem);
			try {
	    		FileInputStream input = new FileInputStream(image.toFile());
		        Image imagem = new Image(input);
		        imageView.setImage(imagem);
	    	}catch(IOException e) {e.printStackTrace();}
	    	
			txValor1.setText(""+registroAtual);
			txValor2.setText(""+pdfAndImage.size());
			
			txNome.setText(cliente.getNome());
			txCnpj.setText(cliente.getCnpj());
			txStatus.setText(cliente.getStatus());
			txResultado.setText(mensageria.getDescricao());
			
			String color = "green";
			
			if(mensageria.getStatus().equals(Mensageria.Status.ERRO)) color="red";
			
			String style = "-fx-border-color: "+color+" ; -fx-border-width: 2px;";
			txNome.setStyle(style);
			txCnpj.setStyle(style);
			txStatus.setStyle(style);
			txResultado.setStyle(style);
		}
		private void proximoOuAnterior(Etapa etapa) {
			switch(etapa){
				case PRIMEIRO:
					if(registroAtual!=1) {
		    			registroAtual=1;
		    			preencherFormulario();
		    		}
		    		break;
				case ANTERIOR:
					if(registroAtual>1) {
		    			registroAtual--;
		    			preencherFormulario();
		    		}
					break;
		    	case PROXIMO:
		    		if(registroAtual<pdfAndImage.size()) {
		    			registroAtual++;
		    			preencherFormulario();
		    		}
		    		break;
		    	case ULTIMO:
		    		if(registroAtual!=pdfAndImage.size()) {
		    			registroAtual=pdfAndImage.size();
		    			preencherFormulario();
		    		}
		    		break;
		    	default:
		    		break;
			}
		}
		private void processar(Map<Path,Mensageria> arquivos) {
			File[] file = dir.listFiles();
			for(File f : file) 
				f.delete();
			arquivos.keySet().forEach(c -> {
				try {
					Path arquivo = Paths.get(dir.toString(), c.getFileName().toString().toLowerCase().replace(".pdf", ".jpg"));
					Path im = pdfToImage.criarImagem(c,arquivo);
					pdfAndImage.put(c, im);
				}catch(IOException e) {
					e.printStackTrace();
				}
			});
			preencherFormulario();
		}
}
