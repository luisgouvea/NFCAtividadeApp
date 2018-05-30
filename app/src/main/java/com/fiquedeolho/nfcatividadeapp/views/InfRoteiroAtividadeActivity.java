package com.fiquedeolho.nfcatividadeapp.views;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.APIError;
import com.fiquedeolho.nfcatividadeapp.models.Tarefa;
import com.fiquedeolho.nfcatividadeapp.retrofit.implementation.TarefaImplementation;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfRoteiroAtividadeActivity<T> extends AppCompatActivity implements Callback<T> {

    private WebView webView;
    private TarefaImplementation tarefaImplementation = new TarefaImplementation();

    private Callback<T> requestRetrofit = this;
    public static int idAtividade;
    ArrayList<Tarefa> listaTarefas = null;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf_roteiro_atividade);
        webView = findViewById(R.id.webView1);
        pDialog = new ProgressDialog(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getInfRoteiroExecucao();
    }

    @Override
    protected void onStop() {
        super.onStop();
        closeProgressDialog();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeProgressDialog();
    }

    private void getInfRoteiroExecucao() {
        pDialog.setMessage(getString(R.string.message_progress_dialog));
        pDialog.setCancelable(false);
        pDialog.show();
        tarefaImplementation.requestSelectAllObjectsByIdAtividade(requestRetrofit, idAtividade);
    }

    private void executeWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setInitialScale(180);
        //webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        webView.loadUrl("file:///android_asset/index.html");
        webView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url){
                //webView.loadUrl("javascript:init('" + "TESTE VIEW" + "')");

                String json = new Gson().toJson(listaTarefas);

                webView.loadUrl("javascript:init('" + json + "')");

                closeProgressDialog();
            }
        });
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        responseSelectAllObjectByIdAtividade(call, response);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        closeProgressDialog();
        Toast.makeText(getApplicationContext(), "Ocorreu um erro genérico" + t.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void responseSelectAllObjectByIdAtividade(Call<T> call, Response<T> response) {
        APIError error = null;
        String typeResponse = tarefaImplementation.findResponse(call, response);
        if (typeResponse != "") {
            switch (typeResponse) {
                case "erro":
                    error = tarefaImplementation.resultError();
                    if (error.message() != null) {
                        Toast.makeText(getApplicationContext(), error.message(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Ocorreu um erro genérico", Toast.LENGTH_LONG).show();
                    }
                    break;
                case "getTarefasByIdAtividade":
                    listaTarefas = tarefaImplementation.resultSelectAllObjectsByIdAtividade();
                    if(listaTarefas == null || listaTarefas.size() == 0){
                        closeProgressDialog();
                        showAtividadeSemTarefas();
                        break;
                    }else {
                        executeWebView();
                        break;
                    }
            }
        }
        closeProgressDialog();
    }

    public void showAtividadeSemTarefas() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Ops");
        alertDialog.setMessage("Ainda não existe nenhuma tarefa vinculada a essa atividade.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.show();
    }

    private void closeProgressDialog(){
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }
}
