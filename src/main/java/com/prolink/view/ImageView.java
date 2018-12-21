package com.prolink.view;

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

import com.prolink.controller.ControllerImage;
import com.prolink.model.CadastroBean;

import java.util.List;

import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import java.awt.Color;
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
	public static JScrollPane scrollPane;
	/**
	 * Launch the application.
	 */
	
	ControllerImage controller = new ControllerImage();
	public static void main(String[] args) {
		try {
			ImageView dialog = new ImageView(null,null,null,false,-1,System.getProperty("user.dir")+"/07-2018/pdf");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ImageView(JFrame frame, List<CadastroBean> lista, JTable table, boolean editarUm, int registro,String pastaDosArquivos) {
		super(frame,true);
		initComponents();
		controller.iniciar(this,lista,table,editarUm,registro,pastaDosArquivos);
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
		contentPanel.add(pnBody);
		
		lbIcon = new JLabel("");
		
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(lbIcon);
		scrollPane.setBounds(10,10,pnBody.getWidth()-20,pnBody.getHeight());
		pnBody.add(scrollPane);
		
		pnAuxiliar = new JPanel();
		pnAuxiliar.setBackground(Color.WHITE);
		pnAuxiliar.setBounds(10, 13, 317, 422);
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
			btnRejeitar.setBounds(117, 129, 80, 23);
			btnRejeitar.setActionCommand("Rejeitar");
			btnRejeitar.addActionListener(controller);
			pnAuxiliar.add(btnRejeitar);
		}
		
		JButton btnValidar = new JButton("Validar");
		btnValidar.setBounds(29, 129, 80, 23);
		pnAuxiliar.add(btnValidar);
		btnValidar.setActionCommand("Validar");
		btnValidar.addActionListener(controller);
		{
			cancelButton = new JButton("Sair");
			cancelButton.setBounds(207, 129, 80, 23);
			pnAuxiliar.add(cancelButton);
			cancelButton.setActionCommand("Sair");
			cancelButton.addActionListener(controller);
			
			JTextPane txtpnOChecklistConsegue = new JTextPane();
			txtpnOChecklistConsegue.setText("O Checklist consegue ler arquivos PDF gravados no formato TEXTO, um formato que permite leitura de strings(cacacteres), se o erro for informado, esse resultado é retornado nessa area para validação manual que trata PDF do tipo Imagem.\r\nO PDF que teve o seu nome validado mas o conteudo não foi lido, será mostrado no painel a direita, no momento válido apenas para a 1ª página do arquivo PDF, você deve comparar os campos que achar conveniente e Validar se concordar com o resultado mostrado, caso contrário Rejeitar. Sua ação é encaminhada imediatamente para a tabela de resultados no menu principal.");
			txtpnOChecklistConsegue.setEditable(false);
			txtpnOChecklistConsegue.setBounds(11, 163, 296, 246);
			pnAuxiliar.add(txtpnOChecklistConsegue);
			cancelButton.addActionListener(controller);
		}
		addComponentListener(controller);
		setLocationRelativeTo(null);
	}
}
