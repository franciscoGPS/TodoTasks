package com.codef.todotasks.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by fcorde on 28/09/16.
 */
@Database(name = TodoTasksAppDB.NAME, version = TodoTasksAppDB.VERSION)
public class TodoTasksAppDB {
    public static final String NAME = "TodoTasksAppDB";

    public static final int VERSION = 4;
}