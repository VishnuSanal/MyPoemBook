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

import phone.vishnu.mypoembook.R;
import phone.vishnu.mypoembook.adapter.ViewPagerFragmentAdapter;

public class CreateFragment extends Fragment {

    private final String[] titles = new String[]{"Font", "Background", "Card Colour", "Review"};

    public CreateFragment() {
    }

    public static CreateFragment newInstance() {
        return new CreateFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_create, container, false);

        TabLayout tabLayout = inflate.findViewById(R.id.tabLayout);
        ViewPager2 viewPager = inflate.findViewById(R.id.viewPager);

        viewPager.setAdapter(new ViewPagerFragmentAdapter(requireActivity()));

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
                return R.drawable.ic_color_lens;
            case 3:
                return R.drawable.ic_check;
        }
        return R.drawable.ic_font;
    }
}