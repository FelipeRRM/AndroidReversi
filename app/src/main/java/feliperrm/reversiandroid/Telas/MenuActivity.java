package feliperrm.reversiandroid.Telas;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import feliperrm.reversiandroid.R;
import feliperrm.reversiandroid.Uteis.Application;


public class MenuActivity extends BaseActivity {

    Button pvp, easy, hard, options, about;
    LinearLayout linearLayoutBackground;
    ImageView logo;
    private final int timeBetweenAnims=250;
    float logoX, logoY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findViews();
        setUpViews();
        animateViews();

    }

    private void findViews() {
        pvp = (Button) findViewById(R.id.btnPvP);
        easy = (Button) findViewById(R.id.btnPvEasy);
        hard = (Button) findViewById(R.id.btnPvHard);
        options = (Button) findViewById(R.id.btnOptions);
        linearLayoutBackground = (LinearLayout) findViewById(R.id.linearBackgroundLayout);
        about = (Button) findViewById(R.id.btnAbout);
        logo = (ImageView) findViewById(R.id.logo);
    }

    private void setUpViews(){
        pvp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prox = new Intent(MenuActivity.this,GameActivity.class);
                prox.putExtra("SIZE",((feliperrm.reversiandroid.Uteis.Application) getApplication()).getBoardSize());
                prox.putExtra("AI",0);
                startActivity(prox);
            }
        });

        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prox = new Intent(MenuActivity.this,GameActivity.class);
                prox.putExtra("SIZE",((feliperrm.reversiandroid.Uteis.Application) getApplication()).getBoardSize());
                prox.putExtra("AI",1);
                startActivity(prox);
            }
        });

        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prox = new Intent(MenuActivity.this,GameActivity.class);
                prox.putExtra("SIZE",((feliperrm.reversiandroid.Uteis.Application) getApplication()).getBoardSize());
                prox.putExtra("AI",2);
                startActivity(prox);
            }
        });

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prox = new Intent(MenuActivity.this, OpcoesActivity.class);
                startActivity(prox);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prox = new Intent(MenuActivity.this, AboutActivity.class);
                startActivity(prox);
            }
        });

    }

    private void animateViews(){
        logo.setVisibility(View.INVISIBLE);
        pvp.setVisibility(View.INVISIBLE);
        easy.setVisibility(View.INVISIBLE);
        hard.setVisibility(View.INVISIBLE);
        options.setVisibility(View.INVISIBLE);
        about.setVisibility(View.INVISIBLE);


        logo.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                logo.getViewTreeObserver().removeOnPreDrawListener(this);
                logoX = logo.getX();
                logoY = logo.getY();
                logo.setTranslationY(-600);
                logo.setScaleX(0.5f);
                logo.setScaleY(0.5f);
                logo.setVisibility(View.VISIBLE);
                logo.animate().scaleY(1f).scaleX(1f).setDuration((long) (timeBetweenAnims * 3.5)).setInterpolator(new DecelerateInterpolator()).translationY(0f).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        Handler handler = new Handler();

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pvp.setVisibility(View.VISIBLE);
                            }
                        }, timeBetweenAnims);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                easy.setVisibility(View.VISIBLE);
                            }
                        }, timeBetweenAnims * 2);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hard.setVisibility(View.VISIBLE);
                            }
                        }, timeBetweenAnims * 3);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                options.setVisibility(View.VISIBLE);
                            }
                        }, timeBetweenAnims * 4);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                about.setVisibility(View.VISIBLE);
                            }
                        }, timeBetweenAnims * 5);
                    }
                });
                return true;
            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();
        if(((Application)getApplication()).getBgImageRes()!=-1) {
            linearLayoutBackground.setBackgroundResource(((Application)getApplication()).getBgImageRes());
        }
        else{
            //carrega do disco
        }
    }
}
