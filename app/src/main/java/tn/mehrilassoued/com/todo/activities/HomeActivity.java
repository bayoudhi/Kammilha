package tn.mehrilassoued.com.todo.activities;

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
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tn.mehrilassoued.com.todo.R;
import tn.mehrilassoued.com.todo.activities.adapters.TaskTodayAdapter;
import tn.mehrilassoued.com.todo.activities.adapters.TaskTomorrowAdapter;
import tn.mehrilassoued.com.todo.activities.dao.TaskDAO;
import tn.mehrilassoued.com.todo.activities.models.Task;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView todayRecyclerView;
    private RecyclerView.Adapter todayAdapter;
    private RecyclerView.LayoutManager todayLayoutManager;
    private RecyclerView tomorrowRecyclerView;
    private RecyclerView.Adapter tomorrowAdapter;
    private RecyclerView.LayoutManager tomorrowLayoutManager;


    private static String LOG_TAG = "RecyclerViewActivity";
    private FloatingActionButton addButton;
    private EditText newTaskEditText;
    private TextView todayDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setToolBar();

        //set graphic items
        addButton = (FloatingActionButton) findViewById(R.id.add_task_button);
        newTaskEditText = (EditText) findViewById(R.id.new_task_name);
        todayDateTextView = (TextView) findViewById(R.id.today_date_text_view);


        todayLayoutManager = new LinearLayoutManager(this);
        tomorrowLayoutManager = new LinearLayoutManager(this);


        todayRecyclerView = (RecyclerView) findViewById(R.id.today_recycler_view);
        todayRecyclerView.setHasFixedSize(true);
        tomorrowRecyclerView = (RecyclerView) findViewById(R.id.tomorrow_recycler_view);
        tomorrowRecyclerView.setHasFixedSize(true);


        tomorrowRecyclerView.setLayoutManager(tomorrowLayoutManager);

        todayRecyclerView.setLayoutManager(todayLayoutManager);

        List<Task> tasks3 = TaskDAO.getDataSetOutdated();


        List<Task> tasks2 = TaskDAO.getDataSetNotDone();
        tomorrowAdapter = new TaskTomorrowAdapter(tasks2, this);
        tomorrowRecyclerView.setAdapter(tomorrowAdapter);
        tomorrowRecyclerView.getLayoutParams().height = 110 * tasks2.size();


        List<Task> tasks = TaskDAO.getDataSetToday();
        todayAdapter = new TaskTodayAdapter(tasks, this);
        todayRecyclerView.setAdapter(todayAdapter);
        todayRecyclerView.getLayoutParams().height = 120 * (tasks.size() - 1);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        todayRecyclerView.addItemDecoration(itemDecoration);
        tomorrowRecyclerView.addItemDecoration(itemDecoration);

        DateFormat dateFormat = new SimpleDateFormat("E,dd MMM ");
        todayDateTextView.setText("  "+dateFormat.format(new Date(System.currentTimeMillis())));
    }


    @Override
    protected void onResume() {
        super.onResume();
        ((TaskTodayAdapter) todayAdapter).setOnItemClickListener(new TaskTodayAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
                Toast.makeText(HomeActivity.this, " Clicked on Item " + position, Toast.LENGTH_SHORT).show();
            }
        });


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

            ((TaskTodayAdapter) todayAdapter).addTask(task);
            newTaskEditText.setText("");
            newTaskEditText.clearFocus();
            Toast.makeText(HomeActivity.this, "Task added", Toast.LENGTH_SHORT).show();
            todayRecyclerView.getLayoutParams().height = todayRecyclerView.getLayoutParams().height + 110;

        }
    }
}
