package sample.model;

import sample.util.MyList;

/**
 * User stellt einen Benutzer des Programms dar.
 * <p>
 * Ein Benutzer besitzt einen Namen, ein Alter, eine E-Mail-Adresse und ein Passwort.
 * Die Benutzer werden in einer statischen Liste gespeichert, sodass sie zentral über
 * die Klasse {@code User} verwaltet werden können.
 * <p>
 * Die Klasse stellt Methoden für Registrierung, E-Mail-Prüfung und Anmeldung bereit.
 */
public class User {

    private final String name;
    private final int age;
    private final String email;
    private final String password;

    /** Statische Liste aller registrierten Benutzer */
    private static final MyList<User> USERS = new MyList<>();

    /**
     * Erstellt einen neuen Benutzer mit Name, Alter, E-Mail und Passwort.
     *
     * @param name Name des Benutzers
     * @param age Alter des Benutzers
     * @param email E-Mail-Adresse des Benutzers
     * @param password Passwort des Benutzers
     */
    public User(String name, int age, String email, String password) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.password = password;
    }

    /**
     * Erstellt einen neuen Benutzer und fügt ihn zur Benutzerliste hinzu.
     *
     * @param name Name des Benutzers
     * @param age Alter des Benutzers
     * @param email E-Mail-Adresse des Benutzers
     * @param password Passwort des Benutzers
     */
    public static void addUser(String name, int age, String email, String password) {
        User user = new User(name, age, email, password);
        USERS.add(user);
    }

    /**
     * Prüft, ob eine E-Mail-Adresse bereits registriert ist.
     * <p>
     * Der Vergleich erfolgt ohne Beachtung von Groß- und Kleinschreibung.
     *
     * @param email Zu prüfende E-Mail-Adresse
     * @return {@code true}, wenn die E-Mail bereits existiert, sonst {@code false}
     */
    public static boolean isEmailRegistered(String email) {
        for (int i = 0; i < USERS.getSize(); i++) {
            if (USERS.get(i).getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Prüft die Anmeldedaten eines Benutzers.
     * <p>
     * Wenn E-Mail und Passwort zu einem gespeicherten Benutzer passen, wird dieser
     * Benutzer zurückgegeben. Wenn keine passenden Daten gefunden werden, wird
     * {@code null} zurückgegeben.
     *
     * @param email E-Mail-Adresse des Benutzers
     * @param password Passwort des Benutzers
     * @return Angemeldeter Benutzer oder {@code null}
     */
    public static User authenticate(String email, String password) {
        for (int i = 0; i < USERS.getSize(); i++) {
            User user = USERS.get(i);

            if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Gibt den Namen des Benutzers zurück.
     * @return Name des Benutzers
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt das Alter des Benutzers zurück.
     * @return Alter des Benutzers
     */
    public int getAge() {
        return age;
    }

    /**
     * Gibt die E-Mail-Adresse des Benutzers zurück.
     * @return E-Mail-Adresse des Benutzers
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gibt das Passwort des Benutzers zurück.
     * @return Passwort des Benutzers
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gibt die Liste aller registrierten Benutzer zurück.
     * @return Liste aller Benutzer
     */
    public static MyList<User> getUsers() {
        return USERS;
    }
}
