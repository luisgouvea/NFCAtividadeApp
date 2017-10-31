package com.fiquedeolho.nfcatividadeapp.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fiquedeolho.nfcatividadeapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolder loginViewHolder = new ViewHolder();
    private JSONObject resultLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = new Intent(this, ListAtividadesActivity.class);

        startActivity(intent);

//        this.loginViewHolder.btnLogin = (Button) findViewById(R.id.btn_login);
//        this.loginViewHolder.senha = (EditText) findViewById(R.id.input_password);
//        this.loginViewHolder.login = (EditText) findViewById(R.id.input_email);
//
//        this.loginViewHolder.btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_login) {
            final String senha = this.loginViewHolder.senha.getText().toString();
            final String login = this.loginViewHolder.login.getText().toString();
            this.autenticarUsuario(login, senha);

            //this.postMessage();
//this.requestWithSomeHttpHeaders();

//             Response received from the server
//            Response.Listener<String> responseListener = new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    try {
//                        JSONObject jsonResponse = new JSONObject(response);
//                        boolean success = jsonResponse.getBoolean("success");
//
//                        if (success) {
//                            String g = response;
//                        } else {
//                            String f = response;
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//            LoginRequest loginRequest = new LoginRequest(responseListener);
//            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
//            queue.add(loginRequest);


        }
    }


    private static class ViewHolder {
        EditText login;
        EditText senha;
        Button btnLogin;
    }

    private void postMessage() {

        Map<String, String> parametros = new HashMap<String, String>();
        parametros.put("name", "Rete");

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://192.168.0.4:57016/api/TesteComunicacao/Comunicar", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("teste", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("testeErro", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("name", "Rete");

                return parametros;
            }

//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                HashMap<String, String> params2 = new HashMap<String, String>();
//                params2.put("name", "Val");
//                return new JSONObject(params2).toString().getBytes();
//            }

        };

        sr.setRetryPolicy(new DefaultRetryPolicy(9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }

    private void autenticarUsuario(String login, String senha) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        final Context contextoLogin = this;
        pDialog.setMessage("Aguarde, autenticando...");
        pDialog.show();
        RequestQueue rq = Volley.newRequestQueue(this);
        JSONObject params = new JSONObject();
        try {
            params.put("login", login);
            params.put("senha", senha);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "http://192.168.43.27:57016/api/TesteComunicacao/Comunicar";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                resultLogin = response;
                try {
                    String idUsuario = response.getString("Id");
                    Log.d("ResultJSONLogin", response.toString());
                    pDialog.hide();
                    Intent intent = new Intent(contextoLogin, HomeActivity.class);
                    intent.putExtra("idUsuario", idUsuario);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Erro", "Error: " + error.getMessage());
                pDialog.hide();
                Toast t = Toast.makeText(contextoLogin, "Por favor, tente novamente!", Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();
            }
        });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        rq.add(jsonObjReq);
    }

    public void requestWithSomeHttpHeaders() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.0.4:57016/api/TesteComunicacao/Comunicar";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Teste", "Teste");

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
//
    }

    private class LoginUsuarioService extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                //Create a URL object holding our url
                URL myUrl = new URL("http://nfcatividadeapi.azurewebsites.net/api/TesteComunicacao/Comunicar");
                //Create a connection
                HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();


                //Create a new InputStreamReader
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());

                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                String result = stringBuilder.toString();
                return result;
            } catch (Exception e) {
                Log.d("vv", e.getMessage());
            }
            return null;
        }
    }
}
