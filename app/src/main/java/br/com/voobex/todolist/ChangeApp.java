package br.com.voobex.todolist;

/**
 * Created by SnowFlake on 07.02.2016.
 */
public class ChangeApp {

    private static boolean sActivityVisible;

    public static boolean isActivityVisible(){
        return sActivityVisible;
    }

    public static void activityResumed(){
        sActivityVisible = true;
    }

    public static void activityPaused(){
        sActivityVisible = false;
    }
}
