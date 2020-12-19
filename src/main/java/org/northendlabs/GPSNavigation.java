/*
  Copyright 2020 Andy Hoobing

  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.northendlabs;

import java.lang.Math;






/**
 * This class has the methods to run different GPS navigation methods.
 */
public class GPSNavigation {

    /**
     * gpsUnits are the constants to allow different calculations based upon different units of distance.
     * All algorithms are based on descriptions in http://www.movable-type.co.uk/scripts/latlong.html
     */
    public enum gpsUnits {
        KILOMETERS,
        MILES,
        NAUTICAL_MILES
    }


    private static final double R_IN_KILOMETERS = 6378;
    private static final double R_IN_MILES = 3958.8;
    private static final double R_IN_NAUTICAL_MILES = 3440.1;


    /**
     * This class represents a GPS Point with latitude and longitude.
     */
    public static class GPSPoint {
        public GPSPoint(double lat, double lon) {
            latitude = lat;
            longitude = lon;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        private final double latitude;
        private final double longitude;
    }


    /**
     * This method returns the distance between two GPS points.
     * Algorithm based on: http://www.movable-type.co.uk/scripts/latlong.html
     *
     * @param lat1 Current latitude
     * @param lat2 Destination latitude
     * @param lon1 Current longitude
     * @param lon2 Destination longitude
     * @param unit gpsUnit enum
     * @return Distance between two GPS points in gpsUnit units (MILES, KILOMETERS, NAUTICAL_MILES)
     */
    public double GetDistanceBetweenTwoGPSPoints(double lat1,
                                                 double lat2, double lon1,
                                                 double lon2, gpsUnits unit) {

        double lat1Radians = Math.toRadians(lat1);
        double lat2Radians = Math.toRadians(lat2);
        double deltaLatInRadians = Math.toRadians(lat2 - lat1);
        double deltaLonInRadians = Math.toRadians(lon2 - lon1);

        double a;
        a = Math.sin(deltaLatInRadians / 2) * Math.sin(deltaLatInRadians / 2) +
                Math.cos(lat1Radians) * Math.cos(lat2Radians) *
                        Math.sin(deltaLonInRadians / 2) * Math.sin(deltaLonInRadians / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = 0;

        switch (unit) {
            case KILOMETERS:
                distance = c * R_IN_KILOMETERS;
                break;
            case NAUTICAL_MILES:
                distance = c * R_IN_NAUTICAL_MILES;
                break;
            case MILES:
                distance = c * R_IN_MILES;
                break;
        }

        return distance;
    }


    /**
     * Calculates the bearing in degrees from the one GPS point to another.
     *
     * @param lat1 Current latitude.
     * @param lat2 Destination latitude.
     * @param lon1 Current longitude.
     * @param lon2 Destination longitude.
     * @return Bearing in degress to the destination latitude.
     */
    public double GetGPSBearing(double lat1,
                                double lat2, double lon1,
                                double lon2) {


        double currentLatitudeRadians = Math.toRadians(lat1);
        double destinationLatitudeRadians = Math.toRadians(lat2);
        double currentLongitudeRadians = Math.toRadians(lon1);
        double destinationLongitudeRadians = Math.toRadians(lon2);

        double y;
        y = Math.sin(destinationLongitudeRadians - currentLongitudeRadians) * Math.cos(destinationLatitudeRadians);
        double x;
        x = Math.cos(currentLatitudeRadians) * Math.sin(destinationLatitudeRadians) -
                Math.sin(currentLatitudeRadians) * Math.cos(destinationLatitudeRadians) * Math.cos(destinationLongitudeRadians - currentLongitudeRadians);
        double bearing = Math.atan2(y, x);


        return (bearing * 180 / Math.PI + 360) % 360;

    }


    /**
     * Calculates the new GPS location based on current point plus distance traveled and a bearing.
     *
     * @param currentLat  Current latitude.
     * @param currentLong Current longitude.
     * @param newBearing  Bearing.
     * @param newDistance Distance traveled.
     * @param unit        gpsUnit for distance traveled.
     * @return GPSPoint of the new location.
     */
    public GPSPoint getNewGPSLocation(double currentLat, double currentLong, double newBearing, double newDistance, gpsUnits unit) {


        double lat = Math.toRadians(currentLat);
        double lon = Math.toRadians(currentLong);

        double localR = 0;

        switch (unit) {
            case MILES:
                localR = R_IN_MILES;
                break;
            case NAUTICAL_MILES:
                localR = R_IN_NAUTICAL_MILES;
                break;
            default:
                localR = R_IN_KILOMETERS;
        }

        double newLat = Math.asin(Math.sin(lat) * Math.cos(newDistance / localR)
                + Math.cos(lat) * Math.sin(newDistance / localR) * Math.cos(newBearing));

        double newLon = lon + Math.atan2(Math.sin(newBearing) * Math.sin(newDistance / localR) * Math.cos(lat),
                Math.cos(newDistance / localR) - Math.sin(lat) * Math.sin(newLat));


        return new GPSPoint(Math.toDegrees(newLat), Math.toDegrees(newLon));
    }


}
