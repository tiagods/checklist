package com.prolink.checklist.enuns;

public enum Mensageria {
    DEFAULT("DEFAULT",Status.INFO,""),
    VALIDADO("VALIDADO",Status.OK,"O CNPJ FOI IDENTIFICADO DENTRO DO ARQUIVO E/OU A NOMECLATURA INICIA PELO CODIGO DO CLIENTE"),
    ERROLEITURA("FALHA DE LEITURA",Status.ERRO,"O CNPJ NÃO FOI IDENTIFICADO DENTRO DO ARQUIVO E/OU A NOMECLATURA NÃO INICIA PELO CODIGO DO CLIENTE"),
    LEITURAOCR("VALIDADO POR INTELIGENCIA ARTIFICIAL",Status.INFO,"O CNPJ FOI IDENTIFICADO ATRAVES DO RECURSO OCR, OCR FAZ PARTE DA INTELIGENCIA ARTIFICIAL QUE É CAPAZ DE REALIZAR LEITURA SEMELHANTE A VISÃO HUMANA, OU SEJA NUMEROS E LETRAS SAO IDENTIFICADOS NAS IMAGENS"),
    //LEITURAMANUAL("VALIDADO MANUALMENTE",Status.OK,"VALIDAÇÃO FOI REALIZADA PELO USUARIO"),
    ARQUIVOBRANCO("ARQUIVO EM BRANCO",Status.ERRO,"O ARQUIVO PDF ESTA EM BRANCO OU VAZIO, NESSE CASO, IDENTIFICADO PELA INTELIGENCIA ARTIFICIAL"),
    EMPRESAINVALIDA("O DOCUMENTO PARECE SER DE OUTRO CLIENTE",Status.ERRO,"QUANDO O CNPJ DO CLIENTE NÃO É LOCALIZADO DENTRO DO DOCUMENTO, E O CNPJ DE OUTRO CLIENTE CONSTA DENTRO NO DOCUMENTO"),

    //essa ultima parte vai para os relatorios
    ARQUIVONAOENCONTRADO("ARQUIVO NÃO ENCONTRADO",Status.ERRO,"O ARQUIVO NÃO FOI LOCALIZADO, POR NOMECLATURA OU CNPJ"),
    NOMEINVALIDO("NOME INVALIDO",Status.ERRO,""),
    CNPJINVALIDO("CNPJ INVALIDO",Status.ERRO,""),
    
    VALIDADOEIA("VALIDADO + IA",Status.INFO,""),
    NOMEIGNORADO("LEITURA DESABILITADA",Status.INFO,""),
    CNPJIGNORADO("LEITURA DESABILITADA",Status.INFO,""),
	VALIDADOMANUALEMNTE("VALIDAÇÃO MANUAL",Status.INFO,"O ARQUIVO FOI VALIDADO PELO USUARIO"),
	RECUSADOMANUALMENTE("RECUSADO",Status.ERRO,"O ARQUIVO FOI RECUSADO PELO USUARIO");
	
    private String descricao;
    private Status status;
    private String resumo;
    private
    Mensageria(String resumo,Status status,String descricao){
        this.descricao = descricao;
        this.status=status;
        this.resumo=resumo;
    }
    @Override
    public String toString() {
        return this.resumo+"["+this.status+"]";
    }
    public enum Status{
        ERRO(1),INFO(2),OK(3);
        private int numero;
        Status(int numero){
            this.numero=numero;
        }
        public int getNumero() {
            return numero;
        }
    }

    public Status getStatus() {
        return status;
    }

    public String getDescricao() {
        return descricao;
    }
}
