package tn.mehrilassoued.com.todo.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;


import com.afollestad.materialdialogs.MaterialDialog;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import tn.mehrilassoued.com.todo.R;
import tn.mehrilassoued.com.todo.activities.adapters.ListAdapter;
import tn.mehrilassoued.com.todo.activities.models.Task;


public class ListActivity extends AppCompatActivity {

    public static boolean check = false;

    private RecyclerView todayRecyclerView;
    private RecyclerView.Adapter todayAdapter;
    private RecyclerView.LayoutManager todayLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        todayLayoutManager = new LinearLayoutManager(this);
        todayRecyclerView = (RecyclerView) findViewById(R.id.list_recycler_view);
        todayRecyclerView.setHasFixedSize(true);

        todayRecyclerView.setLayoutManager(todayLayoutManager);


        List<String> lists = new ArrayList<String>();
        lists.add("Inbox");
        lists.add("Today");
        lists.add("In 7 Days");

        List results = new ArrayList<tn.mehrilassoued.com.todo.activities.models.List>();


        ParseQuery<tn.mehrilassoued.com.todo.activities.models.List> query = tn.mehrilassoued.com.todo.activities.models.List.getQuery();

        query.fromLocalDatastore();
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(tn.mehrilassoued.com.todo.activities.models.List list:(List<tn.mehrilassoued.com.todo.activities.models.List>)results){
            lists.add(list.getName());
        }

        lists.add("Create list");
        todayAdapter = new ListAdapter(lists, this);
        todayRecyclerView.setAdapter(todayAdapter);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        todayRecyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (check) {
            todayAdapter.notifyDataSetChanged();
            check = false;
        }
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


                        final Task t = task;
                        task.pinInBackground(StarterApplication.TODO_GROUP_NAME, new SaveCallback() {
                            @Override
                            public void done(ParseException e) {


                                todayAdapter.notifyDataSetChanged();
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
