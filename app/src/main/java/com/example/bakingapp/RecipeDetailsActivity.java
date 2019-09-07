package com.example.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.bakingapp.fragments.StepDetailsFragment;
import com.example.bakingapp.fragments.StepFragment;
import com.example.bakingapp.listeners.FragmentOneListener;
import com.example.bakingapp.models.Step;

import java.util.ArrayList;

public class RecipeDetailsActivity extends AppCompatActivity implements FragmentOneListener {

    FrameLayout detailFragment;
    boolean Tablet;
    private ArrayList<Step> steps;

    String name;

    StepDetailsFragment detailsFragment;

    StepFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        detailFragment = (FrameLayout) findViewById(R.id.fragmentTwo);
        Tablet = true;
        Bundle extras = getIntent().getBundleExtra("bundle");
        name = extras.getString("recipe_name");
        getSupportActionBar().setTitle(name);
        steps = extras.getParcelableArrayList("steps");
        extras.putBoolean("tablet", (detailFragment != null));

        if (savedInstanceState == null) {
            fragment = new StepFragment();
            fragment.setFragmentListener(this);
            fragment.setArguments(extras);
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentOne, fragment).commit();
            //checking if screen size greater than 600dp
            if (detailFragment == null) {
                Tablet = false;
            } else {
                this.setStep(0, steps);
            }
        } else {
            fragment= (StepFragment) getSupportFragmentManager().getFragment(savedInstanceState,"main");
            fragment.setFragmentListener(this);


            if (!fragment.isAdded())
                getSupportFragmentManager().beginTransaction().add(R.id.fragmentOne, fragment).commit();

            if(detailsFragment !=null)
            {
                detailsFragment= (StepDetailsFragment) getSupportFragmentManager().getFragment(savedInstanceState,"detail");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentTwo, detailsFragment).commit();
            }
        }




    }

    @Override
    public void setStep(int index, ArrayList<Step> steps) {
        if (!Tablet) {
            Intent intent = new Intent(this, StepDetailsActivity.class);
            intent.putExtra("steps", steps);
            intent.putExtra("current", index);
            intent.putExtra("name", name);
            startActivity(intent);
        } else {
            detailsFragment = new StepDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("steps", steps);
            detailsFragment.setFragmentListener(this);
            bundle.putInt("current", index);
            bundle.putBoolean("tablet", true);
            detailsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentTwo, detailsFragment).commit();
        }
    }

    @Override
    public void setCurrent(int index) {
        if (Tablet) {
            fragment.updateView(index);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "main", fragment);

        if (Tablet && detailFragment!=null)
        {
            try{
                getSupportFragmentManager().putFragment(outState, "detail", detailsFragment);
            }catch (NullPointerException e) {}

        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        if (detailFragment == null) {
            Tablet = false;
        }
    }
}
