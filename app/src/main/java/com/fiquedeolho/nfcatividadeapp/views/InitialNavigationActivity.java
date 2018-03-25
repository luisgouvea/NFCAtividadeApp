package com.fiquedeolho.nfcatividadeapp.views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.Atividade;
import com.fiquedeolho.nfcatividadeapp.models.TAG;
import com.fiquedeolho.nfcatividadeapp.util.ConstantsURIAPI;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class InitialNavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private InitialNavigationActivity.ViewHolder InitialViewHolder = new InitialNavigationActivity.ViewHolder();

    private String idUsuario;
    private Context contextoInitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_navigation);
        contextoInitial = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idUsuario = extras.getString("idUsuario");
        }
        this.InitialViewHolder.btnAtiviExecutar = findViewById(R.id.btn_ativExecutar);
        this.InitialViewHolder.addFloatingAction = (FloatingActionButton) findViewById(R.id.btn_addFloatingAction);
        this.InitialViewHolder.addFloatingAction.setOnClickListener(this);
        this.InitialViewHolder.btnAtiviExecutar.setOnClickListener(this);
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
        if (id == R.id.menu_camera) {
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

    private static class ViewHolder {
        Button btnAtiviExecutar;
        FloatingActionButton addFloatingAction;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_ativExecutar) {

            this.getAtivExecutar();
        }
        else if(id == R.id.btn_addFloatingAction) {
            Intent intent = new Intent(contextoInitial, AddAtividadeActivity.class);
            startActivity(intent);
        }
    }

    private void getAtivExecutar() {
        final ProgressDialog pDialog = new ProgressDialog(this);
//        pDialog.setMessage("Aguarde, buscando atividades...");
//        pDialog.show();
        RequestQueue rq = Volley.newRequestQueue(this);
        JSONArray params = new JSONArray();
        params.put(this.idUsuario);
        ArrayList<com.fiquedeolho.nfcatividadeapp.models.TAG> aj = new ArrayList<TAG>();
        TAG tg = new TAG();
        tg.setNome("fdsf");
        aj.add(tg);

        Gson f = new Gson();
        String gg = f.toJson(aj);
        params.put(gg);
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST, ConstantsURIAPI.GETATIVIDADESEXECUTAR, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                //adicionar o Response para a proxima pagina
                ArrayList<Atividade> listAtividade = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject atividadeAPI = response.getJSONObject(i);
                        //cria atividade
                        Atividade atividade = new Atividade();
                        atividade.setNome(atividadeAPI.getString("Nome"));
                        atividade.setId(atividadeAPI.getInt("Id"));
                        //cria atividade
                        listAtividade.add(atividade);
                    } catch (Exception e) {
                        Log.d("Erro getAtivExecutar ->", e.getMessage());
                    }
                }
                Intent intent = new Intent(contextoInitial, ListAtividadesActivity.class);
                intent.putExtra("idUsuario", idUsuario);
                intent.putParcelableArrayListExtra("listAtividade", listAtividade);
                startActivity(intent);
                //pDialog.hide();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String h = "fjfjf";
            }
        });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        rq.add(jsonObjReq);
    }
}
