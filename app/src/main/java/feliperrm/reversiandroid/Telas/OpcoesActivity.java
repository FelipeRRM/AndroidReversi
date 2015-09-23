package feliperrm.reversiandroid.Telas;

import android.app.Application;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Handler;

import feliperrm.reversiandroid.Adapters.FragmentViewPagerAdapter;
import feliperrm.reversiandroid.R;

public class OpcoesActivity extends BaseActivity implements ImageFragment.ImageFragmentCallback {

    EditText editP1, editP2;
    ViewPager p1ViewPager, p2ViewPager, bgViewPager;
    FragmentViewPagerAdapter adapter1, adapter2, adapterBg;
    ArrayList<Fragment> fragments1, fragments2, fragmentsBg;
    Button btnSound, btnMusic;
    Spinner spinnerBoardSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcoes);
        findViews();
        setUpBtnMusic();
        setUpBtnSound();
        setUpEditTexts();
        createFragmentArray();
        createAdapters();
        setViewPagerBehaviour();
        setUpSpinner();
    }

    private void findViews(){
        editP1 = (EditText) findViewById(R.id.editTextP1Name);
        editP2 = (EditText) findViewById(R.id.editTextP2Name);
        p1ViewPager = (ViewPager) findViewById(R.id.player1ViewPagerSelector);
        p2ViewPager = (ViewPager) findViewById(R.id.player2ViewPagerSelector);
        btnSound = (Button) findViewById(R.id.btnSound);
        btnMusic = (Button) findViewById(R.id.btnMusic);
        bgViewPager = (ViewPager) findViewById(R.id.backgroundViewPagerSelector);
        spinnerBoardSize = (Spinner) findViewById(R.id.spinnerBoardSize);
    }

    private void setUpBtnSound(){
        if(((feliperrm.reversiandroid.Uteis.Application)getApplication()).isClickSound())
            btnSound.setText(getString(R.string.on_sound));
        else
            btnSound.setText(getString(R.string.off_sound));

        btnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((feliperrm.reversiandroid.Uteis.Application)getApplication()).isClickSound()){
                    ((feliperrm.reversiandroid.Uteis.Application)getApplication()).setClickSound(false);
                    btnSound.setText(getString(R.string.off_sound));
                }
                else{
                    ((feliperrm.reversiandroid.Uteis.Application)getApplication()).setClickSound(true);
                    btnSound.setText(getString(R.string.on_sound));
                }
            }
        });
    }

    private void setUpBtnMusic(){
        if(((feliperrm.reversiandroid.Uteis.Application)getApplication()).isMusic())
            btnMusic.setText(getString(R.string.on_music));
        else
            btnMusic.setText(getString(R.string.off_music));

        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((feliperrm.reversiandroid.Uteis.Application) getApplication()).isMusic()) {
                    ((feliperrm.reversiandroid.Uteis.Application) getApplication()).setMusic(false);
                    btnMusic.setText(getString(R.string.off_music));
                } else {
                    ((feliperrm.reversiandroid.Uteis.Application) getApplication()).setMusic(true);
                    btnMusic.setText(getString(R.string.on_music));
                }
            }
        });
    }

    private void setUpEditTexts(){

        if( ((feliperrm.reversiandroid.Uteis.Application)getApplication()).getP1Name()!="Player1" )
            editP1.setText(((feliperrm.reversiandroid.Uteis.Application)getApplication()).getP1Name());
        if( ((feliperrm.reversiandroid.Uteis.Application)getApplication()).getP2Name()!="Player2" )
            editP2.setText(((feliperrm.reversiandroid.Uteis.Application)getApplication()).getP2Name());


        editP1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s!= null && !(s.toString().isEmpty())){
                    ((feliperrm.reversiandroid.Uteis.Application)getApplication()).setP1Name(s.toString());
                }
                /* Deletamos o nome do preferences caso seja deletado do EditText */
                else{
                    ((feliperrm.reversiandroid.Uteis.Application)getApplication()).clearP1Name();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /* Mesma funcionalidade que acima, mas implementado de um jeito ligeiramente diferente */
        editP2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count!=0){
                    ((feliperrm.reversiandroid.Uteis.Application)getApplication()).setP2Name(s.toString());
                }
                /* Deletamos o nome do preferences caso seja deletado do EditText */
                else{
                    ((feliperrm.reversiandroid.Uteis.Application)getApplication()).clearP2Name();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void createFragmentArray(){
        fragments1 = new ArrayList<>();
        fragments1.add(ImageFragment.newInstance(R.drawable.white_piece));
        fragments1.add(ImageFragment.newInstance(R.drawable.black_piece));
        fragments1.add(ImageFragment.newInstance(R.drawable.flat_white));
        fragments1.add(ImageFragment.newInstance(R.drawable.flat_black));
        fragments1.add(ImageFragment.newInstance(R.drawable.florzinha));
        fragments1.add(ImageFragment.newInstance(R.drawable.psicodelico1));
        fragments1.add(ImageFragment.newInstance(R.drawable.psicodelico2));
        fragments1.add(ImageFragment.newInstance(R.drawable.psicodelico3));



        fragments2 = new ArrayList<>();
        fragments2.add(ImageFragment.newInstance(R.drawable.white_piece));
        fragments2.add(ImageFragment.newInstance(R.drawable.black_piece));
        fragments2.add(ImageFragment.newInstance(R.drawable.flat_white));
        fragments2.add(ImageFragment.newInstance(R.drawable.flat_black));
        fragments2.add(ImageFragment.newInstance(R.drawable.florzinha));
        fragments2.add(ImageFragment.newInstance(R.drawable.psicodelico1));
        fragments2.add(ImageFragment.newInstance(R.drawable.psicodelico2));
        fragments2.add(ImageFragment.newInstance(R.drawable.psicodelico3));



        fragmentsBg = new ArrayList<>();
        fragmentsBg.add(ImageFragment.newInstance(R.drawable.background));
        fragmentsBg.add(ImageFragment.newInstance(R.drawable.table));
    }

    private void createAdapters(){
        adapter1 = new FragmentViewPagerAdapter(getSupportFragmentManager(),fragments1);
        p1ViewPager.setAdapter(adapter1);

        adapter2 = new FragmentViewPagerAdapter(getSupportFragmentManager(),fragments2);
        p2ViewPager.setAdapter(adapter2);

        adapterBg = new FragmentViewPagerAdapter(getSupportFragmentManager(),fragmentsBg);
        bgViewPager.setAdapter(adapterBg);

    }

    private void setViewPagerBehaviour(){



        p1ViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                ImageFragment imageFragment = (ImageFragment) adapter1.getItem(position);
                Object imageInfo = imageFragment.getImageInformation();
                Log.d("imageInfo", imageInfo.getClass() + imageInfo.toString());
                ((feliperrm.reversiandroid.Uteis.Application)getApplication()).setP1ImageNum(position);
                if (imageInfo instanceof String)
                    ((feliperrm.reversiandroid.Uteis.Application) getApplication()).setP1ImagePath((String) imageInfo);
                else
                    ((feliperrm.reversiandroid.Uteis.Application)getApplication()).setP1ImageRes((Integer) imageInfo);
            }
        });

        p2ViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                ImageFragment imageFragment = (ImageFragment) adapter2.getItem(position);
                Object imageInfo = imageFragment.getImageInformation();
                Log.d("imageInfo", imageInfo.getClass() + imageInfo.toString());
                ((feliperrm.reversiandroid.Uteis.Application)getApplication()).setP2ImageNum(position);
                if (imageInfo instanceof String)
                    ((feliperrm.reversiandroid.Uteis.Application) getApplication()).setP2ImagePath((String) imageInfo);
                else
                    ((feliperrm.reversiandroid.Uteis.Application) getApplication()).setP2ImageRes((Integer) imageInfo);
            }
        });

        bgViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                ImageFragment imageFragment = (ImageFragment) adapterBg.getItem(position);
                Object imageInfo = imageFragment.getImageInformation();
                Log.d("imageInfo", imageInfo.getClass() + imageInfo.toString());
                ((feliperrm.reversiandroid.Uteis.Application)getApplication()).setBgImageNum(position);
                if (imageInfo instanceof String)
                    ((feliperrm.reversiandroid.Uteis.Application) getApplication()).setBgImagePath((String) imageInfo);
                else
                    ((feliperrm.reversiandroid.Uteis.Application) getApplication()).setBgImageRes((Integer) imageInfo);
            }
        });

        /* Temos de adiar a "setagem" dos ViewPagers, de modo a esperar que os fragments
         fiquem pronto e não retornem 0 e nem null para o res e para o path */
        android.os.Handler handler = new android.os.Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                p1ViewPager.setCurrentItem(findViewPagerStartPage(1),false);
                p2ViewPager.setCurrentItem(findViewPagerStartPage(2), false);
                bgViewPager.setCurrentItem(((feliperrm.reversiandroid.Uteis.Application)getApplication()).getBgImageNum(),false);
            }
        });

    }

    private int findViewPagerStartPage(int player){
        /* Old and Slower Way, more prone to error, as all fragments had to be created already in order to provide us the image info to compare to the saved one
         * now we just save the position set, much faster and more reliable */
        /*
        ArrayList<Fragment> arrayList;
        String path;
        int res;
        if(player==1) {
            arrayList = adapter1.getFragments();
            path = ((feliperrm.reversiandroid.Uteis.Application)getApplication()).getP1ImagePath();
            res = ((feliperrm.reversiandroid.Uteis.Application)getApplication()).getP1ImageRes();
        }
        else{
            arrayList = adapter2.getFragments();
            path = ((feliperrm.reversiandroid.Uteis.Application)getApplication()).getP2ImagePath();
            res = ((feliperrm.reversiandroid.Uteis.Application)getApplication()).getP2ImageRes();
        }

        if (res!=-1){
            int i = 0;
            int size = arrayList.size();
            while(i<size){
                if(((ImageFragment)arrayList.get(i)).getImageRes() == res)
                return i;
                i++;
            }

        }
        else{
            int i = 0;
            int size = arrayList.size();
            while(i<size){
                if(((ImageFragment)arrayList.get(i)).getImagePath() == path)
                return i;
                i++;
            }
        }
        if(player==1)
            return 0;
        else
            return 1;
            */
        if(player==1){
            return ((feliperrm.reversiandroid.Uteis.Application)getApplication()).getP1ImageNum();
        }
        else return ((feliperrm.reversiandroid.Uteis.Application)getApplication()).getP2ImageNum();
    }

    private void setUpSpinner(){
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
        adapter.addAll(new String[]{"4", "5", "6", "7", "8"});
        spinnerBoardSize.setAdapter(adapter);
        spinnerBoardSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((feliperrm.reversiandroid.Uteis.Application) getApplication()).setBoardSize(Integer.parseInt(adapter.getItem(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        android.os.Handler handler = new android.os.Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int pos = adapter.getPosition(String.valueOf(((feliperrm.reversiandroid.Uteis.Application) getApplication()).getBoardSize()));
                spinnerBoardSize.setSelection(pos,false);
            }
        });

    }


    @Override
    public void fragmentReady() {

    }
}
