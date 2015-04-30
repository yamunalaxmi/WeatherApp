package example.com.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.survivingwithandroid.weather.lib.model.City;

import java.util.List;

/**
 * Created by Siddhartha on 4/29/2015.
 */
public class CityAdapter extends ArrayAdapter<City> {



    private List<City> cityList;
    private Context ctx;

    CityAdapter(Context ctx ,List<City>  cityList){
        super(ctx, R.layout.city_row);
        this.cityList=cityList;
        this.ctx=ctx;
    }



    @Override
    public City getItem(int position) {
        if(cityList!=null){
            return cityList.get(position);
        }

        return null;
    }

    @Override
    public int getCount() {

        if(cityList==null){
            return 0;
        }
        return cityList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null){

            LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.city_row, null,false);
            TextView tv = (TextView)view.findViewById(R.id.descrCity);

            tv.setText(cityList.get(position).getName()+":"+cityList.get(position).getCountry());
        }

        return view;
    }


    @Override
    public long getItemId(int position) {
        if(cityList== null){
            return -1;
        }

        return cityList.get(position).hashCode();
    }
}
