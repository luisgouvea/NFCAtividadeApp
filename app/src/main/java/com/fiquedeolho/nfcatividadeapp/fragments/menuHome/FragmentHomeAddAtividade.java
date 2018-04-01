package com.fiquedeolho.nfcatividadeapp.fragments.menuHome;

import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.AtividadeRetrofit;
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.models.Atividade;
import com.fiquedeolho.nfcatividadeapp.recyclerView.menuHome.OnListClickInteractionListener;
import com.fiquedeolho.nfcatividadeapp.recyclerView.menuHome.OnListClickInteractionListenerOptionsList;
import com.fiquedeolho.nfcatividadeapp.recyclerView.menuHome.addAtividade.AtividadeListAdpter;
import com.fiquedeolho.nfcatividadeapp.views.AddAtividadeActivity;
import com.fiquedeolho.nfcatividadeapp.views.DetailsAtividadeActivity;
import com.fiquedeolho.nfcatividadeapp.views.InfTarefasActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * Conteudo abaixo do menu.
 * Relacionado a todas as atividades que o usuario logado criou.
 */

public class FragmentHomeAddAtividade extends Fragment implements View.OnClickListener{

    private ViewHolderAddAtivHome mViewHolderAddAtivHome = new ViewHolderAddAtivHome();

    private String idUsuario;
    private static final String[] STATUS_ATIVIDADE = new String[]{"Status da Atividade", "Disponível", "Finalizada"};
    private ArrayList<Atividade> listAtividadeAdicionadas = new ArrayList<Atividade>();
    private ProgressDialog pDialog;
    private View rootView;
    private AtividadeListAdpter atividadeListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_add_ativ, container, false);

        this.mViewHolderAddAtivHome.mViewFloatingActionButton = rootView.findViewById(R.id.btn_addFloatingAction);
        this.mViewHolderAddAtivHome.mViewFloatingActionButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onStart() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setTitle(getString(R.string.title_progress_ativ_adicionadas));
        pDialog.setMessage(getString(R.string.message_progress_dialog));
        pDialog.setCancelable(false);
        pDialog.show();
        Bundle budle = getArguments();
        if (budle != null) {
            idUsuario = budle.getString("idUsuario");
        } else {
            //TODO REVER ESSA LOGICA DEPOIS ARRUMAR ISSO
            idUsuario = "1"; //
        }
        getAtivAdicionadas();
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_addFloatingAction) {
            Intent intent = new Intent(view.getContext(), AddAtividadeActivity.class);
            startActivity(intent);
        }
    }

    private void getAtivAdicionadas() {
        AtividadeRetrofit ativiInterface = BaseUrlRetrofit.retrofit.create(AtividadeRetrofit.class);
        final Call<ArrayList<Atividade>> call = ativiInterface.getAtividadesAdicionadas(this.idUsuario);
        // TODO: Rever essa logica de Thread, ta gambiarra
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        call.enqueue(new Callback<ArrayList<Atividade>>() {
            @Override
            public void onResponse(Call<ArrayList<Atividade>> call, retrofit2.Response<ArrayList<Atividade>> response) {
                listAtividadeAdicionadas = response.body();
                MontaRestanteTela();
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
        this.mViewHolderAddAtivHome.mViewSpinnerAtivAdd = rootView.findViewById(R.id.status_spinner_atividade_add);
        ArrayAdapter adp = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_spinner_item, STATUS_ATIVIDADE);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_item);
        this.mViewHolderAddAtivHome.mViewSpinnerAtivAdd.setAdapter(adp);

        this.mViewHolderAddAtivHome.mVieBtnFiltrarAtivAdd = (Button) rootView.findViewById(R.id.btn_filtrar_atividade_add);
        this.mViewHolderAddAtivHome.mVieBtnFiltrarAtivAdd.setOnClickListener(this);

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

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    mViewHolderAddAtivHome.mViewFloatingActionButton.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        // Implementa o evento de click para passar por parâmetro para a ViewHolder
        OnListClickInteractionListener listener = new OnListClickInteractionListener() {
            @Override
            public void onClick(int id) {
                Bundle bundle = new Bundle();
                bundle.putInt("IdAtividade", id);

                Intent intent = new Intent(rootView.getContext(), DetailsAtividadeActivity.class);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        };

        /**
         * OnListClickInteractionListenerOptionsList interface QUE CRIEI
         Implementacao da acao dos menus na listagem das atividade dentro do RecyclerView
         Parametro: O viewTarget em questao, representa o Text (tres pontinhos) clicado
         */
        OnListClickInteractionListenerOptionsList listenerOptionsList = new OnListClickInteractionListenerOptionsList() {
            @Override
            public void onClick(final View viewTarget) {
                final int idAtividade = viewTarget.getId();
                PopupMenu popupMenu = new PopupMenu(getContext(), viewTarget);
                popupMenu.inflate(R.menu.options_list_ativ_adicionadas);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.mnu_inf_tag:
                                Toast.makeText(getContext(), "Inf Tarefas", Toast.LENGTH_LONG).show();
                                Bundle bundle = new Bundle();
                                bundle.putInt("IdAtividade", idAtividade);

                                Intent intent = new Intent(rootView.getContext(), InfTarefasActivity.class);
                                intent.putExtras(bundle);

                                startActivity(intent);
                                break;
                            case R.id.mnu_deletar_ativ:
                                Toast.makeText(getContext(), "Deletado", Toast.LENGTH_LONG).show();
                                int positionDeletar = descobrePositionArrayListAtiv(idAtividade);
                                listAtividadeAdicionadas.remove(positionDeletar);
                                ObservableRecycler();
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
            }
        };

        // 2 - Definir adapter passando listagem de carros e listener
        atividadeListAdapter = new AtividadeListAdpter(listAtividadeAdicionadas, listener, listenerOptionsList);
        this.mViewHolderAddAtivHome.mViewRecyclerViewAtividadeAdd.setAdapter(atividadeListAdapter);

        this.mViewHolderAddAtivHome.mViewRecyclerViewAtividadeAdd.addItemDecoration(new DividerItemDecoration(rootView.getContext(), LinearLayoutManager.VERTICAL));

        // 3 - Definir um layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
        this.mViewHolderAddAtivHome.mViewRecyclerViewAtividadeAdd.setLayoutManager(linearLayoutManager);
    }

    private int descobrePositionArrayListAtiv(int idAtividade){
        for (int i = 0; i < listAtividadeAdicionadas.size(); i++ ){
            Atividade ativ = listAtividadeAdicionadas.get(i);
            if(ativ.getId() == idAtividade){
                return i;
            }
        }
        return 0;
    }

    private void ObservableRecycler() {
        atividadeListAdapter.notifyDataSetChanged();
    }

    /**
     * ViewHolder dos elementos
     */
    private class ViewHolderAddAtivHome {

        private Button mVieBtnFiltrarAtivAdd;
        private Spinner mViewSpinnerAtivAdd;
        private RecyclerView mViewRecyclerViewAtividadeAdd;
        private FloatingActionButton mViewFloatingActionButton;
    }
}
