package com.swetroye.idgenerator.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.swetroye.idgenerator.IdGenerator;
import com.swetroye.idgenerator.WorkerManager;

public class IdGeneratorImpl implements IdGenerator, InitializingBean {

    /**
     * Bits allocation
     * There's one fix bit in the front representing sign bit
     */
    private int signBits = 1;
    private int timeBits = 40;
    private int datacenterBits = 2;
    private int workerBits = 9;
    private int sequenceBits = 12;

    /**
     * Start timestamp, unit as 10 millisecond. For example 2022-07-15 00:00:00 (ms:
     * 1657814400000, 10ms: 165781440000)
     */
    private String startTimestampStr = "2022-07-15 00:00:00";
    private long startTimestamp = 165781440000L;

    /** Max value */
    private long maxTimeDelta = ~(-1L << timeBits);
    private long maxDatacenterId = ~(-1L << datacenterBits);
    private long maxWorkerId = ~(-1L << workerBits);
    private long maxSequence = ~(-1L << sequenceBits);

    /** Shift bits */
    private int timeShift = datacenterBits + workerBits + sequenceBits;
    private int datacenterShift = workerBits + sequenceBits;
    private int workerShift = sequenceBits;

    /** Parts of id */
    private long datacenterId;
    private long workerId;
    private long sequence = 0;

    /** Others */
    private long lastTimestamp = -1L;
    private final static long MAX_ID_LENGTH = 64;

    @Autowired
    WorkerManager workerManager;

    @Override
    public void afterPropertiesSet() throws Exception {
        // Check & Allocate bits. Then set max & shift.
        // Chech bits
        int totalBits = signBits + timeBits + datacenterBits + workerBits + sequenceBits;
        if (totalBits != MAX_ID_LENGTH) {
            throw new RuntimeException(
                    "Bit allocation error. Total bits " + totalBits + " is not equal to " + MAX_ID_LENGTH);
        }

        // Max value
        maxTimeDelta = ~(-1L << timeBits);
        maxDatacenterId = ~(-1L << datacenterBits);
        maxWorkerId = ~(-1L << workerBits);
        maxSequence = ~(-1L << sequenceBits);

        // Shift bits
        timeShift = datacenterBits + workerBits + sequenceBits;
        datacenterShift = workerBits + sequenceBits;
        workerShift = sequenceBits;

        /** Check datacenter id */
        if (datacenterId > maxDatacenterId) {
            throw new RuntimeException("Datacenter id " + datacenterId + " exceeds the max " + maxDatacenterId);
        }

        /** Set Worker id & check */
        workerId = workerManager.getWorkerId();

        if (workerId > maxWorkerId) {
            throw new RuntimeException("Worker id " + workerId + " exceeds the max " + maxWorkerId);
        }

    }


    @Override
    public long getId() {
        long currentTimestamp = getCurrentTime();

        // Clock drift problem. Needed to be handled by using the timestamp gradually
        // increasing method.
        if (currentTimestamp < lastTimestamp) {
            // undone
        }

        // in the same time interval
        if (currentTimestamp == lastTimestamp) {
            sequence++;

            // Reach the max sequence in the same time interval
            if (sequence > maxSequence) {
                // System.out
                // .println("Reach the max sequence in the same time interval. " + sequence + "
                // > " + maxSequence);
                currentTimestamp = waitForNextTimestamp();
            }
        } else { // in the next time interval
            sequence = 0L; // reset sequence

            lastTimestamp = currentTimestamp;
            // System.out.println("next time interval");
        }

        return (currentTimestamp - startTimestamp) << timeShift
                | datacenterId << datacenterShift
                | workerId << workerShift
                | sequence;
    }

    // for testing purpose
    private void logForTesting() {
        long timeDelta = getCurrentTime() - startTimestamp;
        timeDelta = timeDelta << timeShift;
        datacenterId = 2L << datacenterShift;
        workerId = 1L << workerShift;
        sequence = 50L;
        System.out.println("Start time: " + startTimestamp);
        System.out.println("Current time: " + getCurrentTime());
        System.out.println("Time Delta: " + (getCurrentTime() - startTimestamp));
        System.out.println("Max time delta: " + maxTimeDelta);
        System.out.println("Max datacenter id: " + maxDatacenterId);
        System.out.println("Max worker id: " + maxWorkerId);
        System.out.println("Max sequence: " + maxSequence);
        System.out.println("Time shift: " + timeShift);
        System.out.println("Datacenter shift: " + datacenterShift);
        System.out.println("Worker shift: " + workerShift);
        System.out.println("-------------------------");
        System.out.println("Time Delta: " + timeDelta);
        System.out.println("Datacenter Id: " + datacenterId);
        System.out.println("Worker Id: " + workerId);
        System.out.println("Sequence: " + sequence);

        System.out.println(Long.toBinaryString(timeDelta));
        System.out.println(Long.toBinaryString(datacenterId));
        System.out.println(Long.toBinaryString(workerId));
        System.out.println(Long.toBinaryString(sequence));
        long id = timeDelta | datacenterId | workerId | sequence;
        System.out.println(id);
        System.out.println(Long.toBinaryString(id));
    }

    /**
     * Get current time stamp in every 10 ms
     */
    private long getCurrentTime() {
        return System.currentTimeMillis() / 10;
    }

    /**
     * Wait until get the next timestamp
     */
    private long waitForNextTimestamp() {

        long timestamp = getCurrentTime();
        while (getCurrentTime() <= lastTimestamp) {
            timestamp = getCurrentTime();
        }

        return timestamp;
    }

    /**
     * Getters & Setters
     */
    public int getTimeBits() {
        return timeBits;
    }

    public void setTimeBits(int timeBits) {
        this.timeBits = timeBits;
    }

    public int getDatacenterBits() {
        return datacenterBits;
    }

    public void setDatacenterBits(int datacenterBits) {
        this.datacenterBits = datacenterBits;
    }

    public int getWorkerBits() {
        return workerBits;
    }

    public void setWorkerBits(int workerBits) {
        this.workerBits = workerBits;
    }

    public int getSequenceBits() {
        return sequenceBits;
    }

    public void setSequenceBits(int sequenceBits) {
        this.sequenceBits = sequenceBits;
    }

    public long getDatacenterId() {
        return datacenterId;
    }

    public void setDatacenterId(long datacenterId) {
        this.datacenterId = datacenterId;
    }

    public void setStartTimestampStr(String startTimestampStr) {

        if (!startTimestampStr.isBlank()) {
            this.startTimestampStr = startTimestampStr;

            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTimestampStr);
                // Unit is 10 millisecond, so it needed to be divided by 10.
                this.startTimestamp = date.getTime() / 10;
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    public String getStartTimestampStr() {
        return startTimestampStr;
    }

    public void setStartTimestamp(Long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public Long getStartTimestamp() {
        return this.startTimestamp;
    }
}