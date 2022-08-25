package com.swetroye.idgenerator.entities;

import com.swetroye.idgenerator.enums.WorkerType;

public class Worker {
    /**
     * Unique id which is worker id
     */
    private long id;

    /**
     * Data center id
     */
    private long dataCenterId;

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

    public long getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(long dataCenterId) {
        this.dataCenterId = dataCenterId;
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

    public String getFullId() {
        return "workers:" + String.valueOf(dataCenterId) + ":" + String.valueOf(id);
    }
}
