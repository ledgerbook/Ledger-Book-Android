package com.android.ledgerbook.storage.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.ledgerbook.utils.LocaleUtils;
import com.google.gson.Gson;

public class AppPreferences {

    private Context context;
    private final SharedPreferences preferences;
    private final Gson gson;

    private static final String KEY_LANGUAGE_CODE = "languageCode";

    public AppPreferences(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        gson = new Gson();
    }

    private void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private void saveString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private String getString(String key, String defaultValue) {
        return preferences.getString(key, null);
    }

    public void saveSelectedLanguage(@LocaleUtils.LanguageCode String languageCode) {
        saveString(KEY_LANGUAGE_CODE, languageCode);
    }

    public String getSelectedLanguage() {
        return getString(KEY_LANGUAGE_CODE, null);
    }
}
