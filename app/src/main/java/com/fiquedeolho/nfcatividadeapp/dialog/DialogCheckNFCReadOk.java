package com.fiquedeolho.nfcatividadeapp.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;

public class DialogCheckNFCReadOk extends DialogFragment {

    private ViewHolderDialogNFCOk mViewHolderDialogNFCOk = new ViewHolderDialogNFCOk();
    private DialogCheckNFCRead dialogRead;
    private String textProximoPassoAtividade;

    public static DialogCheckNFCReadOk newInstance() {

        return new DialogCheckNFCReadOk();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_realizar_check_atividade_ok, container, false);
        this.mViewHolderDialogNFCOk.mViewTextProximoPassoCheckOk = view.findViewById(R.id.text_proximo_passo_atividade);
        this.mViewHolderDialogNFCOk.mViewTextProximoPassoCheckOk.setText(textProximoPassoAtividade);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    public void setValuesDialogOk(String proximoPassoAtividade, DialogCheckNFCRead dialogRead) {
        this.textProximoPassoAtividade = proximoPassoAtividade;
        this.dialogRead = dialogRead;
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        this.dialogRead.voltarParaListagem();
    }

    /**
     * ViewHolder dos elementos
     */
    private class ViewHolderDialogNFCOk {
        private TextView mViewTextProximoPassoCheckOk;
    }
}
