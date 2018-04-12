package com.fiquedeolho.nfcatividadeapp.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.SharedPreferences.SavePreferences;
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.TagRetrofit;
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.TarefaRetrofit;
import com.fiquedeolho.nfcatividadeapp.models.Tarefa;
import com.fiquedeolho.nfcatividadeapp.pager.addTarefa.PagerAddTarefaAdpter;
import com.fiquedeolho.nfcatividadeapp.models.TAG;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListener;
import com.fiquedeolho.nfcatividadeapp.recyclerView.infTarefas.listTags.TarefasListTagAdapter;
import com.fiquedeolho.nfcatividadeapp.util.KeysSharedPreference;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class AddTarefaActivity extends AppCompatActivity {

    private ViewHolderAddTarefa mViewHolderAddTarefa = new ViewHolderAddTarefa();
    private ViewHolderVincTag mViewHolderVincTag = new ViewHolderVincTag();
    private int IdAtividade;
    private ProgressDialog pDialog;
    private Context context;
    private TextView[] dots;
    private ArrayList<TAG> listaTags;
    private int[] layouts;
    private PagerAddTarefaAdpter pagerAdapter;
    private int idTagVinculada;
    private TarefasListTagAdapter tarefasListTagAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tarefa);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            IdAtividade = extras.getInt("IdAtividade");
        }

        ListAllTagsAddTarefaVincTag();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        context = this;
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        this.mViewHolderAddTarefa.mViewPagerAddTarefa = (ViewPager) findViewById(R.id.view_pager_add_tarefa);
        this.mViewHolderAddTarefa.mViewDotsLayout = (LinearLayout) findViewById(R.id.layoutDotsAddTarefa);
        this.mViewHolderAddTarefa.mViewBtnSkip = (Button) findViewById(R.id.btn_skip_add_tarefa);
        this.mViewHolderAddTarefa.mViewBtnNext = (Button) findViewById(R.id.btn_next_add_tarefa);


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.content_add_tarefa_inf,
                R.layout.content_add_tarefa_list,
        };

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        //changeStatusBarColor();

        pagerAdapter = new PagerAddTarefaAdpter(layouts, this);
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
                if (current < layouts.length) {
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

    //	viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
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
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        this.mViewHolderAddTarefa.mViewDotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
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


    private void ListAllTagsAddTarefaVincTag() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        //pDialog.setTitle(getString(R.string.title_progress_tarefa_list));
        pDialog.setMessage(getString(R.string.message_progress_dialog));
        pDialog.show();
        TagRetrofit tagInterface = BaseUrlRetrofit.retrofit.create(TagRetrofit.class);
        SavePreferences save = new SavePreferences(this);

        final Call<ArrayList<TAG>> call = tagInterface.getTagsByIdUsuario(save.getSavedInt(KeysSharedPreference.ID_USUARIO_LOGADO));

        call.enqueue(new Callback<ArrayList<TAG>>() {
            @Override
            public void onResponse(Call<ArrayList<TAG>> call, retrofit2.Response<ArrayList<TAG>> response) {
                listaTags = response.body();
                SetarRecyclerView();
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TAG>> call, Throwable t) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
    }

    private void SetarRecyclerView() {

        // 1 - Obter a recyclerview
        this.mViewHolderVincTag.mViewRecyclerViewAddTarefaVincTag = findViewById(R.id.recyclerViewAddTarefaVincTag);

        // Implementa o evento de click para passar por parâmetro para a ViewHolder
        OnListClickInteractionListener listener = new OnListClickInteractionListener() {
            @Override
            public void onClick(int id) {
                if (idTagVinculada != 0) {
                    RecyclerView re = mViewHolderVincTag.mViewRecyclerViewAddTarefaVincTag;
                    RadioButton radio = re.findViewById(idTagVinculada);
                    radio.setChecked(false);
                }
                TAG tag = getTagTarget(id);
                idTagVinculada = tag.getId();
            }
        };

        // 2 - Definir adapter passando listagem de tarefas e listener
        tarefasListTagAdapter = new TarefasListTagAdapter(listaTags, listener);
        this.mViewHolderVincTag.mViewRecyclerViewAddTarefaVincTag.setAdapter(tarefasListTagAdapter);

        this.mViewHolderVincTag.mViewRecyclerViewAddTarefaVincTag.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        // 3 - Definir um layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        this.mViewHolderVincTag.mViewRecyclerViewAddTarefaVincTag.setLayoutManager(linearLayoutManager);
    }

    private TAG getTagTarget(int idTag) {
        for (int i = 0; i < listaTags.size(); i++) {
            TAG tag = listaTags.get(i);
            if (tag.getId() == idTag) {
                return tag;
            }
        }
        return null;
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

    /**
     * ViewHolder dos elementos
     */
    private class ViewHolderAddTarefa {

        private LinearLayout mViewDotsLayout;
        private ViewPager mViewPagerAddTarefa;
        private Button mViewBtnNext;
        private Button mViewBtnSkip;
    }

    private class ViewHolderVincTag {

        private RecyclerView mViewRecyclerViewAddTarefaVincTag;
    }
}
