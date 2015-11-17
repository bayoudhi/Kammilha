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
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import tn.mehrilassoued.com.todo.R;
import tn.mehrilassoued.com.todo.activities.ListActivity;
import tn.mehrilassoued.com.todo.activities.StarterApplication;
import tn.mehrilassoued.com.todo.activities.TaskActivity;
import tn.mehrilassoued.com.todo.activities.models.Subtask;
import tn.mehrilassoued.com.todo.activities.models.Task;

public class TaskTomorrowAdapter extends RecyclerView
        .Adapter<TaskTomorrowAdapter.DataObjectHolderToday> {
    private static String LOG_TAG = "TaskTodayAdapter";
    public static List<Task> tasksss;
    private Context context;
    private static MyClickListener myClickListener;

    public static class DataObjectHolderToday extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView task_name;
        CheckBox task_Done;
        TextView importantButton;
        SwipeLayout swipeLayout;
        LinearLayout importantLayout;
        LinearLayout hiLayout;
        TextView task_date;


        public DataObjectHolderToday(View itemView) {
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


    public TaskTomorrowAdapter(List<Task> tasksss, Context context) {
        this.tasksss = tasksss;
        this.context = context;
    }

    public void clearAll() {
        tasksss.clear();
        notifyDataSetChanged();
    }

    @Override
    public DataObjectHolderToday onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tasks_row, parent, false);

        DataObjectHolderToday dataObjectHolderToday = new DataObjectHolderToday(view);


        return dataObjectHolderToday;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolderToday holder, final int position) {
        holder.task_name.setText(tasksss.get(position).getName());
        holder.task_Done.setChecked(tasksss.get(position).isDone());

        Task task = tasksss.get(position);

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

        if (task.get("date") != null) {
            DateFormat dateFormat = new SimpleDateFormat();
            holder.task_date.setText(dateFormat.format(task.getDate("date")));
        } else {
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
                ListActivity.check = true;

            }
        });

        holder.hiLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTaskDone(position);
                setAllSubtasksDone(position);
                ListActivity.check = true;
            }
        });

        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.importantLayout);
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.hiLayout);

        holder.task_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TaskActivity.class);

                intent.putExtra("id", String.valueOf(position));
                intent.putExtra("from", "tomorrow");
                context.startActivity(intent);
            }
        });

    }

    private void setTaskImportant(int position) {
        Task task = tasksss.get(position);

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
        Task task = tasksss.get(position);

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
        Task task = tasksss.get(position);

        try {
            task.setDone(!task.isDone());
            task.pin(StarterApplication.TODO_GROUP_NAME);
            tasksss.set(position, task);
            notifyDataSetChanged();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public void addTask(Task dataObj, int index) {
        tasksss.add(index, dataObj);
        notifyDataSetChanged();
    }

    public void addTask(Task dataObj) {
        tasksss.add(dataObj);
        notifyDataSetChanged();
    }


    public void deleteTask(int index) {
        Task task = tasksss.get(index);
        try {
            task.delete();
            tasksss.remove(index);
            notifyDataSetChanged();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return tasksss.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    public static List<Task> getTasksss() {
        return tasksss;
    }

    public static void setTasksss(List<Task> tasksss) {
        TaskTomorrowAdapter.tasksss = tasksss;
    }
}