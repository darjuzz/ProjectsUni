package de.uni_bremen.pi2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse repräsentiert einen Knoten mit Koordinaten und ausgehenden Kanten.
 * Ein Knoten kann durch einen Punkt oder eine Strecke gezeichnet werden.
 */
class Node {

    /** Die id des Knotens laut OSM. */
    private final int id;

    /** Die x-Koordinate des Knotens laut OSM. */
    private final double x;

    /** Die y-Koordinate des Knotens laut OSM. */
    private final double y;

    /** Die Liste von ausgehenden Kanten. */
    private final List<Edge> edges = new ArrayList<>();

    /** Der Vorgänger auf dem Weg vom Startknoten zu diesem Knoten. */
    private Node from;

    /** Die Kosten vom Startknoten bis zu diesem Knoten. */
    private double costs;

    /**
     * Konstruktor für einen Knoten mit gegebener id und Koordinaten.
     * @param id Die id des Knotens laut OSM.
     * @param x Die x-Koordinate des Knotens laut OSM.
     * @param y Die y-Koordinate des Knotens laut OSM.
     */
    Node(final int id, final double x, final double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    /**
     * Liefert die id des Knotens.
     * @return Die id des Knotens.
     */
    int getId() {
        return id;
    }

    /**
     * Liefert die Liste der ausgehenden Kanten dieses Knotens.
     * @return Die Liste der ausgehenden Kanten.
     */
    List<Edge> getEdges() {
        return edges;
    }

    /**
     * Setzt den Vorgänger auf dem Weg vom Startknoten zu diesem Knoten sowie die Kosten dieses Wegs.
     * @param from Der Vorgängerknoten auf dem Weg.
     * @param costs Die Kosten vom Startknoten bis zu diesem Knoten.
     */
    void reachedFromAtCosts(final Node from, final double costs) {
        this.from = from;
        this.costs = costs;
    }

    /**
     * Liefert den Vorgängerknoten auf dem Weg vom Startknoten zu diesem Knoten.
     * @return Der Vorgängerknoten oder null, wenn keiner vorhanden ist.
     */
    Node getFrom() {
        return from;
    }

    /**
     * Liefert die Kosten vom Startknoten bis zu diesem Knoten.
     * @return Die Kosten vom Startknoten bis zu diesem Knoten.
     */
    double getCosts() {
        return costs;
    }

    /**
     * Berechnet den euklidischen Abstand zu einem anderen Knoten.
     * @param to Der andere Knoten.
     * @return Der euklidische Abstand zu dem anderen Knoten.
     */
    double distance(final Node to) {
        return Math.sqrt(Math.pow(x - to.x, 2) + Math.pow(y - to.y, 2));
    }

    /**
     * Zeichnet den Knoten als Punkt in der gegebenen Farbe.
     * @param color Die Farbe, in der der Knoten gezeichnet wird.
     */
    void draw(final Color color) {
        RoutePlanner.draw(x, y, color);
    }

    /**
     * Zeichnet eine Strecke zwischen diesem Knoten und einem anderen Knoten in der gegebenen Farbe.
     * @param to Der andere Knoten.
     * @param color Die Farbe, in der die Strecke gezeichnet wird.
     */
    void draw(final Node to, final Color color) {
        RoutePlanner.draw(x, y, to.x, to.y, color);
    }
}
