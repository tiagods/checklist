package com.prolink.utilitarios;

import java.util.Date;

public class DescricaoVersao {
	private String nome="CheckList de Obrigações";
	private String versao="1.2";
	private String data="23.01.2017";
	private String detalhes="*Alterado comportamento das caixas de seleção,\n"
			+ "*Gravação do ultimo caminho";
	/*
	 * Historico das versoes
	 * 1.0 - Versao Final Entregue
	 * 1.1 - Versao aplicado interface ItemListener em combobox 1,2,3,4 e registrado ultimo caminho escolhido
	 * 
	 */
	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}
	/**
	 * @return the versao
	 */
	public String getVersao() {
		return versao;
	}
	
	public String getDate(){
		return data;
	}
	public String getDetalhes(){
		return this.detalhes;
	}
}
