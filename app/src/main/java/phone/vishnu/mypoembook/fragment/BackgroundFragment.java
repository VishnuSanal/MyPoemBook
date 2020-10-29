package phone.vishnu.mypoembook.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import phone.vishnu.mypoembook.R;

public class BackgroundFragment extends Fragment {

    public BackgroundFragment() {
    }

    public static BackgroundFragment newInstance() {
        return new BackgroundFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_background, container, false);
        return inflate;
    }
}