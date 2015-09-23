package feliperrm.reversiandroid.Telas;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import feliperrm.reversiandroid.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends android.support.v4.app.Fragment {


    Bitmap bitmap;
    int imageRes;
    String imagePath;
    static final String IMAGE_PATH_KEY = "pathhkey";
    static final String IMAGE_RES_KEY = "residkey";
    ImageView imageView;
    ImageFragmentCallback callback;


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public ImageFragment() {
        // Required empty public constructor
    }

    public static ImageFragment newInstance(String imagePath){
        ImageFragment imageFragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IMAGE_PATH_KEY,imagePath);
        imageFragment.setArguments(bundle);
        return imageFragment;
    }

    public static ImageFragment newInstance(int imageResource){
        ImageFragment imageFragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IMAGE_RES_KEY, imageResource);
        imageFragment.setArguments(bundle);
        return imageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null){
            imageRes = bundle.getInt(IMAGE_RES_KEY,-1);
            if(imageRes != -1){
                bitmap = BitmapFactory.decodeResource(getResources(),imageRes);
            }
            else{

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_image, container, false);
        findViews(v);
        setUpImageView();
        callback.fragmentReady();
        return v;
    }

    private void findViews(View v){
        imageView = (ImageView) v.findViewById(R.id.imageViewPiecesSelectorFragment);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (ImageFragmentCallback) activity;
    }

    private void setUpImageView(){
        Bundle bundle = getArguments();
        if(bundle!=null){
            imageRes = bundle.getInt(IMAGE_RES_KEY,-1);
            if(imageRes != -1){
                /* Load Image from Resources */
                bitmap = BitmapFactory.decodeResource(getResources(),imageRes);
                imageView.setImageBitmap(bitmap);
            }
            else{
                /* Get Image from Disk */

            }
        }
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Object getImageInformation(){
        if(imageRes!=-1)
            return imageRes;
        else
            return imagePath;
    }

    public interface ImageFragmentCallback{
        public void fragmentReady();
    }

}
