package com.swetroye.idgenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IdGeneratorApplicationTests {

	@Autowired
	private IdGenerator idGenerator;

	@Test
	public void testSerialGenerate() {
		// Generate UID
		long id;
		for (int idx = 0; idx < 1; idx++) {
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

	@Test
	public void testCheckDuplicate() {

		List<Long> idList = new ArrayList<>();

		for (int idx = 0; idx < 1000000; idx++) {
			idList.add(idGenerator.getId());

		}
		// idList.forEach(id -> System.out.print(idGenerator.parseId(id) + ", "));
		// System.out.println("");

		// System.out.println(idList);
		Set<Long> duplicates = findDuplicates(idList);
		System.out.println("Duplicate count -> " + duplicates.size());
		duplicates.forEach(x -> System.out.println(idGenerator.parseId(x)));

		assertEquals(0, duplicates.size());
	}

	private static <T> Set<T> findDuplicates(List<T> list) {
		Set<T> seen = new HashSet<>();
		return list.stream()
				.filter(e -> !seen.add(e))
				.collect(Collectors.toSet());
	}
}
