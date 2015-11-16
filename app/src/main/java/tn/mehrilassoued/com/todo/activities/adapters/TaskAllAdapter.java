package tn.mehrilassoued.com.todo.activities.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import tn.mehrilassoued.com.todo.R;
import tn.mehrilassoued.com.todo.activities.StarterApplication;
import tn.mehrilassoued.com.todo.activities.TaskActivity;
import tn.mehrilassoued.com.todo.activities.models.Subtask;
import tn.mehrilassoued.com.todo.activities.models.Task;

public class TaskAllAdapter extends RecyclerView
        .Adapter<TaskAllAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "TaskAllAdapter";
    public static List<Task> tasks;
    private Context context;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView task_name;
        CheckBox task_Done;
        TextView importantButton;
        SwipeLayout swipeLayout;
        LinearLayout importantLayout;
        LinearLayout hiLayout;
        TextView task_date;


        public DataObjectHolder(View itemView) {
            super(itemView);
            task_name = (TextView) itemView.findViewById(R.id.task_name);
            task_Done = (CheckBox) itemView.findViewById(R.id.task_done);
            importantButton = (TextView) itemView.findViewById(R.id.important);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            importantLayout = (LinearLayout) itemView.findViewById(R.id.important_layout);
            hiLayout = (LinearLayout) itemView.findViewById(R.id.hi_layout);
            task_date = (TextView) itemView.findViewById(R.id.task_date);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }


    public TaskAllAdapter(List<Task> tasks, Context context) {
        this.tasks = tasks;
        this.context = context;
    }

    public void clearAll() {
        tasks.clear();
        notifyDataSetChanged();
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tasks_row, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);


        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        holder.task_name.setText(tasks.get(position).getName());
        holder.task_Done.setChecked(tasks.get(position).isDone());

        Task task = tasks.get(position);

        if (holder.task_Done.isChecked()) {
            holder.task_name.setPaintFlags(holder.task_name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.task_name.setPaintFlags(holder.task_name.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }

        if (task.isImportant()) {
            holder.task_name.setTextColor(Color.RED);
        } else
            holder.task_name.setTextColor(Color.BLACK);

        if (task.isDone()) {
            holder.task_name.setTextColor(Color.GRAY);
        }

        if (task.get("date")!=null) {
            DateFormat dateFormat = new SimpleDateFormat();
            holder.task_date.setText(dateFormat.format(task.getDate("date")));
        }else{
            holder.task_date.setText("");

        }

        /*holder.subtaskDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.listName.setPaintFlags(holder.listName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    try {
                        Task task = lists.get(position);
                        task.setDone(true);
                        task.pin(StarterApplication.TODO_GROUP_NAME);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    holder.listName.setPaintFlags(holder.listName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    try {
                        Task task = lists.get(position);
                        task.setDone(false);
                        task.pin(StarterApplication.TODO_GROUP_NAME);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });*/

        holder.importantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTaskImportant(position);

            }
        });

        holder.hiLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTaskDone(position);
                setAllSubtasksDone(position);
            }
        });

        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.importantLayout);
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.hiLayout);

        holder.task_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TaskActivity.class);

                intent.putExtra("id", String.valueOf(position));
                intent.putExtra("from","all");
                context.startActivity(intent);
            }
        });

    }

    private void setTaskImportant(int position) {
        Task task = tasks.get(position);

        try {
            task.setImportant(!task.isImportant());
            task.pin(StarterApplication.TODO_GROUP_NAME);
            notifyDataSetChanged();
        } catch (ParseException e) {
            task.setImportant(task.isImportant());
            e.printStackTrace();
        }
    }

    private void setAllSubtasksDone(int position) {
        List<Subtask> results = new ArrayList<Subtask>();
        Task task = tasks.get(position);

        ParseQuery<Subtask> query = Subtask.getQuery();
        query.whereEqualTo("parent", task);
        query.fromLocalDatastore();
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (Subtask subtask : results) {

            try {
                subtask.setDone(true);
                subtask.pin(StarterApplication.TODO_GROUP_NAME);
            } catch (ParseException e) {
                e.printStackTrace();
                subtask.setDone(false);
            }
        }


    }

    public void setTaskDone(int position) {
        Task task = tasks.get(position);

        try {
            task.setDone(!task.isDone());
            task.pin(StarterApplication.TODO_GROUP_NAME);
            tasks.set(position, task);
            notifyDataSetChanged();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public void addTask(Task dataObj, int index) {
        tasks.add(index, dataObj);
        notifyDataSetChanged();
    }

    public void addTask(Task dataObj) {
        tasks.add(dataObj);
        notifyDataSetChanged();
    }


    public void deleteTask(int index) {
        Task task = tasks.get(index);
        try {
            task.delete();
            tasks.remove(index);
            notifyDataSetChanged();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    public static List<Task> getTasks() {
        return tasks;
    }

    public static void setTasks(List<Task> tasks) {
        TaskAllAdapter.tasks = tasks;
    }
}