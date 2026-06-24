package com.mycompany.undertale_3.controller;

import javafx.scene.media.*;
import com.mycompany.undertale_3.App;
import com.mycompany.undertale_3.dao.JogadorDAO;
import com.mycompany.undertale_3.model.RankingLinha;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class RankingController {
    @FXML private TableView<RankingLinha> tabela;
    @FXML private TableColumn<RankingLinha, String> nomeCol;
    @FXML private TableColumn<RankingLinha, Integer> pontosCol, runsCol;
    private Media media;
    private MediaPlayer player;

    @FXML void initialize() {
      
       
        String caminho = getClass().getResource("/com/mycompany/undertale_3/Música 1 UNDERTALE Toby Fox.m4a").toString();
        media = new Media(caminho);         
        player = new MediaPlayer(media);      
        player.setCycleCount(MediaPlayer.INDEFINITE); 
        player.play();
  
        
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
        pontosCol.setCellValueFactory(new PropertyValueFactory<>("pontosTotais"));
        runsCol.setCellValueFactory(new PropertyValueFactory<>("runs"));
        tabela.setItems(FXCollections.observableArrayList(new JogadorDAO().rankingTop10()));
    }
    @FXML void menu() { App.trocarTela("menu.fxml", "Menu"); }
}
