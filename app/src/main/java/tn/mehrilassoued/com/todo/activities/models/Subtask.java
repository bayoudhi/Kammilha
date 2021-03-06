package tn.mehrilassoued.com.todo.activities.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.UUID;


@ParseClassName("Subtask")
public class Subtask extends ParseObject {


    //name
    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    //done
    public boolean isDone() {
        return getBoolean("done");
    }

    public void setDone(boolean done) {
        put("done", done);
    }

    /*
    //task
    public String getTask() {
        return getString("parent");
    }

    public void setTask(Task task) {
        put("parent", task);
    }*/


    //query
    public static ParseQuery<Subtask> getQuery() {
        return ParseQuery.getQuery(Subtask.class);
    }

    //draft
    public boolean isDraft() {
        return getBoolean("isDraft");
    }

    public void setDraft(boolean isDraft) {
        put("isDraft", isDraft);
    }
}
