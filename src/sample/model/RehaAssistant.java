package sample.model;

import sample.util.MyList;

/**
 * RehaAssistant erstellt automatische Übungsvorschläge für eine Person.
 * <p>
 * Dafür wird der Reha-Grund der Person ausgewertet und einer einfachen Kategorie
 * zugeordnet. Anschließend werden passende Übungen mit sinnvollen Wiederholungszahlen
 * vorgeschlagen.
 * <p>
 * Die vorgeschlagenen Übungen werden nicht automatisch dauerhaft gespeichert. Sie
 * werden zuerst an den Controller zurückgegeben und können dort nach Bestätigung
 * zur Person hinzugefügt werden.
 */
public class RehaAssistant {

    /**
     * Erstellt passende Übungsvorschläge für eine Person.
     * <p>
     * Die Methode erkennt zuerst über {@link #detectCategory(String)} die passende
     * Kategorie des Reha-Grundes. Danach werden über eine switch-Anweisung passende
     * Übungen erzeugt und in einer Liste zurückgegeben.
     *
     * @param person Person, für die Übungen vorgeschlagen werden sollen
     * @return Liste mit vorgeschlagenen Übungen
     */
    public static MyList<Exercise> suggestExercises(Person person) {
        MyList<Exercise> suggestions = new MyList<>();
        String category = detectCategory(person.getCondition());

        switch (category) {
            case "ARM":
                suggestions.add(new Exercise("Armstrecken", 12));
                suggestions.add(new Exercise("Armkreisen", 15));
                suggestions.add(new Exercise("Wanddrücken", 10));
                break;

            case "SCHULTER":
                suggestions.add(new Exercise("Schulterkreisen", 15));
                suggestions.add(new Exercise("Pendeln des Arms", 12));
                suggestions.add(new Exercise("Wandkrabbeln", 10));
                break;

            case "KNIE":
                suggestions.add(new Exercise("Kniebeugen", 10));
                suggestions.add(new Exercise("Beinheben", 12));
                suggestions.add(new Exercise("Fersenheben", 15));
                break;

            case "RUECKEN":
                suggestions.add(new Exercise("Rumpfbeugen", 10));
                suggestions.add(new Exercise("Katzenbuckel", 12));
                suggestions.add(new Exercise("Beckenheben", 12));
                break;

            case "FRAKTUR":
                suggestions.add(new Exercise("Lockerungsübung", 10));
                suggestions.add(new Exercise("Leichte Dehnung", 10));
                suggestions.add(new Exercise("Kontrollierte Bewegung", 8));
                break;

            case "ARTHROSE":
                suggestions.add(new Exercise("Gelenkmobilisation", 12));
                suggestions.add(new Exercise("Leichte Kniebeugen", 8));
                suggestions.add(new Exercise("Dehnübung", 10));
                break;

            default:
                suggestions.add(new Exercise("Lockerungsübung", 10));
                suggestions.add(new Exercise("Dehnübung", 10));
                suggestions.add(new Exercise("Stabilisationsübung", 10));
                break;
        }
        return suggestions;
    }

    /**
     * Erkennt aus dem Reha-Grund eine einfache Kategorie.
     * <p>
     * Dafür wird geprüft, ob bestimmte Schlüsselwörter im Reha-Grund enthalten sind.
     * Wird kein passendes Schlüsselwort gefunden, wird die Kategorie {@code ALLGEMEIN}
     * zurückgegeben.
     *
     * @param condition Reha-Grund der Person
     * @return Erkannte Kategorie des Reha-Grundes
     */
    private static String detectCategory(String condition) {
        String text = condition.toLowerCase();

        if (text.contains("arm")) {
            return "ARM";
        }

        if (text.contains("schulter")) {
            return "SCHULTER";
        }

        if (text.contains("knie") || text.contains("kreuzband")) {
            return "KNIE";
        }

        if (text.contains("rücken") || text.contains("ruecken")) {
            return "RUECKEN";
        }

        if (text.contains("fraktur") || text.contains("gebrochen")) {
            return "FRAKTUR";
        }

        if (text.contains("arthrose") || text.contains("arthose")) {
            return "ARTHROSE";
        }

        return "ALLGEMEIN";
    }
}
