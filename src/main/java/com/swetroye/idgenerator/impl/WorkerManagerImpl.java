package com.swetroye.idgenerator.impl;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

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
        
        worker.setDataCenterId(datacenterId);
        worker.setId(0);

        // ----- Use lua script to ensure the atomicity of the redis operations -----
        DefaultRedisScript<List> redisScript = new DefaultRedisScript<>();
        // Set script
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis/GetWorkerId.lua")));
        // Set return type
        redisScript.setResultType(List.class);
        
        String keyPattern = "workers:*";
        // param 1: redisScript, param 2: key list, param 3-n: arg (multiple)
        var result = stringRedisTemplate.execute(redisScript, Collections.singletonList(keyPattern), String.valueOf(datacenterId), String.valueOf(maxWorkerId), worker.getPodUid(), String.valueOf(timeout));
        long workerId = (long) result.get(0);
        
        worker.setId(workerId);
        
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
