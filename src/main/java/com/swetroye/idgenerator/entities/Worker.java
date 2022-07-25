package com.swetroye.idgenerator.entities;

import com.swetroye.idgenerator.enums.WorkerType;

public class Worker {
    /**
     * Unique id which is worker id
     */
    private long id;

    /**
     * Container -> hostname; Actual -> IP
     */
    private String hostName;

    /**
     * Container -> port; Actual -> Timestamp + random num
     */
    private String port;

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

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
