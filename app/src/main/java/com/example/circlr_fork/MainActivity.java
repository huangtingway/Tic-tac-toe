package com.example.circlr_fork;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private play_view play_view;
    private TextView circle_win, x_win;
    private static MainActivity instance;
    private ImageView x_img, circle_img;
    private AlertDialog.Builder dialog;
    private AlertDialog alertDialog;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private static String checkbtn = "-1";
    private static int size = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        setinit();
        showReload();
    }

    private void showReload() {
        dialog.setTitle("reload");
        dialog.setMessage("Do yout want to reload last time game data?");
        dialog.setCancelable(false);
        dialog.setPositiveButton("NO", (dialog, which) -> {
            dialog.dismiss();
            player_choose();
            setrecord();
        });
        dialog.setNegativeButton("YES", (dialog, which) -> {
            dialog.dismiss();
            play_view.setPlayer(sp.getInt("nowplayer", -1));
            play_view.setCircle_win(sp.getInt("circle_win", -1));
            play_view.setx_win(sp.getInt("x_win", -1));
            size = sp.getInt("size", 3);
            play_view.setsize(size);
            setrecord();
            changeBackground(size);
            int beforepos[][] = new int[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    beforepos[i][j] = sp.getInt("pos " + i + j, -1);
                }
            }
            play_view.setpos(beforepos);
        });
        alertDialog = dialog.create();
        alertDialog.show();
    }

    public static MainActivity getInstance() {
        return instance;
    }

    private void setinit() {
        circle_win = findViewById(R.id.circle_win);
        x_win = findViewById(R.id.x_win);
        play_view = findViewById(R.id.play_view);
        circle_img = findViewById(R.id.circle_view);
        x_img = findViewById(R.id.x_view);
        dialog = new AlertDialog.Builder(MainActivity.this);
        sp = getSharedPreferences("test", MODE_PRIVATE);
        editor = sp.edit();
    }

    public void setimage(int i) {
        if(i == 1) {
            x_img.setImageResource(R.drawable.x_2);
            circle_img.setImageResource(R.drawable.circle_1);
        } else if(i == 0) {
            x_img.setImageResource(R.drawable.x_1);
            circle_img.setImageResource(R.drawable.circle_2);
        }

    }

    private void setrecord() {
        circle_win.setText("win:" + play_view.get_circle_win());
        x_win.setText("win:" + play_view.get_x_win());
    }

    public void showresult(String winer) {
        dialog.setTitle("result");
        if(winer.equals("O") == true) {
            dialog.setMessage("The winer is O!");
        } else if(winer.equals("X") == true) {
            dialog.setMessage("The winer is X!");
        } else if(winer.equals("peace") == true) {
            dialog.setMessage("peace!");
        }
        dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                restart();
            }
        });
        dialog.setNegativeButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog = dialog.create();
        alertDialog.show();

    }

    public void player_choose() {
        dialog.setTitle("player");
        dialog.setMessage("who start first?");
        dialog.setCancelable(false);

        dialog.setPositiveButton("X", (dialog, which) -> {
            play_view.setPlayer(1);
            dialog.dismiss();
        });
        dialog.setNegativeButton("O", (dialog, which) -> {
            play_view.setPlayer(0);
            dialog.dismiss();
        });
        alertDialog = dialog.create();
        alertDialog.show();

    }

    public void restart(View view) {
        play_view.restart();
        play_view.reset();
        player_choose();
        setrecord();

    }

    public void restart() {
        play_view.restart();
        player_choose();
        setrecord();
    }

    @Override
    protected void onStop() {
        super.onStop();
        editor.putInt("circle_win", play_view.get_circle_win());
        editor.putInt("x_win", play_view.get_x_win());
        editor.putInt("nowplayer", play_view.getNowplayer());
        editor.putInt("size", play_view.getsize());
        savepos(play_view.getpos());
        editor.apply();
    }

    private void savepos(int[][] getpos) {
        for (int i = 0; i < getpos.length; i++) {
            for (int j = 0; j < getpos[i].length; j++) {
                editor.putInt("pos " + i + j, getpos[i][j]);
            }
        }
    }

    public void option(View view) {
        String items[] = {"3x3", "4x4", "5x5"};
        dialog.setTitle("option");
        dialog.setMessage(null);
        dialog.setSingleChoiceItems(items, -1, (dialog, which) -> {
            checkbtn = items[which];
        });
        dialog.setPositiveButton("confirm", (dialog, which) -> {
            changeBackground(checkbtn);
            play_view.setsize(checkbtn);
            dialog.dismiss();
        });
        dialog.setNegativeButton("cancel", (dialog, which) -> {
            checkbtn = "-1";
            dialog.dismiss();
        });
        alertDialog = dialog.create();
        alertDialog.show();
    }

    private void changeBackground(String checkbtn) {
        if(checkbtn.equals("3x3")) {
            play_view.setBackgroundResource(R.drawable.background_1);
        } else if(checkbtn.equals("4x4")) {
            play_view.setBackgroundResource(R.drawable.background_2);
        } else if(checkbtn.equals("5x5")) {
            play_view.setBackgroundResource(R.drawable.background_3);
        }
    }

    private void changeBackground(int checkbtn) {
        if(checkbtn == 3) {
            play_view.setBackgroundResource(R.drawable.background_1);
        } else if(checkbtn == 4) {
            play_view.setBackgroundResource(R.drawable.background_2);
        } else if(checkbtn == 5) {
            play_view.setBackgroundResource(R.drawable.background_3);
        }
    }
}