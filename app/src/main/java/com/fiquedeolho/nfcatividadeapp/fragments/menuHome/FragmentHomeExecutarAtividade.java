package com.fiquedeolho.nfcatividadeapp.fragments.menuHome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.SharedPreferences.SavePreferences;
import com.fiquedeolho.nfcatividadeapp.dialog.DialogCheckNFCRead;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.AtividadeRetrofit;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.models.Atividade;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListenerView;
import com.fiquedeolho.nfcatividadeapp.recyclerView.menuHome.executarAtividade.AtividadeListAdpter;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListener;
import com.fiquedeolho.nfcatividadeapp.util.KeysSharedPreference;
import com.fiquedeolho.nfcatividadeapp.views.InfCheckNFCActivity;
import com.fiquedeolho.nfcatividadeapp.views.InfTarefasExecutorActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Conteudo abaixo do menu
 */

public class FragmentHomeExecutarAtividade extends Fragment implements View.OnClickListener {

    private int idUsuario;
    private ViewHolderExecutarAtivHome mViewHolderExecAtivHome = new ViewHolderExecutarAtivHome();
    private static final String[] STATUS_ATIVIDADE = new String[]{"Status da Atividade", "Disponível", "Finalizada"};
    private ArrayList<Atividade> listAtividadeExecutar = new ArrayList<Atividade>();
    private ProgressDialog pDialog;
    private View rootView;
    private AtividadeListAdpter atividadeListAdapter;
    private DialogCheckNFCRead dialogCheck;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_fazer_ativ, container, false);

        this.mViewHolderExecAtivHome.mViewSpinnerAtivFazer = rootView.findViewById(R.id.status_spinner_atividade_fazer);
        ArrayAdapter adp = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_spinner_item, STATUS_ATIVIDADE);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_item);
        this.mViewHolderExecAtivHome.mViewSpinnerAtivFazer.setAdapter(adp);

        this.mViewHolderExecAtivHome.mVieBtnFiltrarAtivFazer = (Button) rootView.findViewById(R.id.btn_filtrar_atividade_fazer);
        this.mViewHolderExecAtivHome.mVieBtnFiltrarAtivFazer.setOnClickListener(this);
        this.mViewHolderExecAtivHome.mViewTextListAtividadeVaziaFazerAtividade = rootView.findViewById(R.id.textListAtividadeVaziaFazerAtiv);

         dialogCheck = DialogCheckNFCRead.newInstance();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setTitle(getString(R.string.title_progress_ativ_executar));
        pDialog.setMessage(getString(R.string.message_progress_dialog));
        pDialog.setCancelable(false);
        pDialog.show();
        SavePreferences save = new SavePreferences(getContext());
        idUsuario = save.getSavedInt(KeysSharedPreference.ID_USUARIO_LOGADO);
        getAtivExecutar();
    }

    public void MontaRestanteTela() {

        // 1 - Obter a recyclerview
        this.mViewHolderExecAtivHome.mViewRecyclerViewAtividadeFazer = rootView.findViewById(R.id.recyclerViewAtividadeFazer);


        // Implementa o evento de click para passar por parâmetro para a ViewHolder
        OnListClickInteractionListener listener = new OnListClickInteractionListener() {
            @Override
            public void onClick(int id) {
                Bundle bundle = new Bundle();
                bundle.putInt("IdAtividade", id);

                Toast.makeText(getContext(), "//TODO:REVERRR", Toast.LENGTH_LONG).show();

            }
        };

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
                popupMenu.inflate(R.menu.options_list_ativ_executor);
                popupMenu.show();
                final Bundle bundle = new Bundle();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.mnu_item_inf_tarefa_executor:
                                bundle.putInt("IdAtividade", idAtividade);

                                Intent intent = new Intent(rootView.getContext(), InfTarefasExecutorActivity.class);
                                intent.putExtras(bundle);

                                startActivity(intent);
                                break;
                            case R.id.mnu_item_registro_check_nfc_executor:
                                bundle.putInt("IdAtividade", idAtividade);

                                Intent intentRegistroCheck = new Intent(rootView.getContext(), InfCheckNFCActivity.class);
                                intentRegistroCheck.putExtras(bundle);

                                startActivity(intentRegistroCheck);
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
        atividadeListAdapter = new AtividadeListAdpter(listAtividadeExecutar, listener, listenerOptionsList);
        this.mViewHolderExecAtivHome.mViewRecyclerViewAtividadeFazer.setAdapter(atividadeListAdapter);

        this.mViewHolderExecAtivHome.mViewRecyclerViewAtividadeFazer.addItemDecoration(new DividerItemDecoration(rootView.getContext(), LinearLayoutManager.VERTICAL));

        // 3 - Definir um layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
        this.mViewHolderExecAtivHome.mViewRecyclerViewAtividadeFazer.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        /*if (id == R.id.btn_ativExecutar) {

            this.getAtivExecutar();
        } else if (id == R.id.btn_addFloatingAction) {
            Intent intent = new Intent(contextoInitial, AddAtividadeActivity.class);
            startActivity(intent);
        }*/
    }

    private void getAtivExecutar() {
        AtividadeRetrofit ativiInterface = BaseUrlRetrofit.retrofit.create(AtividadeRetrofit.class);
        final Call<ArrayList<Atividade>> call = ativiInterface.getAtividadesExecutar(this.idUsuario);
        call.enqueue(new Callback<ArrayList<Atividade>>() {
            @Override
            public void onResponse(Call<ArrayList<Atividade>> call, retrofit2.Response<ArrayList<Atividade>> response) {
                listAtividadeExecutar = response.body();
                if (listAtividadeExecutar == null || listAtividadeExecutar.size() == 0) {
                    mViewHolderExecAtivHome.mViewTextListAtividadeVaziaFazerAtividade.setVisibility(View.VISIBLE);
                } else {
                    mViewHolderExecAtivHome.mViewTextListAtividadeVaziaFazerAtividade.setVisibility(View.GONE);
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

    /**
     * ViewHolder dos elementos
     */
    private class ViewHolderExecutarAtivHome {

        private Spinner mViewSpinnerAtivFazer;
        private Button mVieBtnFiltrarAtivFazer;
        private RecyclerView mViewRecyclerViewAtividadeFazer;
        private TextView mViewTextListAtividadeVaziaFazerAtividade;
    }
}