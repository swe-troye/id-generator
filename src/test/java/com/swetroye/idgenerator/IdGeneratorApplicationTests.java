package com.swetroye.idgenerator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IdGeneratorApplicationTests {

	@Autowired
	private IdGenerator idGenerator;

	@Test
	public void testSerialGenerate() {
		// IdGenerator idGenerator = new IdGeneratorImpl();
		// Generate UID

		long id;
		for (int idx = 0; idx < 90; idx++) {
			id = idGenerator.getId();

			// Parse Id into [Timestamp, DatacenterId, WorkerId, Sequence]
			// {"Id":"2784076689838080","timestamp":"2022/08/22
			// 09:54:38.040","datacenterId":"1","workerId":"1","sequence":"0"}
			System.out.println(idGenerator.parseId(id));

			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
