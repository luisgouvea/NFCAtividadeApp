package com.fiquedeolho.nfcatividadeapp.dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.TarefaCheckRetrofit;
import com.fiquedeolho.nfcatividadeapp.views.InfTarefasExecutorActivity;

import java.io.UnsupportedEncodingException;

import retrofit2.Call;
import retrofit2.Callback;


public class DialogCheckNFCRead extends DialogFragment {

    private NfcAdapter nfcAdapter;
    private TextView mTvMessage;
    private int idTarefa;

    public static DialogCheckNFCRead newInstance() {

        return new DialogCheckNFCRead();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_realizar_check_atividade, container, false);
        initViews(view);

        NfcManager manager = (NfcManager) getActivity().getSystemService(Context.NFC_SERVICE);
        nfcAdapter = manager.getDefaultAdapter();

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    private void initViews(View view) {

        mTvMessage = (TextView) view.findViewById(R.id.tv_message);
    }

    @Override
    public void onStart() {
        super.onStart();

        checkNFCAtivo();
        Window window = getDialog().getWindow();
        Point size = new Point();

        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        int width = size.x;

        window.setLayout((int) (width * 0.90), (int) (width * 0.75));
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void onResume() {
        super.onResume();
        enableForegroundDispatchSystem();
    }

    @Override
    public void onPause() {
        super.onPause();
        disableForegroundDispatchSystem();
    }

    @Override
    public void onStop() {
        super.onStop();
        disableForegroundDispatchSystem();
    }

    private void enableForegroundDispatchSystem() {

        Intent intent = new Intent(getContext(), InfTarefasExecutorActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);

        IntentFilter[] intentFilters = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(getActivity(), pendingIntent, intentFilters, null);
    }

    private void disableForegroundDispatchSystem() {
        nfcAdapter.disableForegroundDispatch(getActivity());
    }

    public void intentNFCTag(Intent intent, int idTarefa) {
        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            this.idTarefa = idTarefa;
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (parcelables != null && parcelables.length > 0) {
                readTextFromMessage((NdefMessage) parcelables[0]);
            } else {
                Toast.makeText(getActivity(), "Não foi possivel ler a TAG!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void readTextFromMessage(NdefMessage ndefMessage) {

        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if (ndefRecords != null && ndefRecords.length > 0) {

            NdefRecord ndefRecord = ndefRecords[0];

            String tagContent = getTextFromNdefRecord(ndefRecord);

            Log.d("Teste", tagContent);
            realizarCheck(Integer.valueOf(tagContent));
            //Toast.makeText(this, "Conteúdo Lido!", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(this, "No NDEF records found!", Toast.LENGTH_SHORT).show();
        }

    }

    public String getTextFromNdefRecord(NdefRecord ndefRecord) {
        String tagContent = null;
        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1,
                    payload.length - languageSize - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("getTextFromNdefRecord", e.getMessage(), e);
        }
        return tagContent;
    }

    public void checkNFCAtivo() {
        if (nfcAdapter == null || !nfcAdapter.isEnabled()) {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Ops");
            alertDialog.setMessage("O NFC do dispositivo não está habilitado. \nPor favor, ative.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    private Boolean realizarCheck(int idTag) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Aguarde");
        pDialog.show();
        TarefaCheckRetrofit tarefaCheckInterface = BaseUrlRetrofit.retrofit.create(TarefaCheckRetrofit.class);

        final Call<Boolean> call = tarefaCheckInterface.realizarCheck(idTag, idTarefa);

        call.enqueue(new Callback<Boolean>() {

            @Override
            public void onResponse(Call<Boolean> call, retrofit2.Response<Boolean> response) {
                Boolean result = response.body();
                if (result) {
                    Toast.makeText(getActivity(), "É um check correto!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Não é um check correto!", Toast.LENGTH_SHORT).show();
                }
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                }, 2000);
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
        return true;
    }
}