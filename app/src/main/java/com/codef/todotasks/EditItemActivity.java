package com.codef.todotasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    EditText etEditItem;
    Button btnSave;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        etEditItem = (EditText)findViewById(R.id.etEditItem);
        btnSave = (Button)findViewById(R.id.saveItem);
        etEditItem.setText(getIntent().getStringExtra("text"));
        position = getIntent().getExtras().getInt("position");

        if(etEditItem.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            etEditItem.clearFocus();
        }


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                // Pass relevant data back as a result
                data.putExtra("text", etEditItem.getText().toString());
                data.putExtra("position", position); // ints work too
                // Activity finished ok, return the data
                setResult(RESULT_OK, data); // set result code and bundle data for response
                finish(); // closes the activity, pass data to parent
            }
        });


    }

}
