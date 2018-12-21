/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prolink.controller;

import static com.prolink.view.Menu.cbFiltro;
import static com.prolink.view.Menu.chckbxIgnorarArquivos;
import static com.prolink.view.Menu.comboCNPJ;
import static com.prolink.view.Menu.comboCodigo;
import static com.prolink.view.Menu.comboNome;
import static com.prolink.view.Menu.comboStatus;
import static com.prolink.view.Menu.comboStatus2;
import static com.prolink.view.Menu.jPanel1;
import static com.prolink.view.Menu.jPanel2;
import static com.prolink.view.Menu.jTable2;
import static com.prolink.view.Menu.jTable3;
import static com.prolink.view.Menu.lbDetalhes;
import static com.prolink.view.Menu.lbTitVersao;
import static com.prolink.view.Menu.lbTitulo;
import static com.prolink.view.Menu.progressBar;
import static com.prolink.view.Menu.tbPrincipal;
import static com.prolink.view.Menu.txBuscarNome;
import static com.prolink.view.Menu.txCaminhoArquivo;
import static com.prolink.view.Menu.txCaminhoOutros;
import static com.prolink.view.Menu.txCaminhoPDF;
import static com.prolink.view.Menu.txIconValido;
import static com.prolink.view.Menu.txIconValido1;
import static com.prolink.view.Menu.txIconValido2;
import static com.prolink.view.Menu.txStatus;
import static com.prolink.view.Menu.txView1;
import static com.prolink.view.Menu.txView2;
import static com.prolink.view.Menu.txView3;
import static com.prolink.view.Menu.txView4;
import static com.prolink.view.Menu.txView5;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.prolink.model.Arquivo;
import com.prolink.model.ArquivosBean;
import com.prolink.model.CadastroBean;
import com.prolink.model.LeitoraPdf;
import com.prolink.model.PlanilhaBean;
import com.prolink.model.PlanilhaDao;
import com.prolink.model.Relatorio;
import com.prolink.model.Tabela;
import com.prolink.utilitarios.DescricaoVersao;
import com.prolink.utilitarios.Help;
import com.prolink.view.ImageView;
import com.prolink.view.Menu;

/**
 *
 * @author Tiago
 */
public class ControllerMenu implements ActionListener, MouseListener, ItemListener{
    @SuppressWarnings("rawtypes")
	ArrayList cliente;//array pai, nele serão armazenados todos os clientes
    PlanilhaBean bean;
    Tabela tabela;//classe que trata e cuida das tabelas da Classe Menu
    List<String> listaStatus; //lista de todos os status
    String caminho;
    Set<File> morto = new HashSet<>();//lista usada para armazenar arquivos mortos
    Set<String> listaNova;/*lista simples para guardar status não repetidos 
    dos clientes e enviar para tabela*/
    Set<File> arquivosNaoLidos = new HashSet<>();
    List<String> filtroUser;
    String lastDir="";
    /*
    Colunas para cada tipo de registro / apenas para melhor visualização
    */
    boolean cancelar = false;
    File temp = new File(System.getProperty("user.dir")+"/temp");
    File fileSaida = new File(temp+"/Cadastro.xls");;//arquivo temporario no sistema
    int delimitador;
    boolean atualizar = false;
    
    DescricaoVersao descricao;
    
    Menu menu;
    boolean firstPageOnly = true;
    
    public void iniciar(Menu menu){
    	this.menu=menu;
    	descricao = new DescricaoVersao();
    	String titulo = descricao.getNome()+" "+descricao.getVersao();
    	lbTitulo.setText(titulo);
    	
    }
  //agendador para verificar se existe uma nova versao
  	public void Alertar(){
      	Alertas al = new Alertas();
      	Thread thread = new Thread(al);
      	thread.start();
      }
      public class Alertas implements Runnable{
      	@Override
      	public void run(){
      		try{
      			
      			lbDetalhes.setBackground(Color.RED);
      			lbDetalhes.setForeground(Color.WHITE);
      			lbTitVersao.setBackground(Color.WHITE);
      			lbTitVersao.setForeground(Color.RED);
      			Thread.sleep(2000);
      			lbTitVersao.setBackground(Color.RED);
      			lbTitVersao.setForeground(Color.WHITE);
      			lbDetalhes.setBackground(Color.WHITE);
      			lbDetalhes.setForeground(Color.RED);
      			Thread.sleep(2000);
      			Alertar();
              }catch(InterruptedException e){
              	e.printStackTrace();
              }
      	}
      }
      
    
    public void atualizarAgora(){
    	try{
    		Runtime.getRuntime().exec("java -jar update.jar plk*link815");
    		System.exit(0);
		}catch(Exception e){
			e.printStackTrace();
		}
    }
    public void travarCampos(JPanel panel){//metodo para travar campos para não edição
        for(int i =0; i<panel.getComponentCount();i++){
            if(panel.getComponent(i) instanceof JTextField)
                ((JTextField)panel.getComponent(i)).setEnabled(false);
        }
    }
    @SuppressWarnings("unchecked")
	public void carregarCombo(JComboBox box, int combo){
        String[] letra = {"","A","B","C","D","E","F","G","H","I","J","K","L","M",
            "N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        
        for (String l : letra){
            if(l.equals(""))
                box.addItem("");
            else
                box.addItem(l);
        }
        switch(combo){
            case 1:
            box.setSelectedItem("A");
            break;
            case 2:
            if(bean.retorna("K").isEmpty())
            	box.setSelectedItem("A");
            else
            	box.setSelectedItem("K");
            break;
            case 3:
            	if(bean.retorna("R").isEmpty())
                	box.setSelectedItem("A");
                else
                	box.setSelectedItem("R");
            break;
            case 4:
            case 5:
            	if(bean.retorna("B").isEmpty())
                	box.setSelectedItem("A");
                else
                	box.setSelectedItem("B");
            break;
            default:
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            case "Iniciar"://iniciar tratamento dos campos e liberar procura de dados dos clientes nos diretorios
                caminho = txCaminhoArquivo.getText();
                if(!caminho.equals("") && new File(caminho).isFile()){
                    if(!txCaminhoPDF.getText().equals("") || !txCaminhoOutros.getText().equals("")){
                        //if(validarNumero(txDelimitador.getText())){
                            if(txBuscarNome.getText().equals("")){
                                String comentario = "Se você não informar uma expressão resumida\n "
                                        + "o numero de resultados podem ultrapassar a quantidade de registros além do esperado para cada busca\n"
                                        + "O processo pode demora mais para terminar!\nMesmo assim deseja continuar?";
                                int valor = JOptionPane.showConfirmDialog(null,comentario,"Filtro não informado!",JOptionPane.YES_NO_OPTION);
                                if(valor == JOptionPane.YES_OPTION)
                                    Time();
                                else
                                    txBuscarNome.setFocusable(true);
                            }
                            else
                                Time();
                        //}
                    }
                    else
                        JOptionPane.showMessageDialog(null, "Informe um diretorio valido dos arquivos PDF ou para outros arquivos!");
                        
                }
                else
                    JOptionPane.showMessageDialog(null, "Primeiro informe um arquivo valido para importar!");
                break;
            
            case "CarregarArquivo":
//            	limparComponents();
            	if(tabela==null)
            		tabela = new Tabela();
            	tabela.limparTabela(tbPrincipal);
            	tabela.limparTabela(jTable2);
            	tabela.limparTabela(jTable3);
            	caminho = carregarArquivo(true, false, "Abrir Arquivo...");
                File arquivo  = new File(caminho);
                if(arquivo.getName().equals("Cadastro.xls")){
                    FileOutputStream system = null;
                    FileInputStream stream = null;
                    FileChannel entrada;
                    FileChannel saida;
                    try{
                        stream = new FileInputStream(caminho);
                        system  = new FileOutputStream(fileSaida);
                        entrada = stream.getChannel();
                        saida = system.getChannel();
                        entrada.transferTo(0, entrada.size(), saida);
                        caminho = fileSaida.getAbsolutePath();
                    }catch (IOException ex) {
                    }finally{
                        try {
                            system.close();
                            stream.close();
                        } catch (IOException ex) {}
                    }
                }
                if(!caminho.equals("")){
                    txCaminhoArquivo.setText(caminho);
                    Arquivo validador = new Arquivo();
                    if(validador.validarArquivo(new File(caminho))){//validar a extensao do arquivo
                        PlanilhaDao planilha = new PlanilhaDao();
                        bean = new PlanilhaBean();
                        if(planilha.lerPlanilha(bean, txStatus, new File(caminho))){
                            carregarCombo(comboCodigo, 1);
	                        carregarCombo(comboNome, 2);
	                        carregarCombo(comboCNPJ, 3);
	                        carregarCombo(comboStatus, 4);
	                        carregarCombo(comboStatus2, 5);
                            cbFiltro.setSelected(true);
                            refresh();
                            
                            if(!txView5.getText().equals("")){
                               preencherTabela();
                            }
                                                    
                        }
                        else
                            JOptionPane.showMessageDialog(null, "Não foi possivel ler o arquivo de origem,\n"
                                    + "Verifique se o arquivo é valido!\nArquivo protegidos por senhas não podem ser abertos!");
                    }
                    if(fileSaida.exists())
                        fileSaida.delete();//deletar arquivo temporario
                }
                comboCodigo.addItemListener(this);
                comboNome.addItemListener(this);
                comboCNPJ.addItemListener(this);
                comboStatus.addItemListener(this);
                
                mostrarIcones(txCaminhoArquivo,txIconValido);
                mostrarIcones(txCaminhoPDF, txIconValido1);
                mostrarIcones(txCaminhoOutros,txIconValido2);
                
                break;
            case "CarregarDir":
                String diretorio = carregarArquivo(false, false, "Escolher caminho...");
                if(!diretorio.equals(""))
                    txCaminhoPDF.setText(diretorio);
                mostrarIcones(txCaminhoArquivo,txIconValido);
                mostrarIcones(txCaminhoPDF, txIconValido1);
                mostrarIcones(txCaminhoOutros,txIconValido2);
                
                break;
            case "CarregarOutros":
                String diretorio2 = carregarArquivo(false, false, "Escolher caminho...");
                if(!diretorio2.equals(""))
                    txCaminhoOutros.setText(diretorio2);
                mostrarIcones(txCaminhoArquivo,txIconValido);
                mostrarIcones(txCaminhoPDF, txIconValido1);
                mostrarIcones(txCaminhoOutros,txIconValido2);
                break;
            case "Filtro":
                if(cbFiltro.isSelected()){
                    comboStatus2.setEnabled(true);
                    if(!txView5.getText().equals("")){
                        preencherTabela();
                    }
                }
                break;
            case "PreencherTabelas":
                if(!txView5.getText().equals("")){
                    preencherTabela();
                }
                break;
            case "AddUm":
                if(tabela==null)
                	tabela=new Tabela();
                tabela.addUmOrRemoveUm(jTable2, jTable3, "adicionar", "tabela 1");
                break;
            case "AddTodos":
            	if(tabela==null)
            		tabela=new Tabela();
                tabela.addTudoOrRemoveTudo(jTable2, jTable3);
                break;
            case "RemoveUm":
            	if(tabela==null)
                	tabela=new Tabela();
                tabela.addUmOrRemoveUm(jTable3, jTable2, "remover", "tabela 2");
                break;
            case "RemoveTodos":
            	if(tabela==null)
                	tabela=new Tabela();
                tabela.addTudoOrRemoveTudo(jTable3, jTable2);
                break;
            case "abrirSobre":
            	Help help = new Help();
                JOptionPane.showMessageDialog(null, help.getSobre());
                break;
            case "abrirNomeArquivo":
                help = new Help();
                JOptionPane.showMessageDialog(null, help.getCuringa());
                break;
            case "Exportar":
                String export = carregarArquivo(false, true, "Salvar arquivo");
                if(!export.equals("") && cliente!=null){
                   PlanilhaDao planilha = new PlanilhaDao();
                   List<CadastroBean> lista = tabela.pegarDadosTabela(tbPrincipal);
                   planilha.exportToExcel(lista, new File(export+".xls"));
                }
                else
                    JOptionPane.showMessageDialog(null, "Não existe dados para gerar relatorio!");
                
                break;
            case "Relatorio":
            	if(tabela==null)
                	tabela = new Tabela();
                List<CadastroBean> lista;
                lista = tabela.pegarDadosTabela(tbPrincipal);
                if(!lista.isEmpty()){
                    Relatorio relatorio = new Relatorio();
                    relatorio.imprimir(lista);
                }
                else
                    JOptionPane.showMessageDialog(null, "Não existe dados para gerar relatorio!");
                break;
            case "Cancelar":
                String msg = "Deseja cancelar a tarefa atual, os registros só serão mostrados até a fase atual?";
                int valor = JOptionPane.showConfirmDialog(null, msg, "Cancelar Tarefa", JOptionPane.YES_NO_OPTION);
                if(valor==JOptionPane.YES_OPTION){
                    cancelar = true;
                }
                break;
            case "Validar":
            	lista = tabela.pegarDadosTabela(tbPrincipal);
                if(!lista.isEmpty()){
                	ImageView dialog = new ImageView(menu,lista,tbPrincipal,false,-1,txCaminhoPDF.getText());
                	dialog.setVisible(true);
                }
            	break;
            	default:
            		break;
        }
    }
    private void preencherTabela(){
        //preencher tabela de status
        listaStatus = new ArrayList<>();
        listaStatus = bean.retorna((String)comboStatus2.getSelectedItem());//pega o calor do combo e retorno a array que ela pertence
        listaNova = new TreeSet<>();
        for(int i=0; i<listaStatus.size(); i++){
            if(i!=0){
                listaNova.add(listaStatus.get(i).toUpperCase());
            }
        }
        @SuppressWarnings("rawtypes")
		Iterator iterator = listaNova.iterator();
        Tabela tabelas = new Tabela();
        tabelas.limparTabela(jTable3);
        tabelas.carregaTodos(jTable2, iterator);
        
    }
    
    public void refresh(){
    	try{
    		txView1.setText(bean.retorna((String)comboCodigo.getSelectedItem()).get(0)==null ? "" : bean.retorna((String)comboCodigo.getSelectedItem()).get(0) );
    	}catch (IndexOutOfBoundsException e) {
    		e.printStackTrace();
    	}
    	try{
    		txView2.setText(bean.retorna((String)comboNome.getSelectedItem()).get(0)==null ? "": bean.retorna((String)comboNome.getSelectedItem()).get(0));
    	}catch (IndexOutOfBoundsException e) {
    		
    	}
    	try{
    		txView3.setText(bean.retorna((String)comboCNPJ.getSelectedItem()).get(0)==null ? "" : bean.retorna((String)comboCNPJ.getSelectedItem()).get(0));
    	}catch (IndexOutOfBoundsException e) {
    	}
    	try{
    		txView4.setText(bean.retorna((String)comboStatus.getSelectedItem()).get(0)==null ? "" : bean.retorna((String)comboStatus.getSelectedItem()).get(0));
    		txView5.setText(bean.retorna((String)comboStatus2.getSelectedItem()).get(0)==null ? "" : bean.retorna((String)comboStatus2.getSelectedItem()).get(0));
    	}catch (IndexOutOfBoundsException e) {
    	}
    }
    private void Time(){
        Iniciar iniciar = new Iniciar();
        Thread time = new Thread(iniciar);
        time.start();
    }
    public class Iniciar implements Runnable{
        @SuppressWarnings({ "unchecked", "rawtypes", "unused" })
		@Override
        public void run(){
        	cliente = new ArrayList();
        	Arquivo arquivo = new Arquivo();

        	List<File> diretorio1 = arquivo.pegarArquivos(txCaminhoPDF.getText(), true, "pdf");
        	List<File> diretorioOpcional=null;

        	if(!txCaminhoOutros.getText().equals(""))
        		diretorioOpcional = arquivo.pegarArquivos(txCaminhoOutros.getText(), false, null);

        	Tabela tabela = new Tabela();
        	tabela.limparTabela(tbPrincipal);

        	TreeSet<String> mapa;//pega fitro do status

        	if(tabela.pegarNumeroDeLinhas(jTable3)>0)
        		mapa = tabela.pegarValores(jTable3);
        	else
        		mapa = tabela.pegarValores(jTable2);

        	int totalRegistros = 0;
        	for(String status : bean.retorna((String)comboStatus.getSelectedItem())){
        		if(mapa.contains(status.toUpperCase()))
        			totalRegistros++;
        	}

        	filtroUser = new ArrayList<>();
        	if(!txBuscarNome.getText().equals("")){
        		String[] lista = txBuscarNome.getText().split(",");
        		for(String s : lista){
        			if(!s.trim().equals("")){
        				filtroUser.add(Normalizer.normalize(s.trim().toUpperCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", ""));
        			}
        		}

        	}
        	progressBar.setVisible(true);
        	
        	int v = 0; //linha=v, coluna=i
            for(int h = 1; h<bean.retorna((String)comboCodigo.getSelectedItem()).size(); h++){
            	if(cancelar){ 
            		txStatus.setText("Tarefa cancelada pelo usuario!");
            		break;
            	}
            	String status=bean.retorna((String)comboStatus.getSelectedItem()).get(h);
            	if(mapa.contains(status.toUpperCase())){
            		ArquivosBean ab = new ArquivosBean();
            		cliente.add(new ArrayList());
            		String codigo=bean.retorna((String)comboCodigo.getSelectedItem()).get(h);
            		String nome=bean.retorna((String)comboNome.getSelectedItem()).get(h);
            		String cnpj=bean.retorna((String)comboCNPJ.getSelectedItem()).get(h);
            		String[] name = nome.split(" ");
            		
            		((ArrayList)cliente.get(v)).add(codigo);
            		((ArrayList)cliente.get(v)).add(status);
            		((ArrayList)cliente.get(v)).add(nome);
            		((ArrayList)cliente.get(v)).add(cnpj);
            		
            		if(!txCaminhoPDF.getText().equals("")){
            			((ArrayList)cliente.get(v)).add(pegaNoNome(diretorio1, codigo, false, ab));//buscar codigo no nome

            			if(!cnpj.trim().equals("99.999.999/9999-99") && !cnpj.trim().equals("") && 
            					cnpj.trim().replace(".", "").replace("/", "").replace("-", "").length()>12)//validando cnpj
            			{	
            				try{
            					Long.parseLong(cnpj.replace(".", "").replace("/", "").replace("-", "").trim());
            					((ArrayList)cliente.get(v)).add(buscarNoConteudo(diretorio1, cnpj, true, ab,firstPageOnly));//buscar cnpjnoconteudo
            				}catch(NumberFormatException e ){
            					e.printStackTrace();
            					((ArrayList)cliente.get(v)).add("CNPJ Inválido");//buscar cnpjnoconteudo
            				}
            			}
            			else
            				((ArrayList)cliente.get(v)).add("CNPJ Inválido");//buscar cnpjnoconteudo

            		}
            		else{
            			((ArrayList)cliente.get(v)).add("");//buscar codigo no nome
            			((ArrayList)cliente.get(v)).add("");//buscar cnpjnoconteudo
            		}

            		if(!txCaminhoOutros.getText().equals("")){//se o caminho outros foi informado ele vai adicionar valor
            			((ArrayList)cliente.get(v)).add(pegaNoNome(diretorioOpcional, 
            					cnpj.replace("/", "").replace("-", "").replace(".", ""), true, ab));//pegar cnpj do nome do arquivo
            		}
            		else{
            			((ArrayList)cliente.get(v)).add("");
            		}
            		v++;
            		txStatus.setText("Processando cliente "+codigo+" ("+name[0]+") = "+v+" de "+totalRegistros+" registros!");
                	int percent = ((v*100)/(totalRegistros));
            		progressBar.setValue(percent);
            	} 
            }
        	try{
        		txStatus.setText("Concluido!!!");
        		progressBar.setValue(100);
            	Thread.sleep(3000);
        		txStatus.setText("");
        		progressBar.setValue(0);
            	progressBar.setVisible(false);
        	}catch(InterruptedException e){
        		e.printStackTrace();
        		progressBar.setValue(0);
        		progressBar.setVisible(false);
        	}  
        	tabela.preencherTabela(menu,tbPrincipal, cliente);
        	if(arquivosNaoLidos.size()>0){
        		if(arquivo==null)
        			arquivo = new Arquivo();
        		String localPathName="resumo";
        		String txtName = "CheckList_de_Obrigacoes.txt";
        		try{
        			arquivo.gravarTxtInconsistencias(arquivosNaoLidos, localPathName+"/"+txtName);
        			arquivo.copiarArquivosInconsistentes(arquivosNaoLidos, new File(localPathName));
        		}catch(IOException e){
        			JOptionPane.showMessageDialog(null, "Erro ao copiar os arquivos \n"+e.getMessage());
        		}
        		JOptionPane.showMessageDialog(null, "Foi gerado uma lista de arquivos não lidos, salvo dentro da pasta "+localPathName);

        	}
        }
    }
    public String pegaNoNome(List<File> arquivos, String valorProcurado, boolean cnpj, ArquivosBean ab){
        String encontrado = "Não Existe";
        
        for(int i = 0; i< arquivos.size(); i++){
            if(arquivos.get(i).isFile()){
                if(cnpj==true){
                    if(arquivos.get(i).getName().contains(valorProcurado)){
                        if(encontrado.equals("Não Existe"))
                            encontrado = arquivos.get(i).getName();
                        else
                            encontrado+=","+arquivos.get(i).getName();
                        ab.setArquivos(arquivos.get(i));
                    }
                    
                }
                else{//buscara nome
                    String arq = Normalizer.normalize(arquivos.get(i).getName().toUpperCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                    String arqSub = (arq.trim()).substring(0, 4);//pegar os 4 primeiros digitos do
                    
                    boolean filtrar = true;//por default é true
                    boolean ignorarArquivos= false;
                    //se o botao foi selecionado, os valores do conteudo serao ignorados
                    if(chckbxIgnorarArquivos.isSelected()){
                    	ignorarArquivos=true;
                    }
                    //estou percorrendo o filtro digitado pelo usuario, se o arquivo contiver o nome digitado saira do loop
                    for(int j=0; j< filtroUser.size(); j++){
                    	if(ignorarArquivos==false){
                    		/*se o arquivo contiver o nome do filtro.get(j) retorna true e o break
                    		*permite que saia imediatamente do loop e executar proxima ação, se id do cliente bate com o nome do conteudo
                    		*/
	                    	if(arq.contains(filtroUser.get(j))){
	                    		filtrar = true;
	                    		break;
	                    	}
	                    	else
	                    		filtrar=false;//caso nao encontre continuara procurando na lista
                    	}
                    	else{
                    		/*diferente do primeiro, se o arquivo contiver um valor especificado pelo filtro digitado
                    		*ele desabilita a proxima ação e sai do loop
                    		*/
	                    	if(arq.contains(filtroUser.get(j))){
		                    	filtrar = false;
		                    	break;
	                    	}
	                    	else
	                    		filtrar=true;
                    	}
                    }
                    if(arqSub.equals(valorProcurado) && filtrar == true){//verifica se no nome do arquivo existe o valor procurado + dentro do filtro informado
	                	if(encontrado.equals("Não Existe"))
	                		encontrado = arquivos.get(i).getName();
	                	else
	                		encontrado+=","+arquivos.get(i).getName();
	                	ab.setArquivos(arquivos.get(i));
	                }
                    
                }
            }
        }
        
        
        return encontrado;
    }
    public String buscarNoConteudo(List<File> lista, String valorProcurado, boolean listar, ArquivosBean ab, boolean firstPage){
        String encontrado = "Não Existe";
        String valor =".";
        if(!txBuscarNome.getText().equals("")){
            valor = Normalizer.normalize(txBuscarNome.getText().toUpperCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        }
        Set<File> list = ab.getArquivos();
        for(File f : list){
        	if(arquivosNaoLidos.contains(f)) continue;//pular se houve tentativa de leitura desse arquivo
        	if(f.isFile()){
                String arq = Normalizer.normalize(f.getName().toUpperCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                
                boolean filtrar = true;//por default é true
                boolean ignorarArquivos= false;
                //se o botao foi selecionado, os valores do conteudo serao ignorados
                if(chckbxIgnorarArquivos.isSelected()){
                	ignorarArquivos=true;
                }
                //estou percorrendo o filtro digitado pelo usuario, se o arquivo contiver o nome digitado saira do loop
                for(int j=0; j< filtroUser.size(); j++){
                	if(!ignorarArquivos){
                		/*se o arquivo contiver o nome do filtro.get(j) retorna true e o break
                		*permite que saia imediatamente do loop e executar proxima ação, se id do cliente bate com o nome do conteudo
                		*/
                    	if(arq.contains(filtroUser.get(j))){
                    		filtrar = true;
                    		break;
                    	}
                    	else
                    		filtrar=false;//caso nao encontre continuara procurando na lista
                	}
                	else{
                		/*diferente do primeiro, se o arquivo contiver um valor especificado pelo filtro digitado
                		*ele desabilita a proxima ação e sai do loop
                		*/
                    	if(arq.contains(filtroUser.get(j))){
	                    	filtrar = false;
	                    	break;
                    	}
                    	else
                    		filtrar=true;
                	}
                }
                
                if(!morto.contains(f) && filtrar == true){
                    LeitoraPdf leitoraPDF = new LeitoraPdf();
                    if(leitoraPDF.verificarTexto(f, valorProcurado,true)){
                        if(encontrado.equals("Não Existe"))
                            encontrado = f.getName();
                        else
                            encontrado+=","+f.getName();
                        if(leitoraPDF.totalPages()==1)
                        	estaMorto(f);
                    }//inserir arquivos não lidos em uma arraylist
                    if(!leitoraPDF.conseguiLer()){
                    	arquivosNaoLidos.add(f);
                    }
                }
            }
        }
        
        if(listar==true && encontrado.equals("Não Existe")){// if(arqEncontrados.isEmpty() || encontrado.equals("Não Existe")){
            for(File f : lista){
                if(f.isFile()){
                    String arq = Normalizer.normalize(f.getName().toUpperCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                    if(!morto.contains(f) && arq.contains(valor)){
                        LeitoraPdf leitoraPDF = new LeitoraPdf();
                        if(leitoraPDF.verificarTexto(f, valorProcurado,true)){
                            if(encontrado.equals("Não Existe"))
                                encontrado = f.getName();
                            else
                                encontrado+=","+f.getName();
                            if(leitoraPDF.totalPages()==1)
                            	estaMorto(f);
                        }
                    }
                }
            }
        }
        return encontrado;
    }
    public void estaMorto(File morto){
        this.morto.add(morto);
    }
        
    @Override
    public void mouseClicked(MouseEvent e) {
        if(atualizar)
        	atualizarAgora();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    
    private String carregarArquivo(boolean mostrarArquivos, boolean salvar, String title){
        JFileChooser chooser = new JFileChooser();
        chooser.setAcceptAllFileFilterUsed(mostrarArquivos);
        chooser.setDialogTitle(title);
        if(!lastDir.equals(""))
    		chooser.setSelectedFile(new File(lastDir));
    	String local = "";
        if(mostrarArquivos){
        	chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.addChoosableFileFilter(new FileNameExtensionFilter("Planilha do Excel", ".xls,.xlsx"));
            int retorno = chooser.showOpenDialog(null);
            if(retorno==JFileChooser.OPEN_DIALOG){
                local = chooser.getSelectedFile().getPath();
                lastDir = chooser.getSelectedFile().getParent();
            }
        }
        else if(salvar==false){
        	chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int retorno = chooser.showOpenDialog(null);
            if(retorno==JFileChooser.APPROVE_OPTION){
                local = chooser.getSelectedFile().getAbsolutePath();//
                lastDir = local;
            }
        }
        else if(salvar){
        	chooser.addChoosableFileFilter(new FileNameExtensionFilter("Planilha do Excel (*.xls)", ".xls"));
            int retorno = chooser.showSaveDialog(null);
            if(retorno==JFileChooser.APPROVE_OPTION){
                local = chooser.getSelectedFile().getAbsolutePath(); //
                lastDir = chooser.getSelectedFile().getParent();
            }
        }
        return local;
    }
    public void mostrarIcones(JTextField text, JLabel label){
        if(text.getText().equals("")){
            label.setIcon(new ImageIcon(getClass().getResource("/images/iconX.png")));
        }
        else
            label.setIcon(new ImageIcon(getClass().getResource("/images/iconV.png")));
    }
	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange()==ItemEvent.DESELECTED){
			JComboBox<String> combo = (JComboBox<String>)e.getSource();
			if(!combo.getSelectedItem().equals(""))
				refresh();
			if(!txView5.getText().equals("")){
				preencherTabela();
			}
		}
		
	}
}
