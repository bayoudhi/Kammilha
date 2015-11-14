package tn.mehrilassoued.com.todo.activities.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;


@ParseClassName("List")
public class List extends ParseObject{


    //name
    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }



    //query
    public static ParseQuery<List> getQuery() {
        return ParseQuery.getQuery(List.class);
    }

    //draft
    public boolean isDraft() {
        return getBoolean("isDraft");
    }

    public void setDraft(boolean isDraft) {
        put("isDraft", isDraft);
    }
}
