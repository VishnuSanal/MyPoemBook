package phone.vishnu.mypoembook.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import phone.vishnu.mypoembook.R;
import phone.vishnu.mypoembook.model.Poem;

import static phone.vishnu.mypoembook.activity.MainActivity.DESCRIPTION_EXTRA;
import static phone.vishnu.mypoembook.activity.MainActivity.TITLE_EXTRA;

public class CreateFragment extends Fragment {

    public CreateFragment() {
    }

    public static CreateFragment newInstance(Poem poem) {
        Bundle args = new Bundle();
        args.putString(TITLE_EXTRA, poem.getTitle());
        args.putString(DESCRIPTION_EXTRA, poem.getDescription());
        CreateFragment fragment = new CreateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_create, container, false);
        return inflate;
    }
}