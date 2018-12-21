package com.prolink.checklist.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.prolink.checklist.model.Indexador;
import javafx.scene.control.Alert;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LeitorExcel {

	Logger logger = LoggerFactory.getLogger(getClass());

	public ArrayList<List<Indexador>> readWorkbook(File arquivo) {

		ArrayList<List<Indexador>> indexadorList = new ArrayList<>();
		// informando a senha de descriptografia
		//Biff8EncryptionKey.setCurrentUserPassword("PLKCONTRATOS");
		Workbook workbook = null;
		try {
			if(arquivo.getName().toUpperCase().endsWith(".xls")) {
				NPOIFSFileSystem fs = new NPOIFSFileSystem(arquivo, true);
				workbook = new HSSFWorkbook(fs.getRoot(), true);
			}
			else{
				workbook = new XSSFWorkbook(new FileInputStream(arquivo));
			}
			// removendo senha para leitura
			//Biff8EncryptionKey.setCurrentUserPassword(null);
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> linha = sheet.rowIterator();
			while (linha.hasNext()) {
				Row row = linha.next();
				//Cell cell = row.getCell(0);
				//if(readingCell(cell).equals("")) break;
				List<Indexador> indexador = new ArrayList<>();
				Iterator<Cell> columns = row.cellIterator();
				while (columns.hasNext()) {
					Cell celula = columns.next();
					Indexador index = new Indexador(celula.getColumnIndex(), readingCell(celula));
					indexador.add(index);
				}
				indexadorList.add(indexador);
			}
		}catch (IOException e){
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Erro");
			alert.setHeaderText("Erro ao carregar planilha");
			alert.setContentText("Falha ao carregar planilha: "+e.getMessage());
			alert.getDialogPane().setMinSize(600,200);
			alert.showAndWait();
		}
		finally {
			try {
				if(workbook!=null) workbook.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		return indexadorList;
	}

	private String readingCell(Cell celula) { // metodo usado para tratar as celulas
		switch (celula.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(celula))
				return new SimpleDateFormat("dd/MM/yyyy").format(celula.getDateCellValue());// campo do tipo data
																							// formatando no ato
			else {
				return String.valueOf((long) celula.getNumericCellValue());// campo do tipo numerico, convertendo double
																			// para long
			}
		case Cell.CELL_TYPE_STRING:
			return String.valueOf(celula.getStringCellValue()).trim();// conteudo do tipo texto
		case Cell.CELL_TYPE_BOOLEAN:
			return "";
		case Cell.CELL_TYPE_BLANK:
			return "";
		default:
			return "";
		}
		
	}
	public void copyWorkbook(String origemPlan, String destinoPlan) {
		try {
			// copiando arquivo para um local temporario
			Path local = Paths.get(origemPlan);
			Path destino = Paths.get(destinoPlan);
			Files.copy(local, destino, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

}
