package br.com.voobex.todolist.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import br.com.voobex.todolist.model.TarefaModel;


public class Alarme {

    private static Alarme sInstance;
    private Context mContext;
    private AlarmManager mAlarmManager;

    public static Alarme getInstance(){
        if (sInstance == null){
            sInstance = new Alarme();
        }
        return sInstance;
    }

    public void init(Context context){
        this.mContext = context;
        mAlarmManager = (AlarmManager) context.getApplicationContext().
                getSystemService(Context.ALARM_SERVICE);
    }

    public void setAlarm(TarefaModel task){
        Intent intent = new Intent(mContext, RecAlarme.class);
        intent.putExtra("title", task.getTitle());
        intent.putExtra("time_stamp", task.getTimeStamp());
        intent.putExtra("color", task.getPriorityColor());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext.getApplicationContext(),
                (int) task.getTimeStamp(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        mAlarmManager.set(AlarmManager.RTC_WAKEUP, task.getDate(), pendingIntent);
    }

    public void removeAlarm(long taskTimeStump){
        Intent intent = new Intent(mContext, RecAlarme.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, (int) taskTimeStump,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        mAlarmManager.cancel(pendingIntent);
    }
}
