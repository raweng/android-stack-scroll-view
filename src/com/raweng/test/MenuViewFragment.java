package com.raweng.test;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.raweng.stackscrollview.StackScrollView;

public class MenuViewFragment extends ListFragment {

	private boolean mDualPane;
	private int mCurCheckPosition = 0;
	private int itemDetailsId;
	private static final int mId = 0x001;
	
	
	public MenuViewFragment(int itemDetailsId) {
		this.itemDetailsId = itemDetailsId;
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
				
		// Populate list with static array of titles.
		setListAdapter(new MyCustomAdapter(getActivity(), 0, Shakespeare.TITLES,Color.WHITE));

		// Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
		View data = getActivity().findViewById(itemDetailsId);
		
		mDualPane = data != null && data.getVisibility() == View.VISIBLE;
		mDualPane = true;
		data = null;		
		
		
		
		if (savedInstanceState != null) {
			// Restore last state for checked position.
			mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
		}
		
		
		if (mDualPane) {
			// In dual-pane mode, list view highlights selected item.
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Make sure our UI is in the correct state.
        	showDetails(mCurCheckPosition);
		}
	}
	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("curChoice", mCurCheckPosition);
	}
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		showDetails(position);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		return view;	
	}


	/**
     * Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a
     * whole new activity in which it is displayed.
     */
	private void showDetails(int index) {
		mCurCheckPosition = index;
		
		if (mDualPane) {
			// We can display everything in-place with fragments.
            // Have the list highlight this item and show the data.
			getListView().setItemChecked(index, true);
			getListView().setCacheColorHint(0);
			// Check what fragment is shown, replace if needed.
			DataViewFragment details = (DataViewFragment) getFragmentManager().findFragmentById(mId);			
			if (details == null || details.getShownIndex() != index) {
                // Make new fragment to show this selection.
                details = DataViewFragment.newInstance(index, mId);
                // Execute a transaction, replacing any existing
                // fragment with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(mId, details);
             
                ft.commit();
                StackScrollView stackScrollView = ((RootViewController)getActivity()).getStackScrollView();
                stackScrollView.addViewInSlider(details, true);
			}
			details = null;
			
		} else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
            /*Intent intent = new Intent();
            intent.setClass(getActivity(), DetailsActivity.class);
            intent.putExtra("index", index);
            startActivity(intent);*/
        }
	}
}
