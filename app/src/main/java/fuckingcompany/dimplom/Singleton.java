package fuckingcompany.dimplom;

import com.firebase.client.DataSnapshot;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mykhail on 09.05.16.
 */
public class Singleton {
    private static Singleton mInstance = null;

    private String mAuthor;
    private String mImageFile;
    private LatLng mLocation;
    private List<RequestInfo> mListRequest = new ArrayList<>();
    private String mMiniImage;
    private DataSnapshot mDataSnapshot;
    private Singleton() {
    }

    public static Singleton getInstance() {
        if (mInstance == null) {
            mInstance = new Singleton();
        }
        return mInstance;
    }
    public void setAuthor(String author){
        mAuthor = author;
    }
    public void setImageFile(String imageFile){
        mImageFile = imageFile;
    }
    public void setLocation(LatLng location){
        mLocation = location;
    }
    public void setListRequest (List<RequestInfo> listRequest){
        mListRequest = listRequest;
    }
    public void setMiniImage (String image){
        this.mMiniImage = image;
    }
    public void setDataSnaphot (DataSnapshot dataSnaphot){
        mDataSnapshot = dataSnaphot;
    }
    public DataSnapshot getDataSnaphot(){
        return this.mDataSnapshot;
    }
    public String getAuthor(){
        return this.mAuthor;
    }
    public String getImageFile(){
        return mImageFile;
    }
    public String getMiniImage (){
        return mMiniImage;
    }
    public LatLng getLocation(){
        return mLocation;
    }
    public List<RequestInfo> getListRequest (){
        return mListRequest;
    }
}
