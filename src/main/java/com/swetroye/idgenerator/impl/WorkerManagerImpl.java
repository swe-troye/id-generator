package com.swetroye.idgenerator.impl;

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.swetroye.idgenerator.WorkerManager;
import com.swetroye.idgenerator.entities.Worker;
import com.swetroye.idgenerator.enums.WorkerType;
import com.swetroye.idgenerator.utils.DockerUtils;
import com.swetroye.idgenerator.utils.NetUtils;

public class WorkerManagerImpl implements WorkerManager {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    private Worker worker;
    private long timeout;
    private long heartbeatRate;

    @Override
    public long getWorkerId(long datacenterId, long maxWorkerId) {

        worker = buildWorker();

        // **********Needed to be change to Lua Script**********
        worker.setDataCenterId(datacenterId);
        worker.setId(0);

        // Get keys. Pattern -> workers:[workerId]
        Set<String> keySet = stringRedisTemplate.keys("workers:*");
        // keySet.forEach(x -> System.out.println("-----Matched key-----" + x));

        // Find next available worker id
        if (keySet.size() != 0) {
            for (long idx = 0; idx < maxWorkerId - 1; idx++) {
                if (!keySet.contains("workers:" + String.valueOf(datacenterId) + ":" + String.valueOf(idx))) {
                    worker.setId(idx);
                    break;
                }
            }
        }

        // Set value & TTL of this worker
        // Unit of TTL is second
        // Because Redis' basic data type is String, we need to convert workerId from
        // Long to String.
        stringRedisTemplate.opsForValue().set("workers:" + String.valueOf(datacenterId) + ":" + String.valueOf(worker.getId()),
                worker.getPodUid(), timeout, TimeUnit.SECONDS);
        System.out.println("workers:" + String.valueOf(datacenterId) + ":" + String.valueOf(worker.getId()));

        // Start heartbeat to db
        startHeartbeat();

        return worker.getId();
    }

    private Worker buildWorker() {
        Worker worker = new Worker();

        // Is Docker
        if (DockerUtils.isDocker()) {
            worker.setType(WorkerType.CONTAINER.value());
            worker.setPodUid(DockerUtils.getPodUid());
        }
        // Is actual machine
        else {
            worker.setType(WorkerType.ACTUAL.value());
            worker.setPodUid(NetUtils.getLocalAddress() + ":" + System.currentTimeMillis() + "-"
                    + (int) (Math.random() * 10000));
        }

        return worker;
    }

    private void startHeartbeat() {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);

        threadPoolExecutor.execute(() -> {
            while (true) {
                stringRedisTemplate.opsForValue().set("workers:" + String.valueOf(worker.getDataCenterId()) + ":" + String.valueOf(worker.getId()),
                        worker.getPodUid(), timeout, TimeUnit.SECONDS);
                        System.out.println("heartbeat workers:" + String.valueOf(worker.getDataCenterId()) + ":" + String.valueOf(worker.getId()));
                try {
                    Thread.sleep(heartbeatRate * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });
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

    public long getHeartbeatRate() {
        return heartbeatRate;
    }

    public void setHeartbeatRate(long heartbeatRate) {
        this.heartbeatRate = heartbeatRate;
    }
}
