package br.com.voobex.todolist.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import br.com.voobex.todolist.database.DbStart;
import br.com.voobex.todolist.model.TarefaModel;

import java.util.ArrayList;
import java.util.List;


public class SetAlarme extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DbStart dbHelper = new DbStart(context);

        Alarme.getInstance().init(context);
        Alarme alarmHelper = Alarme.getInstance();

        List<TarefaModel> tasks = new ArrayList<>();
        tasks.addAll(dbHelper.query().getTasks(DbStart.SELECTION_STATUS + " OR "
                + DbStart.SELECTION_STATUS, new String[]{Integer.toString(TarefaModel.STATUS_CURRENT),
                Integer.toString(TarefaModel.STATUS_OVERDUE)}, DbStart.TASK_DATE_COLUMN));


        for (TarefaModel task : tasks){
            if (task.getDate() != 0){
                alarmHelper.setAlarm(task);
            }
        }
    }
}
