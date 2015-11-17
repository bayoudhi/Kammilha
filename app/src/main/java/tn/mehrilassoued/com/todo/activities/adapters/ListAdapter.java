package tn.mehrilassoued.com.todo.activities.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import tn.mehrilassoued.com.todo.R;
import tn.mehrilassoued.com.todo.activities.HomeActivity;
import tn.mehrilassoued.com.todo.activities.dao.TaskDAO;
import tn.mehrilassoued.com.todo.activities.models.Task;

public class ListAdapter extends RecyclerView
        .Adapter<ListAdapter
        .DataObjectHolder> {
    public static boolean check = true;
    private static String LOG_TAG = "ListAdapter";
    public static List<String> lists;
    private Context context;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView listName;
        TextView listNumber;

        public DataObjectHolder(View itemView) {
            super(itemView);
            listName = (TextView) itemView.findViewById(R.id.list_name);
            listNumber = (TextView) itemView.findViewById(R.id.list_count);

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


    public ListAdapter(List<String> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lists_row, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);


        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        if (check) {

            holder.listName.setText(lists.get(position));


            int todayNumber = TaskDAO.getTasksTodayCount();
            int allNumber = TaskDAO.getTasksCount();


            int nextNumber = TaskDAO.getTasksNextDaysCount();

            int doneNumber = TaskDAO.getTasksDoneCount();
            int importantNumber = TaskDAO.getTasksImportantCount();

            switch (lists.get(position)) {
                case "Inbox":
                    holder.listNumber.setText(String.valueOf(allNumber));
                    break;
                case "Today":
                    holder.listNumber.setText(String.valueOf(todayNumber));
                    break;
                case "Tomorrow":
                    holder.listNumber.setText(String.valueOf(nextNumber));
                    break;
                case "Important":
                    holder.listNumber.setText(String.valueOf(importantNumber));
                    holder.listName.setTextColor(Color.RED);
                    break;
                case "History":
                    holder.listNumber.setText(String.valueOf(doneNumber));
                    holder.listName.setTextColor(Color.GRAY);
                    holder.listName.setPaintFlags(holder.listName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    break;

            }


            holder.listName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    if (v != null)
               /* Snackbar.make(v,String.valueOf(v.getId()), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                        switch (lists.get(position)) {
                            case "Inbox":
                                intent = new Intent(context, HomeActivity.class);
                                intent.putExtra("show", "all");
                                context.startActivity(intent);
                                break;
                            case "Today":
                                intent = new Intent(context, HomeActivity.class);
                                intent.putExtra("show", "today");
                                context.startActivity(intent);
                                break;
                            case "Tomorrow":
                                intent = new Intent(context, HomeActivity.class);
                                intent.putExtra("show", "next");
                                context.startActivity(intent);
                                break;
                            case "Important":
                                intent = new Intent(context, HomeActivity.class);
                                intent.putExtra("show", "important");
                                context.startActivity(intent);
                                break;
                            case "History":
                                intent = new Intent(context, HomeActivity.class);
                                intent.putExtra("show", "history");
                                context.startActivity(intent);
                                break;
                            default:
                                break;
                        }

                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return lists.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}