package fuckingcompany.dimplom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;


public class DialogFragmentChooseSource extends DialogFragment implements View.OnClickListener {
    private ImageButton mButtonGallery;
    private ImageButton mButtonCamera;

    private View mView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        mView = layoutInflater.inflate(R.layout.fragment_choosesource, null);
        mButtonGallery = (ImageButton) mView.findViewById(R.id.button_fromgallery);
        mButtonCamera = (ImageButton)mView.findViewById(R.id.button_fromcamera);
        mButtonGallery.setOnClickListener(this);
        mButtonCamera.setOnClickListener(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setMessage(getResources().getString(R.string.source))
                .setView(mView);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_fromgallery:{
                startChoosenActivity(true);
                getDialog().dismiss();
                break;
            }
            case R.id.button_fromcamera:{
                startChoosenActivity(false);
                getDialog().dismiss();
                break;
            }
        }
    }
    private void startChoosenActivity(boolean isGallery){
        Intent intent = new Intent(getActivity(),ActivityStepTwoImage.class);
        intent.putExtra(Constans.isGallery,isGallery);
        startActivity(intent);
    }
}
