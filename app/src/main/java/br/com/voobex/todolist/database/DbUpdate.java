package br.com.voobex.todolist.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import br.com.voobex.todolist.model.TarefaModel;


public class DbUpdate {

    protected SQLiteDatabase mDatabase;


    DbUpdate(SQLiteDatabase database) {
        this.mDatabase = database;
    }

    public void title(long timeStamp, String title) {
        update(DbStart.TASK_TITLE_COLUMN, timeStamp, title);
    }

    public void date(long timeStamp, long date) {
        update(DbStart.TASK_DATE_COLUMN, timeStamp, date);
    }

    public void priority(long timeStamp, int priority) {
        update(DbStart.TASK_PRIORITY_COLUMN, timeStamp, priority);
    }

    public void status(long timeStamp, int status){
        update(DbStart.TASK_STATUS_COLUMN, timeStamp, status);
    }

    public void task(TarefaModel task){
        title(task.getTimeStamp(), task.getTitle());
        date(task.getTimeStamp(), task.getDate());
        priority(task.getTimeStamp(), task.getPriority());
        status(task.getTimeStamp(), task.getStatus());
    }

    private void update(String column, long key, String value) {
        ContentValues cv = new ContentValues();
        cv.put(column, value);
        mDatabase.update(DbStart.TASKS_TABLE, cv, DbStart.
                TASK_TIME_STAMP_COLUMN + " = " + key, null);
    }


    private void update(String column, long key, long value) {
        ContentValues cv = new ContentValues();
        cv.put(column, value);
        mDatabase.update(DbStart.TASKS_TABLE, cv, DbStart.TASK_TIME_STAMP_COLUMN + " = " + key, null);
    }
}
