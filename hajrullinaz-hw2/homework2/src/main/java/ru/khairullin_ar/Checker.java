package ru.khairullin_ar;

public final class Checker implements Comparable<Checker> {
    private int x;
    private int y;
    private final boolean isBlackChecker;
    private boolean isCanBeat;
    private boolean isKing;

    public Checker(int x, int y, boolean isBlack, boolean isKing) {
        this.x = x;
        this.y = y;
        isCanBeat = false;
        isBlackChecker = isBlack;
        this.isKing = isKing;
    }

    /**
     * Get info about x coordinate
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Get info about y coordinate
     * @return y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Get info about color
     * @return true if checker is black
     */
    public boolean isBlackChecker() {
        return isBlackChecker;
    }

    /**
     * Get info about opportunity to beat
     * @return true if checker can beat
     */
    public boolean isCanBeat() {
        return isCanBeat;
    }

    /**
     * Get info about king
     * @return true if checker is king
     */
    public boolean isKing() {
        return isKing;
    }

    /**
     * Mark checker that it can beat
     */
    public void makeBeating() {
        isCanBeat = true;
    }

    /**
     * Mark checker that it cannot beat
     */
    public void makeNotBeating() {
        isCanBeat = false;
    }

    /**
     * Mark checker that it king
     */
    public void makeKing() {
        isKing = true;
    }

    /**
     * Move checker to another coordinates
     * @param xToCoord new x coordinate
     * @param yToCoord new y coordinate
     */
    public void move(int xToCoord, int yToCoord) {
        x = xToCoord;
        y = yToCoord;

        if (y == 0 && isBlackChecker) {
            makeKing();
        } else if (y == 7 && !isBlackChecker) {
            makeKing();
        }
    }

    @Override
    public int compareTo(Checker other) {
        if (isKing && !other.isKing()) {
            return -1;
        } else if (!isKing && other.isKing()) {
            return 1;
        }

        if (x > other.getX()) {
            return 1;
        } else if (x < other.getX()) {
            return -1;
        } else {
            return Integer.compare(y, other.getY());
        }
    }
}
