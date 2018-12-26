package com.prolink.checklist;
import com.prolink.checklist.controller.MenuController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartApp extends Application{
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Menu.fxml"));
		loader.setController(new MenuController(primaryStage));
		//FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MenuImage.fxml"));
		//ImageController controller = new ImageController(primaryStage);
		//loader.setController(controller);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}
}
