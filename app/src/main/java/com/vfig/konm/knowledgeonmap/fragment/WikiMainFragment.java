package com.vfig.konm.knowledgeonmap.fragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vfig.konm.knowledgeonmap.R;
import com.vfig.konm.knowledgeonmap.activity.MapsMainActivity;
import com.vfig.konm.knowledgeonmap.adapter.WikiAdapter;
import com.vfig.konm.knowledgeonmap.parser.beans.WikiPageDetail;
import com.vfig.konm.knowledgeonmap.utils.NavigationUtils;

public class WikiMainFragment extends Fragment {

    public static WikiMainFragment newInstance() {

        Bundle args = new Bundle();

        WikiMainFragment fragment = new WikiMainFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        final View inflateView = inflater.inflate(R.layout.fragment_main, container, false);
        RecyclerView recyclerViewMain = inflateView.findViewById(R.id.recycler_view_main);


        WikiAdapter mAdapter = new WikiAdapter(getActivity(), new WikiAdapter.OnViewHolderClick() {
            @Override
            public void onClick(View view, int position, Object item) {

                final String title = ((WikiPageDetail) item).getTitle();
                if (title != null) {
                    NavigationUtils.openWebPage(getActivity(), title);
                }
            }
        });


        mAdapter.setList(((MapsMainActivity) getActivity()).wikiPageDetails);

        recyclerViewMain.setAdapter(mAdapter);

        return inflateView;
    }


}
