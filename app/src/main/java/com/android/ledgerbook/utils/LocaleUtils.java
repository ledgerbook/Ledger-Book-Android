package com.android.ledgerbook.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.StringDef;
import android.text.TextUtils;

import com.android.ledgerbook.App;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

public class LocaleUtils {
    @StringDef({
        LanguageCode.ENGLISH,
        LanguageCode.HINDI
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface LanguageCode {
        String ENGLISH = "en";
        String HINDI = "hi";
    }

    public static boolean isLanguageHindi(Context context) {
        return getSelectedLanguage(context).equals(LanguageCode.HINDI);
    }

    public static Context setLanguage(Context context) {
        return setLanguage(context, getSelectedLanguage(context));
    }

    public static Context setLanguage(Context context, @LanguageCode String language) {
        saveNewLanguage(context, language);
        return updateResources(context, language);
    }

    public static void saveNewLanguage(Context context, @LanguageCode String language) {
        App app = ((App) context.getApplicationContext());
        app.getPreferences().saveSelectedLanguage(language);
        app.setLanguage(language);
    }

    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }


    public static String getSelectedLanguage(Context context) {
        String language = ((App) context.getApplicationContext()).getLanguage();
        return TextUtils.isEmpty(language) ? LanguageCode.ENGLISH : language;
    }
}
