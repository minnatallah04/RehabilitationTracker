package sample.model;

import sample.util.MyList;

/**
 * Exercise stellt eine einzelne Reha-Übung dar.
 * <p>
 * Eine Übung besitzt einen Namen, eine Zielanzahl an Wiederholungen und die bereits
 * erledigten Wiederholungen.
 * <p>
 * Zusätzlich enthält die Klasse eine feste Liste von Übungsvorlagen. Diese Vorlagen
 * werden zum Beispiel für Auswahlfelder im Programm verwendet.
 */
public class Exercise {

    private final String exerciseName;
    private final int targetReps;
    private int completedReps;

    /** Feste Liste mit verfügbaren Übungsvorlagen */
    private static final MyList<String> EXERCISE_TEMPLATES;

    static {
        EXERCISE_TEMPLATES = new MyList<>();
        EXERCISE_TEMPLATES.add("Armstrecken");
        EXERCISE_TEMPLATES.add("Armkreisen");
        EXERCISE_TEMPLATES.add("Schulterkreisen");
        EXERCISE_TEMPLATES.add("Wanddrücken");
        EXERCISE_TEMPLATES.add("Kniebeugen");
        EXERCISE_TEMPLATES.add("Beinheben");
        EXERCISE_TEMPLATES.add("Fersenheben");
        EXERCISE_TEMPLATES.add("Rumpfbeugen");
        EXERCISE_TEMPLATES.add("Katzenbuckel");
        EXERCISE_TEMPLATES.add("Beckenheben");
        EXERCISE_TEMPLATES.add("Dehnübung");
        EXERCISE_TEMPLATES.add("Lockerungsübung");
    }

    /**
     * Erstellt eine neue Übung mit Name und Zielwiederholungen.
     * <p>
     * Die erledigten Wiederholungen werden beim Erstellen auf 0 gesetzt.
     *
     * @param exerciseName Name der Übung
     * @param targetReps Zielanzahl der Wiederholungen
     */
    public Exercise(String exerciseName, int targetReps) {
        this.exerciseName = exerciseName;
        this.targetReps = targetReps;
        this.completedReps = 0;
    }

    /**
     * Fügt erledigte Wiederholungen zur Übung hinzu.
     * <p>
     * Wenn die erledigten Wiederholungen größer als die Zielanzahl werden, wird der
     * Wert auf die Zielanzahl begrenzt.
     *
     * @param reps Anzahl der hinzuzufügenden Wiederholungen
     */
    public void addReps(int reps) {
        completedReps += reps;
        if (completedReps > targetReps) {
            completedReps = targetReps;
        }
    }

    /**
     * Berechnet den Fortschritt der Übung.
     * <p>
     * Der Fortschritt ergibt sich aus erledigten Wiederholungen geteilt durch
     * Zielwiederholungen. Bei Zielwert 0 wird 0.0 zurückgegeben.
     *
     * @return Fortschritt zwischen 0.0 und 1.0
     */
    public double getProgress() {
        if (targetReps == 0) {
            return 0.0;
        } else {
            return (double) completedReps / targetReps;
        }
    }

    /**
     * Gibt den Namen der Übung zurück.
     * @return Übungsname
     */
    public String getExerciseName() {
        return exerciseName;
    }

    /**
     * Gibt die erledigten Wiederholungen zurück.
     * @return Erledigte Wiederholungen
     */
    public int getCompletedReps() {
        return completedReps;
    }

    /**
     * Gibt die Zielanzahl der Wiederholungen zurück.
     * @return Zielwiederholungen
     */
    public int getTargetReps() {
        return targetReps;
    }

    /**
     * Gibt die feste Liste aller Übungsvorlagen zurück.
     * @return Liste der Übungsvorlagen
     */
    public static MyList<String> getExerciseTemplates() {
        return EXERCISE_TEMPLATES;
    }

    /**
     * Gibt eine kurze Textdarstellung der Übung zurück.
     * @return Übungsname und Fortschritt in Wiederholungen
     */
    @Override
    public String toString() {
        return exerciseName + " | " + completedReps + "/" + targetReps + " Wiederholungen";
    }
}
