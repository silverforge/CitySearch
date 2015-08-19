package com.jana.citysearch.activities;

import android.graphics.drawable.Drawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.jana.citysearch.R;
import com.jana.citysearch.adapters.CityAdapter;
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

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.menu_main)
public class MainActivity
        extends AppCompatActivity {

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

    @Bean
    public CityAdapter cityAdapter;

    
    @AfterViews
    public void mainActivityAfterViews() {
        initialize();
        mainPresenter.attachView(this);
        mainPresenter.displayAllCities();

        WidgetObservable.text(cityName, false)
            .sample(TimeUnit.SECONDS.toSeconds(2), TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(textChangeEvent -> {
                mainPresenter.cityNameChanged(textChangeEvent.text().toString());
            });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.detachView();
        mainPresenter = null;
    }

    @OptionsItem(android.R.id.home)
    public void homePressed() {
        openDrawer();
    }

    // region Private zone (I wish it were at partial class)

    private void initialize() {
        try {
            if (toolbar == null)
                throw new IllegalArgumentException("Toolbar is null");
            if (collapsingToolbarLayout == null)
                throw new IllegalArgumentException("CollapsingToolbarLayout is null");
            if (navigationMenuIcon == null)
                throw new IllegalArgumentException("NavigationMenuIcon is null");
            if (recyclerView == null)
                throw new IllegalArgumentException("RecyclerView is null");
            if (drawerLayout == null)
                throw new IllegalArgumentException("DrawerLayout is null");
            if (navigationView == null)
                throw new IllegalArgumentException("NavigationView is null");

            setSupportActionBar(toolbar);
            collapsingToolbarLayout.setTitle(citySearchText);

            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setHomeAsUpIndicator(navigationMenuIcon);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

            setupDrawerContent(navigationView);

            final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);

            recyclerView.setAdapter(cityAdapter);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Log.e(MainActivity.class.getName(), e.getMessage());
            Toast
                    .makeText(this, String.format("Internal error occured : %s", e.getMessage()), Toast.LENGTH_LONG)
                    .show();
            finish();
        }
    }

    private void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            switch (id) {
                case R.id.drawer_cities:
                    Log.i("DRAWER", "Hi there");
                    break;
            }

            menuItem.setChecked(true);
            drawerLayout.closeDrawers();
            return true;
        });
    }

    // endregion
}
