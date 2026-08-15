package sample.model;

import sample.util.MyList;
import java.time.LocalDate;

/**
 * Manager ist die zentrale Verwaltungsstelle der Anwendung.
 * <p>
 * Die Klasse speichert die aktuelle Person, den aktuell angemeldeten Benutzer,
 * die Personenliste und die Übungshistorie.
 * <p>
 * Der Manager ist als Singleton aufgebaut. Dadurch kann im gesamten Programm
 * über {@link #getInstance()} auf dieselbe Verwaltungsinstanz zugegriffen werden.
 */
public class Manager {

    /** Aktuell ausgewählte Person */
    private Person currentPerson;

    /** Aktuell angemeldeter Benutzer */
    private User currentUser;

    /** Liste aller gespeicherten Personen */
    private final MyList<Person> persons = new MyList<>();

    /** Liste aller gespeicherten Übungsprotokolle */
    private final MyList<ExerciseLog> history = new MyList<>();

    /** Einzige Instanz des Managers */
    private static final Manager INSTANCE = new Manager();

    /**
     * Privater Konstruktor, damit keine weiteren Manager-Objekte erzeugt werden können.
     */
    private Manager() {}

    /**
     * Erstellt eine neue Person und fügt sie zur Personenliste hinzu.
     * <p>
     * Die ID wird automatisch über {@link Person#generateId()} erzeugt.
     *
     * @param name Name der Person
     * @param birthDate Geburtsdatum der Person
     * @param condition Reha-Grund der Person
     */
    public void addPerson(String name, LocalDate birthDate, String condition) {
        String id = Person.generateId();
        Person person = new Person(id, name, birthDate, condition);
        persons.add(person);
    }

    /**
     * Fügt einer Person eine neue Übung hinzu.
     *
     * @param person Person, der die Übung zugeordnet wird
     * @param exerciseName Name der Übung
     * @param repetitions Zielanzahl der Wiederholungen
     */
    public void addExerciseToPerson(Person person, String exerciseName, int repetitions) {
        Exercise exercise = new Exercise(exerciseName, repetitions);
        person.addExercise(exercise);
    }

    /**
     * Trägt erledigte Wiederholungen zu einer Übung ein und speichert den Eintrag
     * zusätzlich in der Übungshistorie.
     *
     * @param person Person, zu der die Übung gehört
     * @param exercise Übung, bei der Wiederholungen eingetragen werden
     * @param reps Anzahl der erledigten Wiederholungen
     */
    public void logExercise(Person person, Exercise exercise, int reps) {
        exercise.addReps(reps);
        ExerciseLog exLog = new ExerciseLog(person.getId(), person.getName(), exercise.getExerciseName(), reps, LocalDate.now());
        history.add(exLog);
    }

    /**
     * Löscht eine Person aus der Personenliste.
     * <p>
     * Zusätzlich werden alle Historieneinträge dieser Person entfernt.
     *
     * @param person Zu löschende Person
     */
    public void deletePerson(Person person) {
        String deletedPersonId = person.getId();
        persons.remove(person);

        MyList<ExerciseLog> newHistory = new MyList<>();
        for (int i = 0; i < history.getSize(); i++) {
            ExerciseLog log = history.get(i);
            if (!log.getPersonId().equals(deletedPersonId)) {
                newHistory.add(log);
            }
        }
        history.clear();
        history.addAll(newHistory);
    }

    /**
     * Löscht eine Übung aus der Übungsliste einer Person.
     * <p>
     * Zusätzlich werden alle Historieneinträge dieser Übung bei dieser Person entfernt.
     *
     * @param person Person, der die Übung gehört
     * @param exercise Zu löschende Übung
     */
    public void deleteExercise(Person person, Exercise exercise) {
        person.getExercises().remove(exercise);

        MyList<ExerciseLog> newHistory = new MyList<>();
        for (int i = 0; i < history.getSize(); i++) {
            ExerciseLog log = history.get(i);
            if (!log.getExerciseName().equals(exercise.getExerciseName()) ||
                    !log.getPersonId().equals(person.getId())) {
                newHistory.add(log);
            }
        }
        history.clear();
        history.addAll(newHistory);
    }

    /**
     * Prüft, ob der aktuell angemeldete Benutzer der Chef-Benutzer ist.
     * @return {@code true}, wenn der Chef angemeldet ist, sonst {@code false}
     */
    public boolean isCurrentUserChef() {
        return currentUser != null && currentUser.getEmail().equalsIgnoreCase("chef@javafx.com");
    }

    /**
     * Prüft die Anmeldedaten eines Benutzers.
     * <p>
     * Bei erfolgreicher Anmeldung wird der gefundene Benutzer als aktueller Benutzer
     * gespeichert.
     *
     * @param email E-Mail-Adresse des Benutzers
     * @param password Passwort des Benutzers
     * @return {@code true}, wenn die Anmeldung erfolgreich war, sonst {@code false}
     */
    public boolean authenticateUser(String email, String password) {
        User user = User.authenticate(email, password);
        if (user != null) {
            currentUser = user;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Meldet den aktuellen Benutzer ab.
     * <p>
     * Dabei werden der aktuelle Benutzer und die aktuell ausgewählte Person zurückgesetzt.
     */
    public void logout() {
        currentUser = null;
        currentPerson = null;
    }

    /**
     * Gibt den aktuell angemeldeten Benutzer zurück.
     * @return Aktueller Benutzer oder {@code null}
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Gibt die aktuell ausgewählte Person zurück.
     * @return Aktuelle Person oder {@code null}
     */
    public Person getCurrentPerson() {
        return currentPerson;
    }

    /**
     * Setzt den aktuell angemeldeten Benutzer.
     * @param user Neuer aktueller Benutzer
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /**
     * Setzt die aktuell ausgewählte Person.
     * @param person Neue aktuelle Person
     */
    public void setCurrentPerson(Person person) {
        this.currentPerson = person;
    }

    /**
     * Gibt die Liste aller Personen zurück.
     * @return Personenliste
     */
    public MyList<Person> getPersons() {
        return persons;
    }

    /**
     * Gibt die Liste aller Übungsprotokolle zurück.
     * @return Historienliste
     */
    public MyList<ExerciseLog> getHistory() {
        return history;
    }

    /**
     * Gibt die einzige Manager-Instanz zurück.
     * @return Singleton-Instanz des Managers
     */
    public static Manager getInstance() {
        return INSTANCE;
    }
}
