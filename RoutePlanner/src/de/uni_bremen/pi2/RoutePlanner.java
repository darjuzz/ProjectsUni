package de.uni_bremen.pi2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Diese Klasse repräsentiert das Hauptfenster der Routenplaner-Anwendung.
 * Sie zeigt eine Karte an und ermöglicht es Benutzern, den kürzesten Weg
 * zwischen zwei Knoten durch Klicken zu finden.
 */
class RoutePlanner extends JPanel {
    /** Hilfsklasse zum Zeichnen von Punkten und Linien auf der Karte. */
    private static class Drawable {
        /** Gibt an, ob es sich um eine Linie oder einen Punkt handelt. */
        final boolean isLine;

        /** Koordinaten. x2 und y2 werden für Punkte nicht verwendet. */
        final double x1, y1, x2, y2;

        /** Die Farbe für das Zeichnen. */
        final Color color;

        /**
         * Erzeugt einen Punkt.
         *
         * @param x     Die x-Koordinate des Punkts.
         * @param y     Die y-Koordinate des Punkts.
         * @param color Die Farbe für das Zeichnen.
         */
        Drawable(final double x, final double y, final Color color) {
            isLine = false;
            x1 = x;
            y1 = y;
            x2 = y2 = 0;
            this.color = color;
        }

        /**
         * Erzeugt eine Linie.
         *
         * @param x1    Die x-Koordinate des Startpunkts.
         * @param y1    Die y-Koordinate des Startpunkts.
         * @param x2    Die x-Koordinate des Endpunkts.
         * @param y2    Die y-Koordinate des Endpunkts.
         * @param color Die Farbe für das Zeichnen.
         */
        Drawable(final double x1, final double y1, final double x2, final double y2, final Color color) {
            isLine = true;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.color = color;
        }
    }

    /** Liste aller Zeichenanweisungen während eines "paint"-Aufrufs. */
    private static final List<Drawable> drawables = new ArrayList<>();

    /** Minimale x-Koordinate in den Daten. Erst nach "paint" gültig. */
    private double xMin;

    /** Minimale y-Koordinate in den Daten. Erst nach "paint" gültig. */
    private double yMin;

    /** x-Skalierungsfaktor für das Zeichnen der Daten. Erst nach "paint" gültig. */
    private double xScale;

    /** y-Skalierungsfaktor für das Zeichnen der Daten. Erst nach "paint" gültig. */
    private double yScale;

    /** Die Karte, die für die Routenplanung verwendet wird. */
    private final OSMMap map;

    /** Der Startknoten, der durch Mausklick ausgewählt wurde. */
    private Node start;

    /** Der Zielknoten, der durch Mausklick ausgewählt wurde. */
    private Node goal;

    /**
     * Konstruiert das Hauptfenster und die Zeichenfläche für den Routenplaner.
     *
     * @throws IOException Wenn das Lesen der Kartendaten fehlschlägt.
     */
    private RoutePlanner() throws IOException {
        // Verwendet den Punkt als Dezimaltrennzeichen beim Einlesen von Text
        Locale.setDefault(new Locale("C"));

        map = new OSMMap();

        final JFrame frame = new JFrame("Routenplaner"); // Erzeugt den Fensterrahmen
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this); // Fügt die Zeichenfläche hinzu
        setBackground(Color.white); // Setzt die Hintergrundfarbe auf weiß
        setPreferredSize(new Dimension(700, 700)); // Setzt die Größe der Zeichenfläche
        frame.pack(); // Passt das Fenster an die Zeichenfläche an
        frame.setVisible(true); // Zeigt das Fenster an

        // Setzt Start- und Zielpositionen per Mausklick
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(final MouseEvent event) {
                // Konvertiert Fensterkoordinaten in Kartenkoordinaten
                final double x = event.getX() / xScale + xMin;
                final double y = (getSize().height - 1 - event.getY()) / yScale + yMin;

                // Der aktuelle Zielknoten wird der neue Startpunkt
                start = goal;

                // Der nächstgelegene Knoten zum Klick wird das neue Ziel
                goal = map.findClosestNode(x, y);

                // Zeichnet die Karte neu
                repaint();
            }
        });
    }

    /**
     * Zeichnet einen Punkt auf die Zeichenfläche. Diese Methode kann nur während eines "paint"-Aufrufs verwendet werden.
     *
     * @param x     x-Koordinate des Punkts.
     * @param y     y-Koordinate des Punkts.
     * @param color Die Farbe für das Zeichnen.
     */
    static void draw(final double x, final double y, final Color color) {
        drawables.add(new Drawable(x, y, color));
    }

    /**
     * Zeichnet eine Linie auf die Zeichenfläche. Diese Methode kann nur während eines "paint"-Aufrufs verwendet werden.
     *
     * @param x1    x-Koordinate des Startpunkts.
     * @param y1    y-Koordinate des Startpunkts.
     * @param x2    x-Koordinate des Endpunkts.
     * @param y2    y-Koordinate des Endpunkts.
     * @param color Die Farbe für das Zeichnen.
     */
    static void draw(final double x1, final double y1, final double x2, final double y2, final Color color) {
        drawables.add(new Drawable(x1, y1, x2, y2, color));
    }

    /**
     * Zeichnet die Karte und die Route. Start- und Zielknoten werden gezeichnet, wenn bekannt.
     * Die Route wird nur gezeichnet, wenn sowohl Start- als auch Zielknoten bekannt sind.
     *
     * @param graphics Der Grafikkontext zum Zeichnen.
     */
    public void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);

        // Löscht vorherige Zeichnungen
        drawables.clear();

        // Zeichnet die Karte
        map.draw();

        // Wenn sowohl Start- als auch Zielknoten bekannt sind, berechnet und zeichnet die kürzeste Route
        if (start != null && goal != null) {
            map.reset();
            shortestPath(start, goal);
        }

        // Markiert ausgewählte Knoten
        if (start != null) {
            start.draw(Color.RED);
        }
        if (goal != null) {
            goal.draw(Color.RED);
        }

        // Ermittelt die Grenzen der zu zeichnenden Elemente
        xMin = Double.POSITIVE_INFINITY;
        yMin = Double.POSITIVE_INFINITY;
        double xMax = Double.NEGATIVE_INFINITY;
        double yMax = Double.NEGATIVE_INFINITY;
        for (final Drawable drawable : drawables) {
            xMin = Math.min(xMin, drawable.x1);
            xMax = Math.max(xMax, drawable.x1);
            yMin = Math.min(yMin, drawable.y1);
            yMax = Math.max(yMax, drawable.y1);
            if (drawable.isLine) {
                xMin = Math.min(xMin, drawable.x2);
                xMax = Math.max(xMax, drawable.x2);
                yMin = Math.min(yMin, drawable.y2);
                yMax = Math.max(yMax, drawable.y2);
            }
        }

        // Zeichnet alle Elemente mit der richtigen Skalierung
        xScale = xMax - xMin > 0 ? getSize().width / (xMax - xMin) : 1;
        yScale = yMax - yMin > 0 ? getSize().height / (yMax - yMin) : 1;
        for (final Drawable drawable : drawables) {
            graphics.setColor(drawable.color);
            if (drawable.isLine) {
                graphics.drawLine((int) ((drawable.x1 - xMin) * xScale),
                        (int) (getSize().height - 1 - (drawable.y1 - yMin) * yScale),
                        (int) ((drawable.x2 - xMin) * xScale),
                        (int) (getSize().height - 1 - (drawable.y2 - yMin) * yScale));
            } else {
                graphics.fillOval((int) ((drawable.x1 - xMin) * xScale - 2),
                        (int) (getSize().height - 1 - (drawable.y1 - yMin) * yScale - 2), 5, 5);
            }
        }
    }

    /**
     * Berechnet den kürzesten Weg zwischen zwei Knoten unter Verwendung des Dijkstra-Algorithmus.
     * Markiert die Route auf der Karte.
     *
     * @param goal Der Startknoten.
     * @param start   Der Zielknoten.
     */
    private void shortestPath(final Node start, final Node goal) {
        // Verwaltung der zu untersuchenden Knoten, sortiert nach den Kosten
        final TreeSet<Node> frontier = new TreeSet<>((a, b) -> {
            int compareCosts = Double.compare(a.getCosts(), b.getCosts());
            return compareCosts != 0 ? compareCosts : Integer.compare(a.getId(), b.getId());
        });

        // Setze die Kosten des Startknotens auf 0 und füge ihn zur Frontier hinzu
        start.reachedFromAtCosts(null, 0);
        frontier.add(start);

        while (!frontier.isEmpty()) {
            // Wähle den Knoten mit den aktuell geringsten Kosten aus der Frontier aus
            final Node current = frontier.pollFirst();

            // Überprüfe, ob der Zielknoten erreicht wurde
            if (current == goal) {
                reconstructAndDrawPath(current);
                break;
            } else {
                // Aktualisiere die Kosten und Vorgängerinformationen für alle Nachbarknoten
                for (final Edge edge : current.getEdges()) {
                    final Node neighbor = edge.getTarget();
                    final double newCosts = current.getCosts() + edge.getCosts();

                    if (neighbor.getCosts() > newCosts) {
                        // Aktualisiere die Kosten und Vorgängerinformationen für den Nachbarknoten
                        frontier.remove(neighbor);
                        neighbor.reachedFromAtCosts(current, newCosts);
                        frontier.add(neighbor);
                        current.draw(neighbor, Color.BLUE); // Visualisiere die Verbindung zum Nachbarknoten
                    }
                }
            }
        }
    }

    /**
     * Rekonstruiert den kürzesten Pfad vom Zielknoten zum Startknoten
     * und markiert die Verbindungen in roter Farbe.
     *
     * @param current Der Zielknoten, von dem aus der Pfad zurückverfolgt wird.
     */
    private void reconstructAndDrawPath(Node current) {
        // Rekonstruiert den Pfad rückwärts und markiert ihn in roter Farbe
        for (Node node = current; node.getFrom() != null; node = node.getFrom()) {
            node.draw(node.getFrom(), Color.RED);
        }
    }

    /**
     * Hauptprogramm, um die Routenplaner-Anwendung zu starten.
     *
     * @param args Ignorierte Befehlszeilenargumente.
     * @throws IOException Wenn das Lesen der Kartendaten fehlschlägt.
     */
    public static void main(final String[] args) throws IOException {
        new RoutePlanner();
    }
}
