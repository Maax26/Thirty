package se.hh.thirty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        String[] gameKeys = {"Low", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        int finalScore = 0;

        Intent intent = getIntent();
        Map scoreMap = (Map)intent.getSerializableExtra("map");

        TextView resultText = findViewById(R.id.resultText);
        TextView scoreText = findViewById(R.id.scoreText);

        StringBuilder result = new StringBuilder();

        for(int i = 0; i < scoreMap.size(); i++){
            result.append(gameKeys[i] + "\t\t\t" + " - " + "\t\t\t" + scoreMap.get(gameKeys[i]) + "\n");
            finalScore += Integer.parseInt((String) scoreMap.get(gameKeys[i]));
        }

        resultText.setText(result.toString());
        scoreText.setText("Final score: " + finalScore);
    }
}