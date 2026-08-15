package sample.util;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.fxml.FXMLLoader;

/**
 * Hilfsklasse zum Laden und Anzeigen der verschiedenen FXML-Views.
 */
public class FXMLHelper {

    /** Pfad zur StartView-FXML */
    private static final String START_VIEW = "/sample/view/StartView.fxml";

    /** Pfad zur LoginView-FXML */
    private static final String LOGIN_VIEW = "/sample/view/LoginView.fxml";

    /** Pfad zur RegisterView-FXML */
    private static final String REGISTER_VIEW = "/sample/view/RegisterView.fxml";

    /** Pfad zur MainView-FXML */
    private static final String MAIN_VIEW = "/sample/view/MainView.fxml";

    /** Pfad zur PersonDetailView-FXML */
    private static final String PERSON_DETAIL_VIEW = "/sample/view/PersonDetailView.fxml";

    /** Pfad zur HistoryView-FXML */
    private static final String HISTORY_VIEW = "/sample/view/HistoryView.fxml";

    /**
     * Lädt das StartView-FXML und zeigt es auf der angegebenen Stage.
     * <p>
     * Intern wird {@link #loadFXML(Stage stage, String fxmlPath, String title)} aufgerufen, um das FXML zu laden.
     * @param stage Die Stage, auf der die Startansicht angezeigt wird.
     */
    public static void loadStartView(Stage stage) {
        loadFXML(stage, START_VIEW, "Start");
    }

    /**
     * Lädt das LoginView-FXML und zeigt es auf der angegebenen Stage.
     * <p>
     * Intern wird {@link #loadFXML(Stage stage, String fxmlPath, String title)} aufgerufen, um das FXML zu laden.
     * @param stage Die Stage, auf der die Loginansicht angezeigt wird.
     */
    public static void loadLoginView(Stage stage) {
        loadFXML(stage, LOGIN_VIEW, "Login Ansicht");
    }

    /**
     * Lädt das RegisterView-FXML und zeigt es auf der angegebenen Stage.
     * <p>
     * Intern wird {@link #loadFXML(Stage stage, String fxmlPath, String title)} aufgerufen, um das FXML zu laden.
     * @param stage Die Stage, auf der die Registrierungsansicht angezeigt wird.
     */
    public static void loadRegisterView(Stage stage) {
        loadFXML(stage, REGISTER_VIEW, "Register Ansicht");
    }

    /**
     * Lädt das MainView-FXML und zeigt es auf der angegebenen Stage.
     * <p>
     * Intern wird {@link #loadFXML(Stage stage, String fxmlPath, String title)} aufgerufen, um das FXML zu laden.
     * @param stage Die Stage, auf der das Dashboard angezeigt wird.
     */
    public static void loadMainView(Stage stage) {
        loadFXML(stage, MAIN_VIEW, "Personen Dashboard");
    }

    /**
     * Lädt das PersonDetailView-FXML und zeigt es auf der angegebenen Stage.
     * <p>
     * Intern wird {@link #loadFXML(Stage stage, String fxmlPath, String title)} aufgerufen, um das FXML zu laden.
     * @param stage Die Stage, auf der die Personendetails angezeigt werden.
     */
    public static void loadPersonDetailView(Stage stage) {
        loadFXML(stage, PERSON_DETAIL_VIEW, "Person Details");
    }

    /**
     * Öffnet die HistoryView als modales Fenster.
     * <p>
     * Die {@link Modality} sorgt dafür, dass das Hauptfenster blockiert wird, solange
     * das History-Fenster geöffnet ist. Das bedeutet, dass der Benutzer das
     * Hauptfenster nicht bedienen kann, bis das modale Fenster geschlossen wird.
     *
     * @param ownerNode Node aus dem Hauptfenster, der als Besitzer für die Modalität dient
     *                  (z.B. ein Button oder ein anderes UI-Element)
     */
    public static void loadHistoryView(Node ownerNode) {
        try {
            FXMLLoader loader = new FXMLLoader(FXMLHelper.class.getResource(HISTORY_VIEW));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());

            stage.setScene(scene);
            stage.setTitle("Historie aller Übungen");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(ownerNode.getScene().getWindow());
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Lädt ein FXML und zeigt es auf der Stage an.
     * <p>
     * Ruft interne Hilfsmethode für die öffentlichen View-Helfer.
     *
     * @param stage Die Stage, auf der die FXML angezeigt wird
     * @param fxmlPath Pfad zur FXML-Datei
     * @param title Titel der Stage
     * @throws Exception Falls die FXML-Datei nicht geladen werden kann
     */
    private static void loadFXML(Stage stage, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(FXMLHelper.class.getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            stage.setTitle(title);
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
