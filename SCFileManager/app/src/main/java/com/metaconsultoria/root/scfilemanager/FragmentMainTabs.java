package com.metaconsultoria.root.scfilemanager;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentMainTabs extends Fragment
        implements TabLayout.OnTabSelectedListener{

    private ViewPager mViewPager;
    private TabLayout mTabLayout;


    public FragmentMainTabs() {
        // Required empty public constructor
    }


    public static FragmentMainTabs newInstance() {
        FragmentMainTabs fragment = new FragmentMainTabs();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_fragment_main_tabs, container, false);

        mViewPager= (ViewPager) view.findViewById(R.id.viewPager_locate);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(new MyTabAdapter(getActivity(),getContext(),getChildFragmentManager()));

        mTabLayout= (TabLayout) view.findViewById(R.id.tabs_locate);
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_1_title));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_2_title));

        mTabLayout.addOnTabSelectedListener(this);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        getActivity().findViewById(R.id.floatingActionButton).setVisibility(View.INVISIBLE);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    //interface das tabs
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int i=tab.getPosition();
        if(i==0){getActivity().findViewById(R.id.floatingActionButton).setVisibility(View.INVISIBLE);}
        else{getActivity().findViewById(R.id.floatingActionButton).setVisibility(View.VISIBLE);}
        mViewPager.setCurrentItem(i);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
