package com.example.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.bakingapp.fragments.StepDetailsFragment;

public class StepDetailsActivity extends AppCompatActivity {

    StepDetailsFragment detailsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        if(savedInstanceState == null)
        {
            Bundle bundle = getIntent().getExtras();

            if(bundle.containsKey("name"))
            {
                getSupportActionBar().setTitle(bundle.getString("name")+" Steps");
            }
            bundle.putBoolean("tablet",false);

            detailsFragment = new StepDetailsFragment();
            detailsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detailsFragment, detailsFragment)
                    .commit();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getSupportFragmentManager().putFragment(outState,"fragment",detailsFragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        detailsFragment = (StepDetailsFragment) getSupportFragmentManager().getFragment(savedInstanceState,"fragment");
        if(detailsFragment.isAdded())
        {
            return;
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.detailsFragment, detailsFragment)
                .commit();
    }

}
