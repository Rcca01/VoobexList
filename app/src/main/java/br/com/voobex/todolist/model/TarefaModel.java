package br.com.voobex.todolist.model;

import br.com.voobex.todolist.R;

import java.util.Date;

/**
 * Created by SnowFlake on 01.02.2016.
 * Class with model of task, priority and status
 */
public class TarefaModel implements Item {

    public static final int PRIORITY_LOW = 0;
    public static final int PRIORITY_NORMAL = 1;
    public static final int PRIORITY_HIGH = 2;

    public static final String[] PRIORITY_LEVELS = {"Low Priority", "Normal priority", "High priority"};

    public static final int STATUS_OVERDUE = 0;
    public static final int STATUS_CURRENT = 1;
    public static final int STATUS_DONE = 2;

    private String mTitle;
    private long mDate;
    private int priority;
    private int mStatus;
    private long mTimeStamp;
    private int mDateStatus;

    public TarefaModel() {
        this.mStatus = -1;
        this.mTimeStamp = new Date().getTime();
    }

    public TarefaModel(String title, long date, int priority, int status, long timeStamp) {
        this.mTitle = title;
        this.mDate = date;
        this.priority = priority;
        this.mStatus = status;
        this.mTimeStamp = timeStamp;
    }

    public int getPriorityColor() {
        switch (getPriority()) {

            case PRIORITY_HIGH:
                if (getStatus() == STATUS_CURRENT || getStatus() == STATUS_OVERDUE) {
                    return R.color.priority_hight;
                } else {
                    return R.color.priority_hight_selected;
                }

            case PRIORITY_NORMAL:
                if (getStatus() == STATUS_CURRENT || getStatus() == STATUS_OVERDUE) {
                    return R.color.priority_normal;
                } else {
                    return R.color.priority_normal_selected;
                }

            case PRIORITY_LOW:
                if (getStatus() == STATUS_CURRENT || getStatus() == STATUS_OVERDUE) {
                    return R.color.priority_low;
                } else {
                    return R.color.priority_low_selected;
                }


            default:
                return 0;
        }
    }

    @Override
    public boolean isTask() {
        return true;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        this.mDate = date;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        this.mStatus = status;
    }

    public long getTimeStamp() {
        return mTimeStamp;
    }

    public int getDateStatus() {
        return mDateStatus;
    }

    public void setDateStatus(int dateStatus) {
        this.mDateStatus = dateStatus;
    }

}
