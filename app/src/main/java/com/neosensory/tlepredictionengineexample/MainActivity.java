package com.neosensory.tlepredictionengineexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.neosensory.tlepredictionengine.TlePredictionEngine;

import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    TextView predictText = findViewById(R.id.textPrediction);

    // To validate, get the latest ISS TLE @ https://www.celestrak.com/NORAD/elements/stations.txt
    String ISSL1 = "1 25544U 98067A   20134.54218028  .00000832  00000-0  22964-4 0  9990";
    String ISSl2 = "2 25544  51.6441 159.9408 0000907 287.5644 232.4942 15.49368071226641";

    double[] latLonAlt = TlePredictionEngine.getSatellitePosition(ISSL1, ISSl2, true);

    double[] xyz = TlePredictionEngine.getSatellitePosition(ISSL1, ISSl2, false);

    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    cal.getTime();
    cal.add(Calendar.HOUR, 1);

    double[] latLonAlt1hrAhead =
        TlePredictionEngine.getSatellitePosition(ISSL1, ISSl2, true, cal.getTime());

    predictText.setText(
        "ISS Lat/Lon/Alt\r\n"
            + latLonAlt[0]
            + ","
            + latLonAlt[1]
            + ","
            + latLonAlt[2]
            + " km\r\n\r\nISS TEME x,y,z:\r\n"
            + xyz[0]
            + "km, "
            + xyz[1]
            + "km, "
            + xyz[2]
            + "km\r\n\r\nISS Lat/Lon/Alt +1 Hr:\r\n"
            + latLonAlt1hrAhead[0]
            + ","
            + latLonAlt1hrAhead[1]
            + ","
            + latLonAlt1hrAhead[2]
            + "km");
  }
}
