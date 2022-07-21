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
		for (int idx = 0; idx < 5; idx++) {
			id = idGenerator.getId();
			System.out.println(String.valueOf(id));
		}

		// Parse UID into [Timestamp, WorkerId, Sequence]
		// {"UID":"180363646902239241","parsed":{ "timestamp":"2017-01-19 12:15:46",
		// "workerId":"4", "sequence":"9" }}
		// System.out.println(idGenerator.parseID(uid));

	}

}
