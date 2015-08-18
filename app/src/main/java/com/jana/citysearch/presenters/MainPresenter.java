package com.jana.citysearch.presenters;

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

import com.jana.citysearch.R;
import com.jana.citysearch.model.MainPresenterParameter;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.IllegalFormatCodePointException;

@EBean
public class MainPresenter {

    private DrawerLayout drawerLayout;

    @RootContext
    public AppCompatActivity parentActivity;

    public void initializeView(MainPresenterParameter presenterParameter)
            throws IllegalArgumentException {

        Toolbar toolbar = presenterParameter.getToolbar();
        CollapsingToolbarLayout collapsingToolbarLayout = presenterParameter.getCollapsingToolbarLayout();
        String citySearchText = presenterParameter.getCitySearchText();
        Drawable navigationMenuIcon = presenterParameter.getNavigationMenuIcon();
        RecyclerView recyclerView = presenterParameter.getRecyclerView();
        DrawerLayout drawerLayout = presenterParameter.getDrawerLayout();
        NavigationView navigationView = presenterParameter.getNavigationView();
        this.drawerLayout = drawerLayout;

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

        parentActivity.setSupportActionBar(toolbar);
        collapsingToolbarLayout.setTitle(citySearchText);

        final ActionBar actionBar = parentActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(navigationMenuIcon);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (navigationView != null)
            setupDrawerContent(navigationView);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(parentActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

//        recyclerView.setAdapter(cityAdapter);
    }

    public void openDrawer() {
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
}
