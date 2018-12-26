package com.prolink.factory;

import com.prolink.config.DBConfig;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao extends DBConfig {
    private Logger logger = LoggerFactory.getLogger(getClass());
    public Conexao() {
        super();
    }
    public Connection getCon(){
        try {
            Class.forName(getDBDRIVER());
            return DriverManager.getConnection(getURL(),getUSER(),getPASSWORD());
        } catch (SQLException|ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Falha ao estabelecer conexao com o servidor");
            alert.setHeaderText("Sem acesso a base de dados");
            alert.showAndWait();
            return null;
        }
    }
}
