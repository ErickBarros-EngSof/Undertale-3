package com.mycompany.undertale_3.controller;

import com.mycompany.undertale_3.model.Item;
import com.mycompany.undertale_3.util.GameSession;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class InventarioController {
    @FXML private TableView<Item> tabela;
    @FXML private TableColumn<Item, String> nomeCol;
    @FXML private TableColumn<Item, Integer> qtdCol;

    @FXML void initialize() {
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
        qtdCol.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        tabela.setItems(GameSession.inventario);
    }
}
