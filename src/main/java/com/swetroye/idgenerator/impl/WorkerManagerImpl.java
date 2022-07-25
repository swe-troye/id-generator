package com.swetroye.idgenerator.impl;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.swetroye.idgenerator.WorkerManager;
import com.swetroye.idgenerator.entities.Worker;
import com.swetroye.idgenerator.enums.WorkerType;
import com.swetroye.idgenerator.utils.DockerUtils;

public class WorkerManagerImpl implements WorkerManager {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    private long timeout;

    @Override
    public long getWorkerId() {

        Worker worker = buildWorker();

        // **********Needed to be change to Lua Script**********
        worker.setId(0);

        // Get keys. Pattern -> workers:[workerId]
        Set<String> keySet = stringRedisTemplate.keys("workers:*");
        // keySet.forEach(x -> System.out.println("-----Matched key-----" + x));

        // Find next available worker id
        if (keySet.size() != 0) {
            for (long idx = 0; idx < Long.MAX_VALUE; idx++) {
                if (!keySet.contains("workers:" + String.valueOf(idx))) {
                    worker.setId(idx);
                    break;
                }
            }
        }

        // Set value & TTL of this worker
        // Unit of TTL is second
        // Because Redis' basic data type is String, we need to convert workerId from
        // Long to String.
        stringRedisTemplate.opsForValue().set("workers:" + String.valueOf(worker.getId()),
                worker.getHostName() + ":" + worker.getPort(), timeout, TimeUnit.SECONDS);

        return worker.getId();
    }

    private Worker buildWorker() {
        Worker worker = new Worker();

        // Is Docker
        if (DockerUtils.isDocker()) {
            worker.setType(WorkerType.CONTAINER.value());
            worker.setHostName(DockerUtils.getHostname());
            worker.setPort(DockerUtils.getPort());
        }
        // Is actual machine
        else {
            worker.setType(WorkerType.ACTUAL.value());
            worker.setHostName("127.0.0.1"); // NetUtils.getLocalAddress()
            worker.setPort(System.currentTimeMillis() + "-" + (int) (Math.random() * 10000));
        }

        return worker;
    }

    /**
     * Getters & Setters
     */
    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
