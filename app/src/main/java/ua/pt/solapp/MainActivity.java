package ua.pt.solapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();
    private ArrayList<Integer> globalIds = new ArrayList<>();
    private ArrayList<WeatherEntry> weatherEntries = new ArrayList<>();
    private int globalId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final CityViewModel cityViewModel = ViewModelProviders.of(this).get(CityViewModel.class);

        Spinner spinner = findViewById(R.id.spinner);
        final Spinner spinner2 = findViewById(R.id.spinner2);
        callRemoteCityAndCreateSimpleCityEntryObjects(cityViewModel, spinner, this);

        /**cityViewModel.getAllCities().observe(this, new Observer<List<CityEntry>>() {
            @Override
            public void onChanged(@Nullable List<CityEntry> cityEntries) {

            }
        });**/

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                //LiveData<Integer> ok = db.cityDao().getGlobalIdLocal(item);
                //idLocal = ok.getValue();
                //Log.w("TAG", ok.getValue().toString() + "");
                //String a = item + idLocal;
                int idLocal = globalIds.get(i);
                setGlobalId(idLocal);
                callRemoteWeatherAndCreateSimpleWeatherEntryObjects(cityViewModel, idLocal, MainActivity.this, spinner2);
                //Toast.makeText(MainActivity.this, idLocal+"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<WeatherEntry> entries = getWeatherEntries();
                TextView textView8 = findViewById(R.id.textView8);
                TextView textView9 = findViewById(R.id.textView9);
                TextView textView10 = findViewById(R.id.textView10);

                try{
                    textView8.setText(entries.get(i).getMinTemp()+" ºC");
                }catch (Exception e){System.out.print(e.getMessage());}

                try{
                    textView9.setText(entries.get(i).getMaxTemp()+" ºC");
                }catch (Exception e){System.out.print(e.getMessage());}

                try{
                    textView10.setText(entries.get(i).getProbPrecipitacao()+"");
                }catch (Exception e){System.out.print(e.getMessage());}

                getIcon(entries.get(i).getIdWeatherType());
                getdescription(entries.get(i).getIdWeatherType());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void callRemoteWeatherAndCreateSimpleWeatherEntryObjects(final CityViewModel cityViewModel, final int idLocal,
                                                                     final Context context, final Spinner spinner) {
        String url = "http://api.ipma.pt/open-data/forecast/meteorology/cities/daily/" + idLocal + ".json";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d(LOG_TAG, "got response");

                // Parse the JSON into a list of weather forecasts
                try {
                    Log.w("TAG", response.toString());
                    WeatherResponse parsedResponse = new OpenWeatherJsonParser().parse(response.toString());
                    Log.d(LOG_TAG, "JSON Parsing finished");

                    if (response != null && parsedResponse.getWeatherForecast().length != 0) {
                        Log.d(LOG_TAG, "JSON not null and has " + parsedResponse.getWeatherForecast().length
                                + " values");
                        Log.w(LOG_TAG, String.format("First value is %d and %s",
                                parsedResponse.getWeatherForecast()[0].getGlobalIdLocal(),
                                parsedResponse.getWeatherForecast()[0].getPredWindDir()));
                        WeatherEntry weatherEntry = null;
                        ArrayList<WeatherEntry> entries = new ArrayList<>();
                        ArrayList<String> dates = new ArrayList<>();
                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        for (int i = 0; i<parsedResponse.getWeatherForecast().length; i++){
                            weatherEntry= parsedResponse.getWeatherForecast()[i];
                            entries.add(weatherEntry);
                            dates.add(df.format(parsedResponse.getWeatherForecast()[i].getDate()));
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, dates);
                        spinner.setAdapter(adapter);

                        setWeatherEntries(entries);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley error", error.getMessage());

            }
        });
        queue.add(jsonObjectRequest);

    }

    private void getIcon(final int idWeatherType) {
        ImageView imageView = findViewById(R.id.imageView);
        switch (idWeatherType){
            case 1:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_wb_sunny_black_24dp)); break;
            case 2:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_partly_cloudy)); break;
            case 3:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_overcast)); break; //sunnyIntervals
            case 4:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_cloudy)); break;
            case 5:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_cloudy_cloudy)); break;
            case 6:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_showers)); break;
            case 7:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_showers_light)); break;
            case 8:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_showers_heavy)); break;
            case 9:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_rain)); break;
            case 10:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_showers_light)); break;
            case 11:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_rain)); break;
            case 12:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_showers)); break;
            case 13:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_showers_light)); break;
            case 14:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_showers_heavy)); break;
            case 15:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_showers_light)); break;
            case 16:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_mist)); break;
            case 17:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_mist)); break;
            case 18:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_snow)); break;
            case 19:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_thunder)); break;
            case 20:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_thunderstorm)); break;
            case 21:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_hail)); break;
            case 22:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_frost)); break;
            case 23:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_thunderstorm)); break;
            case 24:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_cloudy)); break;
            case 25:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_partly_cloudy)); break;
            case 26:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_mist)); break;
            case 27:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_cloudy)); break;
            default:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_info_outline_black_24dp)); break;
        }
    }

    private void getdescription(final int idWeatherType) {
        String url = "http://api.ipma.pt/open-data/weather-type-classe.json";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d(LOG_TAG, "got response");

                // Parse the JSON into a list of weather forecasts
                try {
                    ArrayList<String> locals = new ArrayList<>();
                    ArrayList<Integer> ids = new ArrayList<>();
                    WeatherTypeResponse parsedResponse = new OpenWeatherTypeJsonParser().parse(response.toString());
                    Log.d(LOG_TAG, "JSON Parsing finished");

                    if (response != null && parsedResponse.getWeatherForecast().length != 0) {
                        Log.d(LOG_TAG, "JSON not null and has " + parsedResponse.getWeatherForecast().length
                                + " values");
                        Log.w(LOG_TAG, String.format("First value is %d and %s",
                                parsedResponse.getWeatherForecast()[0].getIdWeatherType(),
                                parsedResponse.getWeatherForecast()[0].getInfo()));
                        WeatherTypeEntry weatherTypeEntry = null;
                        for (int i = 0; i<parsedResponse.getWeatherForecast().length; i++){
                            if (parsedResponse.getWeatherForecast()[i].getIdWeatherType()==idWeatherType){
                                weatherTypeEntry = parsedResponse.getWeatherForecast()[i];
                            }
                        }
                        TextView textView12 = findViewById(R.id.textView12);
                        textView12.setText(weatherTypeEntry.getInfo());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley error", error.getMessage());

            }
        });
        queue.add(jsonObjectRequest);

    }

    private void callRemoteCityAndCreateSimpleCityEntryObjects(final CityViewModel cityViewModel,final Spinner spinner, final Context context) {
        String url = "http://api.ipma.pt/open-data/distrits-islands.json";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(LOG_TAG, "got response");

                        // Parse the JSON into a list of weather forecasts
                        try {
                            ArrayList<String> locals = new ArrayList<>();
                            ArrayList<Integer> ids = new ArrayList<>();
                            CityResponse parsedResponse = new OpenCityJsonParser().parse(response.toString());
                            Log.d(LOG_TAG, "JSON Parsing finished");

                            if (response != null && parsedResponse.getCityForecast().length != 0) {
                                Log.d(LOG_TAG, "JSON not null and has " + parsedResponse.getCityForecast().length
                                        + " values");
                                Log.w(LOG_TAG, String.format("First value is %d and %s",
                                        parsedResponse.getCityForecast()[0].getGlobalIdLocal(),
                                        parsedResponse.getCityForecast()[0].getLocal()));

                                for (int i = 0; i<parsedResponse.getCityForecast().length; i++){
                                    locals.add(parsedResponse.getCityForecast()[i].getLocal());
                                    ids.add(parsedResponse.getCityForecast()[i].getGlobalIdLocal());
                                    if (cityViewModel.getAllCities().getValue() != null) {
                                        cityViewModel.insert(parsedResponse.getCityForecast()[i]);
                                    }
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, locals);
                                spinner.setAdapter(adapter);
                                setGlobalIds(ids);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley error", error.getMessage());

                    }
                });
        queue.add(jsonObjectRequest);
        //return globalIds;
    }

    public void setWeatherEntries(ArrayList<WeatherEntry> weatherEntries){
        this.weatherEntries = weatherEntries;
    }

    public void setGlobalIds(ArrayList<Integer> globalIds){
        this.globalIds = globalIds;
    }

    public ArrayList<WeatherEntry> getWeatherEntries() {
        return weatherEntries;
    }

    public void setGlobalId(int globalId){
        this.globalId = globalId;
    }

    public void getObtainValue(int idLocal){
        Log.w("TAG", idLocal + "");
    }
}
