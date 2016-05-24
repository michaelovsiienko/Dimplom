package fuckingcompany.dimplom;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class FragmentRequestInfo extends Fragment {
    private TextView mTextViewTheme;
    private TextView mTextViewAuthor;
    private TextView mTextViewDate;
    private TextView mTextViewDescription;
    private ImageView mImageViewPhoto;

    public static FragmentRequestInfo newInstance (String theme, String author, String date, String description,String photoPhile){
        FragmentRequestInfo fragmentRequestInfo = new FragmentRequestInfo();
        Bundle bundle = new Bundle();
        bundle.putString(Constans.THEME,theme);
        bundle.putString(Constans.AUTHOR,author);
        bundle.putString(Constans.DATE,date);
        bundle.putString(Constans.CONTENT,description);
        bundle.putString(Constans.PHOTO,photoPhile);
        fragmentRequestInfo.setArguments(bundle);
        return fragmentRequestInfo;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requestinfo, container, false);
        mTextViewTheme = (TextView) view.findViewById(R.id.request_infoTheme);
        mTextViewAuthor= (TextView)view.findViewById(R.id.request_infoAuthor);
        mTextViewDate= (TextView)view.findViewById(R.id.request_infoDate);
        mTextViewDescription= (TextView)view.findViewById(R.id.description_requestInfo228);
        mImageViewPhoto= (ImageView)view.findViewById(R.id.request_infoImage);
        if (getArguments()!=null){
            mTextViewTheme.setText(getArguments().getString(Constans.THEME));
            mTextViewAuthor.setText(getArguments().getString(Constans.AUTHOR));
            mTextViewDate.setText(getArguments().getString(Constans.DATE));
            mTextViewDescription.setText(getArguments().getString(Constans.CONTENT));
            byte[] imageAsBytes = Base64.decode(getArguments().getString(Constans.PHOTO), Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            mImageViewPhoto.setImageBitmap(bmp);
        }
        return view;
    }
}
