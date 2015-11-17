package tn.mehrilassoued.com.todo.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import tn.mehrilassoued.com.todo.R;
import tn.mehrilassoued.com.todo.activities.adapters.TaskAllAdapter;
import tn.mehrilassoued.com.todo.activities.adapters.TaskTodayAdapter;
import tn.mehrilassoued.com.todo.activities.adapters.TaskTomorrowAdapter;
import tn.mehrilassoued.com.todo.activities.dao.TaskDAO;
import tn.mehrilassoued.com.todo.activities.models.Task;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView todayRecyclerView;
    private RecyclerView.Adapter todayAdapter;
    private RecyclerView.LayoutManager todayLayoutManager;


    private static String LOG_TAG = "HomeActivity";
    private FloatingActionButton addButton;
    private EditText newTaskEditText;

    private static String show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setToolBar();

        //set graphic items
        addButton = (FloatingActionButton) findViewById(R.id.add_task_button);
        newTaskEditText = (EditText) findViewById(R.id.new_task_name);


        todayLayoutManager = new LinearLayoutManager(this);


        todayRecyclerView = (RecyclerView) findViewById(R.id.today_recycler_view);
        todayRecyclerView.setHasFixedSize(true);


        todayRecyclerView.setLayoutManager(todayLayoutManager);

        List<Task> tasks = new ArrayList<>();
        String type = getIntent().getStringExtra("show");
        if (type != null)
            show = type;

        RecyclerView.ItemDecoration itemDecoration;

        if (show != null) {
            if (show.equals("all")) {
                tasks = TaskDAO.getTasks();
                todayAdapter = new TaskAllAdapter(tasks, this);
                todayRecyclerView.setAdapter(todayAdapter);
                todayRecyclerView.getLayoutParams().height = 130 * tasks.size();


                itemDecoration =
                        new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
                todayRecyclerView.addItemDecoration(itemDecoration);
                newTaskEditText.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.VISIBLE);
                setTitle("INOBX");
            }

            if (show.equals("today")) {
                tasks = TaskDAO.getTasksToday();
                todayAdapter = new TaskTodayAdapter(tasks, this);
                todayRecyclerView.setAdapter(todayAdapter);
                todayRecyclerView.getLayoutParams().height = 130 * tasks.size();

                itemDecoration =
                        new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
                todayRecyclerView.addItemDecoration(itemDecoration);
                newTaskEditText.setVisibility(View.GONE);
                addButton.setVisibility(View.GONE);
                setTitle("TODAY");
            }

            if (show.equals("next")) {
                tasks = TaskDAO.getTasksNextDays();
                todayAdapter = new TaskTomorrowAdapter(tasks, this);
                todayRecyclerView.setAdapter(todayAdapter);
                todayRecyclerView.getLayoutParams().height = 130 * tasks.size();

                itemDecoration =
                        new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
                todayRecyclerView.addItemDecoration(itemDecoration);
                newTaskEditText.setVisibility(View.GONE);
                addButton.setVisibility(View.GONE);
                setTitle("TOMORROW");
            }
            if (show.equals("important")) {
                tasks = TaskDAO.getTasksImportant();
                todayAdapter = new TaskTomorrowAdapter(tasks, this);
                todayRecyclerView.setAdapter(todayAdapter);
                todayRecyclerView.getLayoutParams().height = 130 * tasks.size();

                itemDecoration =
                        new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
                todayRecyclerView.addItemDecoration(itemDecoration);
                newTaskEditText.setVisibility(View.GONE);
                addButton.setVisibility(View.GONE);
                setTitle("IMPORTANT");
            }

            if (show.equals("history")) {
                tasks = TaskDAO.getTasksDone();
                todayAdapter = new TaskTomorrowAdapter(tasks, this);
                todayRecyclerView.setAdapter(todayAdapter);
                todayRecyclerView.getLayoutParams().height = 130 * tasks.size();

                itemDecoration =
                        new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
                todayRecyclerView.addItemDecoration(itemDecoration);
                newTaskEditText.setVisibility(View.GONE);
                addButton.setVisibility(View.GONE);
                setTitle("HISTORY");
            }
        }




    }


    @Override
    protected void onResume() {
        super.onResume();
        /*((TaskTodayAdapter) todayAdapter).setOnItemClickListener(new TaskTodayAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
                Toast.makeText(HomeActivity.this, " Clicked on Item " + position, Toast.LENGTH_SHORT).show();
            }
        });
*/

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
        List<Task> tasks = new ArrayList<>();
        switch (itemId) {


        }
        return true;
    }


    public void addTask(View view) {
        if (!newTaskEditText.isFocusable())
            newTaskEditText.requestFocus();

        if (!newTaskEditText.getText().toString().isEmpty()) {


            Task task = new Task();
            task.setAuthor(ParseUser.getCurrentUser());
            task.setUuidString();
            task.setName(newTaskEditText.getText().toString());
            task.setDraft(true);
            task.setImportant(false);
            task.setDone(false);


            task.pinInBackground(StarterApplication.TODO_GROUP_NAME);

            ((TaskAllAdapter) todayAdapter).addTask(task);
            newTaskEditText.setText("");
            newTaskEditText.clearFocus();
            Toast.makeText(HomeActivity.this, "Task added", Toast.LENGTH_SHORT).show();
            todayRecyclerView.getLayoutParams().height = todayRecyclerView.getLayoutParams().height + 110;

        }
    }
}
