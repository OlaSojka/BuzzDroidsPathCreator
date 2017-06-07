package Implementations;
import java.io.PrintWriter;
import static java.lang.Math.*;
import static java.lang.System.out;
import static jdk.nashorn.internal.objects.Global.Infinity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Path implements IPath {

    private List<Coordinates> path;
    private int width;
    private Terrain t;
    private double widthInMinutesLat;
    private boolean fieldEnd = false;

    public Path(Terrain t, int w) {
        this.t = t;
        this.width = w;
        this.path = new ArrayList<>();
        getWidthInMinutes();
    }

    private void getWidthInMinutes() {
        this.widthInMinutesLat = (width * Constants.MINUTE) / Constants.MINUTE_METERS;
    }

    private Coordinates fly(Coordinates start, Boundary moveAlong, Boundary nextBorder, boolean vertically, boolean left, double threshold) {
        int i = 0;

        Coordinates end = new Coordinates();
        end.setLatitude(start.getLatitude());
        end.setLongitude(start.getLongitude());

        double xIntersect = 0;
        double yIntersect = 0;
        if((moveAlong.getFactorA() != Infinity && vertically) || (nextBorder.getFactorA() != Infinity && !vertically)) {
            xIntersect = (moveAlong.getFactorB() - nextBorder.getFactorB()) / (nextBorder.getFactorA() - moveAlong.getFactorA());
            yIntersect = moveAlong.getFactorA() * xIntersect + moveAlong.getFactorB();
        }
        else if (moveAlong.getFactorA() == Infinity){
            xIntersect = start.getLongitude();
            yIntersect = nextBorder.getFactorA() * xIntersect + nextBorder.getFactorB();
        }
        else if (nextBorder.getFactorA() == Infinity) {
            xIntersect = start.getLongitude();
            yIntersect = moveAlong.getFactorA() * xIntersect + moveAlong.getFactorB();
        }
        out.println("*** " + vertically);
       // while (abs(end.getLongitude() - xIntersect) > threshold && abs(end.getLatitude() - yIntersect) > threshold) {
        while(i < 10) {
            double xStart = end.getLongitude();
            double yStart = end.getLatitude();
            double a = moveAlong.getFactorA();
            double b = moveAlong.getFactorB();

            double delta_a = (a * a + 1);
            double delta_b = (-2 * xStart + 2 * a * b - 2 * a * yStart);
            double delta_c = (xStart * xStart + b * b - 2 * b * yStart + yStart * yStart - 0.00000001 * Constants.MINUTE * Constants.MINUTE);

            double delta = delta_b * delta_b - 4 * delta_a * delta_c;

            double x = (-delta_b + sqrt(abs(delta))) / (2 * delta_a);
            double y = a * x + b;
            if(vertically) {
                if (y < yStart) {
                    x = (-delta_b - sqrt(abs(delta))) / (2 * delta_a);
                    y = a * x + b;
                }
            }else {
                if ((x < xStart && !left) || (x > xStart && left)) {
                    x = (-delta_b - sqrt(abs(delta))) / (2 * delta_a);
                    y = a * x + b;
                }
            }

            end.setLatitude(y);
            end.setLongitude(x);
            if (i % 20 == 0) {
                this.path.add(new Coordinates(y, x));
            }
            i++;
        }

        this.path.add(new Coordinates(end.getLatitude(), end.getLongitude()));
        return new Coordinates(end.getLatitude(), end.getLongitude());
    }

    private Coordinates flyVertically(Coordinates start, Boundary moveAlong, Boundary nextBorder) {
        return fly(start, moveAlong, nextBorder,true, false,0.00001);
    }

    private Coordinates flyHorizontally(Coordinates start, Boundary moveAlong, boolean left, Boundary nextBorder) {
        return fly(start, moveAlong, nextBorder,false, left,0.000001);
    }

    private Boundary getNextHorizontalBorder(Boundary previousBorder) {
        Boundary next = previousBorder;
        next.setFactorB(next.getFactorB() + this.widthInMinutesLat * sqrt(next.getFactorA() * next.getFactorA() + 1));
        return next;
    }

    private double getMatrixDeterminant(Coordinates p1, Coordinates p2, Coordinates p3) {
        double mult1 = p1.getLongitude() * p2.getLatitude();
        double mult2 = p2.getLongitude() * p3.getLatitude();
        double mult3 = p3.getLongitude() * p1.getLatitude();
        double mult4 = p3.getLongitude() * p2.getLatitude();
        double mult5 = p1.getLongitude() * p3.getLatitude();
        double mult6 = p2.getLongitude() * p1.getLatitude();
        return (mult1 + mult2 + mult3 - mult4 - mult5 - mult6);
    }

    private Boundary getVerticalBorder(Coordinates p, double a, double b, boolean left) {
        double x1 = p.getLongitude() - 1.5 * Constants.MINUTE;
        double y1 = a * x1 + b;
        Coordinates p1 = new Coordinates(y1, x1);

        double x2 = p.getLongitude() + 1.5 * Constants.MINUTE;
        double y2 = a * x2 + b;
        Coordinates p2 = new Coordinates(y2, x2);

        List<Boundary> borders = t.getBorders();

        for (int i = 0; i < borders.size(); i++) {
            Boundary bord = borders.get(i);

            if ((getMatrixDeterminant(p1, p2, bord.getStartPoint())) * (getMatrixDeterminant(p1, p2, bord.getEndPoint())) >= 0)
            {
                continue;
            } else if ((getMatrixDeterminant(p2, bord.getEndPoint(), p1)) * (getMatrixDeterminant(bord.getStartPoint(), bord.getEndPoint(), p2)) >= 0)
            {
                continue;
            } else {
                double xIntersect = 0;
                if(bord.getFactorA() != Infinity)
                    xIntersect = (bord.getFactorB() - b) / (a - bord.getFactorA());
                    if (left && abs(xIntersect - p.getLongitude()) > 0.00001) {
                        return bord;
                    } else if (!left && abs(xIntersect - p.getLongitude()) > 0.00001) {
                        return bord;
                    }
                else {
                    xIntersect = p.getLongitude();
                    if (left && abs(xIntersect - p.getLongitude()) == 0) {
                        return bord;
                    } else if (!left && abs(xIntersect - p.getLongitude()) == 0) {
                        return bord;
                    }
                }

            }
        }
        return new Boundary();
    }

    private void printVector(List<Coordinates> v) {
        try {
            Locale.setDefault(Locale.US);
            PrintWriter writer = new PrintWriter("example.txt", "UTF-8");

            for (int i = 0; i < v.size(); i++) {
                writer.printf("{lat: %.15f, lng: %.15f}", v.get(i).getLatitude(), v.get(i).getLongitude());
                if (i < v.size() - 1) {
                    writer.printf(",");
                }
                writer.printf("\n");
            }
            writer.close();
        } catch (Exception e) {

        }
    }

    public List<Coordinates> calculatePath() {
        Boundary horizontal_flight = t.getBorders().get(0);
        t.getBorders().remove(0);
        horizontal_flight.setFactorB(this.widthInMinutesLat / 2 * sqrt(horizontal_flight.getFactorA() * horizontal_flight.getFactorA() + 1) + horizontal_flight.getFactorB());

        Coordinates start = horizontal_flight.getStartPoint();
        start.setLatitude(start.getLatitude() + widthInMinutesLat / 2);
        this.path.add(start);

        Coordinates end = start;
        int i = 0;
        Boundary next_horizontal_border = horizontal_flight;
        while (!this.fieldEnd)
        {
            //nastêpna 'pionowa'
            Boundary next_border = getVerticalBorder(end, next_horizontal_border.getFactorA(), next_horizontal_border.getFactorB(), false);

            //lecimy w prawo
            end = flyHorizontally(end, next_horizontal_border, false, next_border);

            //nastêpna pozioma granica
            next_horizontal_border = getNextHorizontalBorder(next_horizontal_border);

            //lecimy w górê
            end = flyVertically(end, next_border, next_horizontal_border);

            //nastêpna 'pionowa'
            next_border = getVerticalBorder(end, next_horizontal_border.getFactorA(), next_horizontal_border.getFactorB(), true);

            //lecimy w lewo
            end = flyHorizontally(end, next_horizontal_border, true, next_border);
            next_horizontal_border = getNextHorizontalBorder(next_horizontal_border);

            //lecimy w górê
            end = flyVertically(end, next_border, next_horizontal_border);
            i++;

            if(i == 14)
                this.fieldEnd = true;
        }
        printVector(this.path);
        return this.path;
    }

    public List<Coordinates> getPath() {
        return this.path;
    }

    public void setPathWidth(int w) {
        this.width = w;
    }


}
