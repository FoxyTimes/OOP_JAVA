package ru.minesweeper.model.entities.cell;


public class Cell {
    private Conditions condition;
    private int countBombs; //если количество бомб 9 - то данная клетка сама бомба

    public Cell(Conditions condition) {
        this.condition = condition;
        if (condition == Conditions.closedWithBomb) {
            countBombs=9;
        }
    }

    public Conditions getCondition() {
        return condition;
    }

    public void setCondition(Conditions condition) {
        this.condition = condition;
    }

    public int getCountBombs() {
        return countBombs;
    }

    public void setCountBombs(int countBombs) {
        this.countBombs = countBombs;
    }

    public void addBomb() {
        if (condition==Conditions.closedWithBomb) {
            return;
        }
        this.countBombs++;
    }
}
