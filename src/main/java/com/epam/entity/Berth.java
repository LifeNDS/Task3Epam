package com.epam.entity;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

public class Berth implements Iterable<Container> {

	private static final int DEFAULT_BERTH_CAPACITY = 200;
	private Queue<Container> containers = null;
	private int berthNumber;

	public Berth(int number) {

		berthNumber = number;
		containers = new LinkedBlockingQueue<Container>(DEFAULT_BERTH_CAPACITY);
		List<Container> itemList = generateContainers();
		containers.addAll(itemList);
	}

	public Container getContainer() {
		if (!containers.isEmpty()) {
			return containers.poll();
		} else
			return null;
	}

	public boolean setContainer(Container container) {
		return containers.add(container);
	}

	public int getNumber() {
		return berthNumber;
	}

	@Override
	public Iterator<Container> iterator() {
		return containers.iterator();
	}

	private static List<Container> generateContainers() {

		Random generator = new Random();
		int length = generator.nextInt(20);
		Container[] containers = new Container[length];
		for (int i = 0; i < length; i++) {
			containers[i] = new Container(generator.nextInt(500));
		}

		return Arrays.asList(containers);
	}

}