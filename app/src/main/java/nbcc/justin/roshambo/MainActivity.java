package nbcc.justin.roshambo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private AnimatorSet animation;
    private Rochambo game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ObjectAnimator playerAnimator = ObjectAnimator.ofFloat(findViewById(R.id.player_choice_image), "rotationX", 0f, 360f).setDuration(1000);
        ObjectAnimator gameAnimator = ObjectAnimator.ofFloat(findViewById(R.id.game_choice_image), "rotationX", 0f, 360f).setDuration(1000);
        animation = new AnimatorSet();
        animation.playTogether(gameAnimator, playerAnimator);
        animation.setInterpolator(new AnticipateOvershootInterpolator());

        if (savedInstanceState != null) {
            game = (Rochambo) savedInstanceState.getSerializable("roshambo");
        } else {
            game = new Rochambo();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putSerializable("roshambo", game);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void playRoshambo(View view) {
        int choice;
        int id = view.getId();

        switch (id) {
            case R.id.rock_button:
                choice = Rochambo.ROCK;
                break;
            case R.id.paper_button:
                choice = Rochambo.PAPER;
                break;
            case R.id.scissors_button:
                choice = Rochambo.SCISSOR;
                break;
            default:
                choice = Rochambo.NONE;
                break;
        }
        game.playerMakesMove(choice);
        updateView();
        animation.end();
        animation.start();
    }

    private void updateView() {
        ImageView playerChoice = findViewById(R.id.player_choice_image);
        ImageView gameChoice = findViewById(R.id.game_choice_image);

        playerChoice.setImageResource(getImageFromChoice(game.getPlayerMove()));
        gameChoice.setImageResource(getImageFromChoice(game.getGameMove()));

        TextView resultText = findViewById(R.id.result_text);
        resultText.setText(game.winLoseOrDraw());
    }

    private int getImageFromChoice(int choice) {
        switch (choice) {
            case Rochambo.ROCK:
                return R.mipmap.rock;
            case Rochambo.PAPER:
                return R.mipmap.paper;
            case Rochambo.SCISSOR:
                return R.mipmap.scissors;
            default:
                return R.mipmap.none;
        }
    }
}
