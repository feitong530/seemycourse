package hk.hku.cs.seemycourse;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigationView = null;
    private FrameLayout frameLayout = null;

    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLayout();
    }

    private void initLayout() {
        // setUp FrameLayout
        frameLayout = findViewById(R.id.frameLayout);
        fragments = new ArrayList<>();
        fragments.add(new RecognizeFragment());
        fragments.add(new ProductionFragment());
        fragments.add(new HistoryFragment());



        // Setup Bottom Navigation
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.recognize:
                        // TODO: switch page
                        switchFragment(0);
                        break;
                    case R.id.make:
                        switchFragment(1);
                        break;
                    case R.id.history:
                        switchFragment(2);
                        break;
                    default: return false;
                }
                return true;
            }
        });

        switchFragment(0);
    }

    private void switchFragment(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, fragments.get(index))
                .commit();
    }

}
