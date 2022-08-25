package com.swetroye.idgenerator;

public interface WorkerManager {

    /**
     * 
     * @param datacenterId
     * @param maxWorkerId
     * @return an assigned worker id
     */
    public long getWorkerId(long datacenterId, long maxWorkerId);
    
    /**
     * To delete worker from db when worker is about to be terminated.
     */
    public void terminateWorker();
}
