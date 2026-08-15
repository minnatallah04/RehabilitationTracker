package sample.model;

import java.time.LocalDate;

/**
 * ExerciseLog stellt einen Eintrag im Übungsprotokoll dar.
 * <p>
 * Ein Eintrag speichert, welche Person welche Übung an welchem Datum durchgeführt hat
 * und wie viele Wiederholungen dabei erledigt wurden.
 * <p>
 * Für die Zuordnung zur Person werden die Personen-ID und zusätzlich der Personenname
 * gespeichert.
 */
public class ExerciseLog {

    private final String personId;
    private final String personName;
    private final String exerciseName;
    private final int completedReps;
    private final LocalDate date;

    /**
     * Erstellt einen neuen Übungsprotokoll-Eintrag.
     *
     * @param personId ID der zugehörigen Person
     * @param personName Name der zugehörigen Person
     * @param exerciseName Name der durchgeführten Übung
     * @param completedReps Anzahl der erledigten Wiederholungen
     * @param date Datum des Eintrags
     */
    public ExerciseLog(String personId,String personName, String exerciseName, int completedReps, LocalDate date) {
        this.personId = personId;
        this.personName = personName;
        this.exerciseName = exerciseName;
        this.completedReps = completedReps;
        this.date = date;
    }

    /**
     * Gibt die ID der zugehörigen Person zurück.
     * @return Personen-ID
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * Gibt den Namen der zugehörigen Person zurück.
     * @return Personenname
     */
    public String getPersonName() {
        return personName;
    }

    /**
     * Gibt den Namen der Übung zurück.
     * @return Übungsname
     */
    public String getExerciseName() {
        return exerciseName;
    }

    /**
     * Gibt die Anzahl der erledigten Wiederholungen zurück.
     * @return Erledigte Wiederholungen
     */
    public int getCompletedReps() {
        return completedReps;
    }

    /**
     * Gibt das Datum des Eintrags zurück.
     * @return Datum des Übungseintrags
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Gibt eine kurze Textdarstellung des Übungsprotokolls zurück.
     * @return Datum, Übung und erledigte Wiederholungen
     */
    @Override
    public String toString() {
        return date + ": " + exerciseName + " - " + completedReps + " Wiederholungen.";
    }
}
