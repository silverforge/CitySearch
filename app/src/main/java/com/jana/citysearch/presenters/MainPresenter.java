package com.jana.citysearch.presenters;

import com.jana.citysearch.activities.MainActivity;
import com.jana.citysearch.adapters.CityAdapter;
import com.jana.citysearch.repositories.CityRepository;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

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
            .subscribe(city -> {
                mainActivity.runOnUiThread(() -> cityAdapter.add(city));
            });
    }

    public void displayAllCities() {
        cityAdapter.clearList();
        cityRepository
            .searchAsync("")
			.subscribeOn(Schedulers.newThread())
            .subscribe(city -> {
                mainActivity.runOnUiThread(() -> cityAdapter.add(city));
            });
    }
}
