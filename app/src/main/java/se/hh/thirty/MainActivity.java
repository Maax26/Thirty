package se.hh.thirty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    final int ROLLING = 1;
    final int SCORING = 2;

    int gameState = ROLLING;

    int played = 1;
    Dice[] dice;
    ArrayList<String> gameChoices = new ArrayList<>();
    ArrayList<Dice> scoreDice = new ArrayList<>();
    Map scoreMap = new HashMap();

    Spinner dropdown;
    ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dropdown = findViewById(R.id.spinner);

        dice = new Dice[] {new Dice(1, (ImageButton) findViewById(R.id.diceButton1)),
                new Dice(2, (ImageButton) findViewById(R.id.diceButton2)),
                new Dice(3, (ImageButton) findViewById(R.id.diceButton3)),
                new Dice(4, (ImageButton) findViewById(R.id.diceButton4)),
                new Dice(5, (ImageButton) findViewById(R.id.diceButton5)),
                new Dice(6, (ImageButton) findViewById(R.id.diceButton6))};

        final Button mRollButton = findViewById(R.id.rollButton);

        final Button mNextButton = findViewById(R.id.nextButton);
        mNextButton.setVisibility(View.GONE);

        final TextView mTextView = findViewById(R.id.gameText);
        mTextView.setText(R.string.text_roll_one);

        setupGame();

        mRollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gameState == ROLLING) {
                    played++;

                    switch(played){
                        case 1:
                            mTextView.setText(R.string.text_roll_one);
                            break;
                        case 2:
                            mTextView.setText(R.string.text_roll_two);
                            break;
                        case 3:
                            mTextView.setText(R.string.text_roll_three);
                    }

                    for (Dice d : dice) {
                        if (played <= 3 && !d.clicked) {
                            d.randomize();
                        }
                        if (played >= 3 && d.clicked) {
                            d.toggleClicked();
                        }
                    }
                }

                if(played > 3){
                    calculateScore();
                } else if(played == 3){
                    mRollButton.setText(getResources().getString(R.string.button_score));
                    mNextButton.setVisibility(View.VISIBLE);
                    gameState = SCORING;
                    played++;
                }

                Log.i(TAG, "Played: " + played);
            }
        });

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.diceButton1:
                        dice[0].toggleClicked();
                        if(gameState == SCORING){
                            scoreDice.add(dice[0]);
                        }
                        break;
                    case R.id.diceButton2:
                        dice[1].toggleClicked();
                        if(gameState == SCORING){
                            scoreDice.add(dice[1]);
                        }
                        break;
                    case R.id.diceButton3:
                        dice[2].toggleClicked();
                        if(gameState == SCORING){
                            scoreDice.add(dice[2]);
                        }
                        break;
                    case R.id.diceButton4:
                        dice[3].toggleClicked();
                        if(gameState == SCORING){
                            scoreDice.add(dice[3]);
                        }
                        break;
                    case R.id.diceButton5:
                        dice[4].toggleClicked();
                        if(gameState == SCORING){
                            scoreDice.add(dice[4]);
                        }
                        break;
                    case R.id.diceButton6:
                        dice[5].toggleClicked();
                        if(gameState == SCORING){
                            scoreDice.add(dice[5]);
                        }
                        break;
                }
            }
        };

        for (Dice die : dice) {
            die.getButton().setOnClickListener(listener);
        }

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNextButton.setVisibility(View.GONE);

                gameState = ROLLING;
                played = 0;

                for(Dice d : dice){
                    d.getImage().setVisibility(View.VISIBLE);

                    if(d.clicked){
                        d.toggleClicked();
                    }
                }

                mRollButton.setText(getResources().getString(R.string.button_roll));
                mRollButton.performClick();

                spinnerAdapter.remove((String)dropdown.getSelectedItem());
                spinnerAdapter.notifyDataSetChanged();

                if(dropdown.getCount() == 0){
                    Intent resultIntent = new Intent(MainActivity.this, ResultActivity.class);
                    resultIntent.putExtra("map", (Serializable) scoreMap);
                    startActivity(resultIntent);
                    setupGame();
                }
            }
        });
    }

    public void calculateScore(){
        String spinnerString = dropdown.getSelectedItem().toString();

        int score = 0;
        boolean log = false;

        if(spinnerString.equals("Low")) {
            for (int i = scoreDice.size() - 1; i >= 0; i--) {
                if (scoreDice.get(i).number <= 3) {
                    score += scoreDice.get(i).number;
                    scoreDice.get(i).getImage().setVisibility(View.INVISIBLE);
                    log = true;
                }else{
                    scoreDice.get(i).toggleClicked();
                    scoreDice.remove(i);
                }
            }
        }else {
            for(int i = 0; i < scoreDice.size(); i++){
                score += scoreDice.get(i).number;
            }

            if(score == Integer.parseInt(spinnerString)){
                Toast.makeText(this, R.string.toast_successful, Toast.LENGTH_SHORT).show();
                for(int i = scoreDice.size() - 1; i >= 0; i--){
                    scoreDice.get(i).getImage().setVisibility(View.INVISIBLE);
                    scoreDice.remove(i);
                    log = true;
                }
            }else{
                Toast.makeText(this, "Selected dice does not add up to " + spinnerString + "!", Toast.LENGTH_SHORT).show();
                for(int i = scoreDice.size() - 1; i >= 0; i--){
                    scoreDice.get(i).toggleClicked();
                    scoreDice.remove(i);
                }
            }
        }

        if (scoreDice.size() > 0) {
            scoreDice.subList(0, scoreDice.size()).clear();
        }

        if(log) {
            Log.i(TAG, "Score: " + score);

            int prevScore = Integer.parseInt((String) (scoreMap.get(spinnerString)));
            score += prevScore;
            scoreMap.put(spinnerString, score + "");

            Log.i(TAG, "Logged score: " + scoreMap.get(spinnerString));
        }
    }

    private void setupGame(){
        gameChoices.add("Low");
        /*
        gameChoices.add("4");

        gameChoices.add("5");
        gameChoices.add("6");
        gameChoices.add("7");
        gameChoices.add("8");
        gameChoices.add("9");
        gameChoices.add("10");
        gameChoices.add("11");
        gameChoices.add("12");
        */

        for(int i = 0; i < gameChoices.size(); i++){
            scoreMap.put(gameChoices.get(i), "0");
        }

        for(Dice d: dice){
            d.randomize();
        }

        spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, gameChoices);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(spinnerAdapter);


    }
}