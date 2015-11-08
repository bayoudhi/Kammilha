package tn.mehrilassoued.com.todo.activities.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.parse.ParseException;

import java.util.List;

import tn.mehrilassoued.com.todo.R;
import tn.mehrilassoued.com.todo.activities.StarterApplication;
import tn.mehrilassoued.com.todo.activities.models.Task;

public class TaskAdapter extends RecyclerView
        .Adapter<TaskAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "TaskAdapter";
    private List<Task> tasks;
    private Context context;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView task_name;
        CheckBox task_Done;
        TextView deleteButton;
        SwipeLayout swipeLayout;
        LinearLayout deleteLayout;
        LinearLayout hiLayout;

        public DataObjectHolder(View itemView) {
            super(itemView);
            task_name = (TextView) itemView.findViewById(R.id.task_name);
            task_Done = (CheckBox) itemView.findViewById(R.id.task_done);
            deleteButton = (TextView) itemView.findViewById(R.id.delete);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            deleteLayout = (LinearLayout) itemView.findViewById(R.id.delete_layout);
            hiLayout = (LinearLayout) itemView.findViewById(R.id.hi_layout);
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


    public TaskAdapter(List<Task> tasks, Context context) {
        this.tasks = tasks;
        this.context = context;
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

        if (holder.task_Done.isChecked()) {
            holder.task_name.setPaintFlags(holder.task_name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            holder.task_name.setPaintFlags(holder.task_name.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
        /*holder.task_Done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.task_name.setPaintFlags(holder.task_name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    try {
                        Task task = tasks.get(position);
                        task.setDone(true);
                        task.pin(StarterApplication.TODO_GROUP_NAME);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    holder.task_name.setPaintFlags(holder.task_name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    try {
                        Task task = tasks.get(position);
                        task.setDone(false);
                        task.pin(StarterApplication.TODO_GROUP_NAME);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });*/

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteTask(position);

            }
        });

        holder.hiLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTaskToDone(position);
            }
        });

        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.deleteLayout);
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.hiLayout);
    }

    public void setTaskToDone(int position) {
        Task task=tasks.get(position);
        task.setDone(!task.isDone());
        try {
            task.pin(StarterApplication.TODO_GROUP_NAME);
            tasks.set(position,task);
            notifyDataSetChanged();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public void addTask(Task dataObj, int index) {
        tasks.add(index, dataObj);
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
}