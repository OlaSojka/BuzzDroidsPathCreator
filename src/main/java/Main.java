import Implementations.Coordinates;
import Implementations.Terrain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ola on 2017-06-07.
 */

public class Main {

    public static void main(String[] args) {
        Terrain t = new Terrain();
        t.setBoundaryPoints(getFlightArea());
        t.generateBorders();
        Path p = new Path(t, 10);
        List<Coordinates> path = p.calculatePath();

    }

    public static List<Coordinates> getFlightArea() {
        List<Coordinates> points = new ArrayList();
        points.add(new Coordinates( 50.088, 20.193));
        points.add(new Coordinates( 50.088, 20.20900));
        points.add(new Coordinates( 50.090, 20.20901));
        points.add(new Coordinates( 50.093, 20.207));
        points.add(new Coordinates( 50.093, 20.19200));
        points.add(new Coordinates( 50.092, 20.19201));
        points.add(new Coordinates( 50.091, 20.192));
        return points;
    }

}
