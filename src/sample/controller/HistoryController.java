package sample.controller;

import sample.model.Person;
import sample.model.Manager;
import sample.model.ExerciseLog;

import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * HistoryController verwaltet die Historienansicht einer Person.
 * <p>
 * Der Controller ist mit der Datei {@code HistoryView.fxml} verbunden.
 * Er zeigt alle gespeicherten Übungsprotokolle der aktuell ausgewählten Person an.
 */
public class HistoryController {

    @FXML
    private Label personLabel;

    @FXML
    private TableView<ExerciseLog> historyTable;

    @FXML
    private TableColumn<ExerciseLog, String> exerciseColumn, dateColumn;

    @FXML
    private TableColumn<ExerciseLog, Integer> repsColumn;

    /** Aktuell geöffnete Person */
    private Person person;

    /** Zentrale Verwaltungsinstanz der Anwendung */
    private final Manager manager = Manager.getInstance();

    /**
     * Initialisiert die Historienansicht.
     * <p>
     * Die Methode lädt die aktuelle Person über {@link Manager#getCurrentPerson()},
     * verbindet die Tabellenspalten mit den Eigenschaften von {@link ExerciseLog}
     * und füllt die Tabelle über {@link #refreshTable()}.
     */
    @FXML
    private void initialize() {
        this.person = manager.getCurrentPerson();
        personLabel.setText(person.toString());

        exerciseColumn.setCellValueFactory(new PropertyValueFactory<>("exerciseName"));
        repsColumn.setCellValueFactory(new PropertyValueFactory<>("completedReps"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        refreshTable();
    }

    /**
     * Schließt das aktuelle Historienfenster.
     */
    @FXML
    private void closeWindow() {
        Stage stage = (Stage) historyTable.getScene().getWindow();
        stage.close();
    }

    /**
     * Aktualisiert die Protokolltabelle für die aktuell ausgewählte Person.
     * <p>
     * Die Methode liest alle Einträge aus {@link Manager#getHistory()} und zeigt nur
     * die Einträge an, deren Personen-ID mit der ID der aktuell geöffneten Person
     * übereinstimmt.
     */
    private void refreshTable() {
        historyTable.getItems().clear();
        for (int i = 0; i < manager.getHistory().getSize(); i++) {
            ExerciseLog log = manager.getHistory().get(i);
            if (log.getPersonId().equals(person.getId())) {
                historyTable.getItems().add(log);
            }
        }
    }
}
