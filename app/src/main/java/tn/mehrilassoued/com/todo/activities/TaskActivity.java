package tn.mehrilassoued.com.todo.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import tn.mehrilassoued.com.todo.R;
import tn.mehrilassoued.com.todo.activities.adapters.SubtaskAdapter;
import tn.mehrilassoued.com.todo.activities.adapters.TaskAdapter;
import tn.mehrilassoued.com.todo.activities.models.Subtask;
import tn.mehrilassoued.com.todo.activities.models.Task;

public class TaskActivity extends AppCompatActivity {
    private int id;
    private Task task;
    private EditText nameEditText;
    private EditText noteEditText;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "TaskActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        setToolBar();

        id = Integer.valueOf(getIntent().getStringExtra("id"));
        task = TaskAdapter.tasks.get(id);

        mRecyclerView = (RecyclerView) findViewById(R.id.subtasks_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SubtaskAdapter(getDataSet(), this);
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);


        nameEditText = (EditText) findViewById(R.id.task_name);
        noteEditText = (EditText) findViewById(R.id.task_note);


        nameEditText.setText(task.getName());
        noteEditText.setText(task.getNote());

        nameEditText.addTextChangedListener(new TextWatcher() {
            String oldText = nameEditText.getText().toString();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    {
                        try {
                            task.setName(s.toString());
                            task.pin(StarterApplication.TODO_GROUP_NAME);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }


        });

        noteEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    task.setNote(s.toString());
                    task.pin(StarterApplication.TODO_GROUP_NAME);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        if(getDataSet().isEmpty()){
            mRecyclerView.setVisibility(View.GONE);
        }

    }

    private List<Subtask> getDataSet() {
        List results = new ArrayList<Subtask>();


        ParseQuery<Subtask> query = Subtask.getQuery();
        query.whereEqualTo("parent", task);
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
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_delete) {
            new MaterialDialog.Builder(this)
                    .title("Delete Task")
                    .content("Are you sure?")
                    .positiveText("Yes").negativeText("No").
                    onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                            task.deleteEventually();
                            Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                            startActivity(intent);
                            Toast.makeText(TaskActivity.this, "Task deleted", Toast.LENGTH_SHORT).show();
                        }
                    }).
                    show();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((SubtaskAdapter) mAdapter).setOnItemClickListener(new SubtaskAdapter.MyClickListener() {

            @Override
            public void onItemClick(int position, View v) {

            }
        });


    }

    public void addSubtask(View view) {

        new MaterialDialog.Builder(this)
                .title("Subtask")

                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if (input.toString().isEmpty()) return;
                        try {
                            Subtask subtask = new Subtask();
                            subtask.setName(input.toString());
                            subtask.setDone(false);
                            subtask.add("parent", task);
                            subtask.pin(StarterApplication.TODO_GROUP_NAME);
                            ((SubtaskAdapter) mAdapter).addTask(subtask);
                            Toast.makeText(getApplicationContext(), "Subtask added", Toast.LENGTH_SHORT).show();
                            mRecyclerView.setVisibility(View.VISIBLE);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }).positiveText("Add").negativeText("Cancel").
                show();

    }
}
