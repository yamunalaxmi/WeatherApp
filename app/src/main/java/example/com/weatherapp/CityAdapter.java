package example.com.weatherapp;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.survivingwithandroid.weather.lib.model.City;

import java.util.List;

/**
 * Created by Siddhartha on 4/29/2015.
 */
public class CityAdapter extends ArrayAdapter<City> {

    @Override
    public City getItem(int position) {
        return super.getItem(position);
    }



    public CityAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public int getPosition(City item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
