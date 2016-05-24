package fuckingcompany.dimplom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mykhail on 08.05.16.
 */
public class FragmentRequests extends Fragment   {


    private List<RequestInfo> mListRequests;
    private RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;



    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
            mListRequests =Singleton.getInstance().getListRequest();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requests, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.requests_recyclerview);
        mLayoutManager = new LinearLayoutManager(getActivity());
        RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(mListRequests, getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(rcAdapter);
        return view;
    }




}
