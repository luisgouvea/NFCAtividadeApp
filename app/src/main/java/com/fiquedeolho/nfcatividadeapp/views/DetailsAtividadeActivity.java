package com.fiquedeolho.nfcatividadeapp.views;

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

public class DetailsAtividadeActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_atividade);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout_detais_ativ);
        ScrollView scroll = (ScrollView) findViewById(R.id.ScrollRoteiroTags);

        for (int i = 0; i < 2; i++) {
            LayoutInflater inflater = LayoutInflater.from(this);

            Button botao = (Button) inflater.inflate(R.layout.activity_details_atividade_botao_inflater, linearLayout, false);
            botao.setId(i);
            botao.setText("Nome TAG" + i);
            botao.setOnClickListener(this);
            linearLayout.addView(botao);

            TextView text = (TextView) inflater.inflate(R.layout.activity_details_atividade_text_inflater, scroll, false);
            text.setText("Antecessor "+i +"\n"); // nome da TAG
        }
    }

    @Override
    public void onClick(View view) {
        //quando houver qualquer click, sempre vai para o  Details da TAG
    }
}
