package com.neosensory.tlepredictionengine;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TlePredictionEngine {
    /**
     * Get the current position of a satellite based on TLE at the time the method is called.
     * @param tleLine1 String of te first TLE line
     * @param tleLine2 String of the second TLE line
     * @param useGeodetic boolean. If true, return coordinates in geodetic latitude (degrees), longitude (degrees), and altitude (km). If false, the method will return the TEME coordinates (x,y,z) in km.
     * @return coordinates of the satellite at the current time in either geodetic or TEME
     */
    public static double[] getSatellitePosition(String tleLine1, String tleLine2, Boolean useGeodetic){
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.getTime();
        Tle satelliteTle = new Tle(tleLine1,tleLine2);
        Date currentTime = cal.getTime();
        double rv[][] = satelliteTle.getRV(currentTime);
        // r is satellite position in km using TEME (from center of Earth)
        double[] positions = {rv[0][0], rv[0][1], rv[0][2]}; // x,y,z
        if(!useGeodetic){
            // if we don't need to convert to geodetic output, just supply the TEME positions
            return positions;
        }else{
            double[] latLonAlt =  TemeGeodeticConverter.getLatLonAlt(positions[0],positions[1],positions[2], currentTime);
            return latLonAlt;
        }
    }

    /**
     * Get the  position of a satellite based on TLE at the time specified
     * @param tleLine1 String of te first TLE line
     * @param tleLine2 String of the second TLE line
     * @param useGeodetic boolean. If true, return coordinates in geodetic latitude (degrees), longitude (degrees), and altitude (km). If false, the method will return the TEME coordinates (x,y,z) in km.
     * @param date Date for getting the satellite position at a given point in time.
     * @return coordinates of the satellite at the given point in time in either geodetic or TEME
     */
    public static double[] getSatellitePosition(String tleLine1, String tleLine2, Boolean useGeodetic, Date date){
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(date);
        Date currentTime = cal.getTime();
        Tle satelliteTle = new Tle(tleLine1,tleLine2);
        double rv[][] = satelliteTle.getRV(currentTime);
        // r is satellite position in km using TEME (from center of Earth)
        double[] positions = {rv[0][0], rv[0][1], rv[0][2]}; // x,y,z
        if(!useGeodetic){
            // if we don't need to convert to geodetic output, just supply the TEME positions
            return positions;
        }else{
            double[] latLonAlt =  TemeGeodeticConverter.getLatLonAlt(positions[0],positions[1],positions[2], currentTime);
            return latLonAlt;
        }
    }
}
