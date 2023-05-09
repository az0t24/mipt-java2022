package ru.khairullin_ar;

public final class Position {
    private boolean isEmpty;
    private final boolean isBlack;
    private Checker checker;

    public Position(boolean isBlack) {
        isEmpty = true;
        this.isBlack = isBlack;
    }

    /**
     * Mark position as empty
     */
    public void makeEmpty() {
        isEmpty = true;
    }

    /**
     * Mark position as not empty
     */
    public void makeNotEmpty(Checker newChecker) {
        checker = newChecker;
        isEmpty = false;
    }

    /**
     * Get color of position
     * @return true if position is white
     */
    public boolean isWhiteCell() {
        return !isBlack;
    }

    /**
     * Get info of empty of position
     * @return true if position is empty
     */
    public boolean isEmpty() {
        return isEmpty;
    }

    /**
     * Get checker of position
     * @return null if position is empty or checker on it else
     */
    public Checker getChecker() {
        if (isEmpty) {
            return null;
        }
        return checker;
    }
}
