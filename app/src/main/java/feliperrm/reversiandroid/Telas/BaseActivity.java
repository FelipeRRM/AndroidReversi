package feliperrm.reversiandroid.Telas;

import android.support.v7.app.AppCompatActivity;

import feliperrm.reversiandroid.Uteis.Application;

/**
 * Created by Felipe on 20/09/2015.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        if(((Application)getApplication()).isMusic())
            ((Application)getApplication()).resumeMusic();
    }

    @Override
    protected void onPause() {
        if(((Application)getApplication()).isMusic())
            ((Application)getApplication()).pauseMusic();
        super.onPause();
    }
}
