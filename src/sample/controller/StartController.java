package sample.controller;

import sample.util.FXMLHelper;
import sample.util.AnimationHelper;

import javafx.animation.PauseTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.control.ProgressIndicator;

/**
 * StartController ist mit der Datei {@code StartView.fxml} verbunden und steuert die Startansicht
 * der Anwendung.
 * <p>
 * Beim Start werden Logo und Texte animiert eingeblendet. Nach Abschluss der Animation
 * wird automatisch zur Login-Ansicht gewechselt.
 */
public class StartController {

    @FXML
    private ImageView logoView;

    @FXML
    private Label titleLabel, subtitleLabel;

    @FXML
    private ProgressIndicator loadingCircle;

    @FXML
    private Label loadingLabel;

    /**
     * Initialisiert die Startansicht.
     * <p>
     * Die Methode bereitet Logo, Texte und Ladeanzeige vor. Die Animationen werden
     * über {@link AnimationHelper#logoTransition(ImageView)} und
     * {@link AnimationHelper#textTransition(Label)} abgespielt.
     * Anschließend wird über {@link FXMLHelper#loadLoginView(Stage)} die Login-Ansicht geladen.
     */
    @FXML
    private void initialize() {

        logoView.setOpacity(0);
        titleLabel.setOpacity(0);
        subtitleLabel.setOpacity(0);
        logoView.setScaleX(0.5);
        logoView.setScaleY(0.5);
        titleLabel.setTranslateY(50);
        subtitleLabel.setTranslateY(50);
        loadingLabel.setVisible(false);
        loadingCircle.setVisible(false);

        ParallelTransition logoAnimation = AnimationHelper.logoTransition(logoView);
        logoAnimation.setOnFinished(ignored -> {
            loadingLabel.setVisible(true);
            loadingCircle.setVisible(true);
        });

        SequentialTransition total = new SequentialTransition(logoAnimation, new PauseTransition(Duration.seconds(0.5)),
                AnimationHelper.textTransition(titleLabel), AnimationHelper.textTransition(subtitleLabel));

        total.setOnFinished(ignored -> {
            Stage stage = (Stage) logoView.getScene().getWindow();
            FXMLHelper.loadLoginView(stage);
        });

        total.play();
    }
}
