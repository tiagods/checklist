package com.prolink.checklist.controller;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jfoenix.controls.*;

import com.prolink.checklist.dao.ClienteDAO;
import com.prolink.checklist.enuns.Mensageria;
import com.prolink.checklist.model.*;
import com.prolink.checklist.util.LeitorExcel;
import com.prolink.checklist.util.LeitoraPDF;
import com.prolink.model.CadastroBean;
import com.prolink.model.ExcelGenerico;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jxl.write.WriteException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.eclipse.jdt.internal.compiler.ast.Clinit;
import org.fxutils.maskedtextfield.MaskTextField;
import org.fxutils.maskedtextfield.MaskedTextField;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import static com.prolink.checklist.enuns.Mensageria.VALIDADO;

public class CheckListController implements Initializable{
    @FXML
    private JFXTabPane tabPane;
    @FXML
    private Tab tab1, tab2, tab3;
	@FXML
    private JFXButton btInicio;

    @FXML
    private JFXComboBox<ObrigacaoTipo> cbObrigacao;

    @FXML
    private HBox hboxSelecao;

    @FXML
    private JFXToggleButton tgbAutomaticoManual;

    @FXML
    private HBox hboxPlanilha;

    @FXML
    private JFXTextField txLocalizacaoDatabase;

    @FXML
    private HBox hboxPDF;

    @FXML
    private JFXTextField txLocalizacaoPDF;

    @FXML
    private HBox hboxCombos;

    @FXML
    private JFXComboBox<Indexador> cbNome;

    @FXML
    private JFXComboBox<Indexador> cbCnpj;

    @FXML
    private JFXComboBox<Indexador> cbStatus;

    @FXML
    private HBox hboxCodigo;

    @FXML
    private JFXCheckBox ckCodigo;

    @FXML
    private JFXComboBox<Indexador> cbCodigo;

    @FXML
    private HBox hboxFiltro;

    @FXML
    private JFXComboBox<Indexador> cbFiltroSpecial;

    @FXML
    private VBox vboxFiltroTable;

    @FXML
    private JFXCheckBox ckSelecionarFiltro;

    @FXML
    private TableView<Indexador> tbFiltro;

    @FXML
    private JFXTextField txNomeArquivo;

    @FXML
    private JFXCheckBox ckCnpj;

    @FXML
    private HBox hboxLinhaPlanilha;

    @FXML
    private JFXComboBox<Integer> cbLinhaExcel;

    @FXML
    private MaskedTextField txCnpjIgnorar;
    @FXML
    private TableView<Resultado> tbResultado;
    @FXML
    private TableView<Resumo> tbRelatorio;

    @FXML
    private JFXButton btProcessar;

    @FXML
    private JFXButton btImpressao;

    @FXML
    private JFXButton btCancelar;

    @FXML
    private Label txProgresso;

    private Stage stage;

    private ClienteDAO clienteDAO = new ClienteDAO();

    private ArrayList<List<Indexador>> indexadors = new ArrayList<>();

    private Path ultimaPastaVisitada=null;



    private List<Indexador> getIndexadorDefault(){
        List<Indexador> index = new ArrayList<>();
        index.add(new Indexador(0, "COD"));
        index.add(new Indexador(1, "EMPRESA"));
        index.add(new Indexador(2, "STATUS"));
        index.add(new Indexador(3, "CNPJ"));
        return index;
    }

    public void alert(Alert.AlertType type,String titulo,String assunto, String mensagem){
        Alert alert = new Alert(type);
        alert.setTitle(titulo);
        alert.setHeaderText(assunto);
        alert.setContentText(mensagem);
        if(type != Alert.AlertType.INFORMATION)
            alert.getDialogPane().setMinSize(600,200);
        alert.showAndWait();
    }

    public File carregarPasta(Path diretorioAnterior) {
        Stage stage = new Stage();
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Carregar");
        Path file = diretorioAnterior==null?Paths.get(System.getProperty("user.home")):diretorioAnterior;
        fileChooser.setInitialDirectory(file.toFile());
        return fileChooser.showDialog(stage);
    }
    public File carregarArquivo(Path diretorioAnterior) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Carregar");
        FileChooser.ExtensionFilter filter1 = new FileChooser.ExtensionFilter("Pasta de Tabalho do Excel (*.xlsx)", "*.xlsx");
        FileChooser.ExtensionFilter filter2 = new FileChooser.ExtensionFilter("Pasta de Trabalho do Excel 97-2003 (*.xls)", "*.xls");
        fileChooser.getExtensionFilters().addAll(filter1,filter2);
        Path file = diretorioAnterior==null?Paths.get(System.getProperty("user.home")):diretorioAnterior;
        fileChooser.setInitialDirectory(Files.isDirectory(file)?file.toFile():file.getParent().toFile());
        return fileChooser.showOpenDialog(stage);
    }

    void combos(){
        tab1.setDisable(false);
        tab2.setDisable(true);
        tab3.setDisable(true);

        tabPane.getSelectionModel().selectFirst();
        btInicio.setOnAction(event -> {
            tabPane.getSelectionModel().select(tab2);
            tab1.setDisable(true);
            tab2.setDisable(false);
            tab3.setDisable(true);
        });
        btProcessar.setOnAction(event -> {
            processar();
        });
        btCancelar.setOnAction(event -> {
            tabPane.getSelectionModel().select(tab2);
            tab1.setDisable(true);
            tab2.setDisable(false);
            tab1.setDisable(true);
        });
        cbObrigacao.getItems().addAll(ObrigacaoTipo.values());
        cbObrigacao.getSelectionModel().selectFirst();
        cbObrigacao.valueProperty().addListener((observable, oldValue, newValue) -> {
            hboxSelecao.setVisible(newValue!=null);
            hboxPDF.setVisible(true);
            if(tgbAutomaticoManual.isSelected())
                refreshCombos(getIndexadorDefault());
            else{
                if(!txLocalizacaoDatabase.getText().equals("")) {
                    processarExcel();
                }

            }
        });
        tgbAutomaticoManual.selectedProperty().addListener((observable, oldValue, newValue) -> {
            tgbAutomaticoManual.setText(newValue?"Base Automatica":"Base Manual(Usar arquivo XLS ou XLSX)");
            hboxPlanilha.setVisible(!newValue);
            hboxLinhaPlanilha.setVisible(!newValue);
            hboxCombos.setVisible(!newValue);

            cbCodigo.setDisable(newValue);
            cbCnpj.setDisable(newValue);
            if(newValue) {
                ckCodigo.setSelected(newValue);
                ckCnpj.setSelected(newValue);
                refreshCombos(getIndexadorDefault());
            }
            else{
                refreshCombos(null);
                if(!txLocalizacaoDatabase.getText().equals("")) {
                    processar();
                }
            }
        });
        txLocalizacaoDatabase.setOnMouseClicked(event -> {
            File file = carregarArquivo(ultimaPastaVisitada);
            if(file!=null){
                txLocalizacaoDatabase.setText(file.toString());
                ultimaPastaVisitada = file.getParentFile().toPath();
                processarExcel();
            }
        });

        cbFiltroSpecial.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null) {
                if(tgbAutomaticoManual.isSelected())
                    tbFiltro.getItems().setAll(clienteDAO.findBy(cbFiltroSpecial.getValue()));
                else{
                    List<Indexador> inx = new ArrayList<>();
                    for(List<Indexador> list : indexadors){
                        Optional<Indexador> result = list.stream().filter(p->p.getIndex()==newValue.getIndex()).findFirst();
                        if(result.isPresent()) inx.add(result.get());
                    }
                    if(!inx.isEmpty()) inx.remove(0);
                    tbFiltro.getItems().setAll(inx);
                    tbFiltro.refresh();
                }
            }
            else{
                tbFiltro.getItems().clear();
            }
        });
        ckCodigo.selectedProperty().addListener((observable, oldValue, newValue) -> {
            cbCodigo.setVisible(newValue);
            if(!newValue) cbCodigo.setValue(null);
        });
        ckCnpj.selectedProperty().addListener((observable, oldValue, newValue) -> {
            cbCnpj.setVisible(newValue);
            if(!newValue) {
                alert(Alert.AlertType.WARNING,"Verificação Sugerida","O conteudo dos arquivos pdf's não serao verificados","Ao desmarcar esse campo, o cnpj no conteudo dos arquivos não serão analisados");
                cbCnpj.setValue(null);
            }
        });
        hboxPlanilha.setVisible(false);
        ckCodigo.setSelected(true);
        ckCnpj.setSelected(true);

        ckSelecionarFiltro.selectedProperty().addListener((observable, oldValue, newValue) -> {
            tbFiltro.getItems().stream().forEach(c->c.setHabilitado(newValue)); tbFiltro.refresh();});

        //btProcessar.setVisible(false);

        txLocalizacaoPDF.setOnMouseClicked(event -> {
            File file = carregarPasta(ultimaPastaVisitada);
            if(file!=null){
                txLocalizacaoPDF.setText(file.toString());
                /*
                    //verificar se existe subdiretorios
                    File[] files = file.listFiles();
                    boolean sub = false;
                    for (File f : files) {
                        if (f.isDirectory()) {
                            sub = true;
                            break;
                        }
                    }
                    if (sub) alert(Alert.AlertType.WARNING,"Aviso","Subdiretorios detectado",
                            "O sistema ainda não processa arquivos em subdiretorios, " +
                                    "\nDeixe os arquivos do formato PDF apenas na pasta:\n " + file.toString());
                                    */
                ultimaPastaVisitada = file.toPath();

            }
        });

        btImpressao.setOnAction(event -> {
            Integer[] larguraColunas = new Integer[]{12,15,20,20,43,43,43,43};
            ArrayList<ArrayList> arrayList = new ArrayList<>();

            ArrayList array1 = new ArrayList();
            array1.add("CODIGO");
            array1.add("STATUS");
            array1.add("CNPJ");
            array1.add("NOME");
            array1.add("CODIGO NO NOME");
            array1.add("VALIDACAO NOME");
            array1.add("CNPJ CONTEUDO");
            array1.add("VALIDACAO CONTEUDO-CNPJ");
            arrayList.add(array1);

            int cabecalhoPosition = arrayList.size()-1;

            for(Resultado resultado : tbResultado.getItems()){
                ArrayList<String> array2 = new ArrayList<>();
                Cliente cliente = resultado.getCliente();
                array2.add(String.valueOf(cliente.getId()));
                array2.add(cliente.getStatus());
                array2.add(cliente.getCnpj());
                array2.add(cliente.getNome());

                array2.add(resultado.getArquivoNome()
                        .keySet()
                        .stream()
                        .map(s->s.getFileName().toString()+"="+resultado.getArquivoNome().get(s).toString())
                        .collect(Collectors.joining("\n")));

                if(resultado.getArquivoNome().isEmpty())
                    array2.add(ckCodigo.isSelected()?(resultado.getStatus().equals(Resultado.Status.ERRO)?resultado.getMensageria().toString():Mensageria.ARQUIVONAOENCONTRADO.toString()):Mensageria.NOMEIGNORADO.toString());
                else{
                    array2.add(resultado.getArquivoNome()
                            .values()
                            .stream()
                            .map(Mensageria::toString)
                            .collect(Collectors.joining(";")));

                }
                array2.add(resultado.getArquivoConteudo()
                        .keySet()
                        .stream()
                        .map(s->s.getFileName().toString()+"="+resultado.getArquivoConteudo().get(s).toString())
                        .collect(Collectors.joining("\n")));

                if(resultado.getArquivoConteudo().isEmpty())
                    array2.add(ckCnpj.isSelected()?(resultado.getStatus().equals(Resultado.Status.ERRO)?resultado.getMensageria().toString():Mensageria.ARQUIVONAOENCONTRADO.toString())
                            :Mensageria.CNPJIGNORADO.toString());
                else{
                    array2.add(resultado.getArquivoConteudo()
                            .values()
                            .stream()
                            .map(Mensageria::toString)
                            .collect(Collectors.joining(";")));
                }
                arrayList.add(array2);

            }

            ArrayList a1 = new ArrayList();
            a1.add(Mensageria.VALIDADO.toString());
            a1.add(Mensageria.VALIDADO.getDescricao());

            ArrayList a2 = new ArrayList();
            a2.add(Mensageria.ERROLEITURA.toString());
            a2.add(Mensageria.ERROLEITURA.getDescricao());

            ArrayList a3 = new ArrayList();
            a3.add(Mensageria.LEITURAOCR.toString());
            a3.add(Mensageria.LEITURAOCR.getDescricao());

            ArrayList a4 = new ArrayList();
            a4.add(Mensageria.LEITURAMANUAL.toString());
            a4.add(Mensageria.LEITURAMANUAL.getDescricao());

            ArrayList a5 = new ArrayList();
            a5.add(Mensageria.ARQUIVOBRANCO.toString());
            a5.add(Mensageria.ARQUIVOBRANCO.getDescricao());

            ArrayList a6 = new ArrayList();
            a6.add(Mensageria.EMPRESAINVALIDA.toString());
            a6.add(Mensageria.EMPRESAINVALIDA.getDescricao());

            ArrayList a7 = new ArrayList();
            a7.add(Mensageria.ARQUIVONAOENCONTRADO.toString());
            a7.add(Mensageria.ARQUIVONAOENCONTRADO.getDescricao());

            arrayList.add(new ArrayList());

            ArrayList arrayLegenda = new ArrayList();
            arrayLegenda.add("LEGENDAS");
            arrayList.add(arrayLegenda);
            arrayList.add(a1);
            arrayList.add(a2);
            arrayList.add(a3);
            arrayList.add(a4);
            arrayList.add(a5);
            arrayList.add(a6);
            arrayList.add(a7);

            SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
            File file = new File(System.getProperty("java.io.tmpdir")+"/file-"+sdf.format(new Date())+".xls");
            ExcelGenerico generico = new ExcelGenerico(file.getAbsolutePath(),arrayList,larguraColunas,cabecalhoPosition);
            try {
                generico.gerarExcel();
                Desktop.getDesktop().open(file);
            } catch (WriteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        hboxPlanilha.setVisible(false);
        hboxLinhaPlanilha.setVisible(false);
        hboxCombos.setVisible(false);

        cbCodigo.setDisable(true);
        cbCnpj.setDisable(true);
        ckCodigo.setSelected(true);
        ckCnpj.setSelected(true);
        refreshCombos(getIndexadorDefault());

        txCnpjIgnorar.setPlainText("04110394000191");

        tbResultado.setFocusTraversable(true);

        tbRelatorio.setOnMouseClicked(event -> {
            Resumo resumo = tbRelatorio.getSelectionModel().getSelectedItem();
            if(resumo!=null && resumo.getTotal()>0){
                int index = tbResultado.getSelectionModel().getSelectedIndex();
                System.out.println("Linha selecionada:"+index);

                Resultado anterior = tbResultado.getSelectionModel().getSelectedItem();


                Mensageria m = resumo.getMensageria();
                List<Resultado> list = tbResultado.getItems().stream().filter(r->
                        r.getArquivoConteudo().containsValue(m) ||
                                r.getArquivoNome().containsValue(m) ||
                                r.getMensageria().equals(m)
                ).collect(Collectors.toList());

                tbResultado.getItems().forEach(c->{
                    if(c.getMensageria().equals(Mensageria.CNPJINVALIDO)) System.out.println(c.getCliente().toString()+"-"+c.getMensageria());
                });

                System.out.println("Tamanho lista:"+list.size());

                int ultimo = list.size()-1;
                int ant = list.contains(anterior)?list.indexOf(anterior):-1;

                for (Resultado r : list) {
                    if(tbResultado.getSelectionModel().getSelectedItem()==r) continue;
                    else if(ant==ultimo) {
                        tbResultado.getSelectionModel().select(list.get(0));
                        index = tbResultado.getItems().indexOf(list.get(0));
                        tbResultado.scrollTo(index);
                        tbResultado.requestFocus();
                        break;
                    }
                    else if(list.indexOf(r)>ant){
                        tbResultado.getSelectionModel().select(r);
                        index = tbResultado.getItems().indexOf(r);
                        tbResultado.scrollTo(index);
                        tbResultado.requestFocus();
                        break;
                    }
                }
            }
        });
    }

    public CheckListController(Stage primaryStage) {
    	this.stage=primaryStage;
	}

	@Override
    public void initialize(URL location, ResourceBundle resources) {
    	combos();
    	tabela();
        tabelaResultado();
        tabelaRelatorio();
    }
    private void processarExcel(){
        LeitorExcel excel = new LeitorExcel();
        indexadors = excel.readWorkbook(new File(txLocalizacaoDatabase.getText()));
        int linhas=indexadors.size();
        int colunas=indexadors.get(0).size()>0?indexadors.get(0).size():0;
        if(linhas<2 || colunas==0) {
            alert(Alert.AlertType.ERROR, "Planilha invalida", "Tente novamente", "A planilha deverá ter no minimo 2 linhas e 1 coluna");
            txLocalizacaoDatabase.setText("");
            refreshCombos(null);
        }
        else{
            Set<Integer> linhaset = new TreeSet<>();
            for(int i = 1; i < linhas-1; i++){
                linhaset.add(i);
            }
            cbLinhaExcel.getItems().setAll(linhas);
            cbLinhaExcel.setValue(1);
            refreshCombos(indexadors.get(cbLinhaExcel.getValue()-1));
        }
    }
    private void processar(){
        if(!ckCnpj.isSelected() && !ckCodigo.isSelected()){
            alert(Alert.AlertType.ERROR,"Erro","","Selecione a coluna para CNPJ e/ou CODIGO");
            return;
        }
        Path dir = Paths.get(txLocalizacaoPDF.getText());
        if(txLocalizacaoPDF.getText().equals("") || Files.notExists(dir)) {
            alert(Alert.AlertType.ERROR,"Erro","Caminho dos PDFs não informado","Informe um caminho para os arquivos PDF");
            return;
        }
        List<Cliente> clientes = new ArrayList<>();
        if(tgbAutomaticoManual.isSelected()){
            List<Cliente> novos = new ArrayList<>();
            if(novos==null ) {
                alert(Alert.AlertType.ERROR,"Erro","Erro ao consultar a base de dados","Nao foi possivel carregar os clientes,tente informando o arquivo manualmente");
                return;
            }
            else if(cbFiltroSpecial.getValue()!=null){
                List<Indexador> selecao = tbFiltro.getItems().stream().filter(c->c.isHabilitado()).collect(Collectors.toList());
                novos = new ClienteDAO().listarPor(cbFiltroSpecial.getValue(),selecao);
            }
            else{
                novos = new ClienteDAO().listar();
            }
            clientes = novos;
            txProgresso.setText("MONTANDO LISTA DE PESQUISA");
        }
        else{
            for(int i = cbLinhaExcel.getValue(); i<indexadors.size();i++){
                List<Indexador> indexador = indexadors.get(i);
                Cliente cliente = new Cliente();
                for(Indexador index : indexador){
                    if(cbNome.getValue()!=null && index.getIndex()==cbNome.getValue().getIndex()){
                        cliente.setNome(index.getValor());
                    }
                    if(cbStatus.getValue()!=null && index.getIndex()==cbStatus.getValue().getIndex()){
                        cliente.setStatus(index.getValor());
                    }
                    if(ckCodigo.isSelected() && cbCodigo.getValue()!=null && index.getIndex()==cbCodigo.getValue().getIndex()){
                        try {
                            int valor = Integer.parseInt(index.getValor());
                            cliente.setId(valor);
                        }catch(NumberFormatException e){
                            alert(Alert.AlertType.ERROR,"Campo ID invalido","Numero esperado não foi identificado",
                                    "\nColuna: "+index.getIndex()+"\t Linha: "+(i+1));
                            return;
                        }
                    }
                    if(ckCnpj.isSelected() && cbCnpj.getValue()!=null && index.getIndex()==cbCnpj.getValue().getIndex()){
                        cliente.setCnpj(index.getValor());
                    }

                }
                clientes.add(cliente);
            }
        }
        /*
        Path root = dir.getRoot();
        Path novoCaminho = dir;
        System.out.println(novoCaminho.toString());

        if(!root.equals(Paths.get("C:"))) {
            try{
                Path tmp = Paths.get(System.getProperty("java.io.tmpdir"),dir.getFileName().toString());
                Files.deleteIfExists(tmp);
                Files.copy(dir,tmp,StandardCopyOption.REPLACE_EXISTING);

                boolean ocorreErro = false;
                Files.walk(dir).forEach(file-> {
                    try {Files.copy(file,tmp.resolve(dir.relativize(file)),StandardCopyOption.REPLACE_EXISTING);}
                    catch (IOException e) {ocorreErro = true; e.printStackTrace();}
                });
                novoCaminho = tmp;
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        System.out.println(novoCaminho.toString());
*/
        tabelaResultado();

        tabPane.getSelectionModel().select(tab3);
        tab1.setDisable(true);
        tab2.setDisable(true);
        tab3.setDisable(false);

        List<Resultado> resultados = new ArrayList<>();
        clientes.forEach(c->{
            Resultado res = new Resultado(c);
            if(ckCnpj.isSelected() && !c.isCnpjValido()) {
                res.setMensageria(Mensageria.CNPJINVALIDO);
                res.setStatus(Resultado.Status.ERRO);
            }
            resultados.add(res);
        });

        tbResultado.getItems().clear();
        tbResultado.getItems().addAll(resultados);

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Set<Path> erroLeitura = new HashSet<>();
                Files.walk(dir).forEach(arquivo->{
                    String obrigatoria = txNomeArquivo.getText().toUpperCase();
                    boolean validado = (Files.isRegularFile(arquivo) && arquivo.getFileName().toString().toUpperCase().contains(obrigatoria));
                    if(validado){
                        Resultado sugestao = null;
                        if(ckCodigo.isSelected()){
                            String fileName = arquivo.getFileName().toString();
                            String codigo = "";
                            for(char c: fileName.toCharArray()){
                                try{
                                    Integer.parseInt(String.valueOf(c));
                                    codigo+=c;
                                }catch (NumberFormatException e){
                                    break;
                                }
                            }
                            if(codigo.length()>0) {
                                final String cod = codigo;
                                final int cod2 = Integer.parseInt(codigo);
                                Optional<Resultado> result = tbResultado.getItems().stream().filter(f->f.getCliente().getIdFormatado().equals(cod) || f.getCliente().getId()==cod2).findFirst();
                                if(result.isPresent()){
                                    setTxProgresso("VALIDAÇÃO DE NOME="+arquivo.getFileName().toString()+"-CLIENTE="+result.get().getCliente().toString());
                                    result.get().putArquivoNome(arquivo,VALIDADO);
                                    sugestao = result.get();
                                }
                            }else{
                                setTxProgresso("NENHUM CLIENTE IDENTIFICADO NO ARQUIVO "+arquivo.getFileName().toString());
                                //fazer alguma coisa
                                //alert(Alert.AlertType.ERROR,"Erro","Nao foi identificado um valor numerico para o codigo","Verifique a copluna codigo na sua planilha, deverá ser númerico apenas");
                            }
                        }
                        if(ckCnpj.isSelected()) {
                            LeitoraPDF pdf = new LeitoraPDF();
                            if (pdf.validarExtensao(arquivo)) {
                                PDDocument document = null;
                                try {
                                    document = pdf.abrirPFD(arquivo);
                                    pdf.buscarCNPJ(-1, document, tbResultado, txProgresso, sugestao,erroLeitura,txCnpjIgnorar.getText());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } finally {try {pdf.fechar(document);} catch (IOException e) {e.printStackTrace();}}
                            }
                        }
                        Platform.runLater(() -> tbResultado.refresh());
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Platform.runLater(() -> {
                    txProgresso.setText("CONCLUINDO...");
                    tbResultado.getItems().forEach(c->{
                        if(c.getStatus().equals(Resultado.Status.PENDENTE) &&
                                c.getMensageria().equals(Mensageria.DEFAULT) &&
                                c.getArquivoNome().isEmpty() &&
                                ckCodigo.isSelected()
                                ||
                           c.getStatus().equals(Resultado.Status.PENDENTE) &&
                                c.getMensageria().equals(Mensageria.DEFAULT) &&
                                c.getArquivoConteudo().isEmpty() &&
                                ckCnpj.isSelected()
                                ){
                            c.setMensageria(Mensageria.ARQUIVONAOENCONTRADO);
                            c.setStatus(Resultado.Status.ERRO);
                        }
                    });
                    tbRelatorio.getItems().clear();
                    tbRelatorio.getItems().addAll(recalcular());
                    alert(Alert.AlertType.INFORMATION,"Concluido","Processo concluido com sucesso!","");
                    txProgresso.setText("");
                });
                return null;
            }
        };
        new Thread(task).start();
    }

    private List<Resumo> recalcular(){

        List<Resumo> list = new ArrayList<>();

        //VALIDADO("VALIDADO"),ERROLEITURA("ERRO DE LEITURA"),LEITURAOCR("VALIDADO POR IA"),
        //        LEITURAMANUAL("VALIDADO MANUALMENTE"),ARQUIVOBRANCO("ARQUIVO EM BRANCO"),

        int erroLeitura=0,arquivoBranco=0, validado = 0, arquivoNaoEncontrado=0, leituraOcr=0;

        for (Resultado resultado : tbResultado.getItems()) {
            if(ckCnpj.isSelected() && ckCodigo.isSelected()) {
                validado += resultado.getArquivoNome().containsValue(Mensageria.VALIDADO) ||
                        resultado.getArquivoConteudo().containsValue(Mensageria.VALIDADO)?1:0;
                arquivoNaoEncontrado += resultado.getArquivoNome().size() == 0 || resultado.getArquivoConteudo().size() == 0 ? 1 : 0;
                arquivoBranco += resultado.getArquivoNome().containsValue(Mensageria.ARQUIVOBRANCO) ||
                        resultado.getArquivoConteudo().containsValue(Mensageria.ARQUIVOBRANCO)?1:0;

                erroLeitura += resultado.getArquivoNome().containsValue(Mensageria.ERROLEITURA) ||
                        resultado.getArquivoConteudo().containsValue(Mensageria.ERROLEITURA)?1:0;
                leituraOcr += resultado.getArquivoConteudo().containsValue(Mensageria.LEITURAOCR)?1:0;
            }
            else if(!ckCnpj.isSelected()){
                validado += resultado.getArquivoNome().containsValue(Mensageria.VALIDADO)?1:0;
                arquivoNaoEncontrado += resultado.getArquivoNome().size()==0?1:0;
                arquivoBranco += resultado.getArquivoNome().containsValue(Mensageria.ARQUIVOBRANCO)?1:0;
                erroLeitura += resultado.getArquivoNome().containsValue(Mensageria.ERROLEITURA)?1:0;
            }
            else{
                validado += resultado.getArquivoConteudo().containsValue(Mensageria.VALIDADO)?1:0;
                arquivoNaoEncontrado += resultado.getArquivoConteudo().size()==0?1:0;
                arquivoBranco += resultado.getArquivoConteudo().containsValue(Mensageria.ARQUIVOBRANCO)?1:0;
                erroLeitura += resultado.getArquivoConteudo().containsValue(Mensageria.ERROLEITURA)?1:0;
                leituraOcr += resultado.getArquivoConteudo().containsValue(Mensageria.LEITURAOCR)?1:0;
            }
        }
        if(ckCnpj.isSelected())
            list.add(new Resumo(Mensageria.CNPJINVALIDO,(int)tbResultado.getItems().stream().
                    filter(c->!c.getCliente().isCnpjValido()).count()));
        list.add(new Resumo(Mensageria.VALIDADO,validado));
        list.add(new Resumo(Mensageria.LEITURAOCR,leituraOcr));
        list.add(new Resumo(Mensageria.ERROLEITURA, erroLeitura));
        list.add(new Resumo(Mensageria.ARQUIVOBRANCO, arquivoBranco));
        list.add(new Resumo(Mensageria.ARQUIVONAOENCONTRADO, arquivoNaoEncontrado));
        return list;
    }

    private void refreshCombos(List<Indexador> indexadors){
        cbCodigo.getItems().clear();
        cbNome.getItems().clear();
        cbStatus.getItems().clear();
        cbFiltroSpecial.getItems().clear();
        cbCnpj.getItems().clear();
        tbFiltro.getItems().clear();

        if(cbObrigacao.getValue()!=null) {
            if(indexadors!=null) {
                cbCodigo.getItems().setAll(indexadors);
                cbNome.getItems().setAll(indexadors);
                cbStatus.getItems().setAll(indexadors);
                cbFiltroSpecial.getItems().setAll(indexadors);
                cbCnpj.getItems().setAll(indexadors);
            }
            if (tgbAutomaticoManual.isSelected()) {
                cbCodigo.getSelectionModel().select(0);
                cbNome.getSelectionModel().select(1);
                cbStatus.getSelectionModel().select(2);
                cbFiltroSpecial.getSelectionModel().select(2);
                cbCnpj.getSelectionModel().select(3);
                tbFiltro.getItems().setAll(clienteDAO.findBy(cbFiltroSpecial.getValue()));
                ckSelecionarFiltro.setSelected(true);
            }
            else{
                cbCodigo.getSelectionModel().select(0);
                cbNome.getSelectionModel().select(0);
                cbStatus.getSelectionModel().select(0);
                cbCnpj.getSelectionModel().select(0);
            }

        }
    }

    private void setTxProgresso(String text){
        Platform.runLater(() ->txProgresso.setText(text));
    }
    private void tabela(){
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

    private void tabelaRelatorio(){
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
    private void tabelaResultado(){
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
                            setText(ckCodigo.isSelected()?
                                    Mensageria.ARQUIVONAOENCONTRADO.toString():
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
        tbResultado.setFixedCellSize(50);
        tbResultado.getColumns().add(colunaCliente);
        if(ckCodigo.isSelected())
            tbResultado.getColumns().add(colunaId);
        if(ckCnpj.isSelected())
            tbResultado.getColumns().add(colunaConteudo);
    }
}
