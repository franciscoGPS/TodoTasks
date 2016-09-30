package com.codef.todotasks.entity;

import com.codef.todotasks.db.TodoTasksAppDB;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by fcorde on 28/09/16.
 */


@Table(database = TodoTasksAppDB.class )
public class  Task extends BaseModel implements Serializable {
    @Column
    @PrimaryKey(autoincrement = true)
    int id; // package-private recommended, not required
    @Column
    private Date createdAt;
    @Column
    private Date completedAt;
    @Column
    private String description;

    public Task() {
        this.id = 0;
        this.completedAt = null;
        this.createdAt = new Date();
        this.description = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
