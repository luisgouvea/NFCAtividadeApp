package com.fiquedeolho.nfcatividadeapp.dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.views.InitialNavigationActivity;


public class DialogCheckNFCRead extends DialogFragment {

    private NfcAdapter nfcAdapter;
    private TextView mTvMessage;
    private InitialNavigationActivity activity;

    public static DialogCheckNFCRead newInstance() {

        return new DialogCheckNFCRead();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_realizar_check_atividade,container,false);
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

    private void enableForegroundDispatchSystem() {

        Intent intent = new Intent(getContext(), InitialNavigationActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);

        IntentFilter[] intentFilters = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(getActivity(), pendingIntent, intentFilters, null);
    }

    private void disableForegroundDispatchSystem() {
        nfcAdapter.disableForegroundDispatch(getActivity());
    }

    public void intentNFCTag(Intent intent){
        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Ndef ndef = Ndef.get(tag);
            onNfcDetected(ndef);
        }
    }

    public void onNfcDetected(Ndef ndef){
        readFromNFC(ndef);
    }

    private void readFromNFC(Ndef ndef) {

        try {
            ndef.connect();
            NdefMessage ndefMessage = ndef.getNdefMessage();
            String message = new String(ndefMessage.getRecords()[0].getPayload());
            Log.d("TAG", "readFromNFC: "+message);
            mTvMessage.setText(message);
            ndef.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkNFCAtivo(){
        if (nfcAdapter == null || !nfcAdapter.isEnabled()) {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivityTarget()).create();
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

    public void setActivity(InitialNavigationActivity activity){
        this.activity = activity;
    }


    public InitialNavigationActivity getActivityTarget(){
        return this.activity;
    }
}
