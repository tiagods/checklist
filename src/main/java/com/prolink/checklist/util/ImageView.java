package com.prolink.checklist.util;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;

import com.prolink.checklist.controller.ControllerImage;
import com.prolink.checklist.enuns.Mensageria;
import com.prolink.checklist.model.Cliente;

import java.util.Map;

import javax.swing.SwingConstants;
import java.nio.file.Path;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import java.awt.Color;
import java.awt.Font;

public class ImageView extends JDialog {
	public static JPanel contentPanel = new JPanel();
	public static JTextField txtValue1;
	private JButton cancelButton;
	public static JTextField txValue2;
	private JButton btnPrimeiro;
	private JButton btnAnterior;
	private JButton btnProximo;
	private JButton btnUltimo;
	public static JPanel pnBody, pnAuxiliar;
	private JLabel lbStatus;
	private JButton btnRejeitar;
	public static JLabel txNome;
	public static JLabel txCnpj;
	public static JLabel txStatus;
	public static JLabel lbIcon;
	public static JTextArea txResultado;
	public static JScrollPane scrollPane;
	
	ControllerImage controller = new ControllerImage();
	/*
	public static void main(String[] args) {
		try {
			ImageView dialog = new ImageView(null,null,null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
	public ImageView(Cliente cliente,Map<Path,Mensageria> arquivos) {
		//super(frame,true);
		initComponents();
		controller.iniciar(this,cliente,arquivos);
	}
			
	public void	initComponents(){
		setTitle("Validador Manual");
		setBounds(100, 100, 1080, 720);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
		pnBody = new JPanel();
		pnBody.setBackground(Color.WHITE);
		pnBody.setBounds(345, 13, 700, 637);
		pnBody.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		}
		contentPanel.setLayout(null);
		
		pnAuxiliar = new JPanel();
		pnAuxiliar.setBackground(Color.WHITE);
		pnAuxiliar.setBounds(10, 13, 317, 637);
		contentPanel.add(pnAuxiliar);
		pnAuxiliar.setLayout(null);
		{
			btnPrimeiro = new JButton("<<");
			btnPrimeiro.setActionCommand("Primeiro");
			btnPrimeiro.addActionListener(controller);
			btnPrimeiro.setBounds(11, 11, 49, 23);
			pnAuxiliar.add(btnPrimeiro);
		}
		{
			btnAnterior = new JButton("<");
			btnAnterior.setActionCommand("Anterior");
			btnAnterior.addActionListener(controller);
			btnAnterior.setBounds(64, 11, 41, 23);
			pnAuxiliar.add(btnAnterior);
		}
		{
			txtValue1 = new JTextField();
			txtValue1.setEditable(false);
			txtValue1.setBounds(109, 12, 30, 20);
			pnAuxiliar.add(txtValue1);
			txtValue1.setColumns(3);
		}
		
		JLabel lblDe = new JLabel("de");
		lblDe.setBounds(149, 14, 21, 14);
		pnAuxiliar.add(lblDe);
		lblDe.setHorizontalAlignment(SwingConstants.CENTER);
		
		txValue2 = new JTextField();
		txValue2.setBounds(180, 12, 30, 20);
		txValue2.setEditable(false);
		pnAuxiliar.add(txValue2);
		txValue2.setColumns(3);
		{
			btnProximo = new JButton(">");
			btnProximo.setActionCommand("Proximo");
			btnProximo.addActionListener(controller);
			btnProximo.setBounds(214, 11, 41, 23);
			pnAuxiliar.add(btnProximo);
		}
		{
			btnUltimo = new JButton(">>");
			btnUltimo.setActionCommand("Ultimo");
			btnUltimo.addActionListener(controller);
			btnUltimo.setBounds(259, 11, 49, 23);
			pnAuxiliar.add(btnUltimo);
		}
		
		JLabel lbNome = new JLabel("Nome:");
		lbNome.setBounds(11, 45, 42, 14);
		pnAuxiliar.add(lbNome);
		{
			txNome = new JLabel();
			txNome.setBackground(Color.WHITE);
			txNome.setOpaque(true);
			txNome.setBounds(57, 39, 251, 20);
			pnAuxiliar.add(txNome);
		}
		
		JLabel lbCnpj = new JLabel("Cnpj:");
		lbCnpj.setBounds(11, 73, 42, 14);
		pnAuxiliar.add(lbCnpj);
		{
			txCnpj = new JLabel();
			txCnpj.setBackground(Color.WHITE);
			txCnpj.setOpaque(true);
			txCnpj.setBounds(57, 70, 251, 20);
			pnAuxiliar.add(txCnpj);
		}
		{
			lbStatus = new JLabel("Status:");
			lbStatus.setBounds(11, 101, 49, 14);
			pnAuxiliar.add(lbStatus);
		}
		{
			txStatus = new JLabel();
			txStatus.setBackground(Color.WHITE);
			txStatus.setOpaque(true);
			txStatus.setBounds(57, 98, 251, 20);
			pnAuxiliar.add(txStatus);
		}
		{
			btnRejeitar = new JButton("Rejeitar");
			btnRejeitar.setBounds(116, 242, 80, 23);
			btnRejeitar.setActionCommand("Rejeitar");
			btnRejeitar.addActionListener(controller);
			
			txResultado = new JTextArea();
			txResultado.setFont(new Font("Tahoma", Font.BOLD, 11));
			txResultado.setLineWrap(true);
			txResultado.setWrapStyleWord(true);
			txResultado.setEditable(false);
			txResultado.setBackground(Color.WHITE);
			txResultado.setBounds(83, 126, 225, 105);
			pnAuxiliar.add(txResultado);
			pnAuxiliar.add(btnRejeitar);
		}
		
		JButton btnValidar = new JButton("Validar");
		btnValidar.setBounds(28, 242, 80, 23);
		pnAuxiliar.add(btnValidar);
		btnValidar.setActionCommand("Validar");
		btnValidar.addActionListener(controller);
		{
			cancelButton = new JButton("Sair");
			cancelButton.setBounds(206, 242, 80, 23);
			pnAuxiliar.add(cancelButton);
			cancelButton.setActionCommand("Sair");
			cancelButton.addActionListener(controller);
			
			JTextPane txtpnOChecklistConsegue = new JTextPane();
			txtpnOChecklistConsegue.setText(
					"O Checklist consegue ler arquivos PDF gravados no formato TEXTO, um formato que permite leitura de caracteres (chamados de STRINGS), em 1\u00BA caso pode ser impossivel a leitura de texto, ai entra o recurso de OCR (Inteligencia Artificial) que s\u00E3o imagens, essa tecnologia permite extrair textos de imagens, esse resultado \u00E9 retornado nessa area para valida\u00E7\u00E3o manual que trata PDF do tipo Imagem.\r\nO PDF que teve o seu nome validado mas o conteudo n\u00E3o foi lido, ser\u00E1 mostrado no painel a direita, no momento v\u00E1lido apenas para a 1\u00AA p\u00E1gina do arquivo PDF, Todos os resultados, VALIDOS ou INVALIDOS ser\u00E3o retornados para sua ultima an\u00E1lise se achar necess\u00E1rio, voc\u00EA deve comparar os campos que achar conveniente e Validar se concordar com o resultado mostrado, caso contr\u00E1rio Rejeitar. Sua a\u00E7\u00E3o ser\u00E1 encaminhada imediatamente para a tabela de resultados no menu principal."
					);
			txtpnOChecklistConsegue.setEditable(false);
			txtpnOChecklistConsegue.setBounds(11, 276, 296, 240);
			pnAuxiliar.add(txtpnOChecklistConsegue);
			
			JLabel lblResultado = new JLabel("Resultado:");
			lblResultado.setBounds(11, 129, 62, 14);
			pnAuxiliar.add(lblResultado);
			cancelButton.addActionListener(controller);
		}
		contentPanel.add(pnBody);
		
		lbIcon = new JLabel("");
		
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(lbIcon);
		scrollPane.setBounds(10,10,pnBody.getWidth()-20,pnBody.getHeight());
		pnBody.add(scrollPane);
		addComponentListener(controller);
		setLocationRelativeTo(null);
	}
}
