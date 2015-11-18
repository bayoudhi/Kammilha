package tn.mehrilassoued.com.todo.activities.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import tn.mehrilassoued.com.todo.R;
import tn.mehrilassoued.com.todo.activities.HomeActivity;
import tn.mehrilassoued.com.todo.activities.ListActivity;
import tn.mehrilassoued.com.todo.activities.StarterApplication;
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


            int todayNumber = TaskDAO.getTasksTodayCount();
            int allNumber = TaskDAO.getTasksCount();


            int nextNumber = TaskDAO.getTasksNextDaysCount();

            int doneNumber = TaskDAO.getTasksDoneCount();
            int importantNumber = TaskDAO.getTasksImportantCount();

            switch (lists.get(position)) {
                case "Inbox":
                    holder.listName.setText(lists.get(position));
                    holder.listNumber.setText(String.valueOf(allNumber));
                    break;
                case "Today":
                    holder.listName.setText(lists.get(position));
                    holder.listNumber.setText(String.valueOf(todayNumber));
                    break;
                case "In 7 Days":
                    holder.listName.setText(lists.get(position));
                    holder.listNumber.setText(String.valueOf(nextNumber));
                    break;
                case "Importantt":

                    holder.listName.setText(lists.get(position));
                    holder.listNumber.setText(String.valueOf(importantNumber));
                    holder.listName.setTextColor(Color.RED);

                    break;
                case "History":
                    holder.listNumber.setText(String.valueOf(doneNumber));
                    holder.listName.setTextColor(Color.GRAY);
                    holder.listName.setPaintFlags(holder.listName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    break;
                case "Create list":
                    holder.listName.setText(lists.get(position));
                    holder.listName.setTextColor(Color.BLUE);
                    break;
                default:
                    holder.listName.setText(lists.get(position));
                    //holder.listNumber.setText(String.valueOf(allNumber));
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
                            case "In 7 Days":
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
                            case "Create list":
                                new MaterialDialog.Builder(context)
                                        .title("Add a list")

                                        .inputType(InputType.TYPE_CLASS_TEXT)
                                        .input("", "", new MaterialDialog.InputCallback() {
                                            @Override
                                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                                if (input.toString().isEmpty()) return;

                                                tn.mehrilassoued.com.todo.activities.models.List list=new tn.mehrilassoued.com.todo.activities.models.List();
                                                list.setName(input.toString());
                                                list.setDraft(true);

                                                list.pinInBackground(StarterApplication.TODO_GROUP_NAME, new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        ListActivity.check=true;
                                                    }
                                                });


                                            }
                                        }).positiveText("Add").negativeText("Cancel").
                                        show();
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