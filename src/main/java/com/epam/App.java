package com.epam;

import com.epam.entity.Port;
import com.epam.entity.Ship;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {

		Port port = Port.createPort();

		Thread ship1 = new Thread(new Ship("Ship1", 5, port, 0));
		Thread ship2 = new Thread(new Ship("Ship2", 5, port, 5));
		Thread ship3 = new Thread(new Ship("Ship3", 5, port, 1));
		Thread ship4 = new Thread(new Ship("Ship4", 5, port, 3));

		ship1.start();
		ship2.start();
		ship3.start();
		ship4.start();

	}

}
