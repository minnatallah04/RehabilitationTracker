package sample.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Hilfsklasse für Dialogfenster im Programm.
 * <p>
 * Bietet zentrale Methoden zum Anzeigen von Informations-, Warn-, Fehler- und Bestätigungsdialogen.
 * <p>
 * Diese Klasse erleichtert die Verwendung von JavaFX-Dialogen und sorgt für konsistente Darstellung
 * und Bedienung im gesamten Programm.
 */
public class DialogHelper {

    /**
     * Zeigt ein Informations-Dialogfenster an.
     * <p>
     * Wird verwendet, um den Benutzer über einen Hinweis oder eine erfolgreiche Aktion zu informieren.
     *
     * @param text Der anzuzeigende Informationstext.
     */
    public static void showInfo(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hinweis");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    /**
     * Zeigt ein Warn-Dialogfenster an.
     * <p>
     * Wird verwendet, um den Benutzer auf einen Fehler oder eine unsichere Aktion aufmerksam zu machen.
     *
     * @param text Der Warnungstext, der angezeigt wird.
     */
    public static void showWarning(String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Achtung");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    /**
     * Zeigt ein Bestätigungs-Dialogfenster an mit den Optionen "Ja" und "Nein".
     * <p>
     * Wird verwendet, um vom Benutzer eine eindeutige Zustimmung oder Ablehnung zu einer Aktion zu erhalten.
     *
     * @param text Der Text, der die zu bestätigende Aktion beschreibt.
     * @return {@code true}, wenn der Benutzer "Ja" wählt, {@code false} bei "Nein" oder Abbruch.
     */
    public static boolean showConfirmation(String text) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Bestätigung");
        alert.setHeaderText(null);
        alert.setContentText(text);

        ButtonType yesButton = new ButtonType("Ja");
        ButtonType noButton = new ButtonType("Nein");

        alert.getButtonTypes().setAll(yesButton, noButton);

        return alert.showAndWait().map(response -> response == yesButton).orElse(false);
    }
}
