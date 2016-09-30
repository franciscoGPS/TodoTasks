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

public class MainActivity extends AppCompatActivity {

    ListView lvItems;
    ArrayList<Task> tasks;

    public TaskAdapter taskAdapter;
    private final int REQUEST_CODE = 20;
    private final int ERASE_CODE = 30;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getApplicationContext().deleteDatabase(TodoTasksAppDB.NAME);

        lvItems = (ListView)findViewById(R.id.lvItems);


        //itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);

        readItems();
        taskAdapter = new TaskAdapter(this, this, R.layout.item_task,  tasks);
        lvItems.setAdapter(taskAdapter);

        //setupListViewListener();


    }

    /*private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = tasks.get(position);
                task.delete();
                //tasks.remove(position);
                readItems();
                updateTaskList();
                //writeItems();
                return true;
            }
        });
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            /*String itemText = data.getExtras().getString("text");
            int position = data.getExtras().getInt("position");**/
            Task task = (Task)data.getSerializableExtra("task");
            task.save();

            readItems();
            updateTaskList();
            Toast.makeText(this, task.getDescription(), Toast.LENGTH_SHORT).show();

        }else if (resultCode == ERASE_CODE && requestCode == REQUEST_CODE){
            Task task = (Task)data.getSerializableExtra("task");

            task.delete();
            Toast.makeText(this, "Deleted: " + task.getDescription(), Toast.LENGTH_SHORT).show();
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
        etNewItem.setText("");

        readItems();
        updateTaskList();



    }

    /*public void onDeleteItem(Task task){
        if(task != null) {
            task.delete();
            //task.save();
        }
        readItems();
        updateTaskList();
    }*/

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




        /*File filesDir = getFilesDir();
        File todoFile = new File(filesDir, FILE_NAME);
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        }catch (IOException e){
            items = new ArrayList<String>();
        }*/
    }


}
