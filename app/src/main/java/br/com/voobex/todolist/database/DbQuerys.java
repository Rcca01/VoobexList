package br.com.voobex.todolist.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.voobex.todolist.model.TarefaModel;

import java.util.List;
import java.util.ArrayList;


public class DbQuerys {

    private SQLiteDatabase mDatabase;

    DbQuerys(SQLiteDatabase database) {
        this.mDatabase = database;
    }

    public TarefaModel getTask(long timeStapm) {
        TarefaModel modelTask = null;
        Cursor cursor = mDatabase.query(DbStart.TASKS_TABLE, null, DbStart.SELECTION_TIME_STAMP,
                new String[]{Long.toString(timeStapm)}, null, null, null);
        if (cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndex(DbStart.TASK_TITLE_COLUMN));
            long date = cursor.getLong(cursor.getColumnIndex(DbStart.TASK_DATE_COLUMN));
            int priority = cursor.getInt(cursor.getColumnIndex(DbStart.TASK_PRIORITY_COLUMN));
            int status = cursor.getInt(cursor.getColumnIndex(DbStart.TASK_STATUS_COLUMN));

            modelTask = new TarefaModel(title, date, priority, status, timeStapm);
        }
        cursor.close();

        return modelTask;
    }


    // returns list of tasks
    public List<TarefaModel> getTasks(String selection, String[] selectionArgs, String orderBy) {
        List<TarefaModel> tasks = new ArrayList<>();


        //finds data and forms it into a task
        Cursor c = mDatabase.query(DbStart.TASKS_TABLE, null, selection, selectionArgs, null, null, orderBy);
        if (c.moveToFirst()) {
            do {
                String title = c.getString(c.getColumnIndex(DbStart.TASK_TITLE_COLUMN));
                long date = c.getLong(c.getColumnIndex(DbStart.TASK_DATE_COLUMN));
                int priority = c.getInt(c.getColumnIndex(DbStart.TASK_PRIORITY_COLUMN));
                int status = c.getInt(c.getColumnIndex(DbStart.TASK_STATUS_COLUMN));
                long timeStamp = c.getLong(c.getColumnIndex(DbStart.TASK_TIME_STAMP_COLUMN));

                TarefaModel modelTask = new TarefaModel(title, date, priority, status, timeStamp);
                tasks.add(modelTask);
            } while (c.moveToNext());
        }
        c.close();

        return tasks;
    }
}
