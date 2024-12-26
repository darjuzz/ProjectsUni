package de.uni_bremen.pi2;

/**
 * Diese Klasse repräsentiert eine ausgehende Kante von einem Knoten zu einem Zielknoten.
 * Sie speichert den Zielknoten und die Kosten, um diese Kante zu nutzen.
 */
class Edge {

    /** Der Zielknoten dieser Kante. */
    private final Node target;

    /** Die Kosten, die beim Nutzen dieser Kante entstehen. */
    private final double costs;

    /**
     * Konstruktor für eine Kante mit gegebenem Zielknoten und Kosten.
     * @param target Der Zielknoten, zu dem die Kante führt.
     * @param costs Die Kosten, die beim Nutzen dieser Kante entstehen.
     */
    Edge(final Node target, final double costs) {
        this.target = target;
        this.costs = costs;
    }

    /**
     * Liefert den Zielknoten dieser Kante.
     * @return Der Zielknoten, zu dem die Kante führt.
     */
    Node getTarget() {
        return target;
    }

    /**
     * Liefert die Kosten, die beim Nutzen dieser Kante entstehen.
     * @return Die Kosten der Kante.
     */
    double getCosts() {
        return costs;
    }
}
