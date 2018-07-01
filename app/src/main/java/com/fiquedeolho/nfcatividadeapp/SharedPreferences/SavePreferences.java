package com.fiquedeolho.nfcatividadeapp.SharedPreferences;


import android.content.Context;
import android.content.SharedPreferences;

import com.fiquedeolho.nfcatividadeapp.R;

public class SavePreferences {

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private Context context;

    public SavePreferences(Context context){
        sharedPref = context.getSharedPreferences("shared", Context.MODE_PRIVATE);
        this.editor = sharedPref.edit();
        this.context = context;
    }

    public void saveInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public int getSavedInt(String key){
        int value = sharedPref.getInt(key, 0);
        return value;
    }

    public void saveString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getSavedString(String key){
        String value = sharedPref.getString(key, "");
        return value;
    }

    public void removeShared(String key) {
        editor.remove(key);
        editor.commit();
    }
}
