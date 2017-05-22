package com.example.apoorva.rockpaperscissors;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.provider.DocumentsContract;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button rock, paper, scissors, restart;
    private TextView score, result;
    int mWins, mLosses, mTies, mSelection, oSelection;
    boolean selectionMade, gamePlaying;
    private int ROCK = 0;
    private int PAPER = 1;
    private int SCISSORS = 2;
    private int RESTART = 3;
    private int NONE_SELECTED = 4;

    private final int WIN = 0;
    private final int LOSS = 1;
    private final int TIE = 2;

    CommHandler commHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rock = (Button) findViewById(R.id.rock);
        paper = (Button) findViewById(R.id.paper);
        scissors = (Button) findViewById(R.id.scissors);
        restart = (Button) findViewById(R.id.restart);
        rock.setOnClickListener(this);
        paper.setOnClickListener(this);
        scissors.setOnClickListener(this);
        restart.setOnClickListener(this);
        score = (TextView) findViewById(R.id.score);
        result = (TextView) findViewById(R.id.result);

        mWins = 0;
        mLosses = 0;
        mTies = 0;
        mSelection = NONE_SELECTED;
        oSelection = NONE_SELECTED;
        selectionMade = false;
        gamePlaying = true;

        commHandler = new CommHandler(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.restart){
            if(!gamePlaying){
                setSelection(v, RESTART);
            }
            else{
                debugToast("Can't restart in the middle of the game");
            }
        }

        if(!selectionMade){

            sendNotification();

            if(v.getId() == R.id.rock) {
                setSelection(v, ROCK);
                selectionMade = true;
            }

            else if (v.getId() == R.id.paper){
                setSelection(v, PAPER);
                selectionMade = true;
            }

            else if (v.getId() == R.id.scissors){
                setSelection(v, SCISSORS);
                selectionMade = true;
            }
        }

    }

    public void setSelection(View v, int selection){
        if(selection == ROCK){
            v.setBackgroundColor(Color.parseColor("#c60962"));
            mSelection = ROCK;
            commHandler.sendMove(ROCK);
        }
        else if(selection == PAPER){
            v.setBackgroundColor(Color.parseColor("#c60962"));
            mSelection = PAPER;
            commHandler.sendMove(PAPER);
        }
        else if(selection == SCISSORS){
            v.setBackgroundColor(Color.parseColor("#c60962"));
            mSelection = SCISSORS;
            commHandler.sendMove(SCISSORS);
        }
        else if(selection == RESTART){
            v.setBackgroundColor(Color.parseColor("#c60962"));
            mSelection = RESTART;
            commHandler.sendMove(RESTART);
            restartGame();
        }
        if((mSelection!=RESTART) && (oSelection != NONE_SELECTED)){
            getResult();
        }
    }

    public void getMove(int selection){
        oSelection = selection;

        if(oSelection == RESTART){
            restartGame();
        }

        else if(selectionMade){
            getResult();
        }
    }

    public void getResult(){

        if( (oSelection == ROCK) && (mSelection == PAPER) ){
            mWins++;
            displayResult(WIN);
        }
        else if( (oSelection == PAPER) && (mSelection == SCISSORS) ){
            mWins++;
            displayResult(WIN);
        }
        else if( (oSelection == SCISSORS) && (mSelection == ROCK) ){
            mWins++;
            displayResult(WIN);
        }
        else if (oSelection == mSelection){
            mTies++;
            displayResult(TIE);
        }
        else {
            mLosses++;
            displayResult(LOSS);
        }

        gamePlaying = false;
    }

    public void displayResult(int gameResult){

        score.setText("Score: " + mWins + ":" + mLosses + " Ties: " + mTies);

        if(gameResult == WIN){
            result.setText("YOU WON!");
        }
        else if (gameResult == LOSS){
            result.setText("YOU LOST :(");
        }
        else {
            result.setText("GAME TIED");
        }

    }

    public void restartGame(){

        if((mSelection == RESTART) && (oSelection == RESTART)){
            rock.setBackgroundColor(Color.parseColor("#ffffff"));
            paper.setBackgroundColor(Color.parseColor("#ffffff"));
            scissors.setBackgroundColor(Color.parseColor("#ffffff"));
            restart.setBackgroundColor(Color.parseColor("#ffffff"));
            mSelection = NONE_SELECTED;
            oSelection = NONE_SELECTED;
            selectionMade = false;
            gamePlaying = true;
            result.setText("");
        }

        else if (mSelection == RESTART){
            rock.setBackgroundColor(Color.parseColor("#ffffff"));
            paper.setBackgroundColor(Color.parseColor("#ffffff"));
            scissors.setBackgroundColor(Color.parseColor("#ffffff"));
            debugToast("Waiting for Watch to restart");
        }

        else if(oSelection == RESTART){
            debugToast("Restart the game");
        }
    }

    public void debugToast (String s ){
        Toast toast = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void sendNotification(){
/*
        //Send a notification to the watch
        int notificationID = 1;
//        //The intent allows user opens the activity on the phone
        Intent viewIntent = new Intent(this, MainActivity.class);
        PendingIntent viewPendingIntent = PendingIntent.getActivity(this, 0, viewIntent, 0);
        //Use the notification builder to create a notification
        NotificationCompat.Builder notificationBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                       .setContentTitle("Rock Paper Scissors")
                        .setContentText("User has made selection " + mSelection)
                        .setContentIntent(viewPendingIntent);
        //Send the notification
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(notificationID, notificationBuilder.build());
*/
    }
}
