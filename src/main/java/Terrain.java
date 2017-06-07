import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

class Terrain {

    private List<Boundary> borders;
    private List<Coordinates> boundaryPoints;

    Terrain() {
        this.boundaryPoints = new ArrayList();
        this.borders = new ArrayList<>();
    }

    void generateBorders() {
        borders.clear();
        for (int index = 0; index < boundaryPoints.size(); index++) {

            Coordinates startCoor = boundaryPoints.get(index);
            Coordinates endCoor = new Coordinates();
            if (index < this.boundaryPoints.size() - 1) {
                endCoor = this.boundaryPoints.get(index + 1);
            } else {
                endCoor = this.boundaryPoints.get(0);
            }

            double distance = getDistance(startCoor, endCoor);
            double factorA = (endCoor.getLatitude() - startCoor.getLatitude())
                    / (endCoor.getLongitude() - startCoor.getLongitude());
            double factorB = startCoor.getLatitude() - factorA * startCoor.getLongitude();

            borders.add(new Boundary(startCoor, endCoor, factorA, factorB, distance));
        }
    }

    int getHorizontalIndex() {
        for (int index = 0; index < borders.size(); index++) {
            Boundary border = borders.get(index);
            if (abs(atan(border.getFactorA())) < 10 * PI / 180)
                return index;
        }
        return -1;
    }

    double getDistance(Coordinates startPoint, Coordinates endPoint) {
        double factorX = endPoint.getLongitude() - startPoint.getLongitude();
        double factorY = endPoint.getLatitude() - startPoint.getLatitude();
        return sqrt(pow(factorX, 2) + pow(factorY, 2));
    }

    void addPointCoordinates(Coordinates coordinates) {
        this.boundaryPoints.add(coordinates);
    }

    List<Boundary> getBorders() {
        return borders;
    }

    void setBorders(List<Boundary> borders) {
        this.borders = borders;
    }

    List<Coordinates> getBoundaryPoints() {
        return boundaryPoints;
    }

    void setBoundaryPoints(List<Coordinates> boundaryPoints) {
        this.boundaryPoints = boundaryPoints;
    }

}
