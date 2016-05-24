package fuckingcompany.dimplom;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by mykhail on 08.05.16.
 */
public class RecyclerViewHolders extends RecyclerView.ViewHolder {
    public ImageView mImageView;
    public TextView mTextViewTheme;
    public TextView mTextViewAuthor;
    public TextView mTextViewDate;
    public CardView mCardView;

    public RecyclerViewHolders(View itemView) {
        super(itemView);
        mImageView = (ImageView) itemView.findViewById(R.id.requests_photoproblem);

        mTextViewTheme = (TextView) itemView.findViewById(R.id.requests_theme);
        mTextViewAuthor = (TextView) itemView.findViewById(R.id.requests_author);
        mTextViewDate = (TextView) itemView.findViewById(R.id.requests_date);
        mCardView = (CardView) itemView.findViewById(R.id.cardview);
    }
}
