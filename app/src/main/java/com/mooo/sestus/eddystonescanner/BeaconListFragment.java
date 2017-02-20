package com.mooo.sestus.eddystonescanner;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class BeaconListFragment extends Fragment {

    private BeaconListPresenter beaconListPresenter;
    private BeaconsAdapter beaconsAdapter;
    private RecyclerView rv_users;
    private RelativeLayout rl_progress;
    private RelativeLayout rl_retry;
    private Button bt_retry;

    public BeaconListFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.beaconListPresenter = new BeaconListPresenter();
        this.beaconsAdapter = new BeaconsAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_beacon_list, container, false);
        rv_users = (RecyclerView) fragmentView.findViewById(R.id.rv_users);
        rl_progress = (RelativeLayout) fragmentView.findViewById(R.id.rl_progress);
        rl_retry = (RelativeLayout) fragmentView.findViewById(R.id.rl_retry);
        bt_retry = (Button) fragmentView.findViewById(R.id.bt_retry);
        setupRecyclerView();
        return fragmentView;
    }

    private void setupRecyclerView() {
        this.rv_users.setLayoutManager(new BeaconsLayoutManager(this.getActivity().getApplicationContext()));
        this.rv_users.setAdapter(beaconsAdapter);
    }
}
