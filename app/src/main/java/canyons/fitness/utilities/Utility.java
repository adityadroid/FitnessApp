package canyons.fitness.utilities;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import canyons.fitness.R;


/**
 * Created by adi on 2/23/17.
 */
public class Utility {
    public final static String FIELD_EMPTY = "One or more fields empty!";
    public final static String SOMETHING_WRONG = "Something went wrong!";
    public final static String DONE = "Done!";




    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }

    public static void showSnack(Context context, View view, String message){

        Snackbar snackbar =  Snackbar.make(view,message,Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();

    }
    public static void showSnackLong(Context context, View view, String message){

        Snackbar snackbar =  Snackbar.make(view,message,Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();

    }
    public static String getDate(){
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        // formattedDate have current date/time
       return formattedDate;

    }
    public static String getBMI(float weight, float height){
        return (weight/(height*height))+"";
    }

}
