package sample.model;

import sample.util.MyList;
import java.time.LocalDate;

/**
 * Person stellt einen Patienten in der Rehabilitation dar.
 * <p>
 * Eine Person besitzt eine ID, einen Namen, ein Geburtsdatum und einen Reha-Grund.
 * <p>
 * Jede Person besitzt außerdem eine eigene Liste von Übungen, die ihr im Rahmen
 * der Rehabilitation zugeordnet wurden.
 */
public class Person {

    private final String id;
    private final String name;
    private final LocalDate birthDate;
    private final String condition;

    private static char letter = 'A';
    private static int number = 1;

    /** Liste der zugeordneten Übungen */
    private final MyList<Exercise> exercises = new MyList<>();

    /**
     * Erstellt eine neue Person mit ID, Name, Geburtsdatum und Reha-Grund.
     *
     * @param id ID der Person
     * @param name Name der Person
     * @param birthDate Geburtsdatum der Person
     * @param condition Reha-Grund der Person
     */
    public Person(String id, String name, LocalDate birthDate, String condition) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.condition = condition;
    }

    /**
     * Fügt der Person eine Übung hinzu.
     * @param exercise Hinzuzufügende Übung
     */
    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }

    /**
     * Berechnet den gesamten Fortschritt aller Übungen der Person.
     * <p>
     * Der Gesamtfortschritt ergibt sich aus dem Durchschnitt der Fortschritte
     * aller zugeordneten Übungen. Hat die Person keine Übungen, wird 0.0 zurückgegeben.
     *
     * @return Gesamtfortschritt zwischen 0.0 und 1.0
     */
    public double getOverallProgress() {
        if (exercises.isEmpty()) {
            return 0.0;
        } else {
            double sum = 0;
            for (int i = 0; i < exercises.getSize(); i++) {
                sum += exercises.get(i).getProgress();
            }
            return sum / exercises.getSize();
        }
    }

    /**
     * Gibt die ID der Person zurück.
     * @return ID der Person
     */
    public String getId() {
        return id;
    }

    /**
     * Gibt den Namen der Person zurück.
     * @return Name der Person
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt das Geburtsdatum der Person zurück.
     * @return Geburtsdatum der Person
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Gibt den Reha-Grund der Person zurück.
     * @return Reha-Grund der Person
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Gibt die Übungsliste der Person zurück.
     * @return Liste der Übungen
     */
    public MyList<Exercise> getExercises() {
        return exercises;
    }

    /**
     * Erzeugt automatisch eine neue ID für eine Person.
     * <p>
     * Die ID besteht aus einem Buchstaben und einer zweistelligen Zahl, zum Beispiel
     * {@code A01}. Nach {@code A99} wird mit {@code B01} weitergezählt.
     *
     * @return Neu erzeugte ID
     */
    public static String generateId() {
        String id = String.format("%c%02d", letter, number);
        number++;
        if (number > 99) {
            number = 1;
            letter++;
        }
        return id;
    }

    /**
     * Registriert eine bereits vorhandene Personen-ID beim Laden aus der Datei.
     * <p>
     * Dadurch wird verhindert, dass nach einem Neustart eine bereits verwendete ID
     * erneut vergeben wird. Wenn zum Beispiel A01 bis A05 geladen wurden, wird die
     * nächste neue Person automatisch A06 erhalten.
     *
     * @param existingId bereits gespeicherte Personen-ID, z.B. A01
     */
    public static void registerExistingId(String existingId) {
        if (existingId == null || !existingId.matches("[A-Z]\\d{2}")) {
            return;
        }
        char existingLetter = existingId.charAt(0);
        try {
            int existingNumber = Integer.parseInt(existingId.substring(1));
            if (existingLetter > letter || (existingLetter == letter && existingNumber >= number)) {
                letter = existingLetter;
                number = existingNumber + 1;
                if (number > 99) {
                    number = 1;
                    letter++;
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gibt eine kurze Textdarstellung der Person zurück.
     * @return Name und Reha-Grund der Person
     */
    @Override
    public String toString() {
        return "Name: " + name + "\n" + "Reha-Grund: " + condition;
    }
}
