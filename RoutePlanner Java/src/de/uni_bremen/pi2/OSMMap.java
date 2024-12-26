package de.uni_bremen.pi2;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Die Klasse kümmert sich um das Einlesen und die Darstellung einer Karte
 * mit Knoten und Kanten. Die Daten stammen von OpenStreetMap (OSM). Sie
 * bietet Methoden zum Zeichnen der Karte und zum Finden des nächstgelegenen
 * Knotens zu einer gegebenen Position.
 */
class OSMMap {

    /** Die Tabelle aller Knoten, die die OSM-Knotennummern als Index verwendet. */
    private final java.util.Map<Integer, Node> nodes = new HashMap<>();

    /**
     * Konstruktor. Liest die Karte aus den Dateien "nodes.txt" und "edges.txt" ein.
     * @throws FileNotFoundException Wenn die Datei "nodes.txt" oder "edges.txt" nicht gefunden wird.
     * @throws IOException Wenn ein Fehler beim Lesen der Dateien auftritt.
     */
    OSMMap() throws FileNotFoundException, IOException {
        // Knoten aus "nodes.txt" einlesen
        try (final Scanner stream = new Scanner(new FileInputStream("nodes.txt"))) {
            while (stream.hasNext()) {
                final int id = stream.nextInt();
                final double x = stream.nextDouble();
                final double y = stream.nextDouble();
                nodes.put(id, new Node(id, x, y));
            }
        }

        // Kanten aus "edges.txt" einlesen und zwei gerichtete Kanten für jede Kante eintragen
        try (final Scanner stream = new Scanner(new FileInputStream("edges.txt"))) {
            while (stream.hasNext()) {
                final Node node1 = nodes.get(stream.nextInt());
                final Node node2 = nodes.get(stream.nextInt());
                final String typeName = stream.nextLine().substring(1); // Der Typ der Kante wird nicht verwendet
                final double costs = node1.distance(node2);
                node1.getEdges().add(new Edge(node2, costs));
                node2.getEdges().add(new Edge(node1, costs));
            }
        }
    }

    /** Zeichnet die Karte mit allen Knoten und Kanten. */
    void draw() {
        for (final Node node : nodes.values()) {
            for (final Edge edge : node.getEdges()) {
                node.draw(edge.getTarget(), Color.BLACK);
            }
        }
    }

    /**
     * Findet den nächstgelegenen Knoten zu den gegebenen Koordinaten.
     * @param x Die x-Koordinate.
     * @param y Die y-Koordinate.
     * @return Der nächstgelegene Knoten oder null, falls keiner gefunden wurde.
     */
    /**
     * Sucht den Knoten in der Karte, der am nächsten zu den gegebenen Koordinaten liegt.
     *
     * @param x Die x-Koordinate.
     * @param y Die y-Koordinate.
     * @return Der nächstgelegene Knoten oder null, wenn keiner gefunden wurde.
     */
    Node findClosestNode(double x, double y) {
        Node closestNode = null;
        double minDistance = Double.POSITIVE_INFINITY;

        // Erzeugt einen temporären Knoten für die Zielkoordinaten
        Node targetNode = new Node(0, x, y);

        // Durchläuft alle Knoten in der Karte und findet den am nächsten liegenden Knoten
        for (Node node : nodes.values()) {
            double distance = node.distance(targetNode);
            if (distance < minDistance) {
                minDistance = distance;
                closestNode = node;
            }
        }

        return closestNode;
    }

    /** Setzt alle Knoten zurück, indem ihre Vorgänger gelöscht und die Kosten auf unendlich gesetzt werden. */
    void reset() {
        for (Node node : nodes.values()) {
            node.reachedFromAtCosts(null, Double.POSITIVE_INFINITY);
        }
    }
}
