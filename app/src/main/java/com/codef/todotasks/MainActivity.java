package com.codef.todotasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.codef.todotasks.db.TodoTasksAppDB;
import com.codef.todotasks.entity.Task;
import com.codef.todotasks.entity.Task_Table;
import com.codef.todotasks.util.TaskAdapter;
import com.raizlabs.android.dbflow.sql.language.SQLite;


import java.util.ArrayList;

import static com.codef.todotasks.util.Constants.*;

public class MainActivity extends AppCompatActivity {

    private ListView lvItems;
    private ArrayList<Task> tasks;

    private TaskAdapter taskAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getApplicationContext().deleteDatabase(TodoTasksAppDB.NAME);

        lvItems = (ListView)findViewById(R.id.lvItems);

        readItems();
        taskAdapter = new TaskAdapter(this, this, R.layout.item_task,  tasks);
        lvItems.setAdapter(taskAdapter);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Task task = (Task)data.getSerializableExtra(TASK);
            task.save();
            readItems();
            updateTaskList();
            Toast.makeText(this, task.getDescription(), Toast.LENGTH_SHORT).show();

        }else if (resultCode == ERASE_CODE && requestCode == REQUEST_CODE){
            Task task = (Task)data.getSerializableExtra(TASK);
            task.delete();
            Toast.makeText(this,  DELETED_TXT+ task.getDescription(), Toast.LENGTH_SHORT).show();
            readItems();
            updateTaskList();
        }

    }

    public void onAddItem(View v){
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String item = etNewItem.getText().toString();
        Task task = new Task();
        task.setDescription(item);
        task.save();
        etNewItem.setText(EMPTY_TXT);
        readItems();
        updateTaskList();

    }

    private void updateTaskList(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            taskAdapter.updateList(tasks);
            taskAdapter.notifyDataSetChanged();
            }
        });

    }

    public void readItems(){

        // Set query conditions based on column

         tasks = (ArrayList<Task>) SQLite.select().
                from(Task.class).orderBy(Task_Table.id,true).
                queryList();
        if(taskAdapter != null) {
            taskAdapter.updateList(tasks);
        }

    }


}
