package tn.mehrilassoued.com.todo.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;


import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.MaterialDialog;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import tn.mehrilassoued.com.todo.R;
import tn.mehrilassoued.com.todo.activities.adapters.ListAdapter;
import tn.mehrilassoued.com.todo.activities.models.Group;
import tn.mehrilassoued.com.todo.activities.models.Task;


public class ListActivity extends AppCompatActivity {

    public static boolean check = false;

    private RecyclerView todayRecyclerView;
    private RecyclerView.Adapter todayAdapter;
    private RecyclerView.LayoutManager todayLayoutManager;

    private List<Group> groups;

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

        loadLists();

    }

    public void loadLists() {
        groups = new ArrayList<Group>();

        ParseQuery<Group> query = Group.getQuery();

        query.fromLocalDatastore();
        try {
            groups = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Group inbox = new Group();
        inbox.setName("Inbox");
        groups.add(0, inbox);

        Group today = new Group();
        today.setName("Today");
        groups.add(1, today);

        Group tomorrow = new Group();
        tomorrow.setName("In 7 Days");
        groups.add(2, tomorrow);

        Group create = new Group();
        create.setName("Create list");
        groups.add(groups.size(), create);


        todayAdapter = new ListAdapter(groups, this);
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

                        saveTask(task);
                       /* final Task t = task;
                        task.pinInBackground(StarterApplication.TODO_GROUP_NAME, new SaveCallback() {
                            @Override
                            public void done(ParseException e) {


                                todayAdapter.notifyDataSetChanged();
                                final View v = view;
                                Snackbar.make(v, "Task added", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        });*/


                    }
                }).positiveText("Add").negativeText("Cancel").
                show();


    }

    public void saveTask(final Task task) {
        CharSequence[] charSequences = new CharSequence[groups.size() - 3];
        int j = 0;
        for (int i = 0; i < groups.size(); i++) {
            if (i == 1 || i == 2 || i == (groups.size() - 1)) {

            } else {
                charSequences[j] = groups.get(i).getName();
                j++;
            }
        }
        new MaterialDialog.Builder(this)
                .title("Select List")
                .items(charSequences)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, final View view, int which, CharSequence text) {
                        final Intent intent = new Intent(ListActivity.this, HomeActivity.class);
                        if (which == 0) {
                            task.add("parent", groups.get(0));


                            intent.putExtra("id", 0);

                        } else {
                            task.add("parent", groups.get(which + 2));
                            intent.putExtra("id", which + 2);
                        }
                        task.pinInBackground(StarterApplication.TODO_GROUP_NAME, new SaveCallback() {
                            @Override
                            public void done(ParseException e) {


                                todayAdapter.notifyDataSetChanged();
                                final View v = view;
                                Snackbar.make(v, "Task added", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                startActivity(intent);

                            }
                        });

                        return true;
                    }
                })
                .positiveText("Save")
                .show();
    }
}
