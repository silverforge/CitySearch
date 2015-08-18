package com.jana.citysearch.activities;

import android.graphics.drawable.Drawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.jana.citysearch.R;
import com.jana.citysearch.model.MainPresenterParameter;
import com.jana.citysearch.presenters.MainPresenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DrawableRes;
import org.androidannotations.annotations.res.StringRes;

import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.android.widget.WidgetObservable;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.menu_main)
public class MainActivity extends AppCompatActivity {

    @StringRes(R.string.citysearch)
    public String citySearchText;

    @DrawableRes(R.drawable.ic_menu)
    public Drawable navigationMenuIcon;

    @ViewById(R.id.drawer_layout)
    public DrawerLayout drawerLayout;

    @ViewById(R.id.recycler_view)
    public RecyclerView recyclerView;

    @ViewById(R.id.toolbar)
    public Toolbar toolbar;

    @ViewById(R.id.collapsingToolbarLayout)
    public CollapsingToolbarLayout collapsingToolbarLayout;

    @ViewById(R.id.nav_view)
    public NavigationView navigationView;

    @ViewById(R.id.cityName)
    public EditText cityName;


    @Bean
    public MainPresenter mainPresenter;


    @AfterViews
    public void mainActivityAfterViews() {
        initialize();
        mainPresenter.displayAllCities();

        WidgetObservable.text(cityName, false)
            .sample(TimeUnit.SECONDS.toSeconds(2), TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(textChangeEvent -> {
                mainPresenter.cityNameChanged(textChangeEvent.text().toString());
            });
    }

    @OptionsItem(android.R.id.home)
    public void homePressed() {
        mainPresenter.openDrawer();
    }

    // region Private zone (I wish it were at partial class)

    private void initialize() {
        try {
            MainPresenterParameter presenterParameter = MainPresenterParameter
                    .builder()
                    .citySearchText(citySearchText)
                    .collapsingToolbarLayout(collapsingToolbarLayout)
                    .drawerLayout(drawerLayout)
                    .navigationMenuIcon(navigationMenuIcon)
                    .navigationView(navigationView)
                    .recyclerView(recyclerView)
                    .toolbar(toolbar)
                    .build();

            mainPresenter.initializeView(presenterParameter);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Log.e(MainActivity.class.getName(), e.getMessage());
            Toast
                    .makeText(this, String.format("Internal error occured : %s", e.getMessage()), Toast.LENGTH_LONG)
                    .show();
            finish();
        }
    }

    // endregion
}
