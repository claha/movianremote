package com.claha.showtimeremote;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

abstract public class NavigationDrawerActivity extends ActionBarActivity {

    private DrawerLayout drawerLayout;

    private ListView drawer;

    private ActionBarDrawerToggle drawerToggle;
    private CharSequence title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceID());

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(getToolbarResourceID());
        setSupportActionBar(toolbar);

        // Drawer layout
        drawerLayout = (DrawerLayout) findViewById(getDrawerLayoutResourceID());
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);

        // Drawer and drawer adapter
        drawer = (ListView) findViewById(getDrawerResourceID());
        ArrayAdapter<String> drawerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, getDrawerItems());
        drawer.setAdapter(drawerAdapter);

        // Click listener
        drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectFragment(position);
            }
        });

        // Drawer Toggle
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, getAppName(), getAppName()) {
            @Override
            public void onDrawerClosed(View drawerView) {
                if (drawerView == drawer) {
                    setTitle(title);
                    invalidateOptionsMenu();
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if (drawerView == drawer) {
                    setTitle(getAppName());
                    invalidateOptionsMenu();
                }
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (drawerView == drawer) {
                    super.onDrawerSlide(drawerView, slideOffset);
                }
            }

        };
        drawerLayout.setDrawerListener(drawerToggle);

        //
        if (savedInstanceState == null) {
            selectFragment(getStartPage());
        } else {
            title = savedInstanceState.getCharSequence("title");
            if (!savedInstanceState.getBoolean("isDrawerOpen")) {
                setTitle(title);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("title", title);
        outState.putBoolean("isDrawerOpen", isDrawerOpen());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(getMenuResourceID(), menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isDrawerOpen()) {
            menu.clear();
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    boolean isDrawerOpen() {
        return drawerLayout.isDrawerOpen(drawer);
    }

    void closeDrawer() {
        drawerLayout.closeDrawer(drawer);
    }

    private void selectFragment(int position) {
        title = getDrawerItems().get(position);
        Fragment fragment = createFragment(getDrawerItems().get(position));

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(getContentResourceID(), fragment).addToBackStack(null).commit();

        setTitle(title);
        closeDrawer();
    }

    abstract protected int getLayoutResourceID();

    abstract protected int getToolbarResourceID();

    abstract protected int getDrawerLayoutResourceID();

    abstract protected int getDrawerResourceID();

    abstract protected List<String> getDrawerItems();

    abstract protected Fragment createFragment(String title);

    abstract protected int getMenuResourceID();

    abstract protected int getContentResourceID();

    abstract protected int getAppName();

    protected int getStartPage() {
        return 0;
    }

}

