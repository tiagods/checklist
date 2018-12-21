package com.prolink.checklist.util;

import com.prolink.checklist.model.Cliente;
import com.prolink.checklist.model.Resultado;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public interface InterfacePDF {
    PDDocument abrirPFD(Path path) throws IOException;
    void buscarCNPJ(int page, PDDocument document, TableView<Resultado> resultados, Label txProgresso, Resultado sugestao, Set<Path> erroLeitura, String cnpjIgnore) throws IOException;
    PDPageTree pageTree(PDDocument document);
    void fechar(PDDocument arquivo) throws IOException;
    boolean validarExtensao(Path path);
    boolean verificarArquivoImagem(PDDocument document) throws IOException;
}
