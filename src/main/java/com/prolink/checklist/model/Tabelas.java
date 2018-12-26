package com.prolink.checklist.model;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.prolink.checklist.controller.ImageController;
import com.prolink.checklist.controller.UtilsController;
import com.prolink.checklist.enuns.Mensageria;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class Tabelas {
    
	private static Tabelas instance;
	
	private TableView<Indexador> tbFiltro;
	private TableView<Resumo> tbRelatorio;
	private TableView<Resultado> tbResultado;
	
	private UtilsController controller;
	
    public static Tabelas getInstance(UtilsController controller) {
		if(instance==null) instance = new Tabelas(controller);
		return instance;
	}
    public Tabelas(UtilsController controller) {
		this.controller=controller;
	}
    private void abrirPainelVerificador(Cliente cliente, Map<Path, Mensageria> item) {
    	try {
    		Stage stage = new Stage();
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MenuImage.fxml"));
    		ImageController control = new ImageController(stage,cliente,item);
    		loader.setController(control);
    		Parent root = loader.load();
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.show();
    		stage.onHidingProperty().addListener(e->{
    			controller.recalcular();
    			tbResultado.refresh();
    			tbRelatorio.refresh();
    		});
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
	}
	public void tabelaFiltro(TableView<Indexador> tbFiltro){
		this.tbFiltro=tbFiltro;
        TableColumn<Indexador, Boolean> colunaEditar = new TableColumn<>("");
        colunaEditar.setCellValueFactory(new PropertyValueFactory<>("habilitado"));
        colunaEditar.setCellFactory((TableColumn<Indexador, Boolean> param) -> {
            final TableCell<Indexador, Boolean> cell = new TableCell<Indexador, Boolean>() {
                final JFXCheckBox checkBox = new JFXCheckBox("");
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        checkBox.setSelected(item);
                        checkBox.selectedProperty().addListener((observable, oldValue, newValue) ->
                                tbFiltro.getItems().get(getIndex()).setHabilitado(newValue)
                        );
                        setGraphic(checkBox);
                        setText(null);
                    }
                }
            };
            return cell;
        });
        colunaEditar.setPrefWidth(40);
        TableColumn<Indexador, String> colunaNome = new TableColumn<>("");
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colunaNome.setMinWidth(150);
        tbFiltro.getColumns().addAll(colunaEditar,colunaNome);
    }
    public void tabelaRelatorio(TableView<Resumo> tbRelatorio){
    	this.tbRelatorio=tbRelatorio;
        TableColumn<Resumo, Mensageria> colunaNome = new TableColumn<>("NOME");
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("mensageria"));
        colunaNome.setPrefWidth(300);
        
        TableColumn<Resumo, Integer> colunaValor = new TableColumn<>("TOTAL");
        colunaValor.setCellValueFactory(new PropertyValueFactory<>("total"));

        TableColumn<Resumo, Boolean> colunaIntervencao = new TableColumn<>("DESCRICAO");
        colunaIntervencao.setCellValueFactory(new PropertyValueFactory<>("intervencao"));
        colunaIntervencao.setCellFactory((TableColumn<Resumo, Boolean> param) -> {
            final TableCell<Resumo, Boolean> cell = new TableCell<Resumo, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Resumo r = tbRelatorio.getItems().get(getIndex());
                        if(r.getTotal()>0 && r.getMensageria().getStatus().equals(Mensageria.Status.ERRO))
                            setText("NECESSÁRIO INTERVENÇÃO");
                        else
                            setText("");
                    }
                }
            };
            return cell;
        });
        colunaIntervencao.setPrefWidth(200);
        tbRelatorio.getColumns().addAll(colunaNome,colunaValor,colunaIntervencao);
    }
    public void tabelaResultado(TableView<Resultado> tbResultado,JFXCheckBox ckCodigo,JFXCheckBox ckCnpj){
        this.tbResultado=tbResultado;
    	tbResultado.getColumns().clear();
        TableColumn<Resultado, Cliente> colunaCliente = new TableColumn<>("CLIENTE");
        colunaCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        colunaCliente.setPrefWidth(150);
        colunaCliente.setCellFactory((TableColumn<Resultado, Cliente> param) -> {
            final TableCell<Resultado, Cliente> cell = new TableCell<Resultado, Cliente>() {
                @Override
                protected void updateItem(Cliente item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
            return cell;
        });
        TableColumn<Resultado, Map<Path,Mensageria>> colunaId = new TableColumn<>("NOME NO ARQUIVO");
        colunaId.setCellValueFactory(new PropertyValueFactory<>("arquivoNome"));
        colunaId.setCellFactory((TableColumn<Resultado, Map<Path,Mensageria>> param) -> {
            final TableCell<Resultado, Map<Path,Mensageria>> cell = new TableCell<Resultado, Map<Path,Mensageria>>() {
                final JFXTextArea textArea = new JFXTextArea();
                @Override
                protected void updateItem(Map<Path,Mensageria> item, boolean empty) {
                    super.updateItem(item, empty);
                    textArea.setEditable(false);
                    textArea.setWrapText(true);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        if(item.isEmpty()){
                            setGraphic(null);
                            setText(ckCodigo.isSelected()?Mensageria.ARQUIVONAOENCONTRADO.toString():"");
                        }
                        else {
                            StringBuilder builder = new StringBuilder();
                            item.keySet().forEach(c -> builder.append(c.getFileName()).append("=").append(item.get(c)).append("\n"));
                            textArea.setText(builder.toString().trim());
                            setGraphic(textArea);
                            setText(null);
                        }
                    }
                }
            };
            return cell;
        });
        colunaId.setPrefWidth(250);
        
        TableColumn<Resultado, Map<Path,Mensageria>> colunaBotaoId = new TableColumn<>("");
        colunaBotaoId.setCellValueFactory(new PropertyValueFactory<>("arquivoNome"));
        colunaBotaoId.setCellFactory((TableColumn<Resultado, Map<Path,Mensageria>> param) -> {
            final TableCell<Resultado, Map<Path,Mensageria>> cell = new TableCell<Resultado, Map<Path,Mensageria>>() {
                final JFXButton button = new JFXButton();
                @Override
                protected void updateItem(Map<Path,Mensageria> item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        if(item.isEmpty()){
                            setGraphic(null);
                        }
                        else {
                        	Resultado resultado = tbResultado.getItems().get(getIndex());
                            button.setText("Abrir");
                        	button.setOnAction(event->abrirPainelVerificador(resultado.getCliente(),item));
                            setGraphic(button);
                            setText(null);
                        }
                    }
                }
            };
            return cell;
        });

        TableColumn<Resultado, Map<Path,Mensageria>> colunaConteudo = new TableColumn<>("CONTEUDO DO ARQUIVO");
        colunaConteudo.setCellValueFactory(new PropertyValueFactory<>("arquivoConteudo"));
        colunaConteudo.setCellFactory((TableColumn<Resultado, Map<Path,Mensageria>> param) -> {
            final TableCell<Resultado, Map<Path,Mensageria>> cell = new TableCell<Resultado, Map<Path,Mensageria>>() {
                final JFXTextArea textArea = new JFXTextArea();
                @Override
                protected void updateItem(Map<Path,Mensageria> item, boolean empty) {
                    super.updateItem(item, empty);
                    textArea.setEditable(false);
                    textArea.setWrapText(true);
                    if (item==null || empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Resultado result = tbResultado.getItems().get(getIndex());
                        if(item.isEmpty()){
                            setGraphic(null);
                            setText(ckCnpj.isSelected()?
                                    (result.getCliente().isCnpjValido()?Mensageria.ARQUIVONAOENCONTRADO.toString()
                                            :Mensageria.CNPJINVALIDO.toString()):
                                    "");
                        }
                        else {
                            StringBuilder builder = new StringBuilder();
                            item.keySet().forEach(c -> builder.append(c.getFileName()).append("=").append(item.get(c)).append("\n"));
                            textArea.setText(builder.toString().trim());
                            setGraphic(textArea);
                            setText(null);
                        }

                    }
                }
            };
            return cell;
        });
        colunaConteudo.setPrefWidth(250);
        
        TableColumn<Resultado, Map<Path,Mensageria>> colunaBotaoConteudo = new TableColumn<>("");
        colunaBotaoConteudo.setCellValueFactory(new PropertyValueFactory<>("arquivoConteudo"));
        colunaBotaoConteudo.setCellFactory((TableColumn<Resultado, Map<Path,Mensageria>> param) -> {
            final TableCell<Resultado, Map<Path,Mensageria>> cell = new TableCell<Resultado, Map<Path,Mensageria>>() {
                final JFXButton button = new JFXButton();
                @Override
                protected void updateItem(Map<Path,Mensageria> item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        if(item.isEmpty()){
                            setGraphic(null);
                        }
                        else {
                        	Resultado resultado = tbResultado.getItems().get(getIndex());
                            button.setText("Abrir");
                            button.setOnAction(event->abrirPainelVerificador(resultado.getCliente(),item));
                            setGraphic(button);
                            setText(null);
                        }
                    }
                }
            };
            return cell;
        });
        tbResultado.setFixedCellSize(75);
        tbResultado.getColumns().add(colunaCliente);
        if(ckCodigo.isSelected()) {
            tbResultado.getColumns().add(colunaId);
            tbResultado.getColumns().add(colunaBotaoId);
        }
        if(ckCnpj.isSelected()) {
            tbResultado.getColumns().add(colunaConteudo);
            tbResultado.getColumns().add(colunaBotaoConteudo);
        }
    }

}
