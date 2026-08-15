package sample.util;

import sample.model.Exercise;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.SequentialTransition;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.util.Duration;

/**
 * Hilfsklasse für Animationen im Programm.
 * <p>
 * Die Klasse enthält statische Methoden für Startanimationen und
 * Erfolgsanimationen bei abgeschlossenen Übungen.
 */
public class AnimationHelper {

    /**
     * Erstellt die Animation für das Logo.
     * <p>
     * Das Logo wird gleichzeitig eingeblendet und von einer kleineren Größe auf
     * die normale Größe skaliert.
     *
     * @param logoView Logo, das animiert werden soll
     * @return Kombination aus Einblende- und Skalierungsanimation
     */
    public static ParallelTransition logoTransition(ImageView logoView) {
        FadeTransition fade = new FadeTransition(Duration.seconds(2), logoView);
        fade.setFromValue(0);
        fade.setToValue(1);

        ScaleTransition scale = new ScaleTransition(Duration.seconds(2), logoView);
        scale.setFromX(0.5);
        scale.setFromY(0.5);
        scale.setToX(1.0);
        scale.setToY(1.0);

        return new ParallelTransition(fade, scale);
    }

    /**
     * Erstellt die Animation für ein Label.
     * <p>
     * Das Label wird eingeblendet und gleichzeitig von unten an seine normale
     * Position verschoben.
     *
     * @param label Label, das animiert werden soll
     * @return Kombination aus Einblende- und Verschiebungsanimation
     */
    public static ParallelTransition textTransition(Label label) {
        FadeTransition fade = new FadeTransition(Duration.seconds(1.5), label);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition move = new TranslateTransition(Duration.seconds(1.5), label);
        move.setFromY(50);
        move.setToY(0);

        return new ParallelTransition(fade, move);
    }

    /**
     * Zeigt eine Erfolgsanimation für eine abgeschlossene Übung.
     * <p>
     * Die Methode erstellt ein Popup und nutzt dafür intern
     * {@link #createBox(Label, Label, String)},
     * {@link #showCenteredPopup(Stage, Node)} und
     * {@link #playSuccessAnimation(Popup, Node, Node)}.
     *
     * @param stage aktuelles Fenster
     * @param exercise abgeschlossene Übung
     */
    public static void playExerciseCompleteAnimation(Stage stage, Exercise exercise) {
        Label icon = new Label("🎉");
        icon.setStyle("-fx-font-size: 70px;");

        Label text = new Label("Übung abgeschlossen!\n" + exercise.getExerciseName());
        text.setStyle("-fx-text-fill: white; -fx-font-size: 32px; -fx-font-weight: bold; -fx-text-alignment: center;");

        VBox box = createBox(icon, text, "linear-gradient(to right, #2563eb, #22c55e)");
        Popup popup = showCenteredPopup(stage, box);
        playSuccessAnimation(popup, box, icon);
    }

    /**
     * Zeigt eine Erfolgsanimation, wenn alle Übungen einer Person abgeschlossen sind.
     * <p>
     * Die Methode erstellt ein Popup und nutzt dafür intern
     * {@link #createBox(Label, Label, String)},
     * {@link #showCenteredPopup(Stage, Node)} und
     * {@link #playSuccessAnimation(Popup, Node, Node)}.
     *
     * @param stage aktuelles Fenster
     * @param personName Name der Person
     */
    public static void playAllExercisesCompletedAnimation(Stage stage, String personName) {
        Label icon = new Label("🏆");
        icon.setStyle("-fx-font-size: 90px;");

        Label text = new Label(personName + "\nhat alle Übungen abgeschlossen!");
        text.setStyle("-fx-text-fill: white; -fx-font-size: 34px; -fx-font-weight: bold; -fx-text-alignment: center;");

        VBox box = createBox(icon, text, "linear-gradient(to right, #f59e0b, #22c55e)");
        Popup popup = showCenteredPopup(stage, box);
        playSuccessAnimation(popup, box, icon);
    }

    /**
     * Erstellt die Box für das Popup.
     * <p>
     * Die Box enthält ein Symbol und einen Text und bekommt eine feste Größe
     * sowie ein einheitliches Design.
     *
     * @param icon Symbol im Popup
     * @param text Text im Popup
     * @param background Hintergrundfarbe der Box
     * @return gestaltete Popup-Box
     */
    private static VBox createBox(Label icon, Label text, String background) {
        VBox box = new VBox(20, icon, text);
        box.setAlignment(Pos.CENTER);
        box.setPrefSize(650, 320);
        box.setStyle("-fx-background-color: " + background + ";" + "-fx-background-radius: 35;" + "-fx-border-color: white;" +
                "-fx-border-width: 4;" + "-fx-border-radius: 35;" + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.45), 35, 0.4, 0, 12);");
        return box;
    }

    /**
     * Erstellt und zeigt ein Popup mittig im aktuellen Fenster.
     *
     * @param stage aktuelles Fenster
     * @param content Inhalt des Popups
     * @return angezeigtes Popup
     */
    private static Popup showCenteredPopup(Stage stage, Node content) {
        Popup popup = new Popup();
        popup.getContent().add(content);
        popup.show(stage);
        popup.setX(stage.getX() + (stage.getWidth() - 650) / 2);
        popup.setY(stage.getY() + (stage.getHeight() - 320) / 2);

        return popup;
    }

    /**
     * Spielt die Erfolgsanimation für das Popup ab.
     * <p>
     * Die Box wird eingeblendet, vergrößert, leicht zurückskaliert und danach
     * wieder ausgeblendet. Das Symbol bewegt sich dabei leicht hin und her.
     *
     * @param popup Popup, das nach der Animation geschlossen wird
     * @param box animierte Box
     * @param icon animiertes Symbol
     */
    private static void playSuccessAnimation(Popup popup, Node box, Node icon) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(250), box);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ScaleTransition zoom = new ScaleTransition(Duration.millis(350), box);
        zoom.setFromX(0.5);
        zoom.setFromY(0.5);
        zoom.setToX(1.08);
        zoom.setToY(1.08);

        ScaleTransition bounce = new ScaleTransition(Duration.millis(180), box);
        bounce.setToX(1);
        bounce.setToY(1);

        RotateTransition rotateIcon = new RotateTransition(Duration.millis(600), icon);
        rotateIcon.setFromAngle(-15);
        rotateIcon.setToAngle(15);
        rotateIcon.setCycleCount(4);
        rotateIcon.setAutoReverse(true);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), box);
        fadeOut.setDelay(Duration.seconds(2.0));
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        SequentialTransition animation = new SequentialTransition(fadeIn, zoom, bounce, fadeOut);
        animation.setOnFinished(ignored -> popup.hide());

        rotateIcon.play();
        animation.play();
    }
}
