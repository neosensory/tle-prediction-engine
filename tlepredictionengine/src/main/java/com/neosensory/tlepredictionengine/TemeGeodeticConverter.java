package com.neosensory.tlepredictionengine;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TemeGeodeticConverter {

  static final double E = 0.081819190842622; // Earth's eccentricity - WGS84 Model
  static final double A = 6378.137; // Earth's semi-major axis (km) - WGS84 Model

  /**
   * Convert a given TEME coordinate to a geodetic latitude/longitude/altitude coordinate
   *
   * @param x TEME coordinate in km
   * @param y TEME coordinate in km
   * @param z TEME coordinate in km
   * @param date Date for coordinate converstion. If using SGP4, this should be the same as the date
   *     for which the prediction is being made.
   * @return the equivalent latitude, longitude, and altitude
   */
  public static double[] getLatLonAlt(double x, double y, double z, Date date) {
    // algorithm credit: http://www.stltracker.com/resources/equations

    double[] latLonAlt = new double[3];

    // longitude calculation - rad
    double longitude = (Math.atan2(y, x) - getGmst(getJulianTime(date))) % (2 * Math.PI);

    // latitude calculation - rad
    double latitude = Math.atan2(z, Math.sqrt(x * x + y * y));
    double latitudeOld;
    do {
      latitudeOld = latitude;
      double c =
          A
              * E
              * E
              * Math.sin(latitudeOld)
              / Math.sqrt(1.0 - E * E * Math.sin(latitudeOld) * Math.sin(latitudeOld));
      latitude = Math.atan2((z + c), Math.sqrt(x * x + y * y));
    } while (Math.abs(latitude - latitudeOld) < 1.0e-10);

    // altitude calculation - km
    double altitude;
    if (Math.cos(latitude) < 0.0001) {
      altitude = z / Math.sin(latitude) - A * Math.sqrt(1 - Math.pow(E, 2));
    } else {
      altitude =
          Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) / Math.cos(latitude)
              - A / (Math.sqrt(1 - Math.pow(E, 2) * Math.pow(Math.sin(latitude), 2)));
    }

    latLonAlt[0] = Math.toDegrees(latitude);
    double uncorrectedLongitude = Math.toDegrees(longitude);
    double correctedLongitude;
    if (uncorrectedLongitude < -180) {
      correctedLongitude = (360 + Math.toDegrees(longitude)) % 360;
    } else {
      correctedLongitude = uncorrectedLongitude;
    }
    latLonAlt[1] = correctedLongitude;
    latLonAlt[2] = altitude;
    return latLonAlt;
  }

  /**
   * Obtain the Greenwich Mean Sidereal Time (GMST)--the angle that accounts for the Earth's
   * rotation needed for converting to an Earth fixed coordinate system
   *
   * @param julianTime the time using the Julian date
   * @return the Greenwich Mean Sidereal Time
   */
  public static double getGmst(double julianTime) {
    double UT = (julianTime + 0.5) % 1.0;
    double T = (julianTime - UT - 2451545.0) / 36525.0;
    double omega = 1.0 + 8640184.812866 / 3155760000.0;
    double gmst0 = 24110.548412 + T * (8640184.812866 + T * (0.093104 - T * 6.2E-6));
    double theta_GMST = ((gmst0 + 86400.0 * omega * UT) % 86400.0) * 2 * Math.PI / 86400.0;
    return theta_GMST; // alternatively use gstime(julianTime) in Sgp4;
  }

  /**
   * Convert to Julian time from the time of day and Gregorian calendar
   * @param date Gregorian calendar date
   * @return the corresponding Julian time
   */
  public static double getJulianTime(Date date) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    cal.setTime(date);
    int year = cal.get(Calendar.YEAR);
    int month =
        cal.get(Calendar.MONTH) + 1; // because Java is ridiculous and arbitrary indexes Jan @ 0
    int day = cal.get(Calendar.DAY_OF_MONTH);
    int hour = cal.get(Calendar.HOUR_OF_DAY);
    int minute = cal.get(Calendar.MINUTE);
    int sec = cal.get(Calendar.SECOND);

    // algorithm credit: https://quasar.as.utexas.edu/BillInfo/JulianDatesG.html
    if ((month == 1) || (month == 2)) {
      year = year - 1;
      month = month + 12;
    }
    int A = year / 100;
    int B = A >> 2;
    int C = 2 - A + B;
    int E = (int) ((double) 365.25 * (year + 4716));
    int F = (int) ((double) 30.6001 * (month + 1));
    double JD = C + day + E + F - 1524.5 + (sec + minute * 60 + hour * 60 * 60) / 86400.0f;
    return JD;
  }
}
