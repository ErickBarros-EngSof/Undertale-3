package com.mycompany.undertale_3;

import com.mycompany.undertale_3.dao.DatabaseSetup;
import com.mycompany.undertale_3.util.GameSession;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        DatabaseSetup.criarTabelasSeNaoExistirem();
        trocarTela("SplashScreen.fxml", "Undertale 3");        
        stage.setMinWidth(900);
        stage.setMinHeight(650);
        stage.show();
    }

    public static void trocarTela(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mycompany/undertale_3/" + fxml));
            Parent root = loader.load();
            Scene scene = new Scene(root, 900, 650);
            scene.getStylesheets().add(App.class.getResource("/com/mycompany/undertale_3/style.css").toExternalForm());
            primaryStage.setTitle(titulo);
            primaryStage.setScene(scene);
            root.requestFocus();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao abrir tela: " + fxml, e);
        }
    }

    public static Stage getPrimaryStage() { return primaryStage; }

    public static void main(String[] args) { launch(args); }
}
