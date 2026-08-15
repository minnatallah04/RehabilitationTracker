package sample.controller;

import sample.model.User;
import sample.model.Person;
import sample.model.Manager;

import sample.util.MyList;
import sample.util.FXMLHelper;
import sample.util.DialogHelper;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import java.time.LocalDate;

/**
 * MainController verwaltet die Hauptansicht der Anwendung.
 * <p>
 * Der Controller ist mit der Datei {@code MainView.fxml} verbunden und steuert
 * das Anzeigen, Hinzufügen, Löschen und Suchen von Personen. Außerdem können Personen
 * geöffnet, neue Mitarbeiter registriert und Benutzer abgemeldet werden.
 */
public class MainController {

    @FXML
    private TextField nameField, conditionField, searchField;

    @FXML
    private TableView<Person> personsTable;

    @FXML
    private TableColumn<Person, String> idColumn, nameColumn, birthColumn, conditionColumn;

    @FXML
    private DatePicker birthDatePicker;

    @FXML
    private Button registerButton;

    @FXML
    private TextArea logArea;

    /** Zentrale Verwaltungsinstanz der Anwendung */
    private final Manager manager = Manager.getInstance();

    /**
     * Initialisiert die Hauptansicht.
     * <p>
     * Diese Methode wird automatisch von JavaFX aufgerufen, nachdem die FXML-Datei geladen
     * und die mit {@code @FXML} markierten Elemente verbunden wurden.
     * <p>
     * Die Methode richtet die Tabellenwerte ein und aktualisiert anschließend die Tabelle über
     * {@link #refreshPersons()}.
     */
    @FXML
    private void initialize() {
        registerButton.setVisible(manager.isCurrentUserChef());
        registerButton.setManaged(manager.isCurrentUserChef());

        User currentUser = manager.getCurrentUser();
        log("Willkommen " + currentUser.getName() + "!");

        birthDatePicker.setValue(LocalDate.of(2000, 1, 1));

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        birthColumn.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        conditionColumn.setCellValueFactory(new PropertyValueFactory<>("condition"));

        personsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        refreshPersons();
    }

    /**
     * Fügt eine neue Person zur Personenliste hinzu.
     * <p>
     * Die Methode liest die Eingaben aus den Textfeldern und prüft ihre Gültigkeit.
     * Bei gültiger Eingabe wird die Person über
     * {@link Manager#addPerson(String, LocalDate, String)} angelegt. Danach wird die
     * Tabelle über {@link #refreshPersons()} aktualisiert.
     */
    @FXML
    private void addPerson() {
        String name = nameField.getText();
        LocalDate birthDate = birthDatePicker.getValue();
        String condition = conditionField.getText();

        if (name.trim().isEmpty() || condition.trim().isEmpty()) {
            DialogHelper.showWarning("Bitte alle Felder ausfühlen.");
        } else {
            manager.addPerson(name, birthDate, condition);

            refreshPersons();
            clearFields();
            birthDatePicker.setValue(LocalDate.of(2000, 1, 1));
            log(name + " wurde hinzugefügt.");
        }
    }

    /**
     * Löscht die ausgewählte Person.
     * <p>
     * Nach Bestätigung wird die Person über {@link Manager#deletePerson(Person)}
     * gelöscht. Danach wird die Tabelle über {@link #refreshPersons()} aktualisiert.
     */
    @FXML
    private void deleteSelectedPerson() {
        Person selected = personsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogHelper.showWarning("Bitte zuerst eine Person auswählen.");
        } else {
            boolean confirm = DialogHelper.showConfirmation(
                    "Wollen Sie die Person \"" + selected.getName() + "\" wirklich löschen?");
            if (confirm) {
                manager.deletePerson(selected);
                log("Person " + selected.getName() + " wurde gelöscht.");
                refreshPersons();
            } else {
                log("Vorgang wurde abgebrochen.");
            }
        }
    }

    /**
     * Sucht Personen anhand des eingegebenen Suchbegriffs.
     * <p>
     * Die Methode durchsucht die Personenliste aus {@link Manager#getPersons()}.
     * Dabei kann nach dem vollständigen Namen, einem Teil des Namens oder nach der
     * Personen-ID gesucht werden. Groß- und Kleinschreibung werden ignoriert.
     * <p>
     * Gefundene Personen werden anschließend in der Tabelle angezeigt.
     */
    @FXML
    private void searchPerson() {
        String searchText = searchField.getText().trim().toLowerCase();

        if (searchText.trim().isEmpty()) {
            DialogHelper.showWarning("Bitte geben Sie einen Suchbegriff ein.");
        } else {
            MyList<Person> matches = new MyList<>();
            for (int i = 0; i < manager.getPersons().getSize(); i++) {
                Person person = manager.getPersons().get(i);
                if (person.getName().toLowerCase().contains(searchText) ||
                        person.getId().toLowerCase().contains(searchText)) {
                    matches.add(person);
                }
            }

            if (matches.isEmpty()) {
                DialogHelper.showInfo("Keine Person mit diesem Namen oder ID gefunden.");
            } else {
                searchField.clear();
                personsTable.getItems().clear();
                for (int i = 0; i < matches.getSize(); i++) {
                    personsTable.getItems().add(matches.get(i));
                }
            }
        }
    }

    /**
     * Öffnet die ausgewählte Person in der Detailansicht.
     * <p>
     * Die Person wird über {@link Manager#setCurrentPerson(Person)} gespeichert.
     * Danach wird die Detailansicht über {@link FXMLHelper#loadPersonDetailView(Stage)} geladen.
     */
    @FXML
    private void openSelectedPerson() {
        Person selectedPerson = personsTable.getSelectionModel().getSelectedItem();

        if (selectedPerson == null) {
            DialogHelper.showWarning("Bitte zuerst eine Person auswählen.");
        } else {
            manager.setCurrentPerson(selectedPerson);
            Stage stage = (Stage) personsTable.getScene().getWindow();
            FXMLHelper.loadPersonDetailView(stage);
        }
    }

    /**
     * Öffnet die Registrierungsansicht.
     * <p>
     * Die Methode lädt die Ansicht über {@link FXMLHelper#loadRegisterView(Stage)}.
     */
    @FXML
    private void openRegisterView() {
        Stage stage = (Stage) personsTable.getScene().getWindow();
        FXMLHelper.loadRegisterView(stage);
    }

    /**
     * Meldet den aktuellen Benutzer ab.
     * <p>
     * Die Methode setzt die aktuelle Sitzung über {@link Manager#logout()} zurück und lädt
     * danach die Login-Ansicht über {@link FXMLHelper#loadLoginView(Stage)}.
     */
    @FXML
    private void logout() {
        manager.logout();
        Stage stage = (Stage) personsTable.getScene().getWindow();
        FXMLHelper.loadLoginView(stage);
    }

    /**
     * Aktualisiert die Personenliste in der Tabelle.
     * <p>
     * Die Methode leert die Tabelle und fügt alle Personen aus
     * {@link Manager#getPersons()} erneut ein.
     */
    @FXML
    private void refreshPersons() {
        personsTable.getItems().clear();
        for (int i = 0; i < manager.getPersons().getSize(); i++) {
            personsTable.getItems().add(manager.getPersons().get(i));
        }
    }

    /**
     * Schreibt eine Nachricht in den Logbereich.
     * @param message Nachricht, die ausgegeben werden soll
     */
    private void log(String message) {
        logArea.appendText(message + "\n");
    }

    /**
     * Leert die Eingabefelder für Name und Reha-Grund.
     */
    private void clearFields() {
        nameField.clear();
        conditionField.clear();
    }
}
