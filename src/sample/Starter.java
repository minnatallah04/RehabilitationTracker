package sample;

import sample.util.FXMLHelper;
import sample.util.DataManager;

import javafx.stage.Stage;
import javafx.application.Application;

/**
 * Einstiegspunkt der Anwendung.
 * <p>
 * Verantwortlich für das Laden der gespeicherten Daten beim Start
 * und das Speichern beim Beenden der Anwendung.
 */
public class Starter extends Application {

    /** Objekt der Klasse DataManager, um auf die Lade- und Speichermethoden zuzugreifen. */
    private final DataManager dataManager = new DataManager();

    /**
     * Startet die Anwendung.
     * <p>
     * Lädt alle gespeicherten Daten über {@link DataManager#loadAll()}
     * und zeigt anschließend die Startansicht über {@link FXMLHelper#loadStartView(Stage)}.
     *
     * @param startStage die Haupt-Stage der Anwendung
     */
    @Override
    public void start(Stage startStage) {
        dataManager.loadAll();
        FXMLHelper.loadStartView(startStage);
        startStage.show();
    }

    /**
     * Wird beim Beenden der Anwendung aufgerufen.
     * <p>
     * Speichert alle Daten (Personen, Übungsprotokolle, Benutzeraccounts) über {@link DataManager#saveAll()}.
     */
    @Override
    public void stop() {
        dataManager.saveAll();
    }

    /**
     * Einstiegspunkt der JavaFX-Anwendung.
     * @param args Kommandozeilenargumente
     */
    public static void main(String[] args) {
        launch(args);
    }
}
