package com.samsung.wexposed;

import java.util.LinkedList;
import java.util.Queue;

public class MovingAverage {
	private int window_size;
	private double cur_avg;
	private Queue<Double> win;

	public MovingAverage(int ws) {
		if (ws <= 0)
			return;
		window_size = ws;
		cur_avg = 0;
		win = new LinkedList<Double>();
	}

	public double next(double num) {
		double tmp = 0;
		int w1 = win.size();
		boolean full = false;

		while (win.size() >= window_size) {
			tmp = win.poll();
			full = true;
		}

		win.add(num);
		// int w2 = win.size();
		// System.out.print("tmp = " + tmp + ", num = " + num + ", w1 = " + w1 +
		// ", w2 = " + w2 + "\t");

		if (!full)
			cur_avg = cur_avg * w1 / win.size();
		cur_avg += (num - tmp) / win.size();

		// System.out.println("post_cur_avg = " + cur_avg);
		return cur_avg;
	}
}
