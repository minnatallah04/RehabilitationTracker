package sample.controller;

import sample.model.User;
import sample.util.FXMLHelper;
import sample.util.DialogHelper;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.application.Platform;

/**
 * RegisterController verwaltet die Registrierung neuer Benutzer.
 * <p>
 * Der Controller ist mit der Datei {@code RegisterView.fxml} verbunden und prüft
 * die eingegebenen Registrierungsdaten. Bei erfolgreicher Registrierung wird der
 * neue Benutzer über {@link User#addUser(String, int, String, String)} gespeichert
 * und anschließend über {@link FXMLHelper#loadMainView(Stage)} zur Hauptansicht gewechselt.
 */
public class RegisterController {

    @FXML
    private TextField nameField, ageField, emailField;

    @FXML
    private PasswordField passwordField, confirmPasswordField;

    @FXML
    private Label errorLabel;

    /**
     * Verarbeitet die Registrierung eines neuen Benutzers.
     * <p>
     * Diese Methode wird automatisch durch den Registrieren-Button in der FXML-Datei
     * aufgerufen. Zuerst werden die Eingabefelder gelesen und geprüft. Dabei wird kontrolliert,
     * ob alle Felder ausgefüllt sind, ob beide Passwörter übereinstimmen, ob das Alter gültig ist,
     * ob die E-Mail auf {@code @javafx.com} endet und ob die E-Mail noch nicht registriert ist.
     * <p>
     * Wenn alle Prüfungen erfolgreich sind, wird der Benutzer über
     * {@link User#addUser(String, int, String, String)} angelegt. Danach wird eine Erfolgsmeldung
     * über {@link DialogHelper#showInfo(String)} angezeigt und über
     * {@link FXMLHelper#loadMainView(Stage)} zurück zur Hauptansicht gewechselt.
     */
    @FXML
    private void handleRegister() {
        errorLabel.setText("");

        String name = nameField.getText().trim();
        String ageStr = ageField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        if (name.isEmpty() || ageStr.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            errorLabel.setText("Bitte alle Felder ausfüllen.");
        } else if (!password.equals(confirm)) {
            errorLabel.setText("Passwörter stimmen nicht überein.");
        } else {
            int age;
            try {
                age = Integer.parseInt(ageStr);
            } catch (NumberFormatException e) {
                errorLabel.setText("Alter muss eine Zahl sein.");
                return;
            }

            if (age < 18) {
                errorLabel.setText("Alter muss mindestens 18 sein.");
            } else if (!email.matches("[\\w.]+@javafx\\.com")) {
                errorLabel.setText("E-Mail muss auf '@javafx.com' enden.");
            } else if (User.isEmailRegistered(email)) {
                errorLabel.setText("E-Mail ist bereits registriert.");
            } else {
                User.addUser(name, age, email, password);
                DialogHelper.showInfo("Registrierung erfolgreich!");
                Stage stage = (Stage) nameField.getScene().getWindow();
                FXMLHelper.loadMainView(stage);
            }
        }
    }

    /**
     * Bricht die Registrierung ab und wechselt zurück zur Hauptansicht.
     * <p>
     * Diese Methode wird durch den Abbrechen-Button aufgerufen und lädt über
     * {@link FXMLHelper#loadMainView(Stage)} wieder die Hauptansicht.
     */
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        FXMLHelper.loadMainView(stage);
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
