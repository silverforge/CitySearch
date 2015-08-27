package com.jana.citysearch.presenters;

import android.text.TextUtils;

import com.jana.citysearch.activities.MainActivity;
import com.jana.citysearch.adapters.CityAdapter;
import com.jana.citysearch.model.City;
import com.jana.citysearch.repositories.CityRepository;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.List;

import rx.schedulers.Schedulers;

@EBean(scope = EBean.Scope.Singleton)
public class MainPresenter {

    private final static Integer BUFFER_SIZE = 100;

    private MainActivity mainActivity;
    private CityAdapter cityAdapter;

    private String lastText;
    private ArrayList<City> lastCities;

    @Bean
    public CityRepository cityRepository;

    public void attachView(MainActivity view) {
        mainActivity = view;
        cityAdapter = mainActivity.cityAdapter;
    }

    public void detachView() {
        cityAdapter = null;
        mainActivity = null;
    }

    public void cityNameChanged(String enteredText) {
        String text = enteredText.replace("\\n", "");
        if (TextUtils.isEmpty(text) && TextUtils.isEmpty(lastText))
            return;

        lastText = text;

        clearList();
        cityRepository
            .searchAsync(text)
            .subscribeOn(Schedulers.newThread())
            .finallyDo(this::saveLastState)
            .buffer(BUFFER_SIZE)
            .subscribe(cityList -> {
                mainActivity.runOnUiThread(() -> cityAdapter.addRange(cityList));
            });
    }

    public void displayCities() {
        clearList();

        if (lastCities != null) {
            restoreState(lastCities);
            return;
        }

        cityRepository
            .searchAsync("")
            .subscribeOn(Schedulers.newThread())
            .finallyDo(this::saveLastState)
            .buffer(BUFFER_SIZE)
            .subscribe(cityList -> {
                mainActivity.runOnUiThread(() -> cityAdapter.addRange(cityList));
            });
    }

    @UiThread
    protected void clearList() {
        cityAdapter.clearList();
    }

    @UiThread
    protected void saveLastState() {
        lastCities = new ArrayList<>(cityAdapter.getList());
    }

    @UiThread
    protected void restoreState(List<City> cities) {
        cityAdapter.addRange(cities);
    }
}
