package com.example.bakingapp;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bakingapp.Networking.NetworkOperations;
import com.example.bakingapp.Networking.OnRequestFinishedListener;
import com.example.bakingapp.adapters.RecipesAdapter;
import com.example.bakingapp.listeners.RecyclerViewItemClickListener;
import com.example.bakingapp.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements OnRequestFinishedListener{

    ArrayList<Recipe>recipes;
    @BindView(R.id.recipesList) RecyclerView recycler;
    @BindView(R.id.progressBar) ProgressBar bar;
    @BindView(R.id.reload)
    Button reload;

    int position;

    private CountingIdlingResource mIdlingResource= new CountingIdlingResource("Loading_Data");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(savedInstanceState == null)
        {
            mIdlingResource.increment();
            NetworkOperations.getRecipes(this);
        }


        recycler.addOnItemTouchListener(new RecyclerViewItemClickListener(MainActivity.this, recycler, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent details=new Intent(MainActivity.this,RecipeDetailsActivity.class);

                Bundle bundle=new Bundle();
                bundle.putParcelableArrayList("steps",
                        (ArrayList<? extends Parcelable>) recipes.get(position).getSteps());
                bundle.putParcelableArrayList("ingredients",
                        (ArrayList<? extends Parcelable>) recipes.get(position).getIngredients());
                bundle.putString("recipe_name",recipes.get(position).getName());
                details.putExtra("bundle",bundle);

                startActivity(details);

            }
            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }


    @Override
    public void onFailure(String message) {
        bar.setVisibility(GONE);
        reload.setVisibility(View.VISIBLE);
        Toast.makeText(MainActivity.this, "No internet connection !", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResponse(Response<List<Recipe>> response) {

        recipes = (ArrayList<Recipe>) response.body();

        reload.setVisibility(GONE);
        bar.setVisibility(GONE);

        renderRecyclerView();

        mIdlingResource.decrement();
    }

    public void renderRecyclerView()
    {
        if(recycler.getTag().equals("tablet"))
        {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 3);
            recycler.setLayoutManager(gridLayoutManager);
        }
        else
        {
            LinearLayoutManager gridLayoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false);
            recycler.setLayoutManager(gridLayoutManager);
        }
        recycler.setAdapter(new RecipesAdapter(MainActivity.this,recipes));
        recycler.getLayoutManager().scrollToPosition(position);
    }


    public void reload(View view)
    {
        bar.setVisibility(View.VISIBLE);
        reload.setVisibility(GONE);
        NetworkOperations.getRecipes(this);
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("recipes",recipes);
        if(recycler.getTag().equals("tablet"))
        {
            outState.putInt("position",((GridLayoutManager)recycler.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition());
        }
        else
        {
            outState.putInt("position",((LinearLayoutManager)recycler.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        recipes=savedInstanceState.getParcelableArrayList("recipes");
        position=savedInstanceState.getInt("position");
        renderRecyclerView();
        bar.setVisibility(GONE);
    }

    /**
     * Only called from test, creates and returns a new {@link HomeIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public CountingIdlingResource getIdlingResource() {
        return mIdlingResource;
    }

}