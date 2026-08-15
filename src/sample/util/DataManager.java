package sample.util;

import sample.model.User;
import sample.model.Person;
import sample.model.Manager;
import sample.model.Exercise;
import sample.model.ExerciseLog;

import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.time.LocalDate;

/**
 * DataManager ist für das Speichern und Laden von Personen, Übungsprotokollen
 * und Benutzeraccounts zuständig.
 * <p>
 * Die Daten werden in drei Textdateien abgelegt:
 * <ul>
 *  <li>{@code persons.txt}: Personen-ID;Personenname;Geburtsdatum;Reha-Grund;Übungen...</li>
 *  <li>{@code history.txt}: Personen-ID;Personenname;Übungsname;erledigte-Wiederholungen;Datum</li>
 *  <li>{@code users.txt}: Benutzername;Alter;E-Mail;Passwort</li>
 * </ul>
 * Die Klasse nutzt das Singleton {@link Manager}, um auf Personen, Übungsprotokolle
 * und Benutzeraccounts zuzugreifen.
 */
public class DataManager {

    /** Textdatei für gespeicherte Personen inkl. Übungen */
    private final String PERSON_FILE = "data/persons.txt";

    /** Textdatei für gespeicherte Übungsprotokolle */
    private final String LOG_FILE = "data/history.txt";

    /** Textdatei für gespeicherte Benutzer-Zugangsdaten */
    private final String USERS_FILE = "data/users.txt";

    /**
     * Speichert alle Daten (Personen, History und Benutzer) in die entsprechenden Textdateien.
     * <p>
     * Diese Methode ruft intern folgende Methoden auf:
     * <ul>
     *  <li>{@link #savePersons()} – speichert alle Personen und ihrer Übungen in {@link #PERSON_FILE}</li>
     *  <li>{@link #saveHistory()} – speichert alle Übungsprotokolle in {@link #LOG_FILE}</li>
     *  <li>{@link #saveUsers()} – speichert alle Benutzer in {@link #USERS_FILE}</li>
     * </ul>
     */
    public void saveAll() {
        savePersons();
        saveHistory();
        saveUsers();
    }

    /**
     * Lädt alle Daten (Personen, History und Benutzer) aus den Textdateien.
     * <p>
     * Diese Methode ruft intern folgende Methoden auf:
     * <ul>
     *  <li>{@link #loadPersons()} – lädt alle Personen inkl. Übungen aus {@link #PERSON_FILE} und ignoriert fehlerhafte Zeilen</li>
     *  <li>{@link #loadHistory()} – lädt alle Übungsprotokolle aus {@link #LOG_FILE} und ignoriert fehlerhafte Zeilen</li>
     *  <li>{@link #loadUsers()} – lädt alle Benutzer aus {@link #USERS_FILE} und ignoriert fehlerhafte Zeilen</li>
     * </ul>
     */
    public void loadAll() {
        loadPersons();
        loadHistory();
        loadUsers();
    }

    /**
     * Speichert alle Personen inkl. Übungen in die Datei {@link #PERSON_FILE}.
     * <p>
     * Dafür wird jede Person aus der Personenliste vom {@link Manager} gelesen und
     * mit {@link #personToString(Person)} in eine speicherbare Textzeile umgewandelt.
     * Jede Person wird anschließend in eine eigene Zeile der Datei geschrieben.
     */
    private void savePersons() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PERSON_FILE))) {
            MyList<Person> persons = Manager.getInstance().getPersons();
            for (int i = 0; i < persons.getSize(); i++) {
                writer.write(personToString(persons.get(i)));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lädt alle gespeicherten Personen und Übungen aus der Datei
     * {@link #PERSON_FILE}.
     * <p>
     * Die Datei wird zeilenweise gelesen. Jede Zeile wird mit
     * {@link #parsePerson(String)} in ein {@link Person}-Objekt umgewandelt.
     * Wenn das Parsen erfolgreich war, wird die Person zur Personenliste in
     * {@link Manager} hinzugefügt. Ungültige Zeilen werden ignoriert.
     * <p>
     * Beim ersten Programmstart kann die Datei noch fehlen. In diesem Fall wird
     * keine Warnung ausgegeben.
     */
    private void loadPersons() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PERSON_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Person person = parsePerson(line);
                if (person != null) {
                    Manager.getInstance().getPersons().add(person);
                }
            }
        } catch (IOException e) {
            // Keine Warnung beim ersten Start
        }
    }

    /**
     * Wandelt eine Person und ihre Übungen in einen String für die Speicherung um.
     * <p>
     * Die einzelnen Werte werden mit Semikolons getrennt gespeichert. Zuerst werden
     * ID, Name, Geburtsdatum und Reha-Grund gespeichert. Danach folgen optional die Übungen
     * mit Übungsname, Zielwiederholungen und erledigten Wiederholungen.
     *
     * @param person Die zu speichernde Person
     * @return String-Repräsentation der Person und ihrer Übungen
     */
    private String personToString(Person person) {
        StringBuilder sb = new StringBuilder();
        sb.append(person.getId()).append(";")
                .append(person.getName()).append(";")
                .append(person.getBirthDate()).append(";")
                .append(person.getCondition());

        MyList<Exercise> exercises = person.getExercises();
        for (int i = 0; i < exercises.getSize(); i++) {
            Exercise ex = exercises.get(i);
            sb.append(";").append(ex.getExerciseName())
                    .append(";").append(ex.getTargetReps())
                    .append(";").append(ex.getCompletedReps());
        }
        return sb.toString();
    }

    /**
     * Parst eine gespeicherte Zeile zurück in eine Person und ihre Übungen.
     * <p>
     * Die Zeile enthält zuerst die gespeicherte Personen-ID, danach Name,
     * Geburtsdatum und Reha-Grund. Anschließend können optional Übungen mit
     * Übungsname, Zielwiederholungen und erledigten Wiederholungen folgen.
     * <p>
     * Die geladene ID wird über {@link Person#registerExistingId(String)} registriert,
     * damit neue Personen nach dem Laden keine bereits vorhandene ID erhalten.
     *
     * @param line Zeile aus der Datei {@code persons.txt}
     * @return Person mit ihren Übungen oder {@code null} bei fehlerhaften Daten
     */
    private Person parsePerson(String line) {
        String[] parts = line.split(";");
        if (parts.length < 4) {
            return null;
        } else {
            try {
                String id = parts[0];
                String name = parts[1];
                LocalDate birthDate = LocalDate.parse(parts[2]);
                String condition = parts[3];

                Person.registerExistingId(id);
                Person person = new Person(id, name, birthDate, condition);

                for (int i = 4; i + 2 < parts.length; i += 3) {
                    String exName = parts[i];
                    int target = Integer.parseInt(parts[i + 1]);
                    int completed = Integer.parseInt(parts[i + 2]);

                    Exercise ex = new Exercise(exName, target);
                    ex.addReps(completed);
                    person.addExercise(ex);
                }
                return person;

            } catch (Exception e) {
                return null;
            }
        }
    }

    /**
     * Speichert alle Übungsprotokolle in die Datei {@link #LOG_FILE}.
     */
    private void saveHistory() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE))) {
            MyList<ExerciseLog> logs = Manager.getInstance().getHistory();
            for (int i = 0; i < logs.getSize(); i++) {
                ExerciseLog log = logs.get(i);

                writer.write(log.getPersonId() + ";" + log.getPersonName() + ";" + log.getExerciseName() +
                        ";" + log.getCompletedReps() + ";" + log.getDate());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lädt alle Übungsprotokolle aus der Datei {@link #LOG_FILE}.
     * <p>
     * Ungültige oder fehlerhafte Zeilen werden still ignoriert, damit das Programm beim Start
     * nicht abbricht, wenn die Datei leer oder beschädigt ist.
     */
    private void loadHistory() {
        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length != 5) {
                    continue;  // Ungültige Zeile ignorieren
                }

                try {
                    String personId = parts[0];
                    String personName = parts[1];
                    String exerciseName = parts[2];
                    int completed = Integer.parseInt(parts[3]);
                    LocalDate date = LocalDate.parse(parts[4]);

                    ExerciseLog log = new ExerciseLog(personId, personName, exerciseName, completed, date);
                    Manager.getInstance().getHistory().add(log);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            // Keine Warnung beim ersten Start
        }
    }

    /**
     * Speichert alle registrierten Benutzer in die Datei {@link #USERS_FILE}.
     * <p>
     * Jede Zeile enthält die Daten eines Benutzers im Format:
     * {@code Name;Alter;E-Mail;Passwort}.
     */
    private void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            MyList<User> users = User.getUsers();
            for (int i = 0; i < users.getSize(); i++) {
                User user = users.get(i);
                writer.write(user.getName() + ";" + user.getAge() + ";" + user.getEmail() + ";" + user.getPassword());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lädt alle Benutzer aus der Datei {@link #USERS_FILE}.
     * <p>
     * Jede Zeile wird auf das Format {@code Name;Alter;E-Mail;Passwort} überprüft.
     * Ungültige Zeilen werden ignoriert. Die Altersangabe wird auf Integer geparst.
     * Die geladenen Benutzer werden über {@link User#addUser(String, int, String, String)} registriert.
     */
    private void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length != 4) {
                    continue;
                }

                try {
                    String name = parts[0];
                    int age = Integer.parseInt(parts[1]);
                    String email = parts[2];
                    String password = parts[3];

                    User.addUser(name, age, email, password);

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            // Keine Warnung beim ersten Start
        }
    }
}
