package fuckingcompany.dimplom;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {
    private List<RequestInfo> mListRequestInfo;
    private Context mContext;

    public RecyclerViewAdapter(List<RequestInfo> requestInfo, Context context ) {
        this.mListRequestInfo = requestInfo;
        this.mContext = context;


    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_requests_content, parent, false);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, final int position) {
        holder.mTextViewDate.setText(mListRequestInfo.get(position).getDate());
        holder.mTextViewAuthor.setText(mListRequestInfo.get(position).getRequestAuthor());
        holder.mTextViewTheme.setText(mListRequestInfo.get(position).getRequestTheme());

        byte[] imageAsBytes = Base64.decode(mListRequestInfo.get(position).getPhoto(), Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        holder.mImageView.setImageBitmap( bmp);

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)mContext).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_container, FragmentRequestInfo.newInstance(
                               mListRequestInfo.get(position).getRequestTheme(),
                               mListRequestInfo.get(position).getRequestAuthor(),
                               mListRequestInfo.get(position).getDate(),
                               mListRequestInfo.get(position).getDescription(),
                               mListRequestInfo.get(position).getPhoto()
                               ))
                        .addToBackStack(null)
                        .commit();
                }
        });
    }

    @Override
    public int getItemCount() {
        return mListRequestInfo.size();
    }
}
