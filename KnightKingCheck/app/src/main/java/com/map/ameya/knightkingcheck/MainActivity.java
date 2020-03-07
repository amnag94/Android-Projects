package com.map.ameya.knightkingcheck;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    GridLayout grid_board;
    double board_dimen,box_dimen;
    Button goHome;
    ImageButton single_box;
    int num_boxes,num_moves,least_num_moves;
    int knight_x,knight_y,start_knight_x,start_knight_y,start_king_x,start_king_y;
    String result = "";

    //Dialog variables
    MyDialog level_over;
    ImageButton one_star, two_star, three_star;
    Button lvlagain, lvlnext, game_play;
    TextView lvlOver, game_play_text;

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onClick(View view) {
        if(view == level_over.view.findViewById(R.id.one_star))
        {

        }
        else if(view == level_over.view.findViewById(R.id.two_star))
        {

        }
        else if(view == level_over.view.findViewById(R.id.three_star))
        {

        }
        else if(view == level_over.view.findViewById(R.id.btnlvlagain))
        {
            level_over.dismiss();
            num_moves = 0;
            //Remove knight from old position
            ImageButton old_knight = (ImageButton) findViewById(((knight_x - 1) * num_boxes) + knight_y);//id is (num_boxes*i) + j + 1 and knight_x and knight_y are i+1 and j+1
            //old_knight.setImageDrawable(null);
            old_knight.setImageResource(0);
            //Add knight to new position
            ImageButton new_knight = (ImageButton) findViewById(((start_knight_x) * num_boxes) + (start_knight_y + 1));
            //new_knight.setImageDrawable(getResources().getDrawable(R.drawable.knight));
            new_knight.setImageResource(R.drawable.knight);
            knight_x = start_knight_x + 1;
            knight_y = start_knight_y + 1;
        }
        else if(view == level_over.view.findViewById(R.id.btnlvlnext))
        {
            level_over.dismiss();
            Intent restart = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(restart);
        }
        else if(view == findViewById(R.id.game_play))
        {
            if (game_play_text.getVisibility() == View.INVISIBLE)
            {
                game_play_text.setVisibility(View.VISIBLE);
                game_play_text.invalidate();
                game_play_text.bringToFront();
            }
            else
            {
                game_play_text.setVisibility(View.INVISIBLE);
            }
        }
        else if(view == findViewById(R.id.goHome)) {
            Intent home = new Intent(getApplicationContext(), OpeningActivity.class);
            startActivity(home);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //For Dialog
        level_over = new MyDialog(this, R.style.MyDialog_choose2, R.layout.layout_dialog, (int) getApplicationContext().getResources().getDimension(R.dimen.dialog_width), (int) getApplicationContext().getResources().getDimension(R.dimen.dialog_height));
        one_star = (ImageButton) level_over.view.findViewById(R.id.one_star);
        two_star = (ImageButton) level_over.view.findViewById(R.id.two_star);
        three_star = (ImageButton) level_over.view.findViewById(R.id.three_star);
        lvlagain = (Button) level_over.view.findViewById(R.id.btnlvlagain);
        lvlnext = (Button) level_over.view.findViewById(R.id.btnlvlnext);
        game_play = (Button) findViewById(R.id.game_play);
        goHome = (Button) findViewById(R.id.goHome);
        lvlOver = (TextView) level_over.view.findViewById(R.id.txtLevelOver);
        game_play_text = (TextView) findViewById(R.id.game_play_text);

        one_star.setOnClickListener(this);
        two_star.setOnClickListener(this);
        three_star.setOnClickListener(this);
        lvlnext.setOnClickListener(this);
        lvlagain.setOnClickListener(this);
        game_play.setOnClickListener(this);
        goHome.setOnClickListener(this);

        level_over.setCanceledOnTouchOutside(false);
        level_over.setCancelable(false);

        grid_board = (GridLayout) findViewById(R.id.grid_board);
        num_boxes = ((int)(100*Math.random())%7) + 8;//Gives a random value from 8 to 15//Write random function
        num_moves = 0;
        /*ViewTreeObserver vto = grid_board.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    this.layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    this.layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }*//*
                board_dimen  = grid_board.getMeasuredWidth();
                //int height = grid_board.getMeasuredHeight();

            }
        });*/

        start_king_x = (int)(100*Math.random())%num_boxes;
        start_king_y = (int)(100*Math.random())%num_boxes;
        start_knight_x = (int)(100*Math.random())%num_boxes;
        start_knight_y = (int)(100*Math.random())%num_boxes;

        //To avoid same position.
        while(start_king_x == start_knight_x && start_king_y == start_knight_y)
        {
            start_knight_x = (int)(100*Math.random())%num_boxes;
            start_knight_y = (int)(100*Math.random())%num_boxes;
        }


        /*start_king_x = num_boxes - 1;
        start_king_y = num_boxes - 1;
        start_knight_x = num_boxes - 2;
        start_knight_y = num_boxes - 2;*/

        least_num_moves = BestMoves.getSmallestRoute(start_knight_x, start_knight_y, start_king_x, start_king_y);

        //Adjust for index mismatch for grid
        int temp = start_king_y;
        start_king_y = start_king_x;
        start_king_x = temp;

        temp = start_knight_y;
        start_knight_y = start_knight_x;
        start_knight_x = temp;

        start_king_x = num_boxes - 1 - start_king_x;
        start_knight_x = num_boxes - 1 - start_knight_x;

        board_dimen = getWindowManager().getDefaultDisplay().getWidth() - (int)(getResources().getDimension(R.dimen.layout_width_offset));
        grid_board.setRowCount(num_boxes);
        grid_board.setColumnCount(num_boxes);
        box_dimen = board_dimen/(double)(num_boxes);
        for(int i = 0;i < num_boxes;i++)
        {
            for(int j = 0;j < num_boxes;j++)
            {
                single_box = new ImageButton(this);
                //single_box.getLayoutParams().height = 40;//(int)(box_dimen);
                //single_box.getLayoutParams().width = 40;//(int)(box_dimen);
                if(Math.abs(i - j) % 2 == 0) {//If difference in column number and row number is even or 0
                    single_box.setBackground(getResources().getDrawable(R.drawable.black_box));
                }
                else
                {//If difference in column number and row number is odd
                    single_box.setBackground(getResources().getDrawable(R.drawable.white_box));
                }
                //if((i + 1) == num_boxes/2 && (j + 1) == num_boxes/2) {
                if(i == start_knight_x && j == start_knight_y)
                {
                    knight_x = i + 1;
                    knight_y = j + 1;
                    //single_box.setImageDrawable(getResources().getDrawable(R.drawable.knight));
                    single_box.setImageResource(R.drawable.knight);
                }
                if(i == start_king_x && j == start_king_y)
                {
                    //King does not move so no need to retain position
                    //single_box.setImageDrawable(getResources().getDrawable(R.drawable.knight));
                    single_box.setImageResource(R.drawable.king);
                }
                //single_box.setPadding(40,40,40,40);
                single_box.setId((num_boxes*i) + j + 1);
                //final int x = i;
                //final int y = j;
                single_box.setTag(j + 1);
                single_box.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int y = Integer.parseInt(view.getTag().toString());//key is j+1
                        int x = ((view.getId() - y) / num_boxes) + 1;//id is (num_boxes*i) + j + 1
                        if ((Math.abs(knight_x - x) == 2 && Math.abs(knight_y - y) == 1) || (Math.abs(knight_x - x) == 1 && Math.abs(knight_y - y) == 2)) {
                            num_moves++;
                            if(x == start_king_x + 1 && y == start_king_y + 1)//x and y are in i+1 j+1 format
                            {
                                if(num_moves == least_num_moves) {
                                    result = "Perfect!!";
                                    lvlOver.setText(result);
                                    one_star.setBackground(getResources().getDrawable(R.drawable.golden_star));
                                    two_star.setBackground(getResources().getDrawable(R.drawable.golden_star));
                                    three_star.setBackground(getResources().getDrawable(R.drawable.golden_star));
                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    lvlnext.setVisibility(View.VISIBLE);
                                    level_over.show();
                                    //Intent restart = new Intent(getApplicationContext(),MainActivity.class);
                                    //startActivity(restart);
                                }
                                else if(num_moves < least_num_moves)
                                {
                                    result = "Unbelievable!!";
                                    lvlOver.setText(result);
                                    one_star.setBackground(getResources().getDrawable(R.drawable.golden_star));
                                    two_star.setBackground(getResources().getDrawable(R.drawable.golden_star));
                                    three_star.setBackground(getResources().getDrawable(R.drawable.golden_star));
                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    lvlnext.setVisibility(View.VISIBLE);
                                    level_over.show();
                                    //Intent restart = new Intent(getApplicationContext(),MainActivity.class);
                                    //startActivity(restart);
                                }
                                else
                                {
                                    if(num_moves - least_num_moves == 1)
                                    {
                                        result = "Almost there!!";
                                        lvlOver.setText(result);
                                        one_star.setBackground(getResources().getDrawable(R.drawable.golden_star));
                                        two_star.setBackground(getResources().getDrawable(R.drawable.golden_star));
                                        three_star.setBackground(getResources().getDrawable(R.drawable.empty_star));
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        lvlnext.setVisibility(View.INVISIBLE);
                                        level_over.show();
                                    }
                                    else if(num_moves - least_num_moves == 2)
                                    {
                                        result = "Close enough!! Try again";
                                        lvlOver.setText(result);
                                        one_star.setBackground(getResources().getDrawable(R.drawable.golden_star));
                                        two_star.setBackground(getResources().getDrawable(R.drawable.empty_star));
                                        three_star.setBackground(getResources().getDrawable(R.drawable.empty_star));
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        lvlnext.setVisibility(View.INVISIBLE);
                                        level_over.show();
                                    }
                                    else
                                    {
                                        result = "Failed";
                                        lvlOver.setText(result);
                                        one_star.setBackground(getResources().getDrawable(R.drawable.empty_star));
                                        two_star.setBackground(getResources().getDrawable(R.drawable.empty_star));
                                        three_star.setBackground(getResources().getDrawable(R.drawable.empty_star));
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        lvlnext.setVisibility(View.INVISIBLE);
                                        level_over.show();
                                    }
                                    /*num_moves = 0;
                                    //Remove knight from old position
                                    ImageButton old_knight = (ImageButton) findViewById(((knight_x - 1) * num_boxes) + knight_y);//id is (num_boxes*i) + j + 1 and knight_x and knight_y are i+1 and j+1
                                    //old_knight.setImageDrawable(null);
                                    old_knight.setImageResource(0);
                                    //Add knight to new position
                                    ImageButton new_knight = (ImageButton) findViewById(((start_knight_x) * num_boxes) + (start_knight_y + 1));
                                    //new_knight.setImageDrawable(getResources().getDrawable(R.drawable.knight));
                                    new_knight.setImageResource(R.drawable.knight);
                                    knight_x = start_knight_x + 1;
                                    knight_y = start_knight_y + 1;
                                    */
                                }
                            }
                            else {
                                /*//Animate knight
                                Animation translate1, translate2, translate3;
                                if((Math.abs(knight_x - x) == 2 && Math.abs(knight_y - y) == 1))
                                {
                                    translate1 = new TranslateAnimation(0,(int)box_dimen,0,(int)box_dimen);
                                }
                                else
                                {
                                    translate1 = new TranslateAnimation(0,(int)box_dimen,0,(int)box_dimen);
                                }*/
                                //Remove knight from old position
                                ImageButton old_knight = (ImageButton) findViewById(((knight_x - 1) * num_boxes) + knight_y);//id is (num_boxes*i) + j + 1 and knight_x and knight_y are i+1 and j+1
                                //old_knight.setImageDrawable(null);
                                old_knight.setImageResource(0);
                                //translate1.setDuration(3000);
                                //translate1.setInterpolator(new LinearInterpolator());
                                //old_knight.setAnimation(translate1);
                                //Add knight to new position
                                ImageButton new_knight = (ImageButton) view;
                                //new_knight.setImageDrawable(getResources().getDrawable(R.drawable.knight));
                                new_knight.setImageResource(R.drawable.knight);
                                knight_x = x;
                                knight_y = y;
                            }
                        }
                    }
                });
                //Add image button to the grid
                grid_board.addView(single_box);
                //single_box.setLayoutParams(new ViewGroup.LayoutParams((int) (box_dimen), (int) (box_dimen)));
                //Set size of each button.
                ViewGroup.LayoutParams params =  single_box.getLayoutParams();
                params.height = (int) (box_dimen);
                params.width = (int) (box_dimen);
                //Set image drawable size i.e. make knight or king image fit inside the button
                single_box.setLayoutParams(params);
                single_box.setAdjustViewBounds(true);
                single_box.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        }
    }
}
//6029104540