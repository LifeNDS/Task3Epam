package com.epam.entity;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

public class Ship implements Runnable {
	public static final Logger LOG = Logger.getLogger(Ship.class);
	private String name;
	private int amountContainer;
	private int capacity;
	private Queue<Container> shipStorage;
	private Port port;
	private Berth berth;
	private Lock lock = new ReentrantLock();

	public Ship(String name, int capacity, Port port, int amountContainer) {
		LOG.info("Create Ship");
		this.name = name;
		if (capacity > 0) {
			this.capacity = capacity;
		}
		if (amountContainer >= 0) {
			this.amountContainer = amountContainer;
		}
		this.port = port;
		this.shipStorage = new ArrayDeque<Container>(capacity);
		generateСargo();

	}

	private void generateСargo() {
		if (amountContainer > 0) {
			Container[] containers = new Container[capacity];
			for (int i = 0; i < amountContainer; i++) {
				containers[i] = new Container(i + 100);
				shipStorage.add(containers[i]);
			}
		}
	}

	public void run() {
		goToPort();

		if (amountContainer == 0) {
			loadShip();
		} else {
			if (amountContainer < capacity && amountContainer != 0) {
				unloadShipe();
				loadShip();
			}
		}
		if (amountContainer == capacity) {
			unloadShipe();
		}
		leaveThePort();

	}

	private void loadShip() {

		for (int i = 0; i < (capacity - amountContainer); i++) {
			Container item = berth.getContainer();
			if (item == null) {
				System.out.println("порт пустой!");
				LOG.info(" порт пустой");
				lock.unlock();
				return;
			}
			if (shipStorage.add(item)) {
				shipStorage.add(item);
				System.out.println(name + " загрузил контейнер №" + item.getRegistrationNumber());
				LOG.info(name + " загрузил контейнер №" + item.getRegistrationNumber());
			} else {
				System.out.println("Не хватает места на складе корабля!");
				LOG.info("Не хватает места на складе корабля!");
				lock.unlock();
				return;
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				LOG.error(e);
			}
		}

	}

	private void unloadShipe() {
		if (amountContainer == 0) {
			System.out.println("Корабль пустой!");
			LOG.info("Корабль пустой!");
			return;
		}
		for (int i = 0; i < amountContainer; i++) {
			Container item = shipStorage.poll();
			if (berth.setContainer(item)) {

				System.out.println(name + " разгрузил контейнер №" + item.getRegistrationNumber());
				LOG.info(name + " разгрузил контейнер #" + item.getRegistrationNumber());

			} else {
				System.out.println("Нету места на складе в порту!");
				LOG.error("Нет места на складе в порту!");
				return;
			}
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				LOG.error(e);
			}
		}
		amountContainer = 0;
	}

	private void goToPort() {
		try {
			getBerth();
			Thread.sleep(new Random(100).nextInt(1000));
			System.out.println(name + " причалил на " + berth.getNumber() + " причал.");
		} catch (InterruptedException e) {
			LOG.error(e);
		}
	}

	private void leaveThePort() {
		try {
			Thread.sleep(new Random(100).nextInt(500));
			System.out.println(name + " отчалил с " + berth.getNumber() + " причала.");
			LOG.info(name + " отчалил с " + berth.getNumber() + " причала.");
		} catch (InterruptedException e) {
			LOG.error(e);
		} finally {
			port.returnResource(berth);
			System.out.println("Причал #" + berth.getNumber() + " свободен.");
			LOG.info("Причал #" + berth.getNumber() + " свободен.");
		}
	}

	private void getBerth() throws InterruptedException {
		do {
			try {
				berth = port.getBerth();
			} catch (InterruptedException e) {

			}
			if (berth == null) {
				System.out.println(name + " ждёт" + berth.getNumber());
				LOG.info(name + " ждет");
				Thread.sleep(500);
			}
		} while (berth == null);
	}
}