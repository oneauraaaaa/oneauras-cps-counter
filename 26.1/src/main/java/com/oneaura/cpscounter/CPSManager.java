package com.oneaura.cpscounter;

import java.util.ArrayDeque;
import java.util.Deque;

public final class CPSManager {
	private static final long WINDOW_MS = 1_000L;
	private static final Deque<Long> LEFT_CLICKS = new ArrayDeque<>();
	private static final Deque<Long> RIGHT_CLICKS = new ArrayDeque<>();

	private CPSManager() {
	}

	public static void recordLeftClick() {
		LEFT_CLICKS.addLast(System.currentTimeMillis());
	}

	public static void recordRightClick() {
		RIGHT_CLICKS.addLast(System.currentTimeMillis());
	}

	public static void tick() {
		long cutoff = System.currentTimeMillis() - WINDOW_MS;
		while (!LEFT_CLICKS.isEmpty() && LEFT_CLICKS.peekFirst() < cutoff) {
			LEFT_CLICKS.removeFirst();
		}
		while (!RIGHT_CLICKS.isEmpty() && RIGHT_CLICKS.peekFirst() < cutoff) {
			RIGHT_CLICKS.removeFirst();
		}
	}

	public static int getLeftCps() {
		return LEFT_CLICKS.size();
	}

	public static int getRightCps() {
		return RIGHT_CLICKS.size();
	}
}
