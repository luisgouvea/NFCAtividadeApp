package com.fiquedeolho.nfcatividadeapp.models;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Luis Eduardo on 29/10/2017.
 */

public class LoginRequest extends StringRequest {

    private static final String LOGIN_REQUEST_URL = "http://192.168.0.4:57016/api/TesteComunicacao/Comunicar";
    private Map<String, String> params;
    public LoginRequest(Response.Listener<String> listener) {
        super(Method.GET, LOGIN_REQUEST_URL, listener, null);

    }

//    @Override
//    protected Map<String, String> getParams() throws AuthFailureError {
//        params = new HashMap<String, String>();
//        params.put("teste","teste");
//        return params;
//    }
}
