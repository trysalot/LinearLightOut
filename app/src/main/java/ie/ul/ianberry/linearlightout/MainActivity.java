package ie.ul.ianberry.linearlightout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class MainActivity extends AppCompatActivity {

    private LightsOutGame mGame;
    final int numButtons = 7;
    TextView mGameStateTextView;
    Button[] mButtons = new Button[numButtons];
    final String GAME_STATE_KEY = "GAME_STATE_KEY";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(GAME_STATE_KEY+"I", mGame.getNumPresses());
//        Log.i("LightsOut","EXP Button presses:"+mGame.getNumPresses());
        outState.putBoolean(GAME_STATE_KEY+"B", !mGame.checkForWin());
//        Log.i("LightsOut","EXP Buttons enabled:"+Boolean.toString(!mGame.checkForWin()));
        outState.putString(GAME_STATE_KEY+"S", mGame.toString());
//        Log.i("LightsOut","EXP Button state string:"+mGame.toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGame = new LightsOutGame(numButtons);

        mGameStateTextView = findViewById(R.id.match_text);
        mButtons[0] = findViewById(R.id.button1);
        mButtons[1] = findViewById(R.id.button2);
        mButtons[2] = findViewById(R.id.button3);
        mButtons[3] = findViewById(R.id.button4);
        mButtons[4] = findViewById(R.id.button5);
        mButtons[5] = findViewById(R.id.button6);
        mButtons[6] = findViewById(R.id.button7);

        if (savedInstanceState != null) {
            int numPresses = savedInstanceState.getInt(GAME_STATE_KEY+"I");
            mGame.setNumPresses(numPresses);
//            Log.i("LightsOut","IMP Button presses:"+Integer.toString(numPresses));
            boolean buttonsEnabled = savedInstanceState.getBoolean(GAME_STATE_KEY+"B");
            enableButtons(buttonsEnabled);
//            Log.i("LightsOut","IMP Buttons enabled:"+Boolean.toString(buttonsEnabled));
            int[] buttonsState = new int[numButtons];
            String buttonStateStr = savedInstanceState.getString(GAME_STATE_KEY+"S");
//            Log.i("LightsOut","IMP Button state string:"+buttonStateStr);
            for (int i = 0; i < buttonsState.length; i++) {
                if (buttonStateStr.charAt(i) == '1') buttonsState[i] = 1;
            }
            mGame.setAllValues(buttonsState);
        }
        updateButtons();
        updateGameStateText();
    }

    private void updateView() {
        updateGameStateText();
        updateButtons();
    }

    private void updateGameStateText() {
        int numPresses = mGame.getNumPresses();
        if (mGame.checkForWin()) {
            mGameStateTextView.setText(getString(R.string.complete, mGame.getNumPresses()));
            enableButtons(FALSE);
        } else if (numPresses <= 0) {
            mGameStateTextView.setText(getString(R.string.start));
        } else if (numPresses == 1) {
            mGameStateTextView.setText(getString(R.string.n_press, numPresses));
        } else {
            mGameStateTextView.setText(getString(R.string.n_presses, numPresses));
        }
    }

    private void updateButtons() {
        for (int i = 0; i < mButtons.length; i++) {
            mButtons[i].setText(Integer.toString(mGame.getValueAtIndex(i)));
        }
    }

    private void enableButtons(boolean yesNo) {
        for (int i = 0; i < mButtons.length; i++) {
            mButtons[i].setEnabled(yesNo);
        }
    }

    public void pressedBoardButton(View view) {
        int buttonIndex = Integer.parseInt(view.getTag().toString());
        mGame.pressedButtonAtIndex(buttonIndex);
        updateView();
    }

    public void pressedNewGame(View view) {
        mGame = new LightsOutGame(numButtons);
        updateGameStateText();
        enableButtons(TRUE);
        updateButtons();
    }
}
