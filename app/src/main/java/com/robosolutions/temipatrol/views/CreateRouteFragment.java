package com.robosolutions.temipatrol.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.material.snackbar.Snackbar;
import com.ramotion.fluidslider.FluidSlider;
import com.robosolutions.temipatrol.R;
import com.robosolutions.temipatrol.model.TemiRoute;
import com.robosolutions.temipatrol.temi.TemiSpeaker;
import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;

import java.util.ArrayList;

import kotlin.Unit;


public class CreateRouteFragment extends Fragment {
    private final String TAG = "CreateRouteFragment";
    private CreateRouteAdapter createRouteAdapter;
    private LocationsAdapter locationsAdapter;
    private CardView saveRouteBtn;
    private EditText routeTitle;
    private RecyclerView createRouteRv, locationsRv;
    private TemiSpeaker temiSpeaker;
    private NavController navController;
    private GlobalViewModel viewModel;
    private FluidSlider slider;
    private FrameLayout parentLayout;
    private int patrolCount;
    private ConstraintLayout exitBtn;

    private String deletedDestination;

    public CreateRouteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        temiSpeaker = new TemiSpeaker(viewModel.getTemiRobot());
        deletedDestination = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.create_route_fragment, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        Animation topAnim = AnimationUtils.loadAnimation(getContext(), R.anim.top_animation);
        CardView pageLogo = view.findViewById(R.id.createRoutePageLogo);
        pageLogo.setAnimation(topAnim);

        parentLayout = view.findViewById(R.id.createRouteFrameLayout);
        parentLayout.setOnTouchListener((v, motionEvent) -> {
            hideKeyboard(v);
            return true;
        });

        routeTitle = view.findViewById(R.id.routeTitle);
        if (viewModel.isRouteUpdate()) {
            routeTitle.setText(viewModel.getUpdatingRoute().getRouteTitle());
        }

        saveRouteBtn = view.findViewById(R.id.saveRouteBtn);
        saveRouteBtn.setOnClickListener(v -> {
            saveCurrentRoute();
        });

        createRouteRv = view.findViewById(R.id.createRouteRv);
        buildRouteRecyclerView();

        ItemTouchHelper myTouchHelper = generateTouchHelper();
        myTouchHelper.attachToRecyclerView(createRouteRv);

        locationsRv = view.findViewById(R.id.locationsRv);
        buildLocationRecyclerView();

        slider = view.findViewById(R.id.patrolCountSlider);
        buildSlider();

        exitBtn = view.findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(v -> {
            navController.navigate(R.id.action_createRouteFragment_to_homeFragment);
        });

    }

    private void buildRouteRecyclerView() {
        createRouteAdapter =  new CreateRouteAdapter(viewModel.getCreateRouteHelperList());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);

        createRouteRv.setLayoutManager(mLayoutManager);
        createRouteRv.setAdapter(createRouteAdapter);
    }

    private void buildLocationRecyclerView() {
        locationsAdapter = new LocationsAdapter(temiSpeaker.getLocationsFromTemi(), viewModel, createRouteAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);

        locationsRv.setLayoutManager(mLayoutManager);
        locationsRv.setAdapter(locationsAdapter);
    }

    private void buildSlider() {
        final int max = 20;
        final int min = 1;
        final int total = max - min;

        slider.setPositionListener(pos -> {
            final String value = String.valueOf( (int)(min + total * pos) );
            slider.setBubbleText(value);
            patrolCount = Integer.parseInt(value);
            return Unit.INSTANCE;
        });


        slider.setPosition(0f);
        slider.setStartText(String.valueOf(min));
        slider.setEndText(String.valueOf(max));

    }

    private void saveCurrentRoute() {
        if (viewModel.isRouteUpdate()) {
            TemiRoute updatingRoute = viewModel.getUpdatingRoute();
            updatingRoute.setRouteTitle(routeTitle.getText().toString());
            updatingRoute.setDestinations(viewModel.getCreateRouteHelperList());
            updatingRoute.setPatrolCount(patrolCount);
            Log.i(TAG, "Route updated: " + updatingRoute.toString());
            viewModel.updateRouteInRepo(updatingRoute);
        } else { // New route being created
            TemiRoute temiRoute = new TemiRoute(routeTitle.getText().toString(),
                    viewModel.getCreateRouteHelperList(), patrolCount);
            Log.i(TAG, "route added: " + temiRoute.toString());
            viewModel.insertRouteIntoRepo(temiRoute);
        }
        navController.navigate(R.id.action_createRouteFragment_to_homeFragment);
    }

    private ItemTouchHelper generateTouchHelper() {
        return new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.START | ItemTouchHelper.END,
                        ItemTouchHelper.UP | ItemTouchHelper.DOWN) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        int from = viewHolder.getAdapterPosition();
                        int to = target.getAdapterPosition();

                        createRouteAdapter.notifyItemMoved(from, to);

                        return true;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        ArrayList<String> route = viewModel.getCreateRouteHelperList();
                        deletedDestination = route.get(position);
                        route.remove(position);
                        createRouteAdapter.notifyItemRemoved(position);

                        Snackbar.make(createRouteRv, "Deleted " + deletedDestination, Snackbar.LENGTH_LONG)
                                .setAction("Undo", v -> {
                                    route.add(position, deletedDestination);
                                    createRouteAdapter.notifyItemInserted(position);
                                }).show();
                    }

                    // 1. This callback is called when a ViewHolder is selected.
                    //    We highlight the ViewHolder here.
                    @Override
                    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                        super.onSelectedChanged(viewHolder, actionState);
                        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                            viewHolder.itemView.setAlpha(0.5f);
                        }
                    }

                    // 2. This callback is called when the ViewHolder is
                    //    unselected (dropped). We unhighlight the ViewHolder here.
                    @Override
                    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                        super.clearView(recyclerView, viewHolder);
                        viewHolder.itemView.setAlpha(1.0f);
                    }

                    @Override
                    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                            @NonNull RecyclerView.ViewHolder viewHolder,
                                            float dX, float dY, int actionState,
                                            boolean isCurrentlyActive) {
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    }
                });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}