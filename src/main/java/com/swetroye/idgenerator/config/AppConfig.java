package com.swetroye.idgenerator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.swetroye.idgenerator.impl.IdGeneratorImpl;
import com.swetroye.idgenerator.impl.WorkerManagerImpl;

@Configuration
public class AppConfig {

    @Value("${id-generator.datacenter-id}")
    private long datacenterId;

    @Value("${id-generator.time-bits}")
    private int timeBits;

    @Value("${id-generator.datacenter-bits}")
    private int datacenterBits;

    @Value("${id-generator.worker-bits}")
    private int workerBits;

    @Value("${id-generator.sequence-bits}")
    private int sequenceBits;

    @Value("${id-generator.start-timestamp-str}")
    private String startTimestampStr;

    @Bean
    public IdGeneratorImpl idGeneratorImpl() {
        // System.out.println("------AppConfig--Creating IdGeneratorImpl object------");
        // System.out.println("------Datacenter Id--" + datacenterId + "------");
        // System.out.println("------Time Bits--" + timeBits + "------");
        // System.out.println("------Datacenter Bits--" + datacenterBits + "------");
        // System.out.println("------Worker Bits--" + workerBits + "------");
        // System.out.println("------Sequence Bits--" + sequenceBits + "------");
        // System.out.println("------Start Timestamp String--" + startTimestampStr +
        // "------");
        IdGeneratorImpl idGeneratorImpl = new IdGeneratorImpl();
        idGeneratorImpl.setDatacenterId(datacenterId);
        idGeneratorImpl.setTimeBits(timeBits);
        idGeneratorImpl.setDatacenterBits(datacenterBits);
        idGeneratorImpl.setWorkerBits(workerBits);
        idGeneratorImpl.setSequenceBits(sequenceBits);
        idGeneratorImpl.setStartTimestampStr(startTimestampStr);

        return idGeneratorImpl;
    }

    @Value("${worker-manager.timeout}")
    private long timeout;

    @Value("${worker-manager.heartbeat-rate}")
    private long heartbeatRate;

    @Bean
    public WorkerManagerImpl workerManagerImpl() {
        WorkerManagerImpl workerManagerImpl = new WorkerManagerImpl();
        workerManagerImpl.setTimeout(timeout);
        workerManagerImpl.setHeartbeatRate(heartbeatRate);
        
        return workerManagerImpl;
    }
}
