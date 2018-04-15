package com.fiquedeolho.nfcatividadeapp.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.fragments.addTarefa.FragmentAddTarefaInf;
import com.fiquedeolho.nfcatividadeapp.fragments.addTarefa.FragmentAddTarefaVincTag;
import com.fiquedeolho.nfcatividadeapp.interfaces.communicationActivity.ActivityCommunicator;
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.TarefaRetrofit;
import com.fiquedeolho.nfcatividadeapp.models.Tarefa;
import com.fiquedeolho.nfcatividadeapp.pager.addTarefa.PagerAddTarefaAdapter;

import retrofit2.Call;
import retrofit2.Callback;

public class AddTarefaActivity extends AppCompatActivity implements ActivityCommunicator {

    private ViewHolderAddTarefa mViewHolderAddTarefa = new ViewHolderAddTarefa();
    private int IdAtividade;
    private ProgressDialog pDialog;
    private Context context;
    private TextView[] dots;
    private PagerAddTarefaAdapter pagerAdapter;
    private int idTagVinculada;
    private FragmentAddTarefaVincTag fragVincTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tarefa);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            IdAtividade = extras.getInt("IdAtividade");
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        context = this;
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        this.mViewHolderAddTarefa.mViewPagerAddTarefa = (ViewPager) findViewById(R.id.view_pager_add_tarefa);
        this.mViewHolderAddTarefa.mViewDotsLayout = (LinearLayout) findViewById(R.id.layoutDotsAddTarefa);
        this.mViewHolderAddTarefa.mViewBtnSkip = (Button) findViewById(R.id.btn_skip_add_tarefa);
        this.mViewHolderAddTarefa.mViewBtnNext = (Button) findViewById(R.id.btn_next_add_tarefa);


        // adding bottom dots
        addBottomDots(0);

        FragmentAddTarefaInf fragInf = new FragmentAddTarefaInf();
        fragVincTag = new FragmentAddTarefaVincTag();
        pagerAdapter = new PagerAddTarefaAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(fragInf);
        pagerAdapter.addFragment(fragVincTag);

        this.mViewHolderAddTarefa.mViewPagerAddTarefa.setAdapter(pagerAdapter);
        this.mViewHolderAddTarefa.mViewPagerAddTarefa.addOnPageChangeListener(viewPagerPageChangeListener);

        this.mViewHolderAddTarefa.mViewBtnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addAtividadeDB = false;
                //launchHomeScreen();
            }
        });

        this.mViewHolderAddTarefa.mViewBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < 2) {
                    // move to next screen
                    mViewHolderAddTarefa.mViewPagerAddTarefa.setCurrentItem(current);
                } else {
                    Tarefa tarefa = new Tarefa();
                    LinearLayout linear = mViewHolderAddTarefa.mViewPagerAddTarefa.findViewById(R.id.first_content_linear_layout_add_tarefa);
                    EditText nomeTarefaEle = linear.findViewById(R.id.input_nomeTarefa);
                    EditText dataFinalizaEle = linear.findViewById(R.id.input_data_finalizacao_tarefa);
                    String nomeAtividadeInput = nomeTarefaEle.getText().toString();
                    String dataFinalizacaoInput = dataFinalizaEle.getText().toString();
                    tarefa.setNome(nomeAtividadeInput);
                    tarefa.setIdAtividade(IdAtividade);
                    tarefa.setIdTag(idTagVinculada);
                    addTarefa(tarefa);
                }
            }
        });

        //getList de usuarios no banco
    }

    @Override
    protected void onStart(){
        super.onStart();
        fragVincTag.ListAllTagsAddTarefaVincTag(this);
    }

    //	viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == 1) {
                // last page. make button text to GOT IT
                mViewHolderAddTarefa.mViewBtnNext.setText(getString(R.string.concluido));
                mViewHolderAddTarefa.mViewBtnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                mViewHolderAddTarefa.mViewBtnNext.setText(getString(R.string.proximo));
                mViewHolderAddTarefa.mViewBtnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void addBottomDots(int currentPage) {
        dots = new TextView[2];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        this.mViewHolderAddTarefa.mViewDotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(45);
            dots[i].setTextColor(colorsInactive[0]);
            this.mViewHolderAddTarefa.mViewDotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[0]);
    }

    private int getItem(int i) {
        return this.mViewHolderAddTarefa.mViewPagerAddTarefa.getCurrentItem() + i;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    /**
     Click no botao voltar da activity
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backToInfTarefas();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addTarefa(final Tarefa tarefa) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setTitle(getString(R.string.title_progress_tarefa_add));
        pDialog.setMessage(getString(R.string.message_progress_dialog));
        pDialog.show();
        TarefaRetrofit tarefaInterface = BaseUrlRetrofit.retrofit.create(TarefaRetrofit.class);
        final Call<Boolean> call = tarefaInterface.addTarefa(tarefa);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, retrofit2.Response<Boolean> response) {
                Boolean result = response.body();
                backToInfTarefas();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
    }

    private void backToInfTarefas() {
        Intent resultIntent = new Intent(this, InfTarefasActivity.class);
        resultIntent.putExtra("IdAtividade", IdAtividade);
        startActivity(resultIntent);
        finish();
    }

    @Override
    public void passDataToActivity(int id) {
        idTagVinculada = id;
    }

    /**
     * ViewHolder dos elementos
     */
    private class ViewHolderAddTarefa {

        private LinearLayout mViewDotsLayout;
        private ViewPager mViewPagerAddTarefa;
        private Button mViewBtnNext;
        private Button mViewBtnSkip;
    }

}
