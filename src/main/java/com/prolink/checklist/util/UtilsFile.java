package com.prolink.checklist.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class UtilsFile {
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
}
