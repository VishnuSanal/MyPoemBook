package phone.vishnu.mypoembook.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import phone.vishnu.mypoembook.fragment.BackgroundFragment;
import phone.vishnu.mypoembook.fragment.ColorFragment;
import phone.vishnu.mypoembook.fragment.FontFragment;
import phone.vishnu.mypoembook.fragment.ReviewFragment;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return FontFragment.newInstance();
            case 1:
                return BackgroundFragment.newInstance();
            case 2:
                return ColorFragment.newInstance();
            case 3:
                return ReviewFragment.newInstance();
        }
        return FontFragment.newInstance();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}