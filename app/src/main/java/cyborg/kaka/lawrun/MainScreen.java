package cyborg.kaka.lawrun;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import cyborg.kaka.lawrun.Fragments.About;
import cyborg.kaka.lawrun.Fragments.FragmentsPagerAdapter;
import cyborg.kaka.lawrun.Fragments.Kernel;
import cyborg.kaka.lawrun.Fragments.Profiles;
import cyborg.kaka.lawrun.Fragments.Stats;
import cyborg.kaka.lawrun.databinding.ScreenMainBinding;

public class MainScreen extends AppCompatActivity {

    int themeColor;
    private ScreenMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ScreenMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.addPages();

        // Tab Switch OnClick
        binding.tabs.tabProfiles.setOnClickListener(v -> binding.mainViewPager.setCurrentItem(0));
        binding.tabs.tabStats.setOnClickListener(v -> binding.mainViewPager.setCurrentItem(1));
        binding.tabs.tabKernel.setOnClickListener(v -> binding.mainViewPager.setCurrentItem(2));

        // Heart Icon Click
        binding.tabs.tabHeart.setOnClickListener(v -> binding.mainViewPager.setCurrentItem(3));

        // Apply Colors To Tab Texts After Tab Change
        binding.mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                resetColors();
                switch (position) {
                    case 0:
                        binding.tabs.tabProfiles.setTextColor(themeColor);
                        binding.tabs.tabIndicator.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        binding.tabs.tabStats.setTextColor(themeColor);
                        binding.tabs.tabIndicatorStats.setVisibility(View.VISIBLE);
                        binding.tabs.tabIndicatorStats.setBackgroundColor(themeColor);
                        break;
                    case 2:
                        binding.tabs.tabKernel.setTextColor(themeColor);
                        binding.tabs.tabIndicatorKernel.setVisibility(View.VISIBLE);
                        binding.tabs.tabIndicatorKernel.setBackgroundColor(themeColor);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    // Reset To Default Colors
    private void resetColors() {
        themeColor = binding.tabs.tabColorHolder.getTextColors().getDefaultColor();

        binding.tabs.tabProfiles.setTextColor(getColor(R.color.text_dark));
        binding.tabs.tabIndicator.setVisibility(View.INVISIBLE);
        binding.tabs.tabStats.setTextColor(getColor(R.color.text_dark));
        binding.tabs.tabIndicatorStats.setVisibility(View.INVISIBLE);
        binding.tabs.tabKernel.setTextColor(getColor(R.color.text_dark));
        binding.tabs.tabIndicatorKernel.setVisibility(View.INVISIBLE);
    }

    // Add Pages To Viewpager
    private void addPages() {
        FragmentsPagerAdapter pagerAdapter = new FragmentsPagerAdapter(this.getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pagerAdapter.addFragment(new Profiles());
        pagerAdapter.addFragment(new Stats());
        pagerAdapter.addFragment(new Kernel());
        pagerAdapter.addFragment(new About());

        // Stop Refreshing Tabs
        binding.mainViewPager.setOffscreenPageLimit(3);

        // Set Adapter
        binding.mainViewPager.setAdapter(pagerAdapter);
    }
}