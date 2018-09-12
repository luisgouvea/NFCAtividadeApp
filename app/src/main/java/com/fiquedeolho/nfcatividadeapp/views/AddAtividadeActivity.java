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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.SharedPreferences.SavePreferences;
import com.fiquedeolho.nfcatividadeapp.dialog.DialogDefaultErro;
import com.fiquedeolho.nfcatividadeapp.models.APIError;
import com.fiquedeolho.nfcatividadeapp.retrofit.ErrorUtils;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.AtividadeRetrofit;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.UsuarioRetrofit;
import com.fiquedeolho.nfcatividadeapp.models.Atividade;
import com.fiquedeolho.nfcatividadeapp.models.Usuario;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListener;
import com.fiquedeolho.nfcatividadeapp.recyclerView.addAtividade.vinculoExecutor.AddAtividadeListVincExecAdapter;
import com.fiquedeolho.nfcatividadeapp.util.KeysSharedPreference;
import com.fiquedeolho.nfcatividadeapp.util.Mask;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class AddAtividadeActivity extends AppCompatActivity {

    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView[] dots;
    public ViewHolderAddAtividade mViewHolderAddAtividade = new ViewHolderAddAtividade();
    private ViewHolderAddAtivVincExecutor mViewHolderAddAtivVincExecutor = new ViewHolderAddAtivVincExecutor();
    private AddAtividadeListVincExecAdapter addAtivVincExecAdpter;
    private int[] layouts;
    private ProgressDialog pDialog;
    private Context context;
    private ArrayList<Usuario> listUsuExecutores;
    private int idUsuarioVinc;
    private DialogDefaultErro dialogDefaultErro;

    /**
     * ViewHolder dos elementos
     */
    public static class ViewHolderAddAtividade {
        private LinearLayout mViewDotsLayout;
        private ViewPager mViewPagerAddAtividade;
        private Button mViewBtnNext;
        private Button mViewBtnSkip;
        private LinearLayout mViewContentOpcoesFormaExecucaoDia;
        private LinearLayout mViewContentRepeticaoFluxoCompleto;
        private LinearLayout mViewContentDiaEspecifico;
    }

    /**
     * ViewHolder dos elementos
     */
    public static class ViewHolderAddAtivVincExecutor {
        private RecyclerView mViewRecyclerViewAddAtivVincExec;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ListAllUsuarioAddAtivVincExecutor();
        setContentView(R.layout.activity_add_atividade);
        context = this;
        /*getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        this.mViewHolderAddAtividade.mViewPagerAddAtividade = (ViewPager) findViewById(R.id.view_pager_add_atividade);
        this.mViewHolderAddAtividade.mViewDotsLayout = (LinearLayout) findViewById(R.id.layoutDotsAddAtividade);
        this.mViewHolderAddAtividade.mViewBtnSkip = (Button) findViewById(R.id.btn_skip_add_atividade);
        this.mViewHolderAddAtividade.mViewBtnNext = (Button) findViewById(R.id.btn_next_add_atividade);

        dialogDefaultErro = DialogDefaultErro.newInstance();

        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.content_add_ativ_inf,
                R.layout.content_add_ativ_vinc_executor
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
                //addAtividadeDB = false;
                launchHomeScreen();
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
                    Boolean faltaCampo = false;
                    /*pDialog = new ProgressDialog(context);
                    pDialog.setMessage("Aguarde, criando atividade...");
                    pDialog.show();*/
                    LinearLayout linear = mViewHolderAddAtividade.mViewPagerAddAtividade.findViewById(R.id.first_content_linear_layout_add_ativ);

                    /**
                     * GET ELEMENTOS
                     */
                    EditText nomeAtivEle = linear.findViewById(R.id.input_nomeAtividade);
                    EditText descAtividadeEle = linear.findViewById(R.id.desc_atividade);
                    EditText dataFinalizaEle = linear.findViewById(R.id.input_data_finalizacao_ativ);
                    RadioButton ativSequencial = linear.findViewById(R.id.forma_execucao_tarefa_finaliza);
                    RadioButton ativPorDia = linear.findViewById(R.id.forma_execucao_por_dia);
                    RadioButton execPorDiaSim = linear.findViewById(R.id.execucao_todo_dia_sim);
                    RadioButton execPorDiaNao = linear.findViewById(R.id.execucao_todo_dia_nao);
                    RadioButton reptFluxoSemLimite = linear.findViewById(R.id.repeti_fluxo_compl_semLimite);
                    RadioButton reptFluxoQtdEsp = linear.findViewById(R.id.repeti_fluxo_compl_qtdEsp);
                    EditText diaExecucaoEle = linear.findViewById(R.id.editText_dia_especifico);
                    EditText numMaximoCicloEle = linear.findViewById(R.id.editText_repeticao_fluxo_rept_fluxo);

                    /**
                     * SET ELEMENTOS
                     */
                    String nomeAtividadeInput = nomeAtivEle.getText().toString();
                    String descAtividade = descAtividadeEle.getText().toString();
                    //String dataFinalizacaoInput = dataFinalizaEle.getText().toString();
                    int maneiraExecucao = 0;
                    int numMaximoCicloFinal = 0;
                    String diaExecucaoFinal = null;
                    if (ativSequencial.isChecked()) {
                        maneiraExecucao = 1;
                    } else if (ativPorDia.isChecked()) {
                        maneiraExecucao = 2;
                        if (execPorDiaNao.isChecked()) {
                            if (diaExecucaoEle == null || diaExecucaoEle.getText().toString().isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Por favor, preencha o dia de execucao", Toast.LENGTH_LONG).show();
                                faltaCampo = true;
                                return;
                            } else {
                                diaExecucaoFinal = diaExecucaoEle.getText().toString();
                            }
                        } else if (execPorDiaSim.isChecked() == false) {
                            Toast.makeText(getApplicationContext(), "Defina a ocorrência do dia", Toast.LENGTH_LONG).show();
                            faltaCampo = true;
                            return;
                        }

                        if (!reptFluxoSemLimite.isChecked() && !reptFluxoQtdEsp.isChecked()) {
                            Toast.makeText(getApplicationContext(), "Por favor, preencha a repetição do fluxo", Toast.LENGTH_LONG).show();
                            faltaCampo = true;
                            return;
                        }
                        if (reptFluxoQtdEsp.isChecked()) {
                            if (numMaximoCicloEle == null || numMaximoCicloEle.getText().toString().isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Por favor, preencha a quantidade especifíca de repetições", Toast.LENGTH_LONG).show();
                                faltaCampo = true;
                                return;
                            }
                            numMaximoCicloFinal = Integer.parseInt(numMaximoCicloEle.getText().toString());
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Por favor, preencha o modo de execução da atividade", Toast.LENGTH_LONG).show();
                        faltaCampo = true;
                        return;
                    }

                    /**
                     * CRIA ATIVIDADE
                     */
                    SavePreferences shared = new SavePreferences(context);
                    Atividade atividade = new Atividade();
                    if (maneiraExecucao != 0) {
                        atividade.setIdModoExecucao(maneiraExecucao);
                    }
                    if (diaExecucaoFinal != null && !diaExecucaoFinal.isEmpty()) {
                        atividade.setDiaExecucao(diaExecucaoFinal);
                    }
                    if (numMaximoCicloFinal != 0) {
                        atividade.setNumMaximoCiclo(numMaximoCicloFinal);
                    }
                    atividade.setNome(nomeAtividadeInput);
                    atividade.setDescricao(descAtividade);
                    atividade.setIdUsuarioCriador(shared.getSavedInt(KeysSharedPreference.ID_USUARIO_LOGADO));
                    atividade.setIdUsuarioExecutor(idUsuarioVinc);
                    //atividade.setDataFinalizacao(dataFinalizacaoInput);
                    if (faltaCampo == false) {
                        pDialog = new ProgressDialog(context);
                        pDialog.setMessage("Aguarde, criando atividade...");
                        pDialog.show();
                        addAtividade(atividade);
                    } else {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                    }
                    //launchHomeScreen();
                }
            }
        });

        //getList de usuarios no banco
    }

    public void HabilitaInfFormaExecDia(View v) {
        this.mViewHolderAddAtividade.mViewContentOpcoesFormaExecucaoDia = findViewById(R.id.content_opcoes_forma_execucao_dia);
        this.mViewHolderAddAtividade.mViewContentRepeticaoFluxoCompleto = findViewById(R.id.content_repeticao_fluxo_completo);
        this.mViewHolderAddAtividade.mViewContentOpcoesFormaExecucaoDia.setVisibility(View.VISIBLE);
        this.mViewHolderAddAtividade.mViewContentRepeticaoFluxoCompleto.setVisibility(View.VISIBLE);

        final ScrollView scrollView = findViewById(R.id.scrollAddAtiv);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }


    public void DesabilitaInfFormaExecDia(View v) {
        this.mViewHolderAddAtividade.mViewContentOpcoesFormaExecucaoDia = findViewById(R.id.content_opcoes_forma_execucao_dia);
        this.mViewHolderAddAtividade.mViewContentRepeticaoFluxoCompleto = findViewById(R.id.content_repeticao_fluxo_completo);
        this.mViewHolderAddAtividade.mViewContentOpcoesFormaExecucaoDia.setVisibility(View.GONE);
        this.mViewHolderAddAtividade.mViewContentRepeticaoFluxoCompleto.setVisibility(View.GONE);

        final ScrollView scrollView = findViewById(R.id.scrollAddAtiv);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    public void HabilitarDiaEspecifico(View v) {
        this.mViewHolderAddAtividade.mViewContentDiaEspecifico = findViewById(R.id.content_dia_especifico);
        this.mViewHolderAddAtividade.mViewContentDiaEspecifico.setVisibility(View.VISIBLE);
    }

    public void DesabilitarDiaEspecifico(View v) {
        this.mViewHolderAddAtividade.mViewContentDiaEspecifico = findViewById(R.id.content_dia_especifico);
        this.mViewHolderAddAtividade.mViewContentDiaEspecifico.setVisibility(View.GONE);
    }


    public void HabilitarQtdRepeticoes(View v) {
        LinearLayout linear = findViewById(R.id.qtd_numero_repeticoes_fluxo_completo);
        linear.setVisibility(View.VISIBLE);
        final ScrollView scrollView = findViewById(R.id.scrollAddAtiv);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    public void DesabilitarQtdRepeticoes(View v) {
        LinearLayout linear = findViewById(R.id.qtd_numero_repeticoes_fluxo_completo);
        linear.setVisibility(View.GONE);

        final ScrollView scrollView = findViewById(R.id.scrollAddAtiv);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private void ListAllUsuarioAddAtivVincExecutor() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        //pDialog.setTitle(getString(R.string.title_progress_tarefa_list));
        pDialog.setMessage(getString(R.string.message_progress_dialog));
        pDialog.show();
        UsuarioRetrofit ativiInterface = BaseUrlRetrofit.retrofit.create(UsuarioRetrofit.class);
        SavePreferences save = new SavePreferences(this);

        final Call<ArrayList<Usuario>> call = ativiInterface.listAllUsuarios(save.getSavedInt(KeysSharedPreference.ID_USUARIO_LOGADO));
        call.enqueue(new Callback<ArrayList<Usuario>>() {
            @Override
            public void onResponse(Call<ArrayList<Usuario>> call, retrofit2.Response<ArrayList<Usuario>> response) {
                listUsuExecutores = response.body();
                SetarRecyclerView();
                //ObservableRecycler();
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Usuario>> call, Throwable t) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
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
            dots[i].setTextSize(45);
            dots[i].setTextColor(colorsInactive[0]);
            this.mViewHolderAddAtividade.mViewDotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[0]);
    }

    private int getItem(int i) {
        return this.mViewHolderAddAtividade.mViewPagerAddAtividade.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        startActivity(new Intent(AddAtividadeActivity.this, InitialNavigationActivity.class));
        finish();
    }


    /*@Override
    protected void onPause(){
        super.onPause();
    }*/

    @Override
    protected void onStop() {
        super.onStop();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        if (dialogDefaultErro != null && dialogDefaultErro.isVisible()) {
            dialogDefaultErro.dismiss();
        }
    }

    private Boolean addAtividade(final Atividade atividade) {
        AtividadeRetrofit atividadeInterface = BaseUrlRetrofit.retrofit.create(AtividadeRetrofit.class);
        final Call<Boolean> call = atividadeInterface.criarAtividade(atividade);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, retrofit2.Response<Boolean> response) {
                if (response.code() == 200) {
                    launchHomeScreen();
                } else {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    APIError error = ErrorUtils.parseError(response);
                    dialogDefaultErro.setTextErro(error.message());
                    dialogDefaultErro.show(getSupportFragmentManager(), "dialog");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                dialogDefaultErro.setTextErro(t.getMessage());
                dialogDefaultErro.show(getSupportFragmentManager(), "dialog");
            }
        });
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

    private void SetarRecyclerView() {

        // 1 - Obter a recyclerview
        this.mViewHolderAddAtivVincExecutor.mViewRecyclerViewAddAtivVincExec = findViewById(R.id.recyclerViewAddAtivVincExec);

        // Implementa o evento de click para passar por parâmetro para a ViewHolder
        OnListClickInteractionListener listener = new OnListClickInteractionListener() {
            @Override
            public void onClick(int id) {
                if (idUsuarioVinc != 0) {
                    RecyclerView re = mViewHolderAddAtivVincExecutor.mViewRecyclerViewAddAtivVincExec;
                    RadioButton radio = re.findViewById(idUsuarioVinc);
                    radio.setChecked(false);
                }
                Usuario usuario = getUsuarioTarget(id);
                idUsuarioVinc = usuario.getIdUsuario();
            }
        };

        // 2 - Definir adapter passando listagem de tarefas e listener
        addAtivVincExecAdpter = new AddAtividadeListVincExecAdapter(listUsuExecutores, listener);
        this.mViewHolderAddAtivVincExecutor.mViewRecyclerViewAddAtivVincExec.setAdapter(addAtivVincExecAdpter);

        this.mViewHolderAddAtivVincExecutor.mViewRecyclerViewAddAtivVincExec.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        // 3 - Definir um layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        this.mViewHolderAddAtivVincExecutor.mViewRecyclerViewAddAtivVincExec.setLayoutManager(linearLayoutManager);
    }

    private Usuario getUsuarioTarget(int idUsuario) {
        for (int i = 0; i < listUsuExecutores.size(); i++) {
            Usuario usuario = listUsuExecutores.get(i);
            if (usuario.getIdUsuario() == idUsuario) {
                return usuario;
            }
        }
        return null;
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

            switch (position) {
                case 0:
                    EditText dataFinalizaElement = view.findViewById(R.id.input_data_finalizacao_ativ);
                    dataFinalizaElement.addTextChangedListener(Mask.insert("##/##/####", dataFinalizaElement));
                    break;
                case 1:
                    //SetarRecyclerView(view);
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
