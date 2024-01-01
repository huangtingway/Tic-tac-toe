package com.example.circlr_fork;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;

public class play_view extends View {

    private static int nowplayer = -1;
    private static int circle_win = 0;
    private static int x_win = 0;
    private int viewH, viewW;
    private static int pos[][];
    private static float parts_line, parts_row;
    private Bitmap circle_icon, x_icon;
    private Resources resources;
    private static boolean isinit = false;
    private boolean ispeace = true;
    private static int size = 3;

    private boolean isfinish = false;

    public play_view(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        resources = context.getResources();
        circle_icon = BitmapFactory.decodeResource(resources, R.drawable.circle_1);
        x_icon = BitmapFactory.decodeResource(resources, R.drawable.x_1);
    }

    private void init() {

        viewW = getWidth();
        viewH = getHeight();
        parts_line = (float) ((viewW) / size);
        parts_row = (float) ((viewH) / size);
        pos = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                pos[i][j] = -1;
            }
        }

        Matrix matrix = new Matrix();
        matrix.postScale((parts_line * 0.7f / circle_icon.getWidth()), (parts_row * 0.7f / circle_icon.getHeight()));
        circle_icon = Bitmap.createBitmap(circle_icon, 0, 0, circle_icon.getWidth(), circle_icon.getHeight(), matrix, false);
        x_icon = Bitmap.createBitmap(x_icon, 0, 0, x_icon.getWidth(), x_icon.getHeight(), matrix, false);
        isinit = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!isinit) init();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                float x = ((parts_line * j) + (parts_line * (j + 1))) / 2.0f - (circle_icon.getWidth() / 2.0f) + 10;
                float y = ((parts_row * i) + (parts_row * (i + 1))) / 2.0f - (circle_icon.getHeight() / 2.0f) + 10;
                if(pos[i][j] == 0) {
                    canvas.drawBitmap(circle_icon, x, y, null);
                } else if(pos[i][j] == 1) {
                    canvas.drawBitmap(x_icon, x, y, null);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            showmark(event.getX(), event.getY());
        }
        return true;
    }

    private void showmark(float x, float y) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                float top = parts_row * i;
                float bottom = parts_row * (i + 1);
                float right = parts_line * (j + 1);
                float left = parts_line * j;
                if(top == 0) top += 20;
                if(bottom == viewH) bottom -= 20;
                if(right == viewW) right -= 20;
                if(left == 0) left += 20;

                if(y >= top && y <= bottom && x <= right && x >= left) {
                    addpos(i, j);
                }
            }
        }
        postInvalidate();
        decide_win();
    }

    private void addpos(int i, int j) {
        if(pos[i][j] == -1) {
            pos[i][j] = nowplayer;
            changenowPlayer();
        }
    }

    private void decide_win() {
        ispeace = true;
        for (int i = 0; i < size; i++) {
            isfinish = true;
            for (int j = 0; j < size; j++) {
                if(pos[i][j] != pos[i][0] || pos[i][j] == -1) {
                    isfinish = false;
                }
            }
            if(isfinish == true) {
                decideWinner();
                return;
            }
        }

        for (int i = 0; i < size; i++) {
            isfinish = true;
            for (int j = 0; j < size; j++) {
                if(pos[j][i] != pos[0][i] || pos[j][i] == -1) {
                    isfinish = false;
                }
            }
            if(isfinish == true) {
                decideWinner();
                return;
            }
        }

        isfinish = true;
        for (int i = 0; i < size; i++) {
            if(pos[i][i] != pos[0][0] || pos[i][i] == -1) {
                isfinish = false;
            }
        }
        if(isfinish == true) {
            decideWinner();
            return;
        }

        isfinish = true;
        for (int i = 0, j = size - 1; i < size; i++, j--) {
            if(pos[i][j] != pos[0][size - 1] || pos[i][j] == -1) {
                isfinish = false;
            }
        }
        if(isfinish == true) {
            decideWinner();
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(pos[i][j] == -1) {
                    ispeace = false;
                }
            }
        }

        if(ispeace) {
            MainActivity.getInstance().showresult("peace");
        }

    }

    private void decideWinner() {
        if(nowplayer == 1) {
            circle_win++;
            MainActivity.getInstance().showresult("O");
        } else {
            x_win++;
            MainActivity.getInstance().showresult("X");
        }
    }

    public void setPlayer(int i) {
        if(i == 0 || i == 1) {
            nowplayer = i;
        } else {
            nowplayer = -1;
        }
        MainActivity.getInstance().setimage(nowplayer);
    }

    private void changenowPlayer() {

        if(nowplayer == 0) {
            nowplayer = 1;
        } else if(nowplayer == 1) {
            nowplayer = 0;
        }
        MainActivity.getInstance().setimage(nowplayer);

    }

    public int get_circle_win() {
        return circle_win;
    }

    public int get_x_win() {
        return x_win;
    }

    public void setCircle_win(int circle_win) {
        this.circle_win = circle_win;
    }

    public void setx_win(int x_win) {
        this.x_win = x_win;
    }

    public int[][] getpos() {
        return pos;
    }

    public void setpos(int pos[][]) {
        this.pos = pos;
        postInvalidate();
    }

    public int getNowplayer() {
        return nowplayer;
    }

    public void restart() {
        init();
        postInvalidate();
    }

    public void reset() {
        x_win = 0;
        circle_win = 0;
    }

    public void setsize(String checkbtn) {
        if(checkbtn.equals("3x3")) {
            size = 3;
        } else if(checkbtn.equals("4x4")) {
            size = 4;
        } else if(checkbtn.equals("5x5")) {
            size = 5;
        }
        restart();
    }

    public void setsize(int checkbtn) {
        if(checkbtn == 3) {
            size = 3;
        } else if(checkbtn == 4) {
            size = 4;
        } else if(checkbtn == 5) {
            size = 5;
        }
        restart();
    }

    public int getsize() {
        return size;
    }
}
