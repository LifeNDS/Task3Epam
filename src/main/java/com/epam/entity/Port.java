package com.epam.entity;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Port {

	private static final int BERTH_NUMBER = 3;
	private final Semaphore semaphore = new Semaphore(BERTH_NUMBER, true);
	private Queue<Berth> berthList = new LinkedList<Berth>();
	private Lock lock = new ReentrantLock();

	private Port() {

	}

	public static Port createPort() {
		Port port = new Port();
		port.berthList.addAll(generateBerth());
		return port;
	}

	public static Port createPort(int numberBerth) {
		Port port = new Port();
		for (int i = 0; i < numberBerth; i++) {
			port.berthList.add(new Berth(i));
		}
		return port;
	}

	public void returnResource(Berth berth) {
		berthList.add(berth);
		System.out.println("возврат причала #" + berth.getNumber());
		semaphore.release();
	}

	public Berth getBerth() throws InterruptedException {
		lock.lock();
		for (;;) {
			try {
				if (semaphore.tryAcquire(100, TimeUnit.MILLISECONDS)) {
					Berth berth = berthList.poll();
					if (berth != null) {
						System.out.println("Причал #" + berth.getNumber() + " зарезервирован.");

					}
					lock.unlock();
					return berth;
				}
			} catch (InterruptedException e) {

			}

		}

	}

	private static Collection<? extends Berth> generateBerth() {
		LinkedList<Berth> list = new LinkedList<Berth>();
		for (int i = 1; i <= BERTH_NUMBER; i++) {
			list.add(new Berth(i));
		}
		return list;
	}

}