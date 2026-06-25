package com.mycompany.undertale_3.controller;

import com.mycompany.undertale_3.App;
import com.mycompany.undertale_3.dao.UpgradeDAO;
import com.mycompany.undertale_3.util.GameSession;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class UpgradesController {
    
    @FXML private Label pontosLabel;
    private final UpgradeDAO dao = new UpgradeDAO();
    @FXML void initialize() { pontosLabel.setText("Pontos da run para gastar: " + GameSession.pontosRun); }
    @FXML void comprarHp() { comprar("hp", 120); }
    @FXML void comprarAtk() { comprar("atk", 100); }
    @FXML void comprarDef() { comprar("def", 110); }
    private void comprar(String tipo, int custo) {
        if (GameSession.pontosRun < custo) { new Alert(Alert.AlertType.WARNING, "Pontos insuficientes.").showAndWait(); return; }
        GameSession.pontosRun -= custo;
        dao.comprar(GameSession.jogador.getId(), tipo);
        pontosLabel.setText("Pontos da run para gastar: " + GameSession.pontosRun);
        new Alert(Alert.AlertType.INFORMATION, "Upgrade comprado e salvo no banco.").showAndWait();
    }
    @FXML void voltar() { 
        
        App.trocarTela("jogo.fxml", "Gameplay"); 
    }
}
