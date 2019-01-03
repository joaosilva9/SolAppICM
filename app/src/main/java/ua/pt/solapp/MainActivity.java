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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Integer> globalIds = new ArrayList<>();
    private ArrayList<Weather> weathers = new ArrayList<>();
    private int globalId = 1;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private ApiService apiService = retrofit.create(ApiService.class);
    private int code = 200;

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

                if (getCode()==200) {
                    ArrayList<Weather> entries = getWeathers();
                    TextView textView8 = findViewById(R.id.textView8);
                    TextView textView9 = findViewById(R.id.textView9);
                    TextView textView10 = findViewById(R.id.textView10);

                    try {
                        textView8.setText(entries.get(i).gettMin() + " ºC");
                    } catch (Exception e) {
                        System.out.print(e.getMessage());
                    }

                    try {
                        textView9.setText(entries.get(i).gettMax() + " ºC");
                    } catch (Exception e) {
                        System.out.print(e.getMessage());
                    }

                    try {
                        textView10.setText(entries.get(i).getPrecipitaProb() + "");
                    } catch (Exception e) {
                        System.out.print(e.getMessage());
                    }


                    getIcon(entries.get(i).getIdWeatherType());
                    callGetDescrition(entries.get(i).getIdWeatherType());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        

    }

    private void callRemoteWeatherAndCreateSimpleWeatherEntryObjects(final CityViewModel cityViewModel, final int idLocal,
                                                                     final Context context, final Spinner spinner){
        Call<WeatherInfo> weatherInfoCall = apiService.getWeather(idLocal);
        weatherInfoCall.enqueue(new Callback<WeatherInfo>() {
            @Override
            public void onResponse(Call<WeatherInfo> call, retrofit2.Response<WeatherInfo> response) {
                int codeResponse = response.code();
                setCode(codeResponse);
                if (codeResponse == 200) {
                    ArrayList<Weather> weathers = response.body().getData();
                    Weather weather = null;
                    ArrayList<String> dates = new ArrayList<>();
                    setWeathers(weathers);
                    for (int i = 0; i < weathers.size(); i++) {
                        weather = weathers.get(i);
                        dates.add(weather.getForecastDate());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, dates);
                    spinner.setAdapter(adapter);
                }
                else{
                    TextView textView8 = findViewById(R.id.textView8);
                    TextView textView9 = findViewById(R.id.textView9);
                    TextView textView10 = findViewById(R.id.textView10);
                    TextView textView12 = findViewById(R.id.textView12);
                    ImageView imageView = findViewById(R.id.imageView);

                    textView8.setText("Não disponível");
                    textView9.setText("Não disponível");
                    textView10.setText("Não disponível");
                    textView12.setText("Não disponível");
                    imageView.setImageDrawable(getDrawable(R.drawable.ic_info_outline_black_24dp));
                }
            }

            @Override
            public void onFailure(Call<WeatherInfo> call, Throwable t) {

            }
        });

    }

    private void getIcon(final int idWeatherType) {
        ImageView imageView = findViewById(R.id.imageView);
        switch (idWeatherType) {
            case 1:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_wb_sunny_black_24dp));
                break;
            case 2:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_partly_cloudy));
                break;
            case 3:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_overcast));
                break; //sunnyIntervals
            case 4:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_cloudy));
                break;
            case 5:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_cloudy_cloudy));
                break;
            case 6:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_showers));
                break;
            case 7:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_showers_light));
                break;
            case 8:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_showers_heavy));
                break;
            case 9:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_rain));
                break;
            case 10:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_showers_light));
                break;
            case 11:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_rain));
                break;
            case 12:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_showers));
                break;
            case 13:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_showers_light));
                break;
            case 14:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_showers_heavy));
                break;
            case 15:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_showers_light));
                break;
            case 16:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_mist));
                break;
            case 17:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_mist));
                break;
            case 18:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_snow));
                break;
            case 19:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_thunder));
                break;
            case 20:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_thunderstorm));
                break;
            case 21:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_hail));
                break;
            case 22:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_frost));
                break;
            case 23:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_thunderstorm));
                break;
            case 24:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_cloudy));
                break;
            case 25:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_partly_cloudy));
                break;
            case 26:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_mist));
                break;
            case 27:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_cloudy));
                break;
            default:
                imageView.setImageDrawable(getDrawable(R.drawable.ic_info_outline_black_24dp));
                break;
        }
    }

    private void callGetDescrition(final int idWeatherType){
        Call<TypeDataWeather> typeDataWeatherCall = apiService.getWeatherTypeInfo();
        typeDataWeatherCall.enqueue(new Callback<TypeDataWeather>() {
            @Override
            public void onResponse(Call<TypeDataWeather> call, retrofit2.Response<TypeDataWeather> response) {
                ArrayList<WeatherTypeEntry> weatherTypeEntries = response.body().getWeatherTypeEntries();
                WeatherTypeEntry weatherTypeEntry = null;
                for (int i = 0; i<weatherTypeEntries.size(); i++){
                    if (weatherTypeEntries.get(i).getIdWeatherType()==idWeatherType){
                        weatherTypeEntry = weatherTypeEntries.get(i);
                    }
                }
                TextView textView12 = findViewById(R.id.textView12);
                textView12.setText(weatherTypeEntry.getInfo());
            }

            @Override
            public void onFailure(Call<TypeDataWeather> call, Throwable t) {

            }
        });

    }

    private void callRemoteCityAndCreateSimpleCityEntryObjects(final CityViewModel cityViewModel,final Spinner spinner, final Context context){
        Call<DistrictInfo> ok = apiService.getDistrict();
        ok.enqueue(new Callback<DistrictInfo>() {
            @Override
            public void onResponse(Call<DistrictInfo> call, retrofit2.Response<DistrictInfo> response) {
                ArrayList<String> locals = new ArrayList<>();
                ArrayList<Integer> ids = new ArrayList<>();
                ArrayList<CityEntry> cityEntries;
                cityEntries = response.body().getData();
                for (int i = 0; i<cityEntries.size(); i++){
                    locals.add(cityEntries.get(i).getLocal());
                    ids.add(cityEntries.get(i).getGlobalIdLocal());
                    if (cityViewModel.getAllCities().getValue() != null) {
                        cityViewModel.insert(cityEntries.get(i));
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_single_choice, locals);
                spinner.setAdapter(adapter);
                setGlobalIds(ids);
            }

            @Override
            public void onFailure(Call<DistrictInfo> call, Throwable t) {

            }
        });
    }

    public void setWeathers(ArrayList<Weather> weathers){
        this.weathers = weathers;
    }

    public void setGlobalIds(ArrayList<Integer> globalIds){
        this.globalIds = globalIds;
    }

    public ArrayList<Weather> getWeathers() {
        return weathers;
    }

    public void setGlobalId(int globalId){
        this.globalId = globalId;
    }

    public void setCode(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
