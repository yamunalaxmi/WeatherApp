package example.com.weatherapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.WeatherConfig;
import com.survivingwithandroid.weather.lib.exception.ApiKeyRequiredException;
import com.survivingwithandroid.weather.lib.exception.WeatherLibException;
import com.survivingwithandroid.weather.lib.exception.WeatherProviderInstantiationException;
import com.survivingwithandroid.weather.lib.model.City;
import com.survivingwithandroid.weather.lib.model.CurrentWeather;
import com.survivingwithandroid.weather.lib.provider.openweathermap.OpenweathermapProviderType;
import com.survivingwithandroid.weather.lib.request.WeatherRequest;

import java.util.List;


public class MainActivity extends ActionBarActivity {
    WeatherClient.ClientBuilder builder;
    WeatherConfig config;
    WeatherClient client;
    private City currentCity;

    private ListView cityListView;

    Toolbar toolbar;
    // Widget
    private TextView tempView;
    private ImageView weatherIcon;
    private TextView pressView;
    private TextView humView;
    private TextView windView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        tempView = (TextView) findViewById(R.id.temp);
        weatherIcon = (ImageView) findViewById(R.id.weather_icon);
        pressView = (TextView) findViewById(R.id.pressure);
        humView = (TextView) findViewById(R.id.hum);
        windView = (TextView) findViewById(R.id.wind);


        builder = new WeatherClient.ClientBuilder();
        config = new WeatherConfig();

        config.unitSystem = WeatherConfig.UNIT_SYSTEM.M;
        config.lang = "en"; // If you want to use english
        config.maxResult = 5; // Max number of cities retrieved
        config.numDays = 6; // Max num of days in the forecast

        try {
            initializeWeather();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        if (id == R.id.action_search) {
            // We show the dialog
            Dialog dialog = createDialog();
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }



    void initializeWeather(){


        try {
            // build a wether client

            // create the client to get the weather
            Log.i("WL","building the client");
            client = builder.attach(getApplicationContext())
                    .provider(new OpenweathermapProviderType() )
                    .httpClient(com.survivingwithandroid.weather.lib.client.okhttp.WeatherDefaultClient.class)
                    .config(config)
                    .build();


            client.getCurrentCondition(new WeatherRequest("2988507"),new WeatherClient.WeatherEventListener(){

                @Override
                public void onWeatherError(WeatherLibException e) {
                    System.out.println("WL getting the current weather condition");
                    Log.d("WL", "Weather Error - parsing data");
                    e.printStackTrace();
                }

                @Override
                public void onWeatherRetrieved(CurrentWeather currentWeather) {

                    //float currentTemp = currentWeather.weather.temperature.getTemp();
                   // Log.d("WL", "City [" + currentWeather.weather.location.getCity() + "] Current temp [" + currentTemp + "]");
                    float currentTemp = currentWeather.weather.temperature.getTemp();
                    System.out.println("WL getting the current weather condition");
                    Log.d("WL","city["+currentWeather.weather.location.getCity()+"] current temp["+currentTemp+"]");
                }

                @Override
                public void onConnectionError(Throwable throwable) {

                    Log.d("WL", "Connection Error ");
                    throwable.printStackTrace();
                }

            });
            //getWeather();


        } catch (WeatherProviderInstantiationException e) {
            e.printStackTrace();
        }catch (Throwable t){

            t.printStackTrace();
        }

    }


   public void getWeather() {


       try {


           client.getCurrentCondition(new WeatherRequest( currentCity.getId()), new WeatherClient.WeatherEventListener() {
                       @Override
                       public void onWeatherRetrieved(CurrentWeather currentWeather) {
                           // We have the current weather now
                           // Update subtitle toolbar

                           String temprature = String.format("%.0f",currentWeather.weather.temperature.getTemp());
                           String wthrcondition = currentWeather.weather.currentCondition.getDescr().toString();

                           String windspeed = String.valueOf(currentWeather.weather.wind.getSpeed());
                           String pressure = String.valueOf(currentWeather.weather.currentCondition.getPressure());
                           String humid = String.valueOf(currentWeather.weather.currentCondition.getHumidity());

                           tempView.setText(temprature);
                           toolbar.setSubtitle(wthrcondition);
                           windView.setText(windspeed);
                           pressView.setText(pressure);
                           humView.setText(humid);



                          // System.out.println("City:" + currentWeather.weather.currentCondition.);
                           System.out.println("weather condition:" + currentWeather.weather.currentCondition.getDescr());
                           System.out.println("Temp:" + String.format("%.0f", currentWeather.weather.temperature.getTemp()));
                           System.out.println("Pressure:" + String.valueOf(currentWeather.weather.currentCondition.getPressure()));
                           System.out.println("WindSpeed:" + String.valueOf(currentWeather.weather.wind.getSpeed()));
                           System.out.println("Humidity:" + String.valueOf(currentWeather.weather.currentCondition.getHumidity()));
                         //  weatherIcon.setImageResource(WeatherIconMapper.getWeatherResource(currentWeather.weather.currentCondition.getIcon(), currentWeather.weather.currentCondition.getWeatherId()));

                         // setToolbarColor(currentWeather.weather.temperature.getTemp());


                       }

               @Override
               public void onConnectionError(Throwable throwable) {
                   Log.d("WL","Connection error");
               }

               @Override
               public void onWeatherError(WeatherLibException e) {
                   Log.d("WL","Parsing error");
               }
           });
       } catch (ApiKeyRequiredException e) {
           e.printStackTrace();
       }
   }


    private Dialog createDialog(){

        AlertDialog.Builder  builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog,null);
        builder.setView(v);

        EditText et = (EditText)v.findViewById(R.id.ptnEdit);
        cityListView = (ListView)v.findViewById(R.id.cityList);
        cityListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentCity = (City)parent.getItemAtPosition(position);
            }
        });

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(count >3) {

                    client.searchCity(s.toString(), new WeatherClient.CityEventListener() {

                        @Override
                        public void onCityListRetrieved(List<City> cities) {

                            CityAdapter ca = new CityAdapter(MainActivity.this,cities);
                            cityListView.setAdapter(ca);
                        }

                        @Override
                        public void onWeatherError(WeatherLibException e) {

                        }

                        @Override
                        public void onConnectionError(Throwable throwable) {

                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            });

        builder.setPositiveButton("Accept",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                // We update toolbar
                toolbar.setTitle(currentCity.getName() + "," + currentCity.getCountry());
                // Start getting weather
                getWeather();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        return builder.create();
    }

}
