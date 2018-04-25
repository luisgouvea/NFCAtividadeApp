package com.fiquedeolho.nfcatividadeapp.dialog;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;

public class DialogCheckNFCReadFail extends DialogFragment{

    private ViewHolderDialogNFCFail mViewHolderDialogNFCFail = new ViewHolderDialogNFCFail();
    private String textCausaErroCheckFail;
    private String textSolucaoErroCheckFail;

    public static DialogCheckNFCReadFail newInstance() {

        return new DialogCheckNFCReadFail();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_realizar_check_atividade_fail, container, false);
        this.mViewHolderDialogNFCFail.mViewTextCausaErroCheckFail = view.findViewById(R.id.textCausaErroCheckFail);
        this.mViewHolderDialogNFCFail.mViewTextCausaErroCheckFail.setText(this.textCausaErroCheckFail);
        this.mViewHolderDialogNFCFail.mViewTextSolucaoErroCheckFail = view.findViewById(R.id.textSolucaoErroCheckFail);
        this.mViewHolderDialogNFCFail.mViewTextSolucaoErroCheckFail.setText(this.textSolucaoErroCheckFail);
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    public void setValuesDialogFail(String textoCausaErro, String textoSolucaoErro) {
        this.textCausaErroCheckFail = textoCausaErro;
        this.textSolucaoErroCheckFail = textoSolucaoErro;
    }

    /**
     * ViewHolder dos elementos
     */
    private class ViewHolderDialogNFCFail {
        private TextView mViewTextCausaErroCheckFail;
        private TextView mViewTextSolucaoErroCheckFail;
    }
}
