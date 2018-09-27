package velo.daank.com.velolijst;

import android.app.ListActivity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);

        //kijken of json bestand al opgeslagen is, indien niet opslaan met room
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "velo").allowMainThreadQueries().build();
        List<Station> stations = db.stationDao().getAll();

        Context ctx = getApplicationContext();

        if (stations.size() < 1) {
            // json downloaden en opslaan

            try {
                JSONArray arr = new JSONArray(loadJSONFromAsset(ctx));
                for (int i = 0 ; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    int id = obj.getInt("id");
                    String name = obj.getString("naam");
                    String lat = obj.getString("point_lat");
                    String lng = obj.getString("point_lng");
                    db.stationDao().insertAll(new Station(id, name, lat, lng));
                    Log.d("Station insert", name);
                }
            } catch (JSONException ex) {
                Log.d("JSONException", ex.toString());
            }
        }
        else {
            // list opvullen met data uit geheugen
            Log.d("Geheugen", "JSON al in geheugen");

            ArrayList<String> stationNames = new ArrayList<>();
            for (int x = 0; x < stations.size(); x++) {
                stationNames.add(stations.get(x).getName());
            }
            String[] values = stationNames.toArray(new String[stationNames.size()]);
            // toevoegen aan adapter
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, values);
            setListAdapter(adapter);
        }

    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("stations.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();

        Intent i = new Intent(this, Map.class);
        i.putExtra("station", item);
        // set the request code to any code you like,
        // you can identify the callback via this code
        startActivityForResult(i, 1);
    }
}
