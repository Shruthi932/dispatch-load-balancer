
package com.freightfox.dispatch.util;

public class DistanceCalculator {

    private static final double EARTH_RADIUS_KM = 6371.0;

    public static double calculateDistance(
            double latitude1,
            double longitude1,
            double latitude2,
            double longitude2) {

        double lat1 = Math.toRadians(latitude1);
        double lon1 = Math.toRadians(longitude1);

        double lat2 = Math.toRadians(latitude2);
        double lon2 = Math.toRadians(longitude2);

        double latitudeDifference = lat2 - lat1;
        double longitudeDifference = lon2 - lon1;

        double a = Math.pow(Math.sin(latitudeDifference / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(longitudeDifference / 2), 2);

        double c = 2 * Math.atan2(
                Math.sqrt(a),
                Math.sqrt(1 - a)
        );

        return EARTH_RADIUS_KM * c;
    }
}