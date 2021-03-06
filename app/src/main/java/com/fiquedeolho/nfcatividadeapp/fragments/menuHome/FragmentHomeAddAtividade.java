package com.fiquedeolho.nfcatividadeapp.fragments.menuHome;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.SharedPreferences.SavePreferences;
import com.fiquedeolho.nfcatividadeapp.models.FiltroPesquisaHome;
import com.fiquedeolho.nfcatividadeapp.recyclerView.menuHome.addAtividade.AtividadeViewHolder;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.AtividadeRetrofit;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.models.Atividade;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListener;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListenerView;
import com.fiquedeolho.nfcatividadeapp.recyclerView.menuHome.addAtividade.AtividadeListAdpter;
import com.fiquedeolho.nfcatividadeapp.util.KeysSharedPreference;
import com.fiquedeolho.nfcatividadeapp.views.AddAtividadeActivity;
import com.fiquedeolho.nfcatividadeapp.views.DetalhesAtividadeActivity;
import com.fiquedeolho.nfcatividadeapp.views.EditarAtividadeActivity;
import com.fiquedeolho.nfcatividadeapp.views.InfCheckNFCActivity;
import com.fiquedeolho.nfcatividadeapp.views.InfRoteiroAtividadeActivity;
import com.fiquedeolho.nfcatividadeapp.views.InfTarefasCriadorActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * Conteudo abaixo do menu.
 * Relacionado a todas as atividades que o usuario logado criou.
 */

public class FragmentHomeAddAtividade extends Fragment implements View.OnClickListener, AtividadeViewHolder.ClickListener {

    private ViewHolderAddAtivHome mViewHolderAddAtivHome = new ViewHolderAddAtivHome();

    private int idUsuario;
    private ArrayList<Atividade> listAtividadeAdicionadas = new ArrayList<Atividade>();
    private ProgressDialog pDialog;
    private View rootView;
    private AtividadeListAdpter atividadeListAdapter;
    private AtividadeViewHolder.ClickListener listenerFiltro = this;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_add_ativ, container, false);

        this.mViewHolderAddAtivHome.mViewFloatingActionButton = rootView.findViewById(R.id.btn_addFloatingAction);
        this.mViewHolderAddAtivHome.mViewFloatingActionButton.setOnClickListener(this);

        this.mViewHolderAddAtivHome.mViewTextListAtividadeVaziaAddAtividade = rootView.findViewById(R.id.textListAtividadeVaziaHomeAddAtiv);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setTitle(getString(R.string.title_progress_ativ_adicionadas));
        pDialog.setMessage(getString(R.string.message_progress_dialog));
        pDialog.setCancelable(false);
        pDialog.show();
        SavePreferences save = new SavePreferences(getContext());
        idUsuario = save.getSavedInt(KeysSharedPreference.ID_USUARIO_LOGADO);
        getAtivAdicionadas();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_addFloatingAction) {
            Intent intent = new Intent(view.getContext(), AddAtividadeActivity.class);
            startActivity(intent);
        }
    }

    private void filtrarAtividades(int idStatusAtividade, EditText dataCriacao) {
        if (idStatusAtividade == 0 && dataCriacao.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, preencha algum campo!", Toast.LENGTH_LONG).show();
        } else {
            FiltroPesquisaHome filtro = new FiltroPesquisaHome();
            if (idStatusAtividade != 0) {
                filtro.setIdStatusAtividade(idStatusAtividade);
            }
            if (dataCriacao.getText() != null && !dataCriacao.getText().toString().equals("")) {
                //2013-09-29
                String[] dataSplit = dataCriacao.getText().toString().split("/");
                String ano = dataSplit[2];
                String mes = dataSplit[1];
                String dia = dataSplit[0];
                String newDate = ano + "-" + mes + "-" + dia;
                if (!newDate.isEmpty()) {
                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = (Date) formatter.parse(newDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    filtro.setDataCriacao(date);
                }
            }
            SavePreferences save = new SavePreferences(getContext());
            int idUsuario = save.getSavedInt(KeysSharedPreference.ID_USUARIO_LOGADO);
            filtro.setIdUsuario(idUsuario);
            requestFiltro(filtro);
        }

    }

    private void requestFiltro(FiltroPesquisaHome filtro) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        //pDialog.setTitle(getString(R.string.title_progress_ativ_adicionadas));
        pDialog.setMessage(getString(R.string.message_progress_dialog));
        pDialog.setCancelable(false);
        pDialog.show();
        AtividadeRetrofit ativiInterface = BaseUrlRetrofit.retrofit.create(AtividadeRetrofit.class);
        final Call<ArrayList<Atividade>> call = ativiInterface.filtrarAtividadesAdicionar(filtro);
        call.enqueue(new Callback<ArrayList<Atividade>>() {
            @Override
            public void onResponse(Call<ArrayList<Atividade>> call, retrofit2.Response<ArrayList<Atividade>> response) {
                listAtividadeAdicionadas = response.body();
                if (listAtividadeAdicionadas == null || listAtividadeAdicionadas.size() == 0) {
                    filtroPesquisaEmpty();
                } else {
                    mViewHolderAddAtivHome.mViewRecyclerViewAtividadeAdd.setVisibility(View.VISIBLE);
                    MontaRestanteTela();
                    Toast.makeText(getActivity(), "Filtro realizado!", Toast.LENGTH_SHORT).show();
                }
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Atividade>> call, Throwable t) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
    }

    public void filtroPesquisaEmpty() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Ops");
        alertDialog.setMessage("Nenhum resultado encontrado.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }

    private void getAtivAdicionadas() {
        AtividadeRetrofit ativiInterface = BaseUrlRetrofit.retrofit.create(AtividadeRetrofit.class);
        final Call<ArrayList<Atividade>> call = ativiInterface.getAtividadesAdicionadas(this.idUsuario);
        call.enqueue(new Callback<ArrayList<Atividade>>() {
            @Override
            public void onResponse(Call<ArrayList<Atividade>> call, retrofit2.Response<ArrayList<Atividade>> response) {
                listAtividadeAdicionadas = response.body();
                if (listAtividadeAdicionadas == null || listAtividadeAdicionadas.size() == 0) {
                    mViewHolderAddAtivHome.mViewTextListAtividadeVaziaAddAtividade.setVisibility(View.VISIBLE);
                } else {
                    mViewHolderAddAtivHome.mViewTextListAtividadeVaziaAddAtividade.setVisibility(View.GONE);
                    MontaRestanteTela();
                }
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Atividade>> call, Throwable t) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
    }

    public void MontaRestanteTela() {

        // 1 - Obter a recyclerview
        this.mViewHolderAddAtivHome.mViewRecyclerViewAtividadeAdd = rootView.findViewById(R.id.recyclerViewAtividadeAdd);

        this.mViewHolderAddAtivHome.mViewRecyclerViewAtividadeAdd.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && mViewHolderAddAtivHome.mViewFloatingActionButton.getVisibility() == View.VISIBLE) {
                    mViewHolderAddAtivHome.mViewFloatingActionButton.hide();
                } else if (dy < 0 && mViewHolderAddAtivHome.mViewFloatingActionButton.getVisibility() != View.VISIBLE) {
                    mViewHolderAddAtivHome.mViewFloatingActionButton.show();
                }
            }

            /*@Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    mViewHolderAddAtivHome.mViewFloatingActionButton.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }*/
        });

        /**
         * OnListClickInteractionListenerView interface QUE CRIEI
         Implementacao da acao dos menus na listagem das atividade dentro do RecyclerView
         Parametro: O viewTarget em questao, representa o Text (tres pontinhos) clicado
         */
        OnListClickInteractionListenerView listenerOptionsList = new OnListClickInteractionListenerView() {
            @Override
            public void onClick(final View viewTarget) {
                final int idAtividade = viewTarget.getId();
                PopupMenu popupMenu = new PopupMenu(getContext(), viewTarget);
                popupMenu.inflate(R.menu.options_list_ativ_criador);
                popupMenu.show();
                final Bundle bundle = new Bundle();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.mnu_inf_tarefa:
                                bundle.putInt("IdAtividade", idAtividade);

                                Intent intent = new Intent(rootView.getContext(), InfTarefasCriadorActivity.class);
                                intent.putExtras(bundle);

                                startActivity(intent);
                                break;
                            case R.id.mnu_detalhes_atividade:
                                DetalhesAtividadeActivity.idAtividade = idAtividade;
                                Intent intentDetalhesAtiv = new Intent(rootView.getContext(), DetalhesAtividadeActivity.class);

                                startActivity(intentDetalhesAtiv);
                                break;
                            case R.id.mnu_item_inf_roteiro_exec_ativ:
                                Intent intentInfRoteiroExecAtiv = new Intent(rootView.getContext(), InfRoteiroAtividadeActivity.class);
                                InfRoteiroAtividadeActivity.idAtividade = idAtividade;
                                startActivity(intentInfRoteiroExecAtiv);
                                break;
                            case R.id.mnu_item_registro_check_nfc_criador:
                                bundle.putInt("IdAtividade", idAtividade);

                                Intent intentRegistroCheck = new Intent(rootView.getContext(), InfCheckNFCActivity.class);
                                intentRegistroCheck.putExtras(bundle);

                                startActivity(intentRegistroCheck);
                                break;
                            case R.id.mnu_editar_ativ:
                                EditarAtividadeActivity.idAtividade = idAtividade;
                                Intent intentEditarAtiv = new Intent(rootView.getContext(), EditarAtividadeActivity.class);
                                startActivity(intentEditarAtiv);
                                break;
                            case R.id.mnu_deletar_ativ:
                                removerAtividade(idAtividade);
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
            }
        };

        for (int i = 0; i < listAtividadeAdicionadas.size(); i++) {
            Atividade ativ = listAtividadeAdicionadas.get(i);
            if (ativ.getIdStatus() == 4) { // removida
                listAtividadeAdicionadas.remove(i);
            }
        }
        listAtividadeAdicionadas.add(0, new Atividade());
        // 2 - Definir adapter passando listagem de carros e listener
        atividadeListAdapter = new AtividadeListAdpter(listAtividadeAdicionadas, listenerOptionsList, listenerFiltro);
        this.mViewHolderAddAtivHome.mViewRecyclerViewAtividadeAdd.setAdapter(atividadeListAdapter);

        this.mViewHolderAddAtivHome.mViewRecyclerViewAtividadeAdd.addItemDecoration(new DividerItemDecoration(rootView.getContext(), LinearLayoutManager.VERTICAL));

        // 3 - Definir um layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
        this.mViewHolderAddAtivHome.mViewRecyclerViewAtividadeAdd.setLayoutManager(linearLayoutManager);
    }

    private void removerAtividade(int idAtividade) {
        requestRemoverAtividade(idAtividade);
    }

    private void requestRemoverAtividade(final int idAtividade) {
        AtividadeRetrofit ativiInterface = BaseUrlRetrofit.retrofit.create(AtividadeRetrofit.class);
        final Call<Boolean> call = ativiInterface.removerAtividadeById(idAtividade);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, retrofit2.Response<Boolean> response) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                if (response.code() != 200) {
                    Toast.makeText(getContext(), "Ocorreu um erro, tente novamente, por favor", Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(getContext(), "Atividade Removida", Toast.LENGTH_LONG).show();
                    int positionDeletar = descobrePositionArrayListAtiv(idAtividade);
                    listAtividadeAdicionadas.remove(positionDeletar);
                    ObservableRecycler();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Toast.makeText(getContext(), "Ocorreu um erro, tente novamente, por favor", Toast.LENGTH_LONG).show();
            }
        });
    }

    private int descobrePositionArrayListAtiv(int idAtividade) {
        for (int i = 0; i < listAtividadeAdicionadas.size(); i++) {
            Atividade ativ = listAtividadeAdicionadas.get(i);
            if (ativ.getId() == idAtividade) {
                return i;
            }
        }
        return 0;
    }

    private void ObservableRecycler() {
        atividadeListAdapter.notifyDataSetChanged();
    }

    @Override
    public void btnFiltrarClicked(int idStatusAtividade, EditText dataCriacao) {
        filtrarAtividades(idStatusAtividade, dataCriacao);
    }

    /**
     * ViewHolder dos elementos
     */
    private class ViewHolderAddAtivHome {

        private RecyclerView mViewRecyclerViewAtividadeAdd;
        private FloatingActionButton mViewFloatingActionButton;
        private TextView mViewTextListAtividadeVaziaAddAtividade;
    }
}
