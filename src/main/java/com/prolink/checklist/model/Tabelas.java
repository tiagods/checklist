package com.prolink.checklist.model;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.prolink.checklist.enuns.Mensageria;
import com.prolink.checklist.util.ImageView;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.nio.file.Path;
import java.util.Map;

import javax.swing.JFrame;

public class Tabelas {
    
	private static Tabelas instance;
	
    public static Tabelas getInstance() {
		if(instance==null) instance = new Tabelas();
		return instance;
	}
	
	public void tabelaFiltro(TableView<Indexador> tbFiltro){
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
        colunaNome.setPrefWidth(150);
        tbFiltro.getColumns().addAll(colunaEditar,colunaNome);
    }
    public void tabelaRelatorio(TableView<Resumo> tbRelatorio){
        TableColumn<Resumo, Mensageria> colunaNome = new TableColumn<>("Nome");
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("mensageria"));

        TableColumn<Resumo, Integer> colunaValor = new TableColumn<>("Total");
        colunaValor.setCellValueFactory(new PropertyValueFactory<>("total"));

        TableColumn<Resumo, Boolean> colunaIntervencao = new TableColumn<>("");
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
        tbRelatorio.getColumns().addAll(colunaNome,colunaValor,colunaIntervencao);
    }
    public void tabelaResultado(TableView<Resultado> tbResultado,JFXCheckBox ckCodigo,JFXCheckBox ckCnpj){
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
                            button.setText("Abrir Arquivo");
                        	button.setOnAction(event->{
                        		ImageView imageView = new ImageView(new JFrame(),resultado.getCliente(),item);
                        		imageView.setVisible(true);
                        	});
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
                            button.setText("Abrir Arquivo");
                        	button.setOnAction(event->{
                        		ImageView imageView = new ImageView(new JFrame(),resultado.getCliente(),item);
                        		imageView.setVisible(true);
                        	});
                            setGraphic(button);
                            setText(null);
                        }
                    }
                }
            };
            return cell;
        });

        tbResultado.setFixedCellSize(50);
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
