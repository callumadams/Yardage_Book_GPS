package greengrid.example.android.yardagebookgps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class GPSLogScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpslog_screen);

        TextView tvLat = findViewById(R.id.tvLatitude);

        GPSTracker gps = new GPSTracker(this);
        if (gps.canGetLocation()){
            tvLat.setText(String.valueOf(gps.getLatitude()));
        } else {
            tvLat.setText("Cant get Location");
        }
    }


}
