package com.fiquedeolho.nfcatividadeapp.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
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
import android.view.WindowManager;
import android.widget.Toast;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.SharedPreferences.SavePreferences;
import com.fiquedeolho.nfcatividadeapp.fragments.menuHome.FragmentHomeAddAtividade;
import com.fiquedeolho.nfcatividadeapp.fragments.menuHome.FragmentHomeExecutarAtividade;
import com.fiquedeolho.nfcatividadeapp.models.APIError;
import com.fiquedeolho.nfcatividadeapp.models.NotificacaoUsuario;
import com.fiquedeolho.nfcatividadeapp.retrofit.implementation.NotificacaoUsuarioImplementation;
import com.fiquedeolho.nfcatividadeapp.util.BadgeDrawable;
import com.fiquedeolho.nfcatividadeapp.util.KeysSharedPreference;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InitialNavigationActivity<T> extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Callback<T> {

    private int countNotificacoesUsu;
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

    private FragmentHomeExecutarAtividade fragExecuteAtiv;

    private Fragment[] setFragments(Bundle budle) {

        Fragment fragAddAtiv = new FragmentHomeAddAtividade();
        fragAddAtiv.setArguments(budle);
        fragExecuteAtiv = new FragmentHomeExecutarAtividade();
        fragExecuteAtiv.setArguments(budle);
        return new Fragment[]{
                fragAddAtiv,
                fragExecuteAtiv
        };
    }

    private NotificacaoUsuarioImplementation notificacaoUsuarioImplementation = new NotificacaoUsuarioImplementation();
    private Callback<T> requestRetrofit = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_navigation);
        Bundle extras = getIntent().getExtras();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (extras != null) {
            countNotificacoesUsu = extras.getInt("countNotificacoes");
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

    /*@Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        fragExecuteAtiv.intentNFCTag(intent);
    }*/

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
        if(countNotificacoesUsu > 0) {
            setCountNotificacao(this, countNotificacoesUsu, menu);
        }
        return true;
    }

    public void setCountNotificacao(Context context, int count, Menu defaultMenu) {
        MenuItem menuItem = defaultMenu.findItem(R.id.actionbar_notification);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_notification_count);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(String.valueOf(count));
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_notification_count, badge);
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
        } else if(id == R.id.actionbar_notification){
            SavePreferences save = new SavePreferences(this);
            int idUsuario = save.getSavedInt(KeysSharedPreference.ID_USUARIO_LOGADO);
            notificacaoUsuarioImplementation.requestSelectAllObjectsByIdUsuario(requestRetrofit, idUsuario);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        APIError error = null;
        String typeResponse = notificacaoUsuarioImplementation.findResponse(call, response);
        if (typeResponse != "") {
            switch (typeResponse) {
                case "erro":
                    error = notificacaoUsuarioImplementation.resultError();
                    if (error.message() != null) {
                        Toast.makeText(getApplicationContext(), error.message(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Ocorreu um erro", Toast.LENGTH_LONG).show();
                    }
                    break;
                case "getAllNotificacaoUsuByIdUsu":
                    ArrayList<NotificacaoUsuario> notificacoes = notificacaoUsuarioImplementation.resultSelectAllObjectByIdUsuario();
                    break;
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_share) {

        }else if (id == R.id.cadastro_tags) {
            Intent intent = new Intent(this, InfTagsActivity.class);
            startActivity(intent);
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
