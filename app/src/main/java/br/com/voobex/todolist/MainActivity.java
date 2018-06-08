package br.com.voobex.todolist;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import br.com.voobex.todolist.about.AboutActivity;
import br.com.voobex.todolist.adapter.AdapterAba;
import br.com.voobex.todolist.alarm.Alarme;
import br.com.voobex.todolist.auth.LoginActivity;
import br.com.voobex.todolist.database.DbStart;
import br.com.voobex.todolist.dialog.AddTarefa;
import br.com.voobex.todolist.dialog.EditarTarefa;
import br.com.voobex.todolist.fragment.TarefaVigenteFrag;
import br.com.voobex.todolist.fragment.TarefaFeitaFrag;
import br.com.voobex.todolist.fragment.TarefaFrag;
import br.com.voobex.todolist.maps.MapsActivity;
import br.com.voobex.todolist.model.TarefaModel;
import br.com.voobex.todolist.preferences.SharedPrefer;

public class MainActivity extends AppCompatActivity
        implements AddTarefa.AddingTaskListener,
        TarefaVigenteFrag.OnTaskDoneListener, TarefaFeitaFrag.OnTaskRestoreListener,
        EditarTarefa.EditingTaskListener {

    //allows to find the fragment, which is associated with Activity
    protected FragmentManager mFragmentManager;

    protected SharedPrefer mPreferenceHelper;
    protected AdapterAba mTabAdapter;
    protected TarefaFrag mCurrentTaskFragment;
    protected TarefaFrag mDoneTaskFragment;
    protected DbStart mDbHelper;
    protected SearchView mSearchView;
    protected Toolbar mToolbar;

    private FirebaseAuth mAuth;

    public DbStart getDbHelper() {
        return mDbHelper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Alarme.getInstance().init(getApplicationContext());

        mAuth = FirebaseAuth.getInstance();
        SharedPrefer.getInstance().init(getApplicationContext());
        mPreferenceHelper = SharedPrefer.getInstance();

        mDbHelper = new DbStart(getApplicationContext());

        // Allows to find the fragment, which is associated with Activity
        mFragmentManager = getFragmentManager();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        setUI(getApplicationContext());
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        ChangeApp.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ChangeApp.activityPaused();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_exit) {
            this.signOut();
            return true;
        }

        if (id == R.id.action_about) {
            Intent intent = new Intent(this,AboutActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_map) {
            Intent intent = new Intent(this,MapsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        mAuth.signOut();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    //Responsible for the user interface.
    public void setUI(Context context) {
        if (mToolbar != null) {
            mToolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white));
            setSupportActionBar(mToolbar);
        }
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.current_task));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.done_task));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        mTabAdapter = new AdapterAba(mFragmentManager);

        viewPager.setAdapter(mTabAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });

        mCurrentTaskFragment = (TarefaVigenteFrag) mTabAdapter.getItem(AdapterAba.CURRENT_TASK_FRAGMENT_POSITION);
        mDoneTaskFragment = (TarefaFeitaFrag) mTabAdapter.getItem(AdapterAba.DONE_TASK_FRAGMENT_POSITION);

        mSearchView = (SearchView) findViewById(R.id.search_view);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mCurrentTaskFragment.findTasks(newText);
                mDoneTaskFragment.findTasks(newText);
                return false;
            }
        });
        FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.fab);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment addingTaskDialogFragment = new AddTarefa();
                addingTaskDialogFragment.show(mFragmentManager, "AddTarefa");
            }
        });

    }


    @Override
    public void onTaskAdded(TarefaModel newTask) {
        mCurrentTaskFragment.addTask(newTask, true);
    }

    @Override
    public void onTaskAddingCancel() {
        Toast.makeText(this, R.string.cancelCreateTask, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTaskDone(TarefaModel task) {
        mDoneTaskFragment.addTask(task, false);
    }

    @Override
    public void onTaskRestore(TarefaModel task) {
        mCurrentTaskFragment.addTask(task, false);
    }

    @Override
    public void onTaskEdited(TarefaModel updatedTask) {
        mCurrentTaskFragment.updateTask(updatedTask);
        mDbHelper.update().task(updatedTask);
    }
}
