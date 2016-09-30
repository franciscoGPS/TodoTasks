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

import static com.codef.todotasks.util.Constants.*;


/**
 * Created by fcorde on 28/09/16.
 */

public class TaskAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Task> tasks;
    private MainActivity mainActivity;

    private LayoutInflater inflater;

    public TaskAdapter(MainActivity activity, Context context, int resource, List<Task> objects) {

        this.context = context;
        this.tasks = (ArrayList<Task>) objects;
        mainActivity = activity;
        inflater = LayoutInflater.from(context);

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
        final TaskViewHolder taskViewHolder;

        Task task = getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_task, parent, false);
            taskViewHolder = new TaskViewHolder(convertView);
            convertView.setTag(taskViewHolder);
        } else {
            taskViewHolder = (TaskViewHolder) convertView.getTag();
        }

        taskViewHolder.tvDesc.setText(task.getDescription() != null ? task.getDescription() : "");

        if (task.getCompletedAt() != null ) {
            taskViewHolder.cbCompleted.setChecked(true);
        }else {
            taskViewHolder.cbCompleted.setChecked(false);
        }


        taskViewHolder.setId(task.getId());
        taskViewHolder.cbCompleted.setId(task.getId());
        taskViewHolder.tvCompletedAt.setText(task.getCompletedAt() != null ?  COMPLETED_SIGN+ DateUtil.format(task.getCompletedAt()) : EMPTY_TXT);
        taskViewHolder.tvDesc.setId(task.getId());

        taskViewHolder.tvDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int taskId = v.getId();
                Intent intent = new Intent(context, EditItemActivity.class);
                intent.putExtra (TASK_ID, taskId);
                ((MainActivity)context).startActivityForResult(intent,REQUEST_CODE);
            }
        });


        taskViewHolder.cbCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Task task = getTask(v.getId());
                    if(((CheckBox)v).isChecked()) {
                        task.setCompletedAt(new Date());
                        taskViewHolder.tvCompletedAt.setText(COMPLETED_SIGN + DateUtil.format(task.getCompletedAt()));
                        task.save();

                    }else {
                        task.setCompletedAt(null);
                        task.save();
                        taskViewHolder.tvCompletedAt.setText(EMPTY_TXT);
                    }
            }

        });


        // Return the cbCompleted view to render on screen
        return convertView;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    public void updateList(ArrayList<Task> tasks){
        this.tasks = tasks;
        notifyDataSetChanged();
    };


     public Task getTask(int taskId){
        return SQLite.select().from(Task.class).where(Task_Table.id.eq(taskId)).querySingle();
    }


    /**
     * ViewHolder Pattern implementation
     */
    private class TaskViewHolder  {
        TextView tvDesc, tvCompletedAt;
        CheckBox cbCompleted;
        private int id;


        public TaskViewHolder(View item) {
            tvDesc = (TextView) item.findViewById(R.id.tv_task);
            tvCompletedAt = (TextView) item.findViewById(R.id.tv_date);
            cbCompleted = (CheckBox) item.findViewById(R.id.cb_completed);
        }

        public void setId (int id){
            this.id = id;
        }

        public int getId (){
            return this.id;
        }


    }
}