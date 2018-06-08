package br.com.voobex.todolist.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import br.com.voobex.todolist.fragment.TarefaVigenteFrag;
import br.com.voobex.todolist.fragment.TarefaFeitaFrag;

public class AdapterAba extends FragmentStatePagerAdapter {

    public static final int CURRENT_TASK_FRAGMENT_POSITION = 0;
    public static final int DONE_TASK_FRAGMENT_POSITION = 1;

    private Fragment[] mFragments = {
            new TarefaVigenteFrag(),
            new TarefaFeitaFrag(),
    };

    public AdapterAba(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }

}
