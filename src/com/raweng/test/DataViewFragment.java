package com.raweng.test;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.androidstackscrollview.RootViewController;
import com.raweng.stackscrollview.StackScrollView;

public class DataViewFragment extends ListFragment {
	
	private int mCurCheckPosition = 0;
	private int sId = 0x111;
	
	
	public static int[] color = {Color.BLUE,Color.GRAY,Color.CYAN,Color.RED,Color.GREEN,Color.YELLOW,Color.GRAY,Color.CYAN,Color.RED,Color.BLUE,Color.GRAY};
	public static int numberOfCardOnDeck;
	
	

	public DataViewFragment() {

	}

	/**
	 * Create a new instance of DetailsFragment.
	 */
	public static DataViewFragment newInstance(int index, int previousFragmentId) {
		DataViewFragment f = new DataViewFragment();
		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putInt("index", index);
		args.putInt("prevFragmentId", previousFragmentId);
		f.setArguments(args);
		args = null;
		
		return f;
	}

	public int getShownIndex() {
		return getArguments().getInt("index", 0);
	}
	
	public int getPreviousFragmentId() {
		return getArguments().getInt("prevFragmentId", sId);
	}



	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		int previousFragmentId = getPreviousFragmentId();
		sId = ++previousFragmentId;
		if (savedInstanceState != null) {
			// Restore last state for checked position.
			mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
		}
		// In dual-pane mode, list view highlights selected item.
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		getListView().setCacheColorHint(0);
		// Populate list with static array of titles.
		if(numberOfCardOnDeck % 2 == 0){
			setListAdapter(new MyCustomAdapter(getActivity(), 0, Shakespeare.TITLES,Color.BLACK));
			
		}else{
			setListAdapter(new MyCustomAdapter(getActivity(), 0, Shakespeare.TITLES2Planet,Color.BLACK));
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
		view.setBackgroundColor(Color.WHITE);
		return view;
		
	}


	/**
	 * Helper function to show the details of a selected item, either by
	 * displaying a fragment in-place in the current UI, or starting a
	 * whole new activity in which it is displayed.
	 */
	private void showDetails(int index) {
		mCurCheckPosition  = index;
		// We can display everything in-place with fragments.
		// Have the list highlight this item and show the data.
		getListView().setItemChecked(index, true);
		// Check what fragment is shown, replace if needed.
		DataViewFragment details = (DataViewFragment) getFragmentManager().findFragmentById(sId);
		if (details == null || details.getShownIndex() != index) {			
			// Make new fragment to show this selection.
			details = DataViewFragment.newInstance(index, sId);

			// Execute a transaction, replacing any existing
			// fragment with this one inside the frame.
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(sId, details);
			//ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.commit();
			StackScrollView stackScrollView = ((RootViewController)getActivity()).getStackScrollView();
			stackScrollView.addViewInSlider(details, false);
			ft = null;
			stackScrollView = null;
			
		}
		details = null;
		
	}	
}
