package phone.vishnu.mypoembook.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

import phone.vishnu.mypoembook.R;
import phone.vishnu.mypoembook.adapter.ViewPagerFragmentAdapter;
import phone.vishnu.mypoembook.model.CreateOptions;
import phone.vishnu.mypoembook.model.Poem;

import static phone.vishnu.mypoembook.activity.MainActivity.DESCRIPTION_EXTRA;
import static phone.vishnu.mypoembook.activity.MainActivity.TITLE_EXTRA;

public class CreateFragment extends Fragment {

    private static final CreateOptions createOptions = new CreateOptions();

    private final String[] titles = new String[]{"Font", "Background", "Review"};

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

        TabLayout tabLayout = inflate.findViewById(R.id.tabLayout);
        ViewPager2 viewPager = inflate.findViewById(R.id.viewPager);

        Poem poem = new Poem(Objects.requireNonNull(getArguments()).getString(TITLE_EXTRA), Objects.requireNonNull(getArguments()).getString(DESCRIPTION_EXTRA));

        viewPager.setAdapter(new ViewPagerFragmentAdapter(Objects.requireNonNull(getActivity()), poem));

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles[position]);
                tab.setIcon(getIcon(position));
            }
        }).attach();

        return inflate;
    }

    private int getIcon(int position) {
        switch (position) {
            case 0:
                return R.drawable.ic_font;
            case 1:
                return R.drawable.ic_comment;
            case 2:
                return R.drawable.ic_check;
        }
        return R.drawable.ic_font;
    }
}