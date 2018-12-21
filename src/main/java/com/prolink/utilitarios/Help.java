/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prolink.utilitarios;

/**
 *
 * @author Tiago
 */
public class Help {
    private final String sobre="Essa é uma ferramenta de codigo aberto, desenvolvido por www.github.com/tiagods \n"
            + "proibido uso com fins lucrativos.\n"
            + "Livre a reprodução desde que não viole os direitos autorais de seu autor.\n"
            + "1-A ferramenta carrega um arquivo xls informado\n"
            + "2-Procura arquivos com as informações pre-assinaladas,\n"
            + "3-Caso encontre, abrirá o arquivo (.pdf) e buscará textos especificos ou conforme programado, CNPJ\n"
            + "4-Se não existir dados no conteudo, trará na tabela de resultados com conflitos\n"
            + "5-Se o arquivo não for encontrado dentro dos criterios, em arquivos restantes,\n"
            + "será buscado com criterios fornecidos e será guardado em campos de divergências \n"
    		+ "6-É possivel que não ocorra a leitura por incompatibilidade no(s) arquivo(s), mesmo assim resultado é mostrado";
    
    private final String curinga="Descreva um curinga na procura dos arquivos, ou seja o nome que o arquivo deve ter, \n" +
            "Por exemplo: 0001 - Declaração.pdf\n" +
            "escreva apenas DECLARAÇÃO ou abrevie, não se preocupe com acentos, maiuscula ou minuscula.\n" +
            "Em caso de  0002- Declaração, 0003 - Declarações (escreva DECLARAÇÃO OU DECLAR)\n"
            + "e o filtro será aplicado para todos os arquivos refinando o tempo de pesquisa,\n"
            + "para incluir mais de um filtro insira ',' (virgula) entre as palavras,\n"
            + "tambem atraves da opção Ignorar Nomeclatura marcada, o(s) valores informados no campo serão ignorados."
            ;

    /**
     * @return the sobre
     */
    public String getSobre() {
        return sobre;
    }

    /**
     * @return the curinga
     */
    public String getCuringa() {
        return curinga;
    }
    
    
    
}
