package tn.mehrilassoued.com.todo.activities.dao;

import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tn.mehrilassoued.com.todo.activities.models.Group;
import tn.mehrilassoued.com.todo.activities.models.Task;

/**
 * Created by azize on 11/16/2015.
 */
public abstract class TaskDAO {
    public static List<Task> getTasksDone() {
        List results = new ArrayList<Task>();


        ParseQuery<Task> query = Task.getQuery();
        query.orderByAscending("date");
        query.whereEqualTo("done", true);
        query.fromLocalDatastore();
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return results;
    }

    public static int getTasksDoneCount() {
        List results = new ArrayList<Task>();


        ParseQuery<Task> query = Task.getQuery();
        query.orderByAscending("date");
        query.whereEqualTo("done", true);
        query.fromLocalDatastore();
        try {
            return query.count();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }


    public static List<Task> getTasksToday() {

        List results = new ArrayList<Task>();

        Date nextDay = new Date();
        nextDay.setTime(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        c.setTime(nextDay);
        c.add(Calendar.DATE, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        nextDay = c.getTime();

        ParseQuery<Task> query1 = Task.getQuery();

        query1.whereLessThan("date", nextDay);
        query1.orderByAscending("date");
        query1.fromLocalDatastore();

        try {
            results = query1.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static int getTasksTodayCount() {


        Date nextDay = new Date();
        nextDay.setTime(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        c.setTime(nextDay);
        c.add(Calendar.DATE, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        nextDay = c.getTime();

        ParseQuery<Task> query1 = Task.getQuery();

        query1.whereLessThan("date", nextDay);
        query1.orderByAscending("date");
        query1.fromLocalDatastore();


        try {
            return query1.count();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static List<Task> getDataSetOutdated() {
        List results = new ArrayList<Task>();

        Calendar c = Calendar.getInstance();
        Date currentDay = new Date();
        c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        currentDay.setTime(c.getTimeInMillis());


        ParseQuery<Task> query = Task.getQuery();
        query.whereLessThan("date", currentDay);
        query.orderByAscending("date");
        query.fromLocalDatastore();
        try {
            results = query.find();
            System.out.println(results.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return results;
    }

    public static List<Task> getTasks() {
        List results = new ArrayList<Task>();


        ParseQuery<Task> query = Task.getQuery();
        query.orderByAscending("date");
        query.fromLocalDatastore();
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return results;
    }

    public static int getTasksCount() {
        ParseQuery<Task> query = Task.getQuery();
        query.orderByAscending("date");
        query.fromLocalDatastore();

        try {
            return query.count();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return 0;
    }


    public static List<Task> getTasksNextDays() {
        List results = new ArrayList<Task>();

        Date nextDay = new Date();
        nextDay.setTime(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        c.setTime(nextDay);
        c.add(Calendar.DATE, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        nextDay = c.getTime();


        ParseQuery<Task> query = Task.getQuery();

        query.whereGreaterThanOrEqualTo("date", nextDay);
        query.orderByAscending("date");
        query.whereEqualTo("done", false);
        query.whereNotEqualTo("date", null);
        query.fromLocalDatastore();
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return results;
    }

    public static int getTasksNextDaysCount() {

        Date nextDay = new Date();
        nextDay.setTime(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        c.setTime(nextDay);
        c.add(Calendar.DATE, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        nextDay = c.getTime();


        ParseQuery<Task> query = Task.getQuery();

        query.whereGreaterThanOrEqualTo("date", nextDay);
        query.orderByAscending("date");
        query.whereEqualTo("done", false);
        query.whereNotEqualTo("date", null);
        query.fromLocalDatastore();
        try {
            return query.count();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static List<Task> getTasksImportant() {
        List results = new ArrayList<Task>();


        ParseQuery<Task> query = Task.getQuery();


        query.whereEqualTo("important", true);

        query.fromLocalDatastore();
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return results;
    }

    public static int getTasksImportantCount() {
        List results = new ArrayList<Task>();


        ParseQuery<Task> query = Task.getQuery();


        query.whereEqualTo("important", true);

        query.fromLocalDatastore();
        try {
            return query.count();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }


    public static List<Task> getTasksByList(Group group) {
        List results = new ArrayList<Task>();


        ParseQuery<Task> query = Task.getQuery();
        query.whereEqualTo("parent", group);
        query.orderByAscending("date");
        query.fromLocalDatastore();
        try {
            results = query.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return results;
    }

    public static int getTasksByListCount(Group group) {


        ParseQuery<Task> query = Task.getQuery();
        query.whereEqualTo("parent", group);
        query.orderByAscending("date");
        query.fromLocalDatastore();
        try {
            return query.count();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
