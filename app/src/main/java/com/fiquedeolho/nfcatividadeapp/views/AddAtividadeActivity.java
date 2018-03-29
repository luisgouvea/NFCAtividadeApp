package com.fiquedeolho.nfcatividadeapp.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.util.ConstantsURIAPI;
import com.fiquedeolho.nfcatividadeapp.util.Mask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class AddAtividadeActivity extends AppCompatActivity {

    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView[] dots;
    public ViewHolderAddAtividade mViewHolderAddAtividade = new ViewHolderAddAtividade();
    private int[] layouts;

    /**
     * ViewHolder dos elementos
     */
    public static class ViewHolderAddAtividade {
        private LinearLayout mViewDotsLayout;
        private ViewPager mViewPagerAddAtividade;
        private Button mViewBtnNext;
        private Button mViewBtnSkip;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       //getSupportActionBar().hide();  // VERIFICAR DEPOIS SE VOU USAR OU NAO

        setContentView(R.layout.activity_add_atividade);
        /*getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        this.mViewHolderAddAtividade.mViewPagerAddAtividade = (ViewPager) findViewById(R.id.view_pager_add_atividade);
        this.mViewHolderAddAtividade.mViewDotsLayout = (LinearLayout) findViewById(R.id.layoutDotsAddAtividade);
        this.mViewHolderAddAtividade.mViewBtnSkip = (Button) findViewById(R.id.btn_skip_add_atividade);
        this.mViewHolderAddAtividade.mViewBtnNext = (Button) findViewById(R.id.btn_next_add_atividade);


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.activity_screen_slide_page_fragment,
                R.layout.content_add_atividade_slide1
        };

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        //changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        this.mViewHolderAddAtividade.mViewPagerAddAtividade.setAdapter(myViewPagerAdapter);
        this.mViewHolderAddAtividade.mViewPagerAddAtividade.addOnPageChangeListener(viewPagerPageChangeListener);

        this.mViewHolderAddAtividade.mViewBtnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen(false);
            }
        });

        this.mViewHolderAddAtividade.mViewBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    mViewHolderAddAtividade.mViewPagerAddAtividade.setCurrentItem(current);
                } else {
                    launchHomeScreen(true);
                }
            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        this.mViewHolderAddAtividade.mViewDotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[0]);
            this.mViewHolderAddAtividade.mViewDotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[0]);
    }

    private int getItem(int i) {
        return this.mViewHolderAddAtividade.mViewPagerAddAtividade.getCurrentItem() + i;
    }

    private void launchHomeScreen(Boolean criarAtividade) {
        if(criarAtividade){
            LinearLayout linear = this.mViewHolderAddAtividade.mViewPagerAddAtividade.findViewById(R.id.first_content_linear_layout_add_ativ);
            EditText nomeAtivEle = linear.findViewById(R.id.input_nomeAtividade);
            EditText dataFinalizaEle = linear.findViewById(R.id.input_data_finalizacao_ativ);
            String nomeAtividade = nomeAtivEle.getText().toString();
            String dataFinalizacao = dataFinalizaEle.getText().toString();
            Boolean resultado = addAtividade(nomeAtividade, dataFinalizacao);
            final ProgressDialog pDialog = new ProgressDialog(this);
            if(!resultado){
                pDialog.setMessage("Ocorreu algum problema para a inserção");
                pDialog.show();
            }
            pDialog.hide();
        }
        startActivity(new Intent(AddAtividadeActivity.this, InitialNavigationActivity.class));
        finish();
    }

    private Boolean addAtividade(String nome, String data) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        final Context contextoAddAtividade = this;
        pDialog.setMessage("Aguarde, criando atividade...");
        pDialog.show();
        RequestQueue rq = Volley.newRequestQueue(this);
        JSONObject params = new JSONObject();
        try {
            params.put("nomeAtividade", nome);
            params.put("dataFinalizacao", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ConstantsURIAPI.ADDATIVIDADE, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                /*try {
                    String idUsuario = response.getString("Id");
                    Log.d("ResultJSONLogin", response.toString());
                    pDialog.hide();
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                pDialog.hide();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Erro", "Error: " + error.getMessage());
                pDialog.hide();
/*                Toast t = Toast.makeText(contextoLogin, "Por favor, tente novamente!", Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();*/
            }
        });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        rq.add(jsonObjReq);
        // TODO: Rever esse uso, pode existir uma outra forma e ainda, isso pode dar problema, pesquisar...
        try {
            future.get(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            String g = e.getMessage();
        }
        return true;
    }

    //	viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                mViewHolderAddAtividade.mViewBtnNext.setText(getString(R.string.concluido));
                mViewHolderAddAtividade.mViewBtnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                mViewHolderAddAtividade.mViewBtnNext.setText(getString(R.string.proximo));
                mViewHolderAddAtividade.mViewBtnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);

            switch (position){
                case 0:
                    EditText dataFinalizaElement = view.findViewById(R.id.input_data_finalizacao_ativ);
                    dataFinalizaElement.addTextChangedListener(Mask.insert("##/##/####", dataFinalizaElement));
                break;
                case 1:
                break;
            }
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
