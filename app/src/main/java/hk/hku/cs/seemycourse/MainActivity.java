package hk.hku.cs.seemycourse;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * Main Activity of the App
 */
public class MainActivity extends AppCompatActivity {

    private Fragment[] fragments;
    private static final int FRAGMENT_RECOGNIZE  = 0;
    private static final int FRAGMENT_PRODUCTION = 1;
    private static final int FRAGMENT_HISTORY    = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLayout();
    }

    /**
     * Initialize Layout
     */
    private void initLayout() {
        // SetUp Fragments Container
        fragments = new Fragment[3];

        // Setup Bottom Navigation
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.recognize:
                        switchFragment(FRAGMENT_RECOGNIZE);
                        break;
                    case R.id.make:
                        switchFragment(FRAGMENT_PRODUCTION);
                        break;
                    case R.id.history:
                        switchFragment(FRAGMENT_HISTORY);
                        break;
                    default: return false;
                }
                return true;
            }
        });

        switchFragment(FRAGMENT_RECOGNIZE);
    }

    /**
     * Switch between fragments in the main activity
     * @param index index of fragment
     */
    private void switchFragment(int index) {
        // Check whether the fragment is loaded otherwise create it
        loadFragment(index);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, fragments[index])
                .commit();
    }

    /**
     * Check whether the fragment is loaded or not
     * Create it if not loaded
     * @param index index of fragment
     */
    private void loadFragment(int index) {
        if (fragments[index] != null) return;
        switch (index) {
            case FRAGMENT_RECOGNIZE:
                fragments[index] = new RecognizeFragment();
                break;
            case FRAGMENT_PRODUCTION:
                fragments[index] = new ProductionFragment();
                break;
            case FRAGMENT_HISTORY:
                fragments[index] = new GameFragment();
                break;
            default: break;
        }
    }
}
