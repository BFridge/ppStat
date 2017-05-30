package fridge.com.hilistview.welcome;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fridge.com.hilistview.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class WelComeActivityFragment extends Fragment {

    public WelComeActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wel_come, container, false);
    }
}
