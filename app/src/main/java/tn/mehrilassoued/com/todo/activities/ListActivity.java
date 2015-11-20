package tn.mehrilassoued.com.todo.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import tn.mehrilassoued.com.todo.R;
import tn.mehrilassoued.com.todo.activities.adapters.ListAdapter;
import tn.mehrilassoued.com.todo.activities.dao.TaskDAO;
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

       // if(TaskDAO.getTasksTodayCount()!=0){
            Group today = new Group();
            today.setName("Today");
            groups.add(1, today);
        //}

        //if(TaskDAO.getTasksNextDaysCount()!=0){
            Group tomorrow = new Group();
            tomorrow.setName("In 7 Days");
            groups.add(2, tomorrow);
        //}


        Group create = new Group();
        create.setName("Create list");
        groups.add(groups.size(), create);


        todayAdapter = new ListAdapter(groups, this);
        todayRecyclerView.setAdapter(todayAdapter);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        //todayRecyclerView.addItemDecoration(itemDecoration);
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
        boolean wrapInScrollView = true;
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Create task")
                .customView(R.layout.layout, wrapInScrollView).positiveText("Add").negativeText("Cancel").build();

        //Create the SPINNER to select the LIST
        Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner);
        CharSequence[] charSequences = new CharSequence[groups.size() - 3];
        int j = 0;
        for (int i = 0; i < groups.size(); i++) {
            if (i == 1 || i == 2 || i == (groups.size() - 1)) {

            } else {
                charSequences[j] = groups.get(i).getName();
                j++;
            }
        }
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item, charSequences);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        final EditText taskName = (EditText) dialog.findViewById(R.id.editText);

        final Spinner listId = (Spinner) dialog.findViewById(R.id.spinner);


        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
        taskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0)
                    dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                else dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
            }
        });

        dialog.getBuilder().onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {


                if (taskName.getText().toString().isEmpty()) return;

                Task task = new Task();
                task.setAuthor(ParseUser.getCurrentUser());
                task.setUuidString();
                task.setName(taskName.getText().toString());
                task.setDraft(true);
                task.setImportant(false);
                task.setDone(false);

                saveTask(task, (int) listId.getSelectedItemId());
            }


        });



        dialog.show();

    }

    public void saveTask(final Task task, final int listId) {
        if (listId != 0)
            task.add("parent", groups.get(listId + 2));
        task.pinInBackground(StarterApplication.TODO_GROUP_NAME, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                todayAdapter.notifyDataSetChanged();
            }
        });
    }
}
