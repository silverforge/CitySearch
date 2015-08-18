package com.jana.citysearch.model;

import android.graphics.drawable.Drawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import lombok.Getter;
import lombok.experimental.Builder;

@Builder
public class MainPresenterParameter {

    @Getter
    private Toolbar toolbar;

    @Getter
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Getter
    private String citySearchText;

    @Getter
    private Drawable navigationMenuIcon;

    @Getter
    private RecyclerView recyclerView;

    @Getter
    private DrawerLayout drawerLayout;

    @Getter
    private NavigationView navigationView;
}
