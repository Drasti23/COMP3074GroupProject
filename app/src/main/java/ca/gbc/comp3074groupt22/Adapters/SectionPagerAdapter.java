package ca.gbc.comp3074groupt22.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

import ca.gbc.comp3074groupt22.SectionFragment;

public class SectionPagerAdapter extends FragmentStateAdapter {

    private final List<String> sectionNames;

    public SectionPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<String> sectionNames) {
        super(fragmentActivity);
        this.sectionNames = sectionNames;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return SectionFragment.newInstance(sectionNames.get(position));
    }

    @Override
    public int getItemCount() {
        return sectionNames.size();
    }
}
