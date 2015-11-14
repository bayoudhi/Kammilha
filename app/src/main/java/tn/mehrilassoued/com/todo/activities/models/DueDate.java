package tn.mehrilassoued.com.todo.activities.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;


@ParseClassName("DueDate")
public class DueDate extends ParseObject {


    //date
    public String getDate() {
        return getString("date");
    }

    public void setDate(Date date) {
        put("date", date);
    }

    //reminder
    public String getReminder() {
        return getString("reminder");
    }

    public void setReminder(String reminder) {
        put("reminder", reminder);
    }

    //query
    public static ParseQuery<DueDate> getQuery() {
        return ParseQuery.getQuery(DueDate.class);
    }

    //draft
    public boolean isDraft() {
        return getBoolean("isDraft");
    }

    public void setDraft(boolean isDraft) {
        put("isDraft", isDraft);
    }
}
