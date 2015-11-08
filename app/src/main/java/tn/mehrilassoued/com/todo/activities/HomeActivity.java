package tn.mehrilassoued.com.todo.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import tn.mehrilassoued.com.todo.R;
import tn.mehrilassoued.com.todo.activities.adapters.TaskAdapter;
import tn.mehrilassoued.com.todo.activities.models.Task;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "RecyclerViewActivity";
    private FloatingActionButton addButton;
    private EditText newTaskEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setToolBar();

        addButton = (FloatingActionButton) findViewById(R.id.add_task_button);
        newTaskEditText = (EditText) findViewById(R.id.new_task_name);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new TaskAdapter(getDataSet(), this);
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        // Code to Add an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).addTask(new DataObject("Hamza","Mehri"),0);

        // Code to remove an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).deleteTask(index);

        if (getDataSet().isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((TaskAdapter) mAdapter).setOnItemClickListener(new TaskAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
                Toast.makeText(HomeActivity.this, " Clicked on Item " + position, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private List<Task> getDataSet() {
        List results = new ArrayList<Task>();


        ParseQuery<Task> query = Task.getQuery();
        query.fromLocalDatastore();
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return results;
    }

    private void setToolBar() {
        Toolbar tb = (Toolbar) findViewById(R.id.include);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        ab.setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        String btnName = null;

        switch (itemId) {

            case R.id.action_settings:
                btnName = "Settings";
                break;


        }
        Toast.makeText(HomeActivity.this, "Button ==" + btnName, Toast.LENGTH_SHORT).show();
        return true;
    }

    public void addTask(View view) {


        if (!newTaskEditText.getText().toString().isEmpty()) {


            Task task = new Task();
            task.setAuthor(ParseUser.getCurrentUser());
            task.setUuidString();
            task.setName(newTaskEditText.getText().toString());
            task.setDraft(true);
            task.setImportant(false);
            task.setDone(false);

            try {
                task.pin(StarterApplication.TODO_GROUP_NAME);
                ((TaskAdapter) mAdapter).addTask(task);
                newTaskEditText.setText("");
                newTaskEditText.clearFocus();
                Toast.makeText(HomeActivity.this, "Task added", Toast.LENGTH_SHORT).show();
                mRecyclerView.setVisibility(View.VISIBLE);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }
}
