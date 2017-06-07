class Boundary {

    private Coordinates startPoint;
    private Coordinates endPoint;

    private double factorA;
    private double factorB;
    private double length;

    Boundary() {

    }

    Boundary(Coordinates startPoint, Coordinates endPoint, double factorA, double factorB, double length) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.factorA = factorA;
        this.factorB = factorB;
        this.length = length;
    }

    Coordinates getStartPoint() {
        return startPoint;
    }

    void setStartPoint(Coordinates startPoint) {
        this.startPoint = startPoint;
    }

    Coordinates getEndPoint() {
        return endPoint;
    }

    void setEndPoint(Coordinates endPoint) {
        this.endPoint = endPoint;
    }

    double getFactorA() {
        return factorA;
    }

    void setFactorA(double factorA) {
        this.factorA = factorA;
    }

    double getFactorB() {
        return factorB;
    }

    void setFactorB(double factorB) {
        this.factorB = factorB;
    }

    double getLength() {
        return length;
    }

    void setLength(double length) {
        this.length = length;
    }

}
