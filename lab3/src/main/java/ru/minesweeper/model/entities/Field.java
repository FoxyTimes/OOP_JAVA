package ru.minesweeper.model.entities;


import ru.minesweeper.observer.Notifications;
import ru.minesweeper.errors.OutOfSizeField;
import ru.minesweeper.model.entities.cell.Cell;
import ru.minesweeper.model.entities.cell.Conditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.*;

public class Field {
    List<List<Cell>> field;
    int height;
    int width;

    public Field(int rows, int cols) {
        this.height = rows;
        this.width = cols;

        field = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            field.add(new ArrayList<>());
            for (int j = 0; j < cols; j++) {
                field.get(i).add(new Cell(Conditions.undefined));
            }
        }
    }

    public void setCell(int row, int col, Cell cell) {
        field.get(row).set(col, cell);
    }

    public Cell getCell(int row, int col) {
        return field.get(row).get(col);
    }

    private boolean checkWin() {
        boolean win = true;
        for (List<Cell> row : field) {
            for (Cell cell : row) {
                if (cell.getCondition() != Conditions.opened && cell.getCondition()!=Conditions.closedWithBomb) {
                    win = false;
                }
            }
        }
        return win;
    }

    public void printField() {
        for (int i = 0; i < field.size(); i++) {
            for (int j = 0; j < field.get(i).size(); j++) {
                System.out.print(field.get(i).get(j).getCondition().ordinal() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void printBombs() {
        for (int i = 0; i < field.size(); i++) {
            for (int j = 0; j < field.get(i).size(); j++) {
                System.out.print(field.get(i).get(j).getCountBombs() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public Notifications openCell(int x, int y) {
        if (getCell(y, x).getCondition() == Conditions.closedWithBomb) {
            return Notifications.GAME_RUINED;
        }
        getCell(y,x).setCondition(Conditions.opened);
        clearFieldFromZeroCells();
        if (checkWin()) {
            return Notifications.GAME_WINED;
        }
        return Notifications.GAME_CHANGED;
    }

    public boolean isAroundOpenedCellsWithoutNumbers(int row, int col) {
        if (row>0) {
            if (getCell(row-1, col).getCondition()==Conditions.opened&&getCell(row-1, col).getCountBombs()==0) {
                return true;
            }
        }
        if (col>0) {
            if (getCell(row, col-1).getCondition()==Conditions.opened&&getCell(row, col-1).getCountBombs()==0) {
                return true;
            }
        }
        if (col<width-1) {
            if (getCell(row, col+1).getCondition()==Conditions.opened&&getCell(row, col+1).getCountBombs()==0) {
                return true;
            }
        }
        if (row<height-1) {
            if (getCell(row+1, col).getCondition()==Conditions.opened&&getCell(row+1, col).getCountBombs()==0) {
                return true;
            }
        }
        if (row>0&&col>0) {
            if (getCell(row-1, col-1).getCondition()==Conditions.opened&&getCell(row-1, col-1).getCountBombs()==0) {
                return true;
            }
        }
        if (row<height-1&&col<width-1) {
            if (getCell(row+1, col+1).getCondition()==Conditions.opened&&getCell(row+1, col+1).getCountBombs()==0) {
                return true;
            }
        }
        if (row>0&&col<width-1) {
            if (getCell(row-1, col+1).getCondition()==Conditions.opened&&getCell(row-1, col+1).getCountBombs()==0) {
                return true;
            }
        }
        if (row<height-1&&col>0) {
            if (getCell(row+1, col-1).getCondition()==Conditions.opened&&getCell(row+1, col-1).getCountBombs()==0) {
                return true;
            }
        }
        return false;
    }

    public int nextBomb(int leftCells) {
        Random rand = new Random();
        return rand.nextInt(leftCells);
    }

    public void countAroundBombs() {
        for (int row = 0; row != height; row++) {
            for (int col = 0; col != width; col++) {
                if (row > 0 && col > 0) {
                    if (getCell(row - 1, col - 1).getCondition() == Conditions.closedWithBomb) {
                        getCell(row, col).addBomb();
                    }
                }
                if (row > 0) {
                    if (getCell(row - 1, col).getCondition() == Conditions.closedWithBomb) {
                        getCell(row, col).addBomb();
                    }
                }
                if (col > 0) {
                    if (getCell(row, col - 1).getCondition() == Conditions.closedWithBomb) {
                        getCell(row, col).addBomb();
                    }
                }
                if (row > 0 && col < width - 1) {
                    if (getCell(row - 1, col + 1).getCondition() == Conditions.closedWithBomb) {
                        getCell(row, col).addBomb();
                    }
                }
                if (col < width - 1) {
                    if (getCell(row, col + 1).getCondition() == Conditions.closedWithBomb) {
                        getCell(row, col).addBomb();
                    }
                }
                if (row < height - 1) {
                    if (getCell(row + 1, col).getCondition() == Conditions.closedWithBomb) {
                        getCell(row, col).addBomb();
                    }
                }
                if (row < height - 1 && col > 0) {
                    if (getCell(row + 1, col - 1).getCondition() == Conditions.closedWithBomb) {
                        getCell(row, col).addBomb();
                    }
                }
                if (row < height - 1 && col < width - 1) {
                    if (getCell(row + 1, col + 1).getCondition() == Conditions.closedWithBomb) {
                        getCell(row, col).addBomb();
                    }
                }
            }
        }
    }

    public void putBombsInTheField(int leftBombs, int leftCells) {
        while (leftBombs != 0) {
            int numberBomb = nextBomb(leftCells);
            for (int row = 0; row != height; row++) {
                for (int col = 0; col != width; col++) {
                    if (getCell(row, col).getCondition() == Conditions.closedWithoutBomb) {
                        numberBomb--;
                        if (numberBomb == -1) {
                            setCell(row, col, new Cell(Conditions.closedWithBomb));
                            break;
                        }
                    }
                }
                if (numberBomb == -1) {
                    break;
                }
            }
            leftBombs--;
            leftCells--;
        }
    }

    public void clearFieldFromZeroCells() {
        boolean exit = false;
        while (!exit) {
            exit = true;
            for (int row = 0; row != height; row++) {
                for (int col = 0; col != width; col++) {
                    if (isAroundOpenedCellsWithoutNumbers(row, col) && getCell(row, col).getCondition() == Conditions.closedWithoutBomb) {
                        getCell(row, col).setCondition(Conditions.opened);
                        exit = false;
                    }
                }
            }
        }
    }

    public void openStartSquare(int startY, int startX, int clearRadius) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (abs(startY - row) < clearRadius && abs(startX - col) < clearRadius) {
                    setCell(row, col, new Cell(Conditions.opened));
                } else {
                    setCell(row, col, new Cell(Conditions.closedWithoutBomb));
                }
            }
        }
    }

    public void generateField(int startX, int startY, int minRadius, int maxRadius, int countBombs) {

        if (maxRadius > width || maxRadius > height) {
            throw new OutOfSizeField("Max Radius is " + maxRadius + " but width is " + width + " and height is " + height + " in:" + getClass().getSimpleName());
        }

        if (maxRadius < minRadius) {
            throw new OutOfSizeField("Max Radius is lower then Min Radius in:" + getClass().getName());
        }

        Random rand = new Random();

        //радиус от начальной клетки с открытыми клетками//погрешность в радиусе
        int clearRadius = minRadius + rand.nextInt(maxRadius);

        //осталось свободных клеток для размещения бомб
        int leftCells = height * width - ((2 * (clearRadius - 1) + 1) * (2 * (clearRadius - 1) + 1));


        //заполняем начальную площадь пустыми клетками
        openStartSquare(startY, startX, clearRadius);

        //ставим бомбы
        putBombsInTheField(countBombs, leftCells);

        //считаем бомбы вокруг клеток
        countAroundBombs();

        //чистим клетки без бомб рядом с центром
        clearFieldFromZeroCells();

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
