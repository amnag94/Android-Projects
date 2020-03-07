package com.example.ameyadeepaknagnur.sudoku;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;

public class PuzzleActivity extends AppCompatActivity {

    GridLayout grid_sudoku;
    int size_puzzle;
    double MEDIUM = 0.55;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        // Initializations
        grid_sudoku = findViewById(R.id.sudoku_layout);

        size_puzzle = 9;

        SudokuModel model = setUpPuzzle(MEDIUM);
        setGridSudoku(grid_sudoku, model, size_puzzle);

    }

    private void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(new View(activity).getWindowToken(), 0);
    }

    private void setGridSudoku(GridLayout grid_sudoku, SudokuModel model, int size_puzzle) {

        grid_sudoku.setRowCount(size_puzzle);
        grid_sudoku.setColumnCount(size_puzzle);

        for(int row = 0; row < size_puzzle; row++) {
            for (int column = 0; column < size_puzzle; column++) {
                int current_value = model.puzzle[row][column];

                EditText textBox = new EditText(this);
                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                //layoutParams.height = GridLayout.LayoutParams.MATCH_PARENT;
                //layoutParams.width = GridLayout.LayoutParams.MATCH_PARENT;
                //textBox.setLayoutParams(layoutParams);
                textBox.setTextSize(getResources().getDimension(R.dimen.text_XS));
                textBox.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textBox.setInputType(InputType.TYPE_CLASS_NUMBER);
                textBox.setBackgroundColor(getResources().getColor(R.color.transparent));
                textBox.setText(current_value == 0 ? " " : current_value + "");
                //textBox.setMax

                if(current_value != 0)
                {
                    textBox.setKeyListener(null);
                    textBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            v.clearFocus();
                            //hideKeyboard(getApplicationContext().);
                        }
                    });
                }

                CardView cardView = new CardView(this);

                layoutParams.height = (int)(getResources().getDimension(R.dimen.grid_item_size));
                layoutParams.width = (int)(getResources().getDimension(R.dimen.grid_item_size));
                cardView.setLayoutParams(layoutParams);

                if((row + column) % 2 == 0) {
                    cardView.setBackgroundResource(R.drawable.white_box);
                }

                cardView.addView(textBox);
                grid_sudoku.addView(cardView);

            }
        }
    }

    private int[] getAllValues()
    {
        int[] available_values = new int[this.size_puzzle];

        for (int number = 1; number <= size_puzzle; number++)
        {
            available_values[number - 1] = number;
        }

        return available_values;
    }

    private SudokuModel setUpPuzzle(double difficulty)
    {
        SudokuModel model = SudokuModel.getInstance(size_puzzle, difficulty);

        int[] available_values = getAllValues();
        model.formSolution(0, 0, available_values);
        model.calculatePuzzleDisplay();

        return model;
    }
}