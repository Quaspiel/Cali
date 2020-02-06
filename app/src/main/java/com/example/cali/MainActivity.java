package com.example.cali;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private int screenWidth;
    private int screenHeight;
    private IoSender io = new IoSender();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Get Screenheight and Screenwidth of the Display
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        // set the Layout to fit the Screen
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
        LayoutParams layoutParams = new LayoutParams(screenWidth,screenHeight);
        layout.setLayoutParams(layoutParams);

        if(getSupportActionBar() != null){
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
        }

        io.connect();

    }

    @Override
    public boolean onTouchEvent(MotionEvent e){

        RelativeLayout layout = findViewById(R.id.layout);
        //MessageSender messageSender= new MessageSender();
        //JSSender jsSender = new JSSender();


        //Points where the Display is touched
        int points = e.getPointerCount();

        //useless?
        layout.removeAllViews();

        //clean the Layout if the last Finger is up
        if(e.getAction() == MotionEvent.ACTION_UP){
            layout.removeAllViews();


        }else{
            //Show Coordinates of the Touch
            for(int i = 0; i < points;i++){

                TextView textView = new TextView(this);
                int x = (int)e.getX(i);
                int y = (int)e.getY(i);
                textView.setText("("+x+", "+y+")");
                textView.setTextSize(30);


                //to get the dynamic Height and Width of the TextView
                textView.measure(0,0);

                TextView tmp = new TextView(this);
                tmp.setText(textView.getMeasuredHeight()+", "+ textView.getMeasuredWidth()+", "+ screenHeight +", "+screenWidth);
                tmp.setTextSize(20);


                //Too close in right corner
                if ( x >= screenWidth - textView.getMeasuredWidth()&& y >= screenHeight - 2*textView.getMeasuredHeight() ) {
                    textView.setX(screenWidth - textView.getMeasuredWidth());
                    textView.setY(screenHeight - 2 * textView.getMeasuredHeight());
                    // Too close to right
                }else if ( x >= screenWidth - textView.getMeasuredWidth()){
                    textView.setX(screenWidth - textView.getMeasuredWidth());
                    textView.setY(y);
                    // Too close to Bottom
                } else if ( y >= screenHeight - 2*textView.getMeasuredHeight()){
                    textView.setX(x);
                    textView.setY(screenHeight - 2*textView.getMeasuredHeight());
                } else {
                    textView.setX(x);
                    textView.setY(y);
                }


                layout.addView(tmp);
                layout.addView(textView);
                if(i==0) {

                    //messageSender.execute(textView.getText().toString());
                    //jsSender.execute(textView.getText().toString());

                    if(io.socket.connected()){
                        Toast.makeText(MainActivity.this, "Socket Connected!!",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, "No connection found",Toast.LENGTH_SHORT).show();
                    }
                    io.send(textView.getText().toString());
                }
            }
        }
        return  true;
    }
}
