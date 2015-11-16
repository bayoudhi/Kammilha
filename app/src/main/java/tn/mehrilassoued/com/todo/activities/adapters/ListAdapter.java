package tn.mehrilassoued.com.todo.activities.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import tn.mehrilassoued.com.todo.R;
import tn.mehrilassoued.com.todo.activities.HomeActivity;
import tn.mehrilassoued.com.todo.activities.dao.TaskDAO;
import tn.mehrilassoued.com.todo.activities.models.Task;

public class ListAdapter extends RecyclerView
        .Adapter<ListAdapter
        .DataObjectHolder> {
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
        holder.listName.setText(lists.get(position));

        List<Task> todayTasks = TaskDAO.getDataSetToday();
        int todayNumber=todayTasks.size();

        List<Task> allTasks = TaskDAO.getDataSet();
        int allNumber=allTasks.size();

        List<Task> nextTasks = TaskDAO.getDataSetNotDone();
        int nextNumber=nextTasks.size();

        int doneNumber = TaskDAO.getDataSetDone().size();
        int importantNumber = TaskDAO.getDataSetImportant().size();

        if (position == 0)
            holder.listNumber.setText(String.valueOf(allNumber));
        else if(position==1)
            holder.listNumber.setText(String.valueOf(todayNumber));
        else if(position==2)
            holder.listNumber.setText(String.valueOf(nextNumber));
        else if(position==3)
            holder.listNumber.setText(String.valueOf(importantNumber));
        else
            holder.listNumber.setText(String.valueOf(doneNumber));
        holder.listName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (v != null)
               /* Snackbar.make(v,String.valueOf(v.getId()), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                    switch (position) {
                        case 0:
                             intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("show","all");
                            context.startActivity(intent);
                            break;
                        case 1:
                             intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("show","today");
                            context.startActivity(intent);
                            break;
                        case 2:
                            intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("show","next");
                            context.startActivity(intent);
                            break;
                        case 3:
                            intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("show","important");
                            context.startActivity(intent);
                            break;
                        case 4:
                            intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("show","history");
                            context.startActivity(intent);
                            break;
                        default:
                            break;
                    }
            }
        });
    }


    @Override
    public int getItemCount() {
        return lists.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}