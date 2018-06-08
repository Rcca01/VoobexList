package br.com.voobex.todolist.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.voobex.todolist.R;
import br.com.voobex.todolist.adapter.AdapterFeito;
import br.com.voobex.todolist.database.DbStart;
import br.com.voobex.todolist.model.TarefaModel;

import java.util.ArrayList;
import java.util.List;



public class TarefaFeitaFrag extends TarefaFrag {

    protected OnTaskRestoreListener mOnTaskRestoreListener;

    public TarefaFeitaFrag() {
        // Required empty public constructor
    }

    public interface OnTaskRestoreListener {
        void onTaskRestore(TarefaModel task);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mOnTaskRestoreListener = (OnTaskRestoreListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTaskRestoreListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_done_task, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rvDoneTasks);

        layoutManager = new LinearLayoutManager(getActivityForTaskFragment());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new AdapterFeito(this);
        mRecyclerView.setAdapter(mAdapter);

        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void findTasks(String title) {
        mAdapter.removeAllItems();
        List<TarefaModel> tasks = new ArrayList<>();
        tasks.addAll(activity.getDbHelper().query().getTasks(DbStart.SELECTION_LIKE_TITLE + " AND "
                + DbStart.SELECTION_STATUS, new String[]{"%" + title + "%",
                Integer.toString(TarefaModel.STATUS_DONE)}, DbStart.TASK_DATE_COLUMN));
        for (int i = 0; i < tasks.size(); i++) {
            addTask(tasks.get(i), false);
        }
    }

    @Override
    public void checkAdapter() {
        if (mAdapter == null) {
            mAdapter = new AdapterFeito(this);
            addTaskFromDB();
        }
    }

    @Override
    public void addTaskFromDB() {
        checkAdapter();
        mAdapter.removeAllItems();
        List<TarefaModel> tasks = new ArrayList<>();
        tasks.addAll(activity.getDbHelper().query().getTasks(DbStart.SELECTION_STATUS,
                new String[]{Integer.toString(TarefaModel.STATUS_DONE)}, DbStart.TASK_DATE_COLUMN));
        for (int i = 0; i < tasks.size(); i++) {
            addTask(tasks.get(i), false);
        }
    }

    @Override
    public void addTask(TarefaModel newTask, boolean saveToDB) {
        int position = -1;

        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            if (mAdapter.getItem(i).isTask()) {
                TarefaModel task = (TarefaModel) mAdapter.getItem(i);
                if (newTask.getDate() < task.getDate()) {
                    position = i;
                    break;
                }
            }
        }

        if (position != -1) {
            mAdapter.addItem(position, newTask);
        } else {
            mAdapter.addItem(newTask);
        }

        if (saveToDB) {
            activity.getDbHelper().saveTask(newTask);
        }
    }


    @Override
    public void moveTask(TarefaModel task) {
        if (task.getDate() != 0) {
            mAlarmHelper.setAlarm(task);
        }
        mOnTaskRestoreListener.onTaskRestore(task);
    }
}
