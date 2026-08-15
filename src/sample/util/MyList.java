package sample.util;

/**
 * MyList ist eine eigene generische Liste auf Basis einer einfach verketteten Liste.
 * <p>
 * Die Klasse kann Elemente eines beliebigen Typs {@code T} speichern. Dafür werden
 * intern einzelne Knoten verwendet, die jeweils einen Wert und eine Referenz auf den
 * nächsten Knoten enthalten.
 * <p>
 * Die Liste unterstützt grundlegende Operationen wie Hinzufügen, Entfernen, Auslesen,
 * Leeren und die Ausgabe als Text.
 *
 * @param <T> Typ der gespeicherten Elemente
 */
public class MyList<T> {

    /** Erster Knoten der Liste */
    private MyListNode head;

    /** Letzter Knoten der Liste */
    private MyListNode tail;

    /** Anzahl der gespeicherten Elemente */
    private int size;

    /**
     * Fügt ein neues Element am Ende der Liste hinzu.
     * <p>
     * Wenn die Liste leer ist, wird das neue Element gleichzeitig Kopf und Ende
     * der Liste. Andernfalls wird es hinter dem bisherigen letzten Element eingefügt.
     *
     * @param element Das hinzuzufügende Element
     */
    public void add(T element) {
        MyListNode node = new MyListNode(element);
        if (head == null) {
            head =  node;
            tail = head;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
    }

    /**
     * Fügt alle Elemente einer anderen MyList an diese Liste an.
     * <p>
     * Ist die übergebene Liste leer oder {@code null}, wird nichts verändert.
     *
     * @param otherList Die Liste, deren Elemente übernommen werden sollen
     */
    public void addAll(MyList<T> otherList) {
        if (otherList == null || otherList.head == null) {
            return;
        }
        MyListNode current = otherList.head;
        while (current != null) {
            add(current.value);
            current = current.next;
        }
    }

    /**
     * Gibt das Element an einer bestimmten Position zurück.
     * <p>
     * Die Liste wird dafür vom Anfang bis zum gewünschten Index durchlaufen.
     * Bei einem ungültigen Index wird {@code null} zurückgegeben.
     *
     * @param index Position des gewünschten Elements
     * @return Element an der angegebenen Position oder {@code null}
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        } else {
            MyListNode current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            return current.value;
        }
    }

    /**
     * Gibt die Anzahl der Elemente in der Liste zurück.
     * @return Anzahl der gespeicherten Elemente
     */
    public int getSize() {
        return size;
    }

    /**
     * Prüft, ob die Liste leer ist.
     * @return {@code true}, wenn keine Elemente gespeichert sind, sonst {@code false}
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Entfernt alle Elemente aus der Liste.
     * <p>
     * Danach zeigen Kopf und Ende auf {@code null} und die Größe wird auf 0 gesetzt.
     */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Entfernt alle Vorkommen eines bestimmten Elements aus der Liste.
     * <p>
     * Zuerst werden passende Elemente am Anfang der Liste entfernt. Danach wird
     * die restliche Liste durchlaufen und jedes weitere passende Element gelöscht.
     *
     * @param element Das zu entfernende Element
     */
    public void remove(T element) {
        // Handle head removal
        while (head != null && element.equals(head.value)) {
            head = head.next;
            size--;
        }
        if (head == null) {
            return;
        }

        MyListNode current = head;
        while (current.next != null) {
            if (element.equals(current.next.value)) {
                if (current.next == tail) {
                    tail = current;
                }

                current.next = current.next.next;
                size--;
            } else {
                current = current.next;
            }
        }
    }

    /**
     * Wandelt die Liste in einen String um.
     * <p>
     * Jedes Element wird in eine eigene Zeile geschrieben. Ist die Liste leer,
     * wird ein leerer String zurückgegeben.
     *
     * @return Textdarstellung der Liste
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "";
        } else {
            StringBuilder result = new StringBuilder();
            MyListNode current = head;
            while (current != null) {
                result.append(current.value.toString());
                current = current.next;
                if (current != null) {
                    result.append("\n");
                }
            }
            return result.toString();
        }
    }

    /**
     * MyListNode ist ein einzelner Knoten der einfach verketteten Liste.
     * <p>
     * Jeder Knoten speichert einen Wert und eine Referenz auf den nächsten Knoten.
     */
    private class MyListNode {

        /** Gespeicherter Wert des Knotens */
        T value;

        /** Referenz auf den nächsten Knoten */
        MyListNode next;

        /**
         * Erstellt einen neuen Knoten mit dem übergebenen Wert.
         * @param value Wert, der im Knoten gespeichert wird
         */
        public MyListNode(T value) {
            this.value = value;
            this.next = null;
        }
    }
}
