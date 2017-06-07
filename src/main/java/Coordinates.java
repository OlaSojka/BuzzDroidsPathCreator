/**
 * Created by Adam Piech on 2017-05-08.
 */

class Coordinates {

    private double latitude;
    private double longitude;

    Coordinates() {}

    Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    double getLatitude() {
        return latitude;
    }

    void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    double getLongitude() {
        return longitude;
    }

    void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
