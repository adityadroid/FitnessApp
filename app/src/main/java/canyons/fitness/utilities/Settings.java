package canyons.fitness.utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by adi on 24/2/2017.
 */
public class Settings {



    public static String getSharedPreference(Context context, String name)
    {
        SharedPreferences settings = context.getSharedPreferences("aditya.thingspeak", 0);
        return settings.getString(name, "");

    }

    public static void setSharedPreference(Context context, String name, String value)
    {
        SharedPreferences settings = context.getSharedPreferences("aditya.thingspeak", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public static void clearSharedPreferences(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences("aditya.thingspeak", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
    }




}
