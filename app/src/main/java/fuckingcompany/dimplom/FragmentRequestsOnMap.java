package fuckingcompany.dimplom;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;


public class FragmentRequestsOnMap extends Fragment implements OnMapReadyCallback {
    private SupportMapFragment mMapFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requestsonmap, container, false);
        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.requests_GoogleMap);
        mMapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.clear();
        for (int i=0; i<Singleton.getInstance().getListRequest().size();i++){
            googleMap.addMarker(new MarkerOptions()
                    .position(Singleton.getInstance().getListRequest().get(i).getLocation() )
                    .title(Singleton.getInstance().getListRequest().get(i).getRequestTheme())).showInfoWindow();
        }
    }
}
