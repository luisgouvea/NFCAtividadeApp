package com.fiquedeolho.nfcatividadeapp.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.Usuario;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.UsuarioRetrofit;

import retrofit2.Call;
import retrofit2.Callback;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolderCreateAccount mViewHolderCreateAccount = new ViewHolderCreateAccount();
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        this.mViewHolderCreateAccount.nome = findViewById(R.id.nome_create_account);
        this.mViewHolderCreateAccount.login = findViewById(R.id.login_create_account);
        this.mViewHolderCreateAccount.senha = findViewById(R.id.senha_create_account);
        this.mViewHolderCreateAccount.btnCreateAccount = findViewById(R.id.btn_create_account);
        this.mViewHolderCreateAccount.btnCreateAccount.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_create_account){
            requestCreateAccount();
        }
    }

    private void requestCreateAccount() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setTitle(getString(R.string.criando_conta_usu));
        pDialog.setMessage(getString(R.string.message_progress_dialog));
        pDialog.show();
        Usuario usuario = new Usuario();
        usuario.setNome(this.mViewHolderCreateAccount.nome.getText().toString());
        usuario.setLogin(this.mViewHolderCreateAccount.login.getText().toString());
        usuario.setSenha(this.mViewHolderCreateAccount.senha.getText().toString());
        UsuarioRetrofit usuInterface = BaseUrlRetrofit.retrofit.create(UsuarioRetrofit.class);
        final Call<Boolean> call = usuInterface.criarConta(usuario);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, retrofit2.Response<Boolean> response) {
                if(response.code() == 200) {
                    Boolean contaCriada = response.body();
                    Intent intent = new Intent(getApplicationContext(), InitialNavigationActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    closeLoadingRequest();
//                    APIError error = ErrorUtils.parseError(response);
//                    dialogDefaultErro.setTextErro(error.message());
//                    dialogDefaultErro.show(getSupportFragmentManager(),"dialog");
                }
                closeLoadingRequest();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                closeLoadingRequest();
//                dialogDefaultErro.setTextErro(t.getMessage());
//                dialogDefaultErro.show(getSupportFragmentManager(),"dialog");
            }
        });
    }

    private void closeLoadingRequest(){
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    private class ViewHolderCreateAccount{
        private EditText nome;
        private EditText login;
        private EditText senha;
        private Button btnCreateAccount;
    }
}
