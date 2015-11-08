package tn.mehrilassoued.com.todo.activities.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;

import java.util.List;

import tn.mehrilassoued.com.todo.R;
import tn.mehrilassoued.com.todo.activities.StarterApplication;
import tn.mehrilassoued.com.todo.activities.TaskActivity;
import tn.mehrilassoued.com.todo.activities.models.Subtask;

public class SubtaskAdapter extends RecyclerView
        .Adapter<SubtaskAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "SubtaskAdapter";
    public static List<Subtask> subtasks;
    private Context context;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView subtaskName;
        CheckBox subtaskDone;
        LinearLayout linearLayout;

        public DataObjectHolder(View itemView) {
            super(itemView);
            subtaskName = (TextView) itemView.findViewById(R.id.subtask_name);
            subtaskDone = (CheckBox) itemView.findViewById(R.id.subtask_done);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.subtasks_linear_layout);
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


    public SubtaskAdapter(List<Subtask> tasks, Context context) {
        this.subtasks = tasks;
        this.context = context;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subtask_row, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);


        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        holder.subtaskName.setText(subtasks.get(position).getName());
        holder.subtaskDone.setChecked(subtasks.get(position).isDone());

        if (holder.subtaskDone.isChecked()) {
            holder.subtaskName.setPaintFlags(holder.subtaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.subtaskName.setPaintFlags(holder.subtaskName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.subtaskDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.subtaskName.setPaintFlags(holder.subtaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    try {
                        Subtask subtask = subtasks.get(position);
                        subtask.setDone(true);
                        subtask.pin(StarterApplication.TODO_GROUP_NAME);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    holder.subtaskName.setPaintFlags(holder.subtaskName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    try {
                        Subtask subtask = subtasks.get(position);
                        subtask.setDone(false);
                        subtask.pin(StarterApplication.TODO_GROUP_NAME);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        holder.subtaskName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.subtaskDone.setChecked(!holder.subtaskDone.isChecked());
            }
        });

        if (subtasks.isEmpty()) {
            holder.linearLayout.setVisibility(View.GONE);
        } else {
            holder.linearLayout.setVisibility(View.VISIBLE);
        }

        holder.subtaskName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                deleteTask(position);
                return true;
            }
        });
    }

    public void addTask(Subtask dataObj) {
        subtasks.add(dataObj);
        notifyDataSetChanged();
    }

    public void addTask(Subtask dataObj, int index) {
        subtasks.add(index, dataObj);
        notifyDataSetChanged();
    }

    public void deleteTask(int index) {
        Subtask subtask = subtasks.get(index);
        try {
            subtask.delete();
            subtasks.remove(index);
            notifyDataSetChanged();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return subtasks.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}