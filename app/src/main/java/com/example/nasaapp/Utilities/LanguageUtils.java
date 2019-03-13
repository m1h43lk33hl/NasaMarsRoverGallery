package com.example.nasaapp.Utilities;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class LanguageUtils {

    public static void setLanguage(String lang, Context baseContext)
    {
        String country = (lang.equals("en") ? "EN" : "NL");

        // Use constructor with country
        Locale locale = new Locale(lang, country);

        // Set new locale
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;

        //Update configuration
        baseContext.getResources().updateConfiguration(config, baseContext.getResources().getDisplayMetrics());
    }
}
