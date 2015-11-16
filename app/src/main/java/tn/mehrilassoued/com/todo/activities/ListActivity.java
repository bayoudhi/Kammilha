package tn.mehrilassoued.com.todo.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tn.mehrilassoued.com.todo.R;
import tn.mehrilassoued.com.todo.activities.adapters.ListAdapter;
import tn.mehrilassoued.com.todo.activities.adapters.TaskTodayAdapter;
import tn.mehrilassoued.com.todo.activities.dao.TaskDAO;
import tn.mehrilassoued.com.todo.activities.models.Task;

public class ListActivity extends AppCompatActivity {

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



    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        List<String> tasks = new ArrayList<>();
        tasks.add("Inbox");
        tasks.add("Today");
        tasks.add("Tomorrow");
        tasks.add("Important");
        tasks.add("History");
        todayAdapter = new ListAdapter(tasks, this);
        todayRecyclerView.setAdapter(todayAdapter);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        todayRecyclerView.addItemDecoration(itemDecoration);
    }
}
