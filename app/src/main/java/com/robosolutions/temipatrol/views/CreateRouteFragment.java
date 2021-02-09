package com.robosolutions.temipatrol.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.robosolutions.temipatrol.R;
import com.robosolutions.temipatrol.model.TemiRoute;
import com.robosolutions.temipatrol.temi.TemiController;
import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;

import java.util.ArrayList;


public class CreateRouteFragment extends Fragment {
    private final String TAG = "CreateRouteFragment";
    private RouteAdapter routeAdapter;
    private Button addLocationBtn, saveRouteBtn;
    private EditText routeTitle;
    private RecyclerView routeRv;
    private TemiController temiController;
    private NavController navController;
    private GlobalViewModel viewModel;
    private ArrayList<String> route;

    private Spinner locationSpinner;
    private ArrayAdapter<String> spinnerAdapter;


    public CreateRouteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        temiController = viewModel.getTemiController();
        route = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.create_route_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        routeTitle = view.findViewById(R.id.routeTitle);
        addLocationBtn = view.findViewById(R.id.addLocationBtn);
        addLocationBtn.setOnClickListener(v -> {
            addDestination();
        });

        saveRouteBtn = view.findViewById(R.id.saveRouteBtn);
        saveRouteBtn.setOnClickListener(v -> {
            saveCurrentRoute();
        });

        routeRv = view.findViewById(R.id.routeRv);
        buildRecyclerView();

        ItemTouchHelper myTouchHelper = generateTouchHelper();
        myTouchHelper.attachToRecyclerView(routeRv);

        locationSpinner = view.findViewById(R.id.locationSpinner);
        buildLocationSpinner();

    }

    private void buildRecyclerView() {
        routeAdapter =  new RouteAdapter(route);
        routeRv.setAdapter(routeAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        routeRv.setLayoutManager(mLayoutManager);
        routeRv.setAdapter(routeAdapter);
    }

    private void buildLocationSpinner() {
        spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,
                temiController.getLocationsFromTemi());
        locationSpinner.setAdapter(spinnerAdapter);
    }

    private void addDestination() {
        route.add(locationSpinner.getSelectedItem().toString());
        Log.i(TAG, "route: " + route.toString());
        routeAdapter.notifyItemInserted(route.size());
    }

    private void saveCurrentRoute() {
        TemiRoute temiRoute = new TemiRoute(routeTitle.getText().toString(), route);
        viewModel.insertRouteIntoRepo(temiRoute);
        navController.navigate(R.id.action_createRouteFragment_to_homeFragment);
    }

    private ItemTouchHelper generateTouchHelper() {
        return new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                        ItemTouchHelper.START | ItemTouchHelper.END, 0) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        int from = viewHolder.getAdapterPosition();
                        int to = target.getAdapterPosition();

                        routeAdapter.notifyItemMoved(from, to);

                        return true;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        // Code block for horizontal swipe.
                        // ItemTouchHelper handles horizontal swipe as well, but
                        // it is not relevant with reordering. Ignoring here
                        if (direction == ItemTouchHelper.START) {
                            Log.i(TAG, "Swiped START direction");
                        } else if (direction == ItemTouchHelper.END) {
                            Log.i(TAG, "Swiped END direction");
                        } else {
                            Log.i(TAG, "Else swiped " + direction);
                        }
                    }
                });
    }
}