package com.mycompany.undertale_3.controller;

import com.mycompany.undertale_3.App;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class SplashController{

    @FXML private StackPane rootPane;
    @FXML private VBox logoContainer;
    @FXML private ImageView logoImage;
    @FXML private Label subtitleLabel;
    @FXML private Rectangle blackOverlay;

    @FXML
    public void initialize() {
        try {
            Image logo = new Image(
                    getClass().getResourceAsStream("/com/mycompany/undertale_3/logo.png")
            );
            logoImage.setImage(logo);
        } catch (Exception e) {
            System.err.println("Logo não encontrada: " + e.getMessage());
        }

        blackOverlay.widthProperty().bind(rootPane.widthProperty());
        blackOverlay.heightProperty().bind(rootPane.heightProperty());

        startAnimation();
    }

    public void startAnimation() {
        SequentialTransition sequence = new SequentialTransition(
                pauseFor(800),
                fadeInLogo(),
                pauseFor(400),
                fadeInSubtitle(),
                pauseFor(1800),
                pulseEffect(),
                pauseFor(600),
                fadeOutAll(),
                pauseFor(600)
        );

        sequence.setOnFinished(e -> transitionToGame());
        sequence.play();
    }

    private PauseTransition pauseFor(double millis) {
        return new PauseTransition(Duration.millis(millis));
    }

    private ParallelTransition fadeInLogo() {
        FadeTransition overlayFade =
                new FadeTransition(Duration.millis(1200), blackOverlay);
        overlayFade.setFromValue(1.0);
        overlayFade.setToValue(0.0);

        FadeTransition logoFade =
                new FadeTransition(Duration.millis(1200), logoContainer);
        logoFade.setFromValue(0.0);
        logoFade.setToValue(1.0);

        ScaleTransition logoScale =
                new ScaleTransition(Duration.millis(1400), logoContainer);
        logoScale.setFromX(0.85);
        logoScale.setFromY(0.85);
        logoScale.setToX(1.0);
        logoScale.setToY(1.0);
        logoScale.setInterpolator(Interpolator.EASE_OUT);

        return new ParallelTransition(overlayFade, logoFade, logoScale);
    }

    private FadeTransition fadeInSubtitle() {
        subtitleLabel.setText("");

        FadeTransition ft =
                new FadeTransition(Duration.millis(900), subtitleLabel);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);

        return ft;
    }

    private SequentialTransition pulseEffect() {
        ScaleTransition up1 =
                new ScaleTransition(Duration.millis(180), logoContainer);
        up1.setToX(1.06);
        up1.setToY(1.06);

        ScaleTransition down1 =
                new ScaleTransition(Duration.millis(180), logoContainer);
        down1.setToX(1.0);
        down1.setToY(1.0);

        PauseTransition beat =
                new PauseTransition(Duration.millis(120));

        ScaleTransition up2 =
                new ScaleTransition(Duration.millis(140), logoContainer);
        up2.setToX(1.04);
        up2.setToY(1.04);

        ScaleTransition down2 =
                new ScaleTransition(Duration.millis(200), logoContainer);
        down2.setToX(1.0);
        down2.setToY(1.0);
        down2.setInterpolator(Interpolator.EASE_OUT);

        return new SequentialTransition(up1, down1, beat, up2, down2);
    }

    private ParallelTransition fadeOutAll() {
        FadeTransition overlayIn =
                new FadeTransition(Duration.millis(1000), blackOverlay);
        overlayIn.setFromValue(0.0);
        overlayIn.setToValue(1.0);
        overlayIn.setInterpolator(Interpolator.EASE_IN);

        FadeTransition logoOut =
                new FadeTransition(Duration.millis(900), logoContainer);
        logoOut.setFromValue(1.0);
        logoOut.setToValue(0.0);

        ScaleTransition logoScaleOut =
                new ScaleTransition(Duration.millis(1000), logoContainer);
        logoScaleOut.setToX(0.95);
        logoScaleOut.setToY(0.95);

        return new ParallelTransition(overlayIn, logoOut, logoScaleOut);
    }

    private void transitionToGame() {
        try {
            App.trocarTela("menu.fxml", "Menu");        
        } catch (Exception e) {
            System.err.println("Erro ao carregar tela principal: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
