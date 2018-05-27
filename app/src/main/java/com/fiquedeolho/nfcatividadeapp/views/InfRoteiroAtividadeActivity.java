package com.fiquedeolho.nfcatividadeapp.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fiquedeolho.nfcatividadeapp.R;

public class InfRoteiroAtividadeActivity extends AppCompatActivity {

    private WebView webView;
    public static int idAtividade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf_roteiro_atividade);


        webView = findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setInitialScale(180);
        //webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        webView.loadUrl("file:///android_asset/index.html");
        webView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url){
                webView.loadUrl("javascript:init('" + "TESTE VIEW" + "')");
            }
        });
    }
}
