package com.jana.citysearch.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.jana.citysearch.R;
import com.jana.citysearch.model.City;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

@EBean
public class CityAdapter
        extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtTitle;

        public ViewHolder(final View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.cityName);
        }
    }

    private List<City> cityList = new ArrayList<>();

    public CityAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_city, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CityAdapter.ViewHolder viewHolder, int i) {
        City city = cityList.get(i);
        viewHolder.txtTitle.setText(city.getName());
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public void clearList() {
        cityList.clear();
        notifyDataSetChanged();
    }

    public void add(City city) {
        cityList.add(city);
        notifyDataSetChanged();
    }
}
