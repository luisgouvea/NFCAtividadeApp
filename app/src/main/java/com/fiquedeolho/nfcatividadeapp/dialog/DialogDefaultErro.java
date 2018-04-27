package com.fiquedeolho.nfcatividadeapp.dialog;


import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;

public class DialogDefaultErro extends DialogFragment implements View.OnClickListener {

    private ViewHolderDialogErrorDefault mViewHolderDialogErrorDefault = new ViewHolderDialogErrorDefault();
    private String textDescricaoErro;

    public static DialogDefaultErro newInstance() {

        return new DialogDefaultErro();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_default_erro, container, false);

        this.mViewHolderDialogErrorDefault.mViewTextDescricaoErro = view.findViewById(R.id.text_descricao_erro);
        this.mViewHolderDialogErrorDefault.mViewBtnOk = view.findViewById(R.id.btn_ok_dialog);

        this.mViewHolderDialogErrorDefault.mViewTextDescricaoErro.setText(this.textDescricaoErro);
        this.mViewHolderDialogErrorDefault.mViewBtnOk.setOnClickListener(this);

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        Point size = new Point();

        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        int width = size.x;

        window.setLayout((int) (width * 0.90), (int) (width * 1.50));
        window.setGravity(Gravity.CENTER);
    }

    public void setTextErro(String message) {
        this.textDescricaoErro = message;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_ok_dialog){
            dismiss();
        }
    }


    /**
     * ViewHolder dos elementos
     */
    private class ViewHolderDialogErrorDefault {
        private TextView mViewTextDescricaoErro;
        private Button mViewBtnOk;
    }
}
