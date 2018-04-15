package com.fiquedeolho.nfcatividadeapp.views;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.fiquedeolho.nfcatividadeapp.R;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

public class VinculoTarefaETagActivity extends AppCompatActivity {

    NfcAdapter nfcAdapter;
    private int IdAtividade;
    private int IdTarefa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setContentView(R.layout.activity_vinculo_tarefa_e_tag);

        NfcManager manager = (NfcManager) this.getSystemService(Context.NFC_SERVICE);
        nfcAdapter = manager.getDefaultAdapter();
        checkNFCAtivo();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            IdAtividade = extras.getInt("IdAtividade"); // sempre vem da activity fragExecAtividade
            IdTarefa = extras.getInt("IdTarefa");
        }
    }

    public void checkNFCAtivo(){
        if (nfcAdapter == null || !nfcAdapter.isEnabled()) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
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
    /**
     Click no botao voltar da activity
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backToInfTarefas();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void backToInfTarefas() {
        Intent resultIntent = new Intent(this, InfTarefasCriadorActivity.class);
        resultIntent.putExtra("IdAtividade", IdAtividade);
        startActivity(resultIntent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableForegroundDispatchSystem();
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableForegroundDispatchSystem();
    }

    private void enableForegroundDispatchSystem() {

        Intent intent = new Intent(this, VinculoTarefaETagActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        IntentFilter[] intentFilters = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    private void disableForegroundDispatchSystem() {
        nfcAdapter.disableForegroundDispatch(this);
    }

    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage) {

        try {

            if (tag == null) {
                Toast.makeText(this, "Tag object cannot be null", Toast.LENGTH_SHORT).show();
                return;
            }

            Ndef ndef = Ndef.get(tag);

            if (ndef == null) {
                // format tag with the ndef format and writes the message.
                formatTag(tag, ndefMessage);
            } else {
                ndef.connect();

                if (!ndef.isWritable()) {
                    Toast.makeText(this, "Tag is not writable!", Toast.LENGTH_SHORT).show();

                    ndef.close();
                    return;
                }

                ndef.writeNdefMessage(ndefMessage);
                ndef.close();

                Toast.makeText(this, "Vinculo realizado com sucesso!", Toast.LENGTH_SHORT).show();
                final Context context = this;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Bundle bundle = new Bundle();
                        bundle.putInt("IdAtividade", IdAtividade);
                        Intent intentActivityListTarefas = new Intent(context, InfTarefasCriadorActivity.class);
                        intentActivityListTarefas.putExtras(bundle);
                        startActivity(intentActivityListTarefas);
                    }
                }, 2000);
            }

        } catch (Exception e) {
            Log.e("writeNdefMessage", e.getMessage());
        }

    }

    private void formatTag(Tag tag, NdefMessage ndefMessage) {
        try {

            NdefFormatable ndefFormatable = NdefFormatable.get(tag);

            if (ndefFormatable == null) {
                Toast.makeText(this, "Tag is not ndef formatable!", Toast.LENGTH_SHORT).show();
                return;
            }


            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();

            Toast.makeText(this, "Conteúdo escrito na TAG!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e("formatTag", e.getMessage());
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NdefMessage ndefMessage = createNdefMessage("ID da atividade: " + IdAtividade + " \nID da tag: " + IdTarefa);

            writeNdefMessage(tag, ndefMessage);
        }
    }

    private NdefMessage createNdefMessage(String content) {

        NdefRecord ndefRecord = createTextRecord(content);

        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});

        return ndefMessage;
    }

    private NdefRecord createTextRecord(String content) {
        try {
            byte[] language;
            language = Locale.getDefault().getLanguage().getBytes("UTF-8");

            final byte[] text = content.getBytes("UTF-8");
            final int languageSize = language.length;
            final int textLength = text.length;
            final ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + languageSize + textLength);

            payload.write((byte) (languageSize & 0x1F));
            payload.write(language, 0, languageSize);
            payload.write(text, 0, textLength);

            return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());

        } catch (UnsupportedEncodingException e) {
            Log.e("createTextRecord", e.getMessage());
        }
        return null;
    }
}
