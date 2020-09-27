package phone.vishnu.mypoembook.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import phone.vishnu.mypoembook.fragment.CardFragment;
import phone.vishnu.mypoembook.fragment.FontFragment;
import phone.vishnu.mypoembook.fragment.ReviewFragment;
import phone.vishnu.mypoembook.model.Poem;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    private Poem poem;

    public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity, Poem poem) {
        super(fragmentActivity);
        this.poem = poem;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return FontFragment.newInstance();
            case 1:
                return CardFragment.newInstance();
            case 2:
                return ReviewFragment.newInstance();
        }
        return new FontFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
