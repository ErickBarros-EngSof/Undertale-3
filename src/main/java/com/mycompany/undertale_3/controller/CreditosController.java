package com.mycompany.undertale_3.controller;

import javafx.scene.media.*;
import com.mycompany.undertale_3.App;
import javafx.fxml.FXML;

public class CreditosController {
    @FXML void voltar() { App.trocarTela("menu.fxml", "Menu"); }
    
    private Media media;
    private MediaPlayer player;

    @FXML
    private void initialize(){
        String caminho = getClass().getResource("/com/mycompany/undertale_3/Música 1 UNDERTALE Toby Fox.m4a").toString();
        media = new Media(caminho);         
        player = new MediaPlayer(media);      
        player.setCycleCount(MediaPlayer.INDEFINITE); 
        player.play();
    }
    
}
