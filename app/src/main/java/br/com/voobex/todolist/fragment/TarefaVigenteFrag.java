package br.com.voobex.todolist.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.voobex.todolist.R;
import br.com.voobex.todolist.adapter.AdapterAtual;
import br.com.voobex.todolist.database.DbStart;
import br.com.voobex.todolist.model.Separador;
import br.com.voobex.todolist.model.TarefaModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class TarefaVigenteFrag extends TarefaFrag {



    protected OnTaskDoneListener mOnTaskDoneListener;

    public TarefaVigenteFrag() {
        // Required empty public constructor
    }

    public interface OnTaskDoneListener {
        void onTaskDone(TarefaModel task);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mOnTaskDoneListener = (OnTaskDoneListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTaskDoneListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tarefa_vigente_frag, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rvCurrentTasks);

        layoutManager = new LinearLayoutManager(getActivityForTaskFragment());

        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new AdapterAtual(this);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }


    @Override
    public void findTasks(String title) {
        mAdapter.removeAllItems();
        List<TarefaModel> tasks = new ArrayList<>();
        tasks.addAll(activity.getDbHelper().query().getTasks(DbStart.SELECTION_LIKE_TITLE + " AND "
                        + DbStart.SELECTION_STATUS + " OR " + DbStart.SELECTION_STATUS,
                new String[]{"%" + title + "%", Integer.toString(TarefaModel.STATUS_CURRENT),
                        Integer.toString(TarefaModel.STATUS_OVERDUE)}, DbStart.TASK_DATE_COLUMN));
        for (int i = 0; i < tasks.size(); i++) {
            addTask(tasks.get(i), false);
        }
    }

    // checkAdapter fix bug
    @Override
    public void checkAdapter() {
        if (mAdapter == null) {
            mAdapter = new AdapterAtual(this);
            addTaskFromDB();
        }
    }

    @Override
    public void addTaskFromDB() {
        checkAdapter();
        mAdapter.removeAllItems();

        List<TarefaModel> tasks = new ArrayList<>();
        tasks.addAll(activity.getDbHelper().query().getTasks(DbStart.SELECTION_STATUS + " OR "
                + DbStart.SELECTION_STATUS, new String[]{Integer.toString(TarefaModel.STATUS_CURRENT),
                Integer.toString(TarefaModel.STATUS_OVERDUE)}, DbStart.TASK_DATE_COLUMN));
        for (int i = 0; i < tasks.size(); i++) {
            addTask(tasks.get(i), false);
        }
    }


    @Override
    public void addTask(TarefaModel newTask, boolean saveToDB) {
        int position = -1;
        Separador separator = null;

        //sorts tasks by date
        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            if (mAdapter.getItem(i).isTask()) {
                TarefaModel task = (TarefaModel) mAdapter.getItem(i);
                if (newTask.getDate() < task.getDate()) {
                    position = i;
                    break;
                }
            }
        }

        if (newTask.getDate() != 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(newTask.getDate());

            if (calendar.get(Calendar.DAY_OF_YEAR) < Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                newTask.setDateStatus(Separador.TYPE_OVERDUE);
                if (!mAdapter.isContainsSeparatorOverdue()) {
                    mAdapter.setContainsSeparatorOverdue(true);
                    separator = new Separador(Separador.TYPE_OVERDUE);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                newTask.setDateStatus(Separador.TYPE_TODAY);
                if (!mAdapter.isContainsSeparatorToday()) {
                    mAdapter.setContainsSeparatorToday(true);
                    separator = new Separador(Separador.TYPE_TODAY);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1) {
                newTask.setDateStatus(Separador.TYPE_TOMORROW);
                if (!mAdapter.isContainsSeparatorTomorrow()) {
                    mAdapter.setContainsSeparatorTomorrow(true);
                    separator = new Separador(Separador.TYPE_TOMORROW);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) > Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1) {
                newTask.setDateStatus(Separador.TYPE_TOMORROW);
                if (!mAdapter.isContainsSeparatorFuture()) {
                    mAdapter.setContainsSeparatorFuture(true);
                    separator = new Separador(Separador.TYPE_FUTURE);
                }
            }

        }

        //sorts to a position below
        if (position != -1) {

            if (!mAdapter.getItem(position - 1).isTask()) {
                if (position - 2 >= 0 && mAdapter.getItem(position - 2).isTask()) {
                    TarefaModel task = (TarefaModel) mAdapter.getItem(position - 2);
                    if (task.getDateStatus() == newTask.getDateStatus()) {
                        position -= 1;
                    }
                } else if (position - 2 < 0 && newTask.getDate() == 0) {
                    position -= 1;
                }
            }

            if (separator != null) {
                mAdapter.addItem(position - 1, separator);
            }
            mAdapter.addItem(position, newTask);
        } else {
            if (separator != null) {
                mAdapter.addItem(separator);
            }
            mAdapter.addItem(newTask);
        }

        if (saveToDB) {
            activity.getDbHelper().saveTask(newTask);
        }
    }


    @Override
    public void moveTask(TarefaModel task) {
        mAlarmHelper.removeAlarm(task.getTimeStamp());
        mOnTaskDoneListener.onTaskDone(task);
    }
}
