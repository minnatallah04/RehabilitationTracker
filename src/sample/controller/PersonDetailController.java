package sample.controller;

import sample.model.Person;
import sample.model.Manager;
import sample.model.Exercise;
import sample.model.RehaAssistant;

import sample.util.FXMLHelper;
import sample.util.DialogHelper;
import sample.util.AnimationHelper;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.control.ProgressBar;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import sample.util.MyList;

/**
 * PersonDetailController verwaltet die Detailansicht einer Person.
 * <p>
 * Der Controller ist mit der Datei {@code PersonDetailView.fxml} verbunden.
 * Er zeigt die Übungen einer Person an, verwaltet Wiederholungen, Übungsvorschläge
 * und den Wechsel zur Historie. Außerdem werden Erfolgsanimationen angezeigt,
 * wenn Übungen abgeschlossen wurden.
 */
public class PersonDetailController {

    @FXML
    private Label personLabel, progressLabel;

    @FXML
    private ListView<Exercise> exerciseListView;

    @FXML
    private ComboBox<String> exerciseCombo;

    @FXML
    private ComboBox<Integer> repetitionsCombo;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private TextField doneRepetitionsField;

    @FXML
    private TextArea logArea;

    /** Aktuell geöffnete Person */
    private Person person;

    /** Zentrale Verwaltungsinstanz der Anwendung */
    private final Manager manager = Manager.getInstance();

    /**
     * Initialisiert die Detailansicht.
     * <p>
     * Die Methode lädt die aktuelle Person über {@link Manager#getCurrentPerson()},
     * füllt die Übungsauswahl über {@link Exercise#getExerciseTemplates()} und
     * aktualisiert die Ansicht über {@link #refreshAndUpdate()}.
     */
    @FXML
    private void initialize() {
        this.person = manager.getCurrentPerson();
        personLabel.setText(person.toString());
        refreshAndUpdate();
        log("Person geöffnet: " + person.getName());

        exerciseCombo.getItems().clear();
        for (int i = 0; i < Exercise.getExerciseTemplates().getSize(); i++) {
            exerciseCombo.getItems().add(Exercise.getExerciseTemplates().get(i));
        }

        repetitionsCombo.getItems().clear();
        for (int i = 5; i <= 50; i += 5) {
            repetitionsCombo.getItems().add(i);
        }
    }

    /**
     * Fügt der Person eine neue Übung hinzu.
     * <p>
     * Die Methode liest Übung und Wiederholungsanzahl aus den Auswahlfeldern.
     * Bei gültiger Auswahl wird die Übung über
     * {@link Manager#addExerciseToPerson(Person, String, int)} hinzugefügt.
     * Danach wird die Ansicht über {@link #refreshAndUpdate()} aktualisiert.
     */
    @FXML
    private void addExercise() {
        Integer repetitions = repetitionsCombo.getValue();
        String exercise = exerciseCombo.getValue();

        if (repetitions == null || exercise == null) {
            DialogHelper.showWarning("Ungültige Eingabe für Übung!");
        } else {
            manager.addExerciseToPerson(person, exercise, repetitions);
            exerciseCombo.setValue(null);
            repetitionsCombo.setValue(null);
            refreshAndUpdate();
            log("Übung wurde hinzugefügt: " + exercise + " (" + repetitions + " Wiederholungen)");
        }
    }

    /**
     * Löscht die ausgewählte Übung.
     * <p>
     * Nach Bestätigung wird die Übung über
     * {@link Manager#deleteExercise(Person, Exercise)} entfernt. Danach wird die Ansicht
     * über {@link #refreshAndUpdate()} aktualisiert.
     */
    @FXML
    private void deleteSelectedExercise() {
        Exercise selected = exerciseListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogHelper.showWarning("Bitte zuerst eine Übung auswählen.");
        } else {
            boolean confirm = DialogHelper.showConfirmation(
                    "Wollen Sie die Übung \"" + selected.getExerciseName() + "\" wirklich löschen?");
            if (confirm) {
                manager.deleteExercise(person, selected);
                log("Übung " + selected.getExerciseName() + " wurde gelöscht.");
                refreshAndUpdate();
            } else {
                log("Der Vorgang wurde abgebrochen");
            }
        }
    }

    /**
     * Trägt erledigte Wiederholungen für die ausgewählte Übung ein.
     * <p>
     * Die Methode prüft die Eingabe und übergibt gültige Wiederholungen an
     * {@link Manager#logExercise(Person, Exercise, int)}. Danach wird geprüft,
     * ob eine einzelne Übung oder alle Übungen abgeschlossen wurden.
     * In diesem Fall wird eine passende Animation über
     * {@link AnimationHelper#playExerciseCompleteAnimation(Stage, Exercise)} oder
     * {@link AnimationHelper#playAllExercisesCompletedAnimation(Stage, String)} angezeigt.
     * <p>
     * Anschließend werden Eingabefeld, Ansicht und Protokoll aktualisiert.
     */
    @FXML
    private void addDoneRepetitions() {
        Exercise selected = exerciseListView.getSelectionModel().getSelectedItem();
        String repetitions = doneRepetitionsField.getText();

        if (selected == null) {
            DialogHelper.showWarning("Bitte wählen Sie eine Übung aus");
        } else {
            try {
                int reps = Integer.parseInt(repetitions);

                if (reps <= 0) {
                    DialogHelper.showWarning("Bitte eine positive Zahl eingeben.");
                } else {
                    boolean wasNotCompletedBefore = selected.getProgress() < 1.0;
                    boolean allWereNotCompletedBefore = !areAllExercisesCompleted();

                    manager.logExercise(person, selected, reps);

                    boolean isCompletedNow = selected.getProgress() >= 1.0;
                    boolean allCompletedNow = areAllExercisesCompleted();

                    Stage stage = (Stage) logArea.getScene().getWindow();
                    if (allWereNotCompletedBefore && allCompletedNow) {
                        AnimationHelper.playAllExercisesCompletedAnimation(stage, person.getName());
                    } else if (wasNotCompletedBefore && isCompletedNow) {
                        AnimationHelper.playExerciseCompleteAnimation(stage, selected);
                    }

                    doneRepetitionsField.clear();
                    refreshAndUpdate();
                    log(reps + " Wiederholungen für: " + person.getName() + " sind eingetragen");
                }

            } catch (NumberFormatException e) {
                DialogHelper.showWarning("Wiederholung muss eine Zahl sein!");
            }
        }
    }

    /**
     * Lässt den Reha-Assistenten Übungen vorschlagen.
     * <p>
     * Die Vorschläge werden über {@link RehaAssistant#suggestExercises(Person)} erstellt
     * und vor dem Übernehmen angezeigt. Bei Bestätigung werden die Übungen zur Person
     * hinzugefügt und die Ansicht über {@link #refreshAndUpdate()} aktualisiert.
     */
    @FXML
    private void suggestExercises() {
        Person person = manager.getCurrentPerson();

        MyList<Exercise> suggestions = RehaAssistant.suggestExercises(person);
        StringBuilder message = new StringBuilder("Der Reha-Assistent schlägt folgende Übungen vor:\n\n");
        for (int i = 0; i < suggestions.getSize(); i++) {
            Exercise exercise = suggestions.get(i);
            message.append("- ").append(exercise.getExerciseName()).append(" (")
                    .append(exercise.getTargetReps()).append(" Wiederholungen)\n");
        }

        message.append("\nMöchten Sie diese Übungen übernehmen?");
        boolean confirm = DialogHelper.showConfirmation(message.toString());

        if (!confirm) {
            log("Vorschläge des Reha-Assistenten wurden abgebrochen.");
        } else {
            for (int i = 0; i < suggestions.getSize(); i++) {
                person.addExercise(suggestions.get(i));
            }

            refreshAndUpdate();
            log("Reha-Assistent hat passende Übungen vorgeschlagen.");
        }
    }

    /**
     * Öffnet die Historie der aktuellen Person.
     * <p>
     * Die Historienansicht wird über {@link FXMLHelper#loadHistoryView(Node)} geladen.
     */
    @FXML
    private void openHistory() {
        FXMLHelper.loadHistoryView(logArea);
    }

    /**
     * Wechselt zurück zur Hauptansicht.
     * <p>
     * Die Hauptansicht wird über {@link FXMLHelper#loadMainView(Stage)} geladen.
     */
    @FXML
    private void goBack() {
        Stage stage = (Stage) logArea.getScene().getWindow();
        FXMLHelper.loadMainView(stage);
    }

    /**
     * Aktualisiert die Übungsliste und den Gesamtfortschritt der Person.
     * <p>
     * Die Methode lädt die Übungen aus {@link Person#getExercises()} neu in die
     * {@code ListView}. Über {@code setCellFactory(...)} wird festgelegt, dass jede
     * Übung mit Name, Wiederholungen und Fortschrittsbalken angezeigt wird.
     * <p>
     * Danach wird der Gesamtfortschritt über {@link Person#getOverallProgress()}
     * berechnet und in {@code progressBar} sowie {@code progressLabel} angezeigt.
     */
    private void refreshAndUpdate() {
        exerciseListView.getItems().clear();
        for (int i = 0; i < person.getExercises().getSize(); i++) {
            exerciseListView.getItems().add(person.getExercises().get(i));
        }

        exerciseListView.setCellFactory(ignored -> new ListCell<>() {
            @Override
            protected void updateItem(Exercise item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    ProgressBar pb = new ProgressBar(item.getProgress());
                    setText(item.getExerciseName() + " (" + item.getCompletedReps() + "/" + item.getTargetReps() + ")");
                    setGraphic(pb);
                }
            }
        });

        double progress = person.getOverallProgress();
        progressBar.setProgress(progress);
        progressLabel.setText((int) (progress * 100) + "%");
    }

    /**
     * Schreibt eine Nachricht in den Logbereich.
     * @param message Auszugebende Nachricht
     */
    private void log(String message) {
        logArea.appendText(message + "\n");
    }

    /**
     * Prüft, ob alle Übungen der aktuellen Person abgeschlossen sind.
     *
     * @return {@code true}, wenn die Übungsliste nicht leer ist und alle Übungen
     * vollständig abgeschlossen sind
     */
    private boolean areAllExercisesCompleted() {
        for (Exercise exercise : exerciseListView.getItems()) {
            if (exercise.getProgress() < 1.0) {
                return false;
            }
        }
        return !exerciseListView.getItems().isEmpty();
    }
}
