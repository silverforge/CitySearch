package com.jana.citysearch.presenters;

import android.support.annotation.Nullable;

import com.jana.citysearch.activities.MainActivity;
import com.jana.citysearch.adapters.CityAdapter;
import com.jana.citysearch.model.City;
import com.jana.citysearch.repositories.CityRepository;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

import rx.schedulers.Schedulers;

@EBean(scope = EBean.Scope.Singleton)
public class MainPresenter {

    private MainActivity mainActivity;
    private CityAdapter cityAdapter;

    @Bean
    public CityRepository cityRepository;

    public void attachView(MainActivity view) {
        mainActivity = view;
        cityAdapter = mainActivity.cityAdapter;
    }

    public void detachView() {
        mainActivity = null;
    }

    public void cityNameChanged(String text) {
        cityAdapter.clearList();
        cityRepository
            .searchAsync(text)
            .subscribeOn(Schedulers.newThread())
            .finallyDo(() -> mainActivity.runOnUiThread(this::saveLastState))
            .subscribe(city -> {
                mainActivity.runOnUiThread(() -> cityAdapter.add(city));
            });
    }

    public void displayCities(@Nullable List<City> cities) {
        cityAdapter.clearList();

        if (cities == null) {
            cityRepository
                    .searchAsync("")
                    .subscribeOn(Schedulers.newThread())
                    .finallyDo(() -> mainActivity.runOnUiThread(this::saveLastState))
                    .subscribe(city -> {
                        mainActivity.runOnUiThread(() -> cityAdapter.add(city));
                    });
        } else {
            mainActivity.runOnUiThread(() -> cityAdapter.addRange(cities));
        }
    }

    private void saveLastState() {
        mainActivity.lastCities = new ArrayList<>(cityAdapter.getList());
    }
}
