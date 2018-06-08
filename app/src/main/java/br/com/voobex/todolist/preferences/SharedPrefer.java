package br.com.voobex.todolist.preferences;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPrefer {

    //The constant whose value is the key
    public static final String SPLASH_IS_INVISIBLE = "splash_is_invisible";

    private static SharedPrefer sInstance;

    private Context mContext;

    private SharedPreferences preferences;

    private SharedPrefer() {

    }

    public static SharedPrefer getInstance() {
        if (sInstance ==null) {
            sInstance = new SharedPrefer();
        }
        return sInstance;
    }

    public void init(Context context) {
        this.mContext = context;
        preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
    }
    // helps to transfer data from checkbox and interacts with SplashScreen
    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    // helps to transfer data from checkbox and interacts with SplashScreen
    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }
}
