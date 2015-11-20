/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package tn.mehrilassoued.com.todo.activities;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import tn.mehrilassoued.com.todo.activities.models.Group;
import tn.mehrilassoued.com.todo.activities.models.Subtask;
import tn.mehrilassoued.com.todo.activities.models.Task;


public class StarterApplication extends Application {

    private static final String PREF_NAME = "com.mehrilassoued.tn";
    private static boolean firstLaunch;
    public static String TODO_GROUP_NAME = "offline";
    public static String SHARED_PREFERENCES_NAME = "com.parse.starter";

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(getApplicationContext());

        //Adding subclasses
        ParseObject.registerSubclass(Group.class);
        ParseObject.registerSubclass(Task.class);
        ParseObject.registerSubclass(Subtask.class);

        // Add your initialization code here
        Parse.initialize(this, "s0NyXJqvdXFxk9h22RoE4gcRd6yrCHsYn49OomtN", "kzm3h2HWa3Ws7uPWThHwO98oc50LJBnlJvABC67s");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);


    }


}
