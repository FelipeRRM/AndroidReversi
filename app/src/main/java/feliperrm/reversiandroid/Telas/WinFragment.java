package feliperrm.reversiandroid.Telas;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import feliperrm.reversiandroid.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class WinFragment extends DialogFragment {

    TextView winName;
    Button btnGoBack;
    String winnerName;
    static final String WINNER_NAME_KEY="winnerkey";


    public WinFragment() {
        // Required empty public constructor
    }

    public static WinFragment createFragment(String winnerName){
        WinFragment winFragment = new WinFragment();
        Bundle bundle = new Bundle();
        bundle.putString(WINNER_NAME_KEY, winnerName);
        winFragment.setArguments(bundle);
        return winFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.fragment_win, container, false);
        recoverBundle();
        findViews(v);
        setUpViews();
        return v;
    }

    private void recoverBundle() {
        Bundle bundle = getArguments();
        if(bundle!=null){
            winnerName = bundle.getString(WINNER_NAME_KEY);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.full_dialog);
    }

    private void findViews(View v){
        winName = (TextView) v.findViewById(R.id.textViewDialogWin);
        btnGoBack = (Button) v.findViewById(R.id.btnGoBackToMenu);
    }

    private void setUpViews(){
        winName.setText(winnerName+" "+getString(R.string.won_the_game));
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Intent prox = new Intent(getActivity(), MenuActivity.class);
        prox.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
       startActivity(prox);
        super.dismiss();
    }
}
