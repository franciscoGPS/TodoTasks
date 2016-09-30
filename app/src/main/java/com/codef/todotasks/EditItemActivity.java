package com.codef.todotasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.codef.todotasks.entity.Task;
import com.codef.todotasks.entity.Task_Table;
import com.codef.todotasks.util.DateUtil;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.Date;


import static com.codef.todotasks.util.Constants.*;

public class EditItemActivity extends AppCompatActivity {

    private EditText etEditItem;
    private Button btnSave, btnDelete;
    private int position;
    private Task task;


    private CheckBox checkBox;
    private TextView tvCompletedAt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        etEditItem = (EditText)findViewById(R.id.etEditItem);
        checkBox = (CheckBox)findViewById(R.id.cb_completed);
        btnSave = (Button)findViewById(R.id.saveItem);
        btnDelete = (Button)findViewById(R.id.deleteItem);
        tvCompletedAt = (TextView)findViewById(R.id.tv_completed_at);

        getValues();


        etEditItem.setText(task.getDescription() != null ? task.getDescription() : EMPTY_TXT);
        if(task.getCompletedAt() != null){
            checkBox.setChecked(true);
            tvCompletedAt.setText(COMPLETED_LABEL+ DateUtil.format(task.getCompletedAt()));
        }


        if(etEditItem.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            etEditItem.clearFocus();
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v instanceof CheckBox)
                    if(((CheckBox)v).isChecked()){
                        task.setCompletedAt(new Date());
                        tvCompletedAt.setText(COMPLETED_LABEL+ DateUtil.format(task.getCompletedAt()));
                    }else {
                        task.setCompletedAt(null);
                        tvCompletedAt.setText(EMPTY_TXT);
                    }
                task.save();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent(getApplicationContext(), MainActivity.class);
                // Pass relevant data back as a result
                task.setDescription(etEditItem.getText().toString());
                data.putExtra(TASK, task);
                data.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                setResult(RESULT_OK, data); // set result code and bundle data for response
                finish(); // closes the activity, pass data to parent
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent(getApplicationContext(), MainActivity.class);
                // Pass relevant data back as a result
                task.setDescription(etEditItem.getText().toString());
                data.putExtra(TASK, task);
                data.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                setResult(ERASE_CODE, data); // set result code and bundle data for response
                finish(); // closes the activity, pass data to parent
            }
        });


    }

    private void getValues(){
        int taskId =  getIntent().getIntExtra(TASK_ID, DEFAULT_INVALID_VALUE );
        getTask(taskId);
    }

    public Task getTask(int taskId) {
        return task = SQLite.select().from(Task.class).where(Task_Table.id.eq(taskId)).querySingle();
    }
}
