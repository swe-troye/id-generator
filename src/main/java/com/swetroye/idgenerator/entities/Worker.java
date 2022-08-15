package com.swetroye.idgenerator.entities;

import com.swetroye.idgenerator.enums.WorkerType;

public class Worker {
    /**
     * Unique id which is worker id
     */
    private long id;

    /**
     * Container -> Pod Uid; Actual -> IP:Timestamp + random num
     */
    private String podUid;

    /**
     * type of {@link WorkerType}
     */
    private int type;

    /**
     * Getters & Setters
     */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPodUid() {
        return podUid;
    }

    public void setPodUid(String podUid) {
        this.podUid = podUid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}