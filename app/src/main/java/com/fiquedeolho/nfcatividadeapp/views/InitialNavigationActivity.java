package com.fiquedeolho.nfcatividadeapp.views;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.fragments.menuHome.FragmentHomeAddAtividade;
import com.fiquedeolho.nfcatividadeapp.fragments.menuHome.FragmentHomeExecutarAtividade;

public class InitialNavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String idUsuario;
    private ViewHolderInitialHome mViewHolderInitialHome = new ViewHolderInitialHome();

    // Titles of the individual pages (displayed in tabs)
    private final String[] PAGE_TITLES = new String[]{
            "Tarefas criadas",
            "Tarefas para fazer"
    };

    private Fragment[] PAGES;

    /**
     * ViewHolder dos elementos
     */
    public static class ViewHolderInitialHome {
        private Toolbar mViewToolbar;
        private DrawerLayout mViewDrawer;
        private NavigationView mViewNavigation;
        private ViewPager mViewPagerTableInitial;
        private TabLayout mViewTabLayuot;
    }

    private Fragment[] setFragments(Bundle budle) {

        Fragment fragAddAtiv = new FragmentHomeAddAtividade();
        fragAddAtiv.setArguments(budle);
        Fragment fragExecuteAtiv = new FragmentHomeExecutarAtividade();
        fragExecuteAtiv.setArguments(budle);
        return new Fragment[]{
                fragAddAtiv,
                fragExecuteAtiv
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_navigation);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idUsuario = extras.getString("idUsuario");
        }
        PAGES = setFragments(extras);
        InitialElementsViews();
    }

    private void InitialElementsViews() {
        this.mViewHolderInitialHome.mViewToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(this.mViewHolderInitialHome.mViewToolbar);

        this.mViewHolderInitialHome.mViewDrawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, this.mViewHolderInitialHome.mViewDrawer, this.mViewHolderInitialHome.mViewToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        this.mViewHolderInitialHome.mViewDrawer.addDrawerListener(toggle);
        toggle.syncState();

        this.mViewHolderInitialHome.mViewNavigation = findViewById(R.id.nav_view);
        this.mViewHolderInitialHome.mViewNavigation.setNavigationItemSelectedListener(this);

        this.mViewHolderInitialHome.mViewPagerTableInitial = findViewById(R.id.view_pager_table_initial);
        this.mViewHolderInitialHome.mViewPagerTableInitial.setAdapter(new ViewPagerTableMenu(this.getSupportFragmentManager()));

        this.mViewHolderInitialHome.mViewTabLayuot = findViewById(R.id.tab_layout_initial);
        this.mViewHolderInitialHome.mViewTabLayuot.setupWithViewPager(this.mViewHolderInitialHome.mViewPagerTableInitial);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.content_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.help_tutorial) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * The table of Fragment of activity
     */

    /* PagerAdapter for supplying the ViewPager with the pages (fragments) to display. */
    public class ViewPagerTableMenu extends FragmentPagerAdapter {

        public ViewPagerTableMenu(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return PAGES[position];
        }

        @Override
        public int getCount() {
            return PAGES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return PAGE_TITLES[position];
        }
    }
}
