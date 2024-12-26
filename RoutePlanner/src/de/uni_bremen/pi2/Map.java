package de.uni_bremen.pi2;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Die Klasse liest die Karte aus Knoten und Kanten ein und
 * repräsentiert diese. Die Daten stammen ursprünglich aus
 * OpenStreetMap (OSM). Dabei werden für jede eingelesene Kante
 * zwei gerichtete Kanten in die Karte eingetragen. Die Klasse
 * stellt eine Methode zur Verfügung, um die Karte zu zeichnen,
 * sowie eine, die zu einem Punkt den dichtesten Knoten ermittelt.
 */
class Map
{
    /**
     * Konstruktor. Liest die Karte ein.
     * @throws FileNotFoundException Entweder die Datei "nodes.txt" oder die
     *         Datei "edges.txt" wurden nicht gefunden.
     * @throws IOException Ein Lesefehler ist aufgetreten.
     */
    Map() throws FileNotFoundException, IOException
    {
        // Lest hier die beiden Dateien nodes.txt und edges.txt ein
        // und erzeugt daraus eine Karte aus Node- und Edge-Objekten
        // Verbindungen sollen immer in beide Richtungen gehen, d.h.
        // ihr braucht zwei Edges pro Zeile aus der edges.txt.
    }

    /** Zeichnen der Karte. */
    void draw()
    {
        // Zeichnet hier alle Kanten der Karte. Hierzu sollte Node.draw benutzt werden.
        // Es können die Original-Koordinaten aus den Knoten benutzt werden. Diese werden
        // automatisch geeignet skaliert.
    }

    /**
     * Findet den dichtesten Knoten zu einer gegebenen Position.
     * @param x Die x-Koordinate.
     * @param y Die y-Koordinate.
     * @return Der Knoten, der der Position am nächsten ist. null,
     *         falls es einen solchen nicht gibt.
     */
    Node getClosest(final double x, final double y)
    {
        return null; // Ersetzen
    }

    /** Löschen aller Vorgängereinträge und Setzen aller Kosten auf unendlich. */
    void reset()
    {
        // Nutzt Node.reachedFromAtCosts für jeden Knoten.
    }
}
