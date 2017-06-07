package Interfaces;

import Implementations.Coordinates;

import java.util.List;

/**
 * Created by Ola on 2017-06-07.
 */
public interface ITerrain {
    void generateBorders();
    void setBoundaryPoints(List<Coordinates> boundaryPoints);
}
