package com.example.ameyadeepaknagnur.sudoku;

import android.content.Context;

public class SudokuModel {

    static SudokuModel model_instance;

    int size_puzzle;
    double difficulty;
    public int[][] puzzle;
    public boolean[][] positions;

    private SudokuModel(int size_puzzle, double difficulty) {

        this.size_puzzle = size_puzzle;
        this.difficulty = difficulty;

        puzzle = new int[size_puzzle][];
        positions = new boolean[size_puzzle][];

        // To avoid NullPointerException
        for (int puzzle_row = 0; puzzle_row < size_puzzle; puzzle_row++)
        {
            puzzle[puzzle_row] = new int[size_puzzle];
            positions[puzzle_row] = new boolean[size_puzzle];
        }

    }

    public static SudokuModel getInstance(int size_puzzle, double difficulty)
    {
        if (model_instance == null)
        {
            model_instance = new SudokuModel(size_puzzle, difficulty);
        }

        return model_instance;
    }

    private boolean validatePuzzle(int row, int column, int value)
    {

        // Check row
        for(int digit : puzzle[row])
        {
            if (digit == value)
            return false;
        }

        // Check column
        for (int index = 0; index < size_puzzle; index++)
        {
            if (puzzle[index][column] == value)
            {
                return false;
            }
        }

        // Check box
        int box_size = (int)Math.sqrt(size_puzzle);

        int box_row_start = row - (row % box_size);
        int box_col_start = column - (column % box_size);

        for (int row_index = 0; row_index < box_size; row_index++)
        {
            for (int col_index = 0; col_index < box_size; col_index++)
            {
                if (puzzle[box_row_start + row_index][box_col_start + col_index] == value)
                {
                    return false;
                }
            }
        }

        return true;

    }

    private void setRandomStart(int[] available_values)
    {

        int start = (int)(Math.random() * 10) % size_puzzle;

        int temp = available_values[start];
        available_values[start] = available_values[0];
        available_values[0] = temp;
    }

    public boolean formSolution(int row, int column, int[] available_values)
    {
        if (row >= size_puzzle)
        {
            return true;
        }

        // Pick random start to adding a value
        setRandomStart(available_values);

        for (int value_index = 0; value_index < size_puzzle; value_index++)
        {

            int current_value = available_values[value_index];

            if (validatePuzzle(row, column, current_value))
            {
                puzzle[row][column] = current_value;

                int next_row = (column + 1) % size_puzzle == 0 ? row + 1 : row;
                int next_column = (column + 1) % size_puzzle;

                int[] available_array = available_values;
                boolean success = formSolution(next_row, next_column, available_values);

                if (success)
                {
                    return success;
                }

                available_values = available_array;
                puzzle[row][column] = 0;

            }
        }

        return false;
    }

    private void formPuzzle()
    {
        for(int row = 0; row < size_puzzle; row++)
        {
            for(int column = 0; column < size_puzzle; column++)
            {
                if (!positions[row][column])
                {
                    puzzle[row][column] = 0;
                }
            }
        }
    }

    public void calculatePuzzleDisplay()
    {
        int num_to_display = (int)(Math.round(size_puzzle * size_puzzle * difficulty));

        int update = (int)(Math.round(size_puzzle * difficulty));

        // To avoid displaying only in one row
        if (update < Math.sqrt(size_puzzle))
        {
            update *= 2;
        }

        int row = 0, column = 0;
        while (num_to_display > 0)
        {
            positions[row][column] = true;

            row = (column + update) >= size_puzzle ? row + 1 : row;
            column = (column + update) % size_puzzle;

            // Wrap around in case num_to_display left
            row = row >= size_puzzle ? 0 : row;

            num_to_display--;
        }

        formPuzzle();
    }

}
