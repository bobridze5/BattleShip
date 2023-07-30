import java.awt.*;
import java.util.ArrayList;

public class Ship {
    private final Color color = Color.GREEN;
    private final int segments;
    private final boolean orientation;
    private final ArrayList<Marker> markers;


    public Ship(int segments, boolean orientation, ArrayList<Marker> markers) {
        this.markers = markers;
        this.segments = segments;
        this.orientation = orientation;
    }

    public boolean getOrientation() {
        return orientation;
    }

    /**
     * Отрисовка кораблей в зависимости от ориентации корабля.
     * Если @Orientation == false --> Horizontal иначе Vertical
     */
    public void setVisibleForShip(boolean isVisible) {
            for (Marker value : markers) {
                value.setVisible(!isVisible);
                //value.paint(g);
            }

    }

}
