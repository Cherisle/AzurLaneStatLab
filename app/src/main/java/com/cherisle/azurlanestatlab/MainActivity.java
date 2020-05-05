package com.cherisle.azurlanestatlab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private static final String app_version = "v1.0.0";
    private AzurLaneDbHelper dbhelper;
    private Context context;

    /*
    private AsyncHttpResponseHandler responseHandler;
    private FloatingActionButton fabMenu, fabAction1, fabAction2;
    private Animation FabOpen,FabClose,Screen_Fin,Screen_Fout;

    private MainActivity activity_reference;
    private Context context;

    //Handlers
    private Handler dialog_handler;
    private Handler ui_update_handler;

    //References from other fragments
    private FeedFragment feedReference;
    private SearchView referenced_sv = null;

    private MediaPlayer mpSuccess;
    private MediaPlayer mpError;

    final Runnable rescan_close = new Runnable()
    {
        public void run()
        {
            dialog_rescan_nfc.dismiss();
        }
    };

     */

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavController navController;
    private NavigationView navigationView;
    private AppBarConfiguration appBarConfiguration;

    public AzurLaneDbHelper getDbHelper()
    {
        return dbhelper;
    }

    /*

    public Context getContext() { return context; }

    public void setSearchViewReference(SearchView sv)
    {
        referenced_sv = sv;
    }

    */

    // Setting Up One Time Navigation
    private void SetupNavigation()
    {
        toolbar = findViewById(R.id.alsl_main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerLayout = findViewById(R.id.drawer);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener()
        {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset)
            {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView)
            {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView)
            {

            }

            @Override
            public void onDrawerStateChanged(int newState)
            {

            }
        });

        Set<Integer> menuSet = new HashSet<>();
        menuSet.add(R.id.alsl_navgraph_home);
        //menuSet.add(R.id.alsl_navgraph_compare_ships);

        AppBarConfiguration.Builder appBarConfigurationBuild = new AppBarConfiguration.Builder(menuSet);
        appBarConfigurationBuild.setDrawerLayout(drawerLayout);

        appBarConfiguration = appBarConfigurationBuild.build();

        navigationView = findViewById(R.id.navigationView);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, this.navController, this.appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        dbhelper = new AzurLaneDbHelper(context.getApplicationContext());
        if(!dbhelper.checkDataBase())
        {
            dbhelper.writeCsvToDb("azurlane_20200415.csv",context);
        }


        Log.i("[DEBUG]", "Manufacturer: " + Build.MANUFACTURER);

        TextView tv_version = (TextView) findViewById(R.id.app_version);
        tv_version.setText(String.format("%1$18s",app_version));

        SetupNavigation();

        //set media players
        //mpSuccess = MediaPlayer.create(this, R.raw.success_sound);
        //mpError = MediaPlayer.create(this, R.raw.error_sound);

        //set handlers
        //dialog_handler = new Handler();
        //ui_update_handler = new Handler();
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        menuItem.setChecked(true);
        drawerLayout.closeDrawers();

        int id = menuItem.getItemId();
        NavOptions.Builder navOptionsBuilder = new NavOptions.Builder().setLaunchSingleTop(true);
        NavOptions navOptions = navOptionsBuilder.build();

        int navLocation = navController.getCurrentDestination().getId();

        switch (id)
        {
            case R.id.nav_home:
                navController.navigate(R.id.alsl_navgraph_home, null, navOptions);
                break;
            /*case R.id.nav_compare_ships:
                navController.navigate(R.id.alsl_navgraph_compare_ships, null, navOptions);
                break;
            case R.id.nav_compare_weapons:
                navController.navigate(R.id.alsl_navgraph_compare_weapons, null, navOptions);
                break;
            case R.id.nav_fleet_builder:
                navController.navigate(R.id.alsl_navgraph_fleet_builder, null, navOptions);
                break;*/
        }
        return true;
    }

    @Override
    public void onBackPressed()
    {
        if(navController.getCurrentDestination().getLabel().equals(getResources().getString(R.string.app_name)))
        {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.exit_title)
                    .setMessage("Are you sure you want to close this application?")
                    //.setPositiveButton("Logout", new DialogInterface.OnClickListener()
                    //{
                    //    @Override
                    //    public void onClick(DialogInterface dialog, int which)
                    //    {
                    //        finish();
                    //    }
                    //})
                    .setNeutralButton("Cancel", null)

                    .setNegativeButton("Exit", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            finishAffinity();
                        }
                    })
                    .show();
        }
        else if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    public void homeCompareShips(View v)
    {
        Log.i("ALSL","HI YOU CLICKED A BUTTON");
        NavOptions.Builder navOptionsBuilder = new NavOptions.Builder().setLaunchSingleTop(true);
        NavOptions navOptions = navOptionsBuilder.build();
        navController.navigate(R.id.alsl_navgraph_compare,null, navOptions);
    }

    /*
    public void TestOnClick(View v)
    {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * On activity destroy (activity not visible and stopped)
     */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    /**
     * On activity pause (when the UI as partially visible to the user e.g. open dialog alert/box)
     */
    @Override
    public void onPause()
    {
        super.onPause();
    }

    /**
     * On activity resume (called after onStart() or when app is fully visible after paused state)
     */
    @Override
    public void onResume()
    {
        super.onResume();
    }
}
