package tn.mehrilassoued.com.todo.activities.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.List;

import tn.mehrilassoued.com.todo.R;
import tn.mehrilassoued.com.todo.activities.HomeActivity;
import tn.mehrilassoued.com.todo.activities.ListActivity;
import tn.mehrilassoued.com.todo.activities.StarterApplication;
import tn.mehrilassoued.com.todo.activities.dao.TaskDAO;
import tn.mehrilassoued.com.todo.activities.models.Group;

public class ListAdapter extends RecyclerView
        .Adapter<ListAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "ListAdapter";
    public static List<Group> groups;
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


    public ListAdapter(List<Group> groups, Context context) {
        this.groups = groups;
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

        int todayNumber = TaskDAO.getTasksTodayCount();
        int allNumber = TaskDAO.getTasksCount();


        int nextNumber = TaskDAO.getTasksNextDaysCount();

        int doneNumber = TaskDAO.getTasksDoneCount();
        int importantNumber = TaskDAO.getTasksImportantCount();

        int tasksNumber = TaskDAO.getTasksByListCount(groups.get(position));
        int inboxNumber = TaskDAO.getTasksByListCount(null);

        switch (groups.get(position).getName()) {
            case "Inbox":
                holder.listName.setText("Inbox");
                holder.listNumber.setText(String.valueOf(inboxNumber));
                break;
            case "Today":
                holder.listName.setText("Today");
                holder.listNumber.setText(String.valueOf(todayNumber));
                break;
            case "Week":
                holder.listName.setText(groups.get(position).getName());
                holder.listNumber.setText(String.valueOf(nextNumber));
                break;
            case "Starred":
                holder.listName.setText(groups.get(position).getName());
                holder.listNumber.setText(String.valueOf(importantNumber));
                break;
           /* case "Importantt":

                holder.listName.setText(groups.get(position).getName());
                holder.listNumber.setText(String.valueOf(importantNumber));
                holder.listName.setTextColor(Color.RED);

                break;
            case "History":
                holder.listNumber.setText(String.valueOf(doneNumber));
                holder.listName.setTextColor(Color.GRAY);
                holder.listName.setPaintFlags(holder.listName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                break;*/
            case "Create list":
                holder.listName.setText(groups.get(position).getName());
                holder.listName.setTextColor(Color.BLUE);
                holder.listNumber.setText("");
                break;
            default:

                holder.listName.setText(groups.get(position).getName());
                if (tasksNumber != 0)
                    holder.listNumber.setText(String.valueOf(tasksNumber));
                else holder.listNumber.setText("");
                holder.listName.setTextColor(Color.BLACK);
                break;


        }


        holder.listName.setOnClickListener(new View.OnClickListener()

                                           {
                                               @Override
                                               public void onClick(View v) {
                                                   Intent intent;

               /* Snackbar.make(v,String.valueOf(v.getId()), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                                                   switch (groups.get(position).getName()) {
                                                       case "Create list":
                                                           new MaterialDialog.Builder(context)
                                                                   .title("Add a list")

                                                                   .inputType(InputType.TYPE_CLASS_TEXT)
                                                                   .input("", "", new MaterialDialog.InputCallback() {
                                                                       @Override
                                                                       public void onInput(MaterialDialog dialog, CharSequence input) {
                                                                           if (input.toString().isEmpty() || input.toString().equals("Inbox")
                                                                                   || input.toString().equals("Week") || input.toString().equals("Starred")||
                                                                                   input.toString().equals("Today")
                                                                                   )
                                                                               return;

                                                                           Group group = new Group();
                                                                           group.setName(input.toString());
                                                                           group.setDraft(true);

                                                                           group.pinInBackground(StarterApplication.TODO_GROUP_NAME, new SaveCallback() {
                                                                               @Override
                                                                               public void done(ParseException e) {
                                                                                   ((ListActivity) context).loadLists();
                                                                               }
                                                                           });


                                                                       }
                                                                   }).positiveText("Add").negativeText("Cancel").
                                                                   show();
                                                           break;
                                                       default:
                                                           intent = new Intent(context, HomeActivity.class);
                                                           intent.putExtra("id", position);
                                                           context.startActivity(intent);
                                                           break;
                                                   }

                                               }
                                           }

        );


    }


    @Override
    public int getItemCount() {
        return groups.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}