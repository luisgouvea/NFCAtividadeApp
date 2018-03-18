package com.fiquedeolho.nfcatividadeapp.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.Atividade;
import com.fiquedeolho.nfcatividadeapp.models.TAG;

import java.util.ArrayList;

public class DetailsAtividadeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_atividade);
        Atividade ativ = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ativ = extras.getParcelable("atividadeForDetalhar");
        }

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout_detais_ativ);
        ScrollView scroll = (ScrollView) findViewById(R.id.ScrollRoteiroTags);
        ArrayList<TAG> listTag = new ArrayList<>();
        listTag = ativ.getListTags();
        StringBuilder antecessoresConcat = new StringBuilder();
        for (int i = 0; i < listTag.size(); i++) {
            TAG tag = listTag.get(i);
            LayoutInflater inflater = LayoutInflater.from(this);

            Button botao = (Button) inflater.inflate(R.layout.activity_details_atividade_botao_inflater, linearLayout, false);
            botao.setId(tag.getId());
            botao.setText(tag.getNome());
            botao.setOnClickListener(this);
            linearLayout.addView(botao);

            //TextView text = (TextView) inflater.inflate(R.layout.activity_details_atividade_text_inflater, scroll, false);
            ArrayList<String> listAntecessores = tag.getListAntecessores();
            //StringBuilder antecessoresConcat = new StringBuilder();
            for (int j = 0; j< listAntecessores.size();j++){
                antecessoresConcat.append(listAntecessores.get(i) + "\n");
            }
//            text.setText(antecessoresConcat.toString()); // nome da TAG
//            linearLayout.addView(text);
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        TextView text = (TextView) inflater.inflate(R.layout.activity_details_atividade_text_inflater, scroll, false);
        text.setText(antecessoresConcat.toString()); // nome da TAG
        linearLayout.addView(text);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.realizar_Check_nfc) {
            Intent intent = new Intent(this, CheckNFCActivity.class);
            startActivity(intent);
        }
    }
}
