package sample.controller;

import sample.model.User;
import sample.model.Manager;
import sample.util.FXMLHelper;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.application.Platform;

/**
 * LoginController verwaltet die Anmeldung der Benutzer.
 * <p>
 * Der Controller ist mit der Datei {@code LoginView.fxml} verbunden und prüft
 * die eingegebenen Anmeldedaten. Bei erfolgreicher Anmeldung wird der Benutzer
 * im {@link Manager} als aktueller Benutzer gespeichert und anschließend über
 * {@link FXMLHelper#loadMainView(Stage)} zur Hauptansicht gewechselt.
 */
public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    /** Zentrale Verwaltungsinstanz der Anwendung */
    private final Manager manager = Manager.getInstance();

    /**
     * Verarbeitet die Anmeldung eines Benutzers.
     * <p>
     * Diese Methode wird durch den Anmelden-Button in der FXML-Datei aufgerufen.
     * Zuerst werden E-Mail und Passwort gelesen und auf leere Eingaben geprüft.
     * Danach werden die Anmeldedaten über {@link User#authenticate(String, String)}
     * geprüft.
     * <p>
     * Bei erfolgreicher Anmeldung wird der Benutzer im {@link Manager} gespeichert und
     * über {@link FXMLHelper#loadMainView(Stage)} die Hauptansicht geladen.
     */
    @FXML
    private void handleLogin() {
        String email  = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Bitte E-Mail und Passwort eingeben.");
        } else {
            User user = User.authenticate(email, password);
            if (user != null && manager.authenticateUser(email, password)) {
                manager.setCurrentUser(user);

                Stage stage = (Stage) emailField.getScene().getWindow();
                FXMLHelper.loadMainView(stage);
            } else {
                errorLabel.setText("Benutzername oder Passwort falsch.");
            }
        }
    }

    /**
     * Schließt die Anwendung.
     * <p>
     * Diese Methode wird durch den App-schließen-Button aufgerufen und beendet das Programm
     * über {@link Platform#exit()}.
     */
    @FXML
    private void handleExit() {
        Platform.exit();
    }
}
