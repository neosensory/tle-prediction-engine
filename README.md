# tle-prediction-engine
A Java Implementation of SGP4 that is able to provide satellite predictions with geodetic (latitude, longitude, altitude) output. This library is built on top of GitHub user aholinch's [SGP4 multiplatform implementations](https://github.com/aholinch/sgp4/) with much credit going to David Vallado's original widely used code. The algorithms for converting to geodetic coordinates are taken from [stltracker.com](http://www.stltracker.com/resources/equations) and the algorithm for converting to Julian time is from [Bill Jefferys' Julian dates page](https://quasar.as.utexas.edu/BillInfo/JulianDatesG.html).

## Some quick background for the layperson
There are a bunch of strange terms being thrown around here. The point of this library is to help developers obtain latitude, longitude, and altitude date from a satellite's two line element (TLE) set. A TLE set is a standardized data format that contains useful information for predicting the state (position/velocity) of an orbiting object at some point in time past/present/future by providing it to a prediction model (SGP4 being one such example). The output from SGP4 is given in the true equator mean equinox (TEME) coordinate system, which is relative to the center of the earth and is inertial, which means the coordinate system does not rotate relative to the stars. Latitude and longitude are Earth fixed, which means we need to account for the Earth's rotation when converting from TEME to latitude and longitude. This library provides tools for doing this.

## Installation
This project takes the form of an Android Studio project, but Android is not required to use the module. You can obtain the library using [JitPack](https://jitpack.io/#neosensory/tle-prediction-engine) or grab the source.

## Usage
See the [JavaDocs](https://neosensory.github.io/tle-prediction-engine/) for a full list of all of the available classes and their methods. Class `TlePredictionEngine` provides a method `getSatellitePosition` which gives the predicted position of a satellite at a given point in time. It takes in a TLE, a boolean for spitting out either TEME or geodetic coordinates, and an optional Date parameter. If the the Date parameter is left out, the method will return the current position at the point in time the method is called.




