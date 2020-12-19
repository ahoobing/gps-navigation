import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GPSNavigationTest {
    @Test
    public void distanceBetweenGPSPoints() {
        GPSNavigation tester = new GPSNavigation(); // MyClass is tested

        // assert statements
        assertEquals(970.1507221252807, tester.GetDistanceBetweenTwoGPSPoints(50.0501, 58.6301, -5.7001, -3.0600, gpsUnits.KILOMETERS), "distance between points must be 969.085959651954");
        assertEquals(549.6049877393127, tester.GetDistanceBetweenTwoGPSPoints(43.5501, 45.5801, -116.2601, -122.5801, gpsUnits.KILOMETERS), "distance between two points should be 549.0017837703296 ");

        assertEquals(523.2699120701126, tester.GetDistanceBetweenTwoGPSPoints(50.0501, 58.6301, -5.7001, -3.0600, gpsUnits.NAUTICAL_MILES), "distance between points must be 523.2647475157902");
        assertEquals(296.44028195704135, tester.GetDistanceBetweenTwoGPSPoints(43.5501, 45.5801, -116.2601, -122.5801, gpsUnits.NAUTICAL_MILES), "distance between two points should be 296.43735615927585 ");

        assertEquals(602.1688113436127, tester.GetDistanceBetweenTwoGPSPoints(50.0501, 58.6301, -5.7001, -3.0600, gpsUnits.MILES), "distance between points must be 602.1619118348943");
        assertEquals(341.13769605870044, tester.GetDistanceBetweenTwoGPSPoints(43.5501, 45.5801, -116.2601, -122.5801, gpsUnits.MILES), "distance between two points should be 341.13378738315345");


    }


    @Test
    public void bearingBetweenGPSPoints() {
        GPSNavigation tester = new GPSNavigation();

        assertEquals(9.105195076794075, tester.GetGPSBearing(50.0501, 58.6301, -5.7001, -3.0600), "Bearing should be: 9.105195076794132");
        assertEquals(296.46377791547945, tester.GetGPSBearing(43.5501, 45.5801, -116.2601, -122.5801), "Bearing should be: 296.4637779154792");
        assertEquals(89.96034503444099, tester.GetGPSBearing(39.2561, 39.088117, -112.414, -106.087544), "Bearing should be: 89.96034503444099 ");

    }


    @Test
    public void newGPSPointWithBearingAndDistance() {
        GPSNavigation tester = new GPSNavigation();

        GPSNavigation.GPSPoint expectedPoint = new GPSNavigation.GPSPoint(53.26673366495985, -1.8500555072465592);
        GPSNavigation.GPSPoint testPoint = tester.getNewGPSLocation(53.320556, -1.729722, 180, 10, gpsUnits.KILOMETERS);

        assertAll("Expected and Test should be the same", () -> assertEquals(expectedPoint.getLatitude(), testPoint.getLatitude()),
                () -> assertEquals(expectedPoint.getLongitude(), testPoint.getLongitude()));


        GPSNavigation.GPSPoint expectedPointMiles = new GPSNavigation.GPSPoint(53.2666750803232, -1.8501861758808835);
        GPSNavigation.GPSPoint testPointMiles = tester.getNewGPSLocation(53.320556, -1.729722, 180, 6.21371, gpsUnits.MILES);
        assertAll("Expected and Test should be the same", () -> assertEquals(expectedPointMiles.getLatitude(), testPointMiles.getLatitude()),
                () -> assertEquals(expectedPointMiles.getLongitude(), testPointMiles.getLongitude()));


        GPSNavigation.GPSPoint expectedPointNauticalMiles = new GPSNavigation.GPSPoint(53.266674994663376, -1.8501863669382295);
        GPSNavigation.GPSPoint testPointNauticalMiles = tester.getNewGPSLocation(53.320556, -1.729722, 180, 5.39957, gpsUnits.NAUTICAL_MILES);
        assertAll("Expected and Test should be the same", () -> assertEquals(expectedPointNauticalMiles.getLatitude(), testPointNauticalMiles.getLatitude()),
                () -> assertEquals(expectedPointNauticalMiles.getLongitude(), testPointNauticalMiles.getLongitude()));
    }


}