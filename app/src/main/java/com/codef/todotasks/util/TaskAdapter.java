package com.codef.todotasks.util;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.codef.todotasks.EditItemActivity;
import com.codef.todotasks.MainActivity;
import com.codef.todotasks.R;
import com.codef.todotasks.entity.Task;
import com.codef.todotasks.entity.Task_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;


/**
 * Created by fcorde on 28/09/16.
 */

public class TaskAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Task> tasks;
    private MainActivity mainActivity;

    private final int REQUEST_CODE = 20;
    private final int ERASE_CODE = 30;



    public TaskAdapter(MainActivity activity, Context context, int resource, List<Task> objects) {

        this.context = context;
        this.tasks = (ArrayList<Task>) objects;
        mainActivity = activity;
    }

    @Override
    public Task getItem(int position) {
        return tasks.get(position);

    }

    @Override
    public long getItemId(int position) {
        return tasks.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Task task = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent, false);

        // Lookup view for data population
        TextView tvDescription = (TextView) convertView.findViewById(R.id.tv_task);
        CheckBox cbCompleted = (CheckBox) convertView.findViewById(R.id.cb_completed);
        // Populate the data into the template view using the data object

        tvDescription.setText(task.getDescription() != null ? task.getDescription() : "");

        if (task.getCompletedAt() != null ) {
            cbCompleted.setChecked(true);
        }else {
            cbCompleted.setChecked(false);
        }

        convertView.setId(task.getId());
        tvDescription.setId(task.getId());
        cbCompleted.setId(task.getId());

        tvDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int taskId = v.getId();
                Intent intent = new Intent(context, EditItemActivity.class);
                intent.putExtra ("task_id", taskId);
                ((MainActivity)context).startActivityForResult(intent,REQUEST_CODE);
            }
        });

            convertView.setOnLongClickListener(onLongClickListener);

            tvDescription.setOnLongClickListener(onLongClickListener);





        cbCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof CheckBox){
                    Task task = getTask(v.getId());
                    if(((CheckBox)v).isChecked()) {
                        task.setCompletedAt(new Date());
                        task.save();
                    }else {
                        task.setCompletedAt(null);
                        task.save();
                    }


                }


            }

        });
        // Return the cbCompleted view to render on screen
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    public void updateList(ArrayList<Task> tasks){
        this.tasks = tasks;
        notifyDataSetChanged();
    }


    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            int taskId = v.getId();

            Task task = getTask(taskId);
            if(task != null) {
                task.delete();
            }else{
                int id = task.getId();
            }

            //mainActivity.onDeleteItem(task);
            mainActivity.readItems();
            notifyDataSetChanged();

            return true;

        }


    };

     public Task getTask(int taskId){
        return SQLite.select().from(Task.class).where(Task_Table.id.eq(taskId)).querySingle();
    }
}