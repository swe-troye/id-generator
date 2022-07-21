package com.swetroye.idgenerator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.swetroye.idgenerator.impl.IdGeneratorImpl;

@Configuration
public class AppConfig {

    @Value("${id-generator.datacenter-id}")
    private long datacenterId;

    @Value("${id-generator.time-bits}")
    private int timeBits;

    @Value("${id-generator.datacenter-bits}")
    private int datacenterBits;

    @Value("${id-generator.machine-bits}")
    private int machineBits;

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
        // System.out.println("------Machine Bits--" + machineBits + "------");
        // System.out.println("------Sequence Bits--" + sequenceBits + "------");
        System.out.println("------Start Timestamp String--" + startTimestampStr + "------");
        IdGeneratorImpl idGeneratorImpl = new IdGeneratorImpl();
        idGeneratorImpl.setDatacenterId(datacenterId);
        idGeneratorImpl.setTimeBits(timeBits);
        idGeneratorImpl.setDatacenterBits(datacenterBits);
        idGeneratorImpl.setMachineBits(machineBits);
        idGeneratorImpl.setSequenceBits(sequenceBits);
        idGeneratorImpl.setStartTimestampStr(startTimestampStr);
        return idGeneratorImpl;
    }
}
