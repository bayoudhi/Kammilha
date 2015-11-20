package tn.mehrilassoued.com.todo.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import tn.mehrilassoued.com.todo.R;
import tn.mehrilassoued.com.todo.activities.adapters.ListAdapter;
import tn.mehrilassoued.com.todo.activities.adapters.TaskAdapter;
import tn.mehrilassoued.com.todo.activities.dao.TaskDAO;
import tn.mehrilassoued.com.todo.activities.models.Group;
import tn.mehrilassoued.com.todo.activities.models.Task;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView RecyclerView;
    private RecyclerView.Adapter Adapter;
    private RecyclerView.LayoutManager layoutManager;


    private static String LOG_TAG = "HomeActivity";
    private FloatingActionButton addTaskButton;

    private List<Task> tasks;
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //set graphic items
        addTaskButton = (FloatingActionButton) findViewById(R.id.add_task_home);

        layoutManager = new LinearLayoutManager(this);


        RecyclerView = (RecyclerView) findViewById(R.id.today_recycler_view);
        RecyclerView.setHasFixedSize(true);


        RecyclerView.setLayoutManager(layoutManager);

        tasks = new ArrayList<>();
        int position = getIntent().getIntExtra("id", -1);
        if (position != -1) {
            group = ListAdapter.groups.get(position);

            RecyclerView.ItemDecoration itemDecoration;





            switch (position) {
                case 1:
                    tasks = TaskDAO.getTasksToday();
                    addTaskButton.setVisibility(View.GONE);
                    break;
                case 2:
                    tasks = TaskDAO.getTasksNextDays();
                    addTaskButton.setVisibility(View.GONE);
                    break;
                default:
                    tasks = TaskDAO.getTasksByList(group);
                    addTaskButton.setVisibility(View.VISIBLE);
                    break;
            }
            Adapter = new TaskAdapter(tasks, this);
            RecyclerView.setAdapter(Adapter);
            if (tasks.size() == 0)
                RecyclerView.getLayoutParams().height = 130;
            else RecyclerView.getLayoutParams().height = 130 * tasks.size();

            itemDecoration =
                    new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
            RecyclerView.addItemDecoration(itemDecoration);
            setTitle(group.getName());
        }

       /* if (type != null) {
            if (position==0) {
                tasks = TaskDAO.getTasks();
                Adapter = new TaskAdapter(tasks, this);
                RecyclerView.setAdapter(Adapter);
                RecyclerView.getLayoutParams().height = 130 * tasks.size();


                itemDecoration =
                        new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
                RecyclerView.addItemDecoration(itemDecoration);
                setTitle("INOBX");
            }

            if (type.equals("today")) {
                tasks = TaskDAO.getTasksToday();
                Adapter = new TaskAdapter(tasks, this);
                RecyclerView.setAdapter(Adapter);
                RecyclerView.getLayoutParams().height = 130 * tasks.size();

                itemDecoration =
                        new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
                RecyclerView.addItemDecoration(itemDecoration);
                setTitle("TODAY");
            }

            if (type.equals("next")) {
                tasks = TaskDAO.getTasksNextDays();
                Adapter = new TaskAdapter(tasks, this);
                RecyclerView.setAdapter(Adapter);
                RecyclerView.getLayoutParams().height = 130 * tasks.size();

                itemDecoration =
                        new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
                RecyclerView.addItemDecoration(itemDecoration);
                setTitle("In 7 Days");
            }
            if (type.equals("important")) {
                tasks = TaskDAO.getTasksImportant();
                Adapter = new TaskAdapter(tasks, this);
                RecyclerView.setAdapter(Adapter);
                RecyclerView.getLayoutParams().height = 130 * tasks.size();

                itemDecoration =
                        new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
                RecyclerView.addItemDecoration(itemDecoration);
                setTitle("IMPORTANT");
            }

            if (type.equals("history")) {
                tasks = TaskDAO.getTasksDone();
                Adapter = new TaskAdapter(tasks, this);
                RecyclerView.setAdapter(Adapter);
                RecyclerView.getLayoutParams().height = 130 * tasks.size();

                itemDecoration =
                        new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
                RecyclerView.addItemDecoration(itemDecoration);
                setTitle("HISTORY");
            }
        }*/


    }


    @Override
    protected void onResume() {
        super.onResume();
        /*((TaskTodayAdapter) Adapter).setOnItemClickListener(new TaskTodayAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
                Toast.makeText(HomeActivity.this, " Clicked on Item " + position, Toast.LENGTH_SHORT).type();
            }
        });
*/
        if (Adapter != null) Adapter.notifyDataSetChanged();
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


    public void addTask(final View view) {
        new MaterialDialog.Builder(this)
                .title("Add a task")

                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if (input.toString().isEmpty()) return;

                        Task task = new Task();
                        task.setAuthor(ParseUser.getCurrentUser());
                        task.setUuidString();
                        task.setName(input.toString());
                        task.setDraft(true);
                        task.setImportant(false);
                        task.setDone(false);
                        task.add("parent", group);

                        final Task t = task;
                        task.pinInBackground(StarterApplication.TODO_GROUP_NAME, new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                ((TaskAdapter) Adapter).addTask(t);
                                ListActivity.check = true;
                                final View v = view;
                                Snackbar.make(v, "Task added", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        });


                    }
                }).positiveText("Add").negativeText("Cancel").
                show();
    }
}
