package com.jdm.main;

import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import java.util.stream.IntStream;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class Main extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.show();
		modifyThreads();
	}
	
	private void modifyThreads() {
		Preferences preferences = Preferences.userNodeForPackage(Main.class).node("threads");
		ResourceBundle bundle = ResourceBundle.getBundle("threads");
		Dialog<Boolean> dialog = new Dialog<>();
		dialog.initStyle(StageStyle.UNIFIED);
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.setOnCloseRequest(event -> {
			dialog.hide();
			dialog.close();
			dialog.setResult(true);
		});
		DialogPane pane = new DialogPane();
		BorderPane borderPane = new BorderPane();
		String[] strings = IntStream.rangeClosed(1, 64).mapToObj(Integer::toString).toArray(String[]::new);
		Label labelSeq = new Label("Sequential ");
		Label label4k1 = new Label("4Ki (1) ");
		Label label4k2 = new Label("4Ki (2) ");
		Label label4k3 = new Label("4Ki (3) ");
		ComboBox<String> boxSeq = new ComboBox<>(FXCollections.observableArrayList(strings));
		boxSeq.setValue(preferences.get("seq", bundle.getString("seq")));
		ComboBox<String> box4k1 = new ComboBox<>(FXCollections.observableArrayList(strings));
		box4k1.setValue(preferences.get("4k1", bundle.getString("4k1")));
		ComboBox<String> box4k2 = new ComboBox<>(FXCollections.observableArrayList(strings));
		box4k2.setValue(preferences.get("4k2", bundle.getString("4k2")));
		ComboBox<String> box4k3 = new ComboBox<>(FXCollections.observableArrayList(strings));
		box4k3.setValue(preferences.get("4k3", bundle.getString("4k3")));
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.addColumn(0, labelSeq, label4k1, label4k2, label4k3);
		gridPane.addColumn(1, boxSeq, box4k1, box4k2, box4k3);
		borderPane.setCenter(gridPane);
		VBox top = new VBox(new Label("Threads"));
		top.setAlignment(Pos.CENTER);
		borderPane.setTop(top);
		HBox bottom = new HBox();
		bottom.setAlignment(Pos.CENTER);
		Button exit = new Button("Exit");
		exit.setCancelButton(true);
		exit.setOnAction(event -> dialog.setResult(true));
		ButtonBar.setButtonData(exit, ButtonBar.ButtonData.CANCEL_CLOSE);
		bottom.getChildren().addAll(new Button("Default"), exit);
		borderPane.setBottom(bottom);
		dialog.setDialogPane(pane);
		dialog.setTitle("Threads");
		pane.setContent(borderPane);
		pane.autosize();
		dialog.setResizable(false);
		dialog.getDialogPane().getScene().getWindow().setOnCloseRequest(event -> ((Window) event.getSource()).hide());
		dialog.showAndWait();
	}
}