package se.hh.thirty;

import android.widget.ImageButton;

import java.util.Random;

public class Dice {
    int[] whiteDice = new int[]{R.drawable.white1, R.drawable.white2, R.drawable.white3, R.drawable.white4, R.drawable.white5, R.drawable.white6};
    int[] greyDice = new int[]{R.drawable.grey1, R.drawable.grey2, R.drawable.grey3, R.drawable.grey4, R.drawable.grey5, R.drawable.grey6};

    int number;
    boolean clicked;
    ImageButton mImageButton;

    Random rand = new Random();

    public Dice(int n, ImageButton b){
        number = n;
        clicked = false;
        mImageButton = b;
        setImage();
    }

    public void setImage(){
        if(!clicked){
            mImageButton.setImageResource(whiteDice[number - 1]);
        } else{
            mImageButton.setImageResource(greyDice[number - 1]);
        }
    }

    public ImageButton getImage(){
        return mImageButton;
    }

    public int getNumber(){
        return number;
    }

    public ImageButton getButton(){
        return mImageButton;
    }

    public void toggleClicked(){
        clicked = !clicked;
        setImage();
    }

    public void randomize(){
        number = rand.nextInt(6) + 1;
        setImage();
    }
}
