package com.mycompany.undertale_3.controller;

import com.mycompany.undertale_3.App;
import com.mycompany.undertale_3.util.GameSession;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class LojaController {
    @FXML private Label moedasLabel;
    @FXML void initialize() { atualizar(); }
    @FXML void comprarPocao() { comprar("Poção", 25); }
    @FXML void comprarBomba() { comprar("Bomba", 50); }
    @FXML void comprarChave() { comprar("Chave Antiga", 80); }
    private void comprar(String item, int preco) {
        if (GameSession.moedasRun < preco) { new Alert(Alert.AlertType.WARNING, "Moedas insuficientes.").showAndWait(); return; }
        GameSession.moedasRun -= preco;
        GameSession.addItem(item, 1);
        atualizar();
    }
    private void atualizar() { moedasLabel.setText("Moedas da run: " + GameSession.moedasRun); }
    @FXML void voltar() { App.trocarTela("jogo.fxml", "Gameplay"); }
}
