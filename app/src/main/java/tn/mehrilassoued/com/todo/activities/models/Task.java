package tn.mehrilassoued.com.todo.activities.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.UUID;

/**
 * Created by Sofienne LASSOUED on 16/10/2015.
 */
@ParseClassName("Task")
public class Task extends ParseObject {
    //name
    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    //note
    public String getNote() {
        return getString("note");
    }

    public void setNote(String note) {
        put("note", note);
    }

    //done
    public boolean isDone() {
        return getBoolean("done");
    }

    public void setDone(boolean done) {
        put("done", done);
    }

    //deleted

    //important
    public boolean isImportant() {
        return getBoolean("important");
    }

    public void setImportant(boolean important) {
        put("important", important);
    }


    //author
    public ParseUser getAuthor() {
        return getParseUser("author");
    }

    public void setAuthor(ParseUser currentUser) {
        put("author", currentUser);
    }


    //UUID
    public void setUuidString() {
        UUID uuid = UUID.randomUUID();
        put("uuid", uuid.toString());
    }

    public String getUuidString() {
        return getString("uuid");
    }

    //query
    public static ParseQuery<Task> getQuery() {
        return ParseQuery.getQuery(Task.class);
    }

    //draft
    public boolean isDraft() {
        return getBoolean("draft");
    }

    public void setDraft(boolean isDraft) {
        put("draft", isDraft);
    }
}
