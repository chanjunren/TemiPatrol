package com.robosolutions.temipatrol.views;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.fangxu.allangleexpandablebutton.AllAngleExpandableButton;
import com.fangxu.allangleexpandablebutton.ButtonData;
import com.fangxu.allangleexpandablebutton.ButtonEventListener;
import com.robosolutions.temipatrol.R;
import com.robosolutions.temipatrol.model.TemiRoute;
import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class HomeFragment extends Fragment implements RouteAdapter.OnRouteClickListener {
    private final String TAG = "HomeFragment";

    private final int ICON_PADDING = 30;
    private final int ADD_ROUTE_INDEX = 1;
    private final int CONF_PAGE_INDEX = 2;
    private final int SIGN_OUT_INDEX = 3;

    private GlobalViewModel viewModel;
    private NavController navController;
    private RecyclerView routeRv;
    private RouteAdapter routeAdapter;
    private HashMap<String, TemiRoute> routeMap;
    private ArrayList<TemiRoute> routes;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        routeMap = new HashMap<>();
        routes = new ArrayList<>();
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        routeRv = view.findViewById(R.id.routeRv);
        installMenuButton(view);
        initializeRecylerView();
        attachLiveDataToRecyclerView();
    }

    // Menu Button >
    // Add Route Button > Configure Button > Sign Out Button
    private void installMenuButton(View view) {
        final AllAngleExpandableButton button = (AllAngleExpandableButton)
                view.findViewById(R.id.homeMenuButton);
        final List<ButtonData> buttonDatas = new ArrayList<>();
        buttonDatas.add(buildDropdownButton());
        buttonDatas.add(buildAddRouteButton());
        buttonDatas.add(buildConfigureButton());
        buttonDatas.add(buildSignOutButton());
        button.setButtonDatas(buttonDatas);
        setListener(button);
    }

    private ButtonData buildDropdownButton() {
        int drawable = R.drawable.ic_drop_down_menu;
        ButtonData buttonData = ButtonData.buildIconButton(getContext(), drawable, ICON_PADDING);
        buttonData.setBackgroundColorId(getContext(), R.color.black);
        return buttonData;
    }

    private ButtonData buildAddRouteButton() {
        int drawable = R.drawable.ic_add_route_icon;
        ButtonData buttonData = ButtonData.buildIconButton(getContext(), drawable, ICON_PADDING);
        buttonData.setBackgroundColorId(getContext(), R.color.red);
        return buttonData;
    }

    private ButtonData buildConfigureButton() {
        int drawable = R.drawable.ic_configure_icon;
        ButtonData buttonData = ButtonData.buildIconButton(getContext(), drawable, ICON_PADDING);
        buttonData.setBackgroundColorId(getContext(), R.color.blue);
        return buttonData;
    }

    private ButtonData buildSignOutButton() {
        int drawable = R.drawable.ic_log_out_icon;
        ButtonData buttonData = ButtonData.buildIconButton(getContext(), drawable, ICON_PADDING);
        buttonData.setBackgroundColorId(getContext(), R.color.yellow);
        return buttonData;
    }


    private void initializeRecylerView() {
        Log.i(TAG, "buildRecyclerView called");
        routeAdapter =  new RouteAdapter(routes, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        routeRv.setAdapter(routeAdapter);
        routeRv.setLayoutManager(mLayoutManager);
    }

    private void attachLiveDataToRecyclerView() {
        final Observer<List<TemiRoute>> routeListObserver = temiRoutes -> {
            Log.i(TAG, "onChanged called");
            routeMap.clear();
            for (TemiRoute route: temiRoutes) {
                routeMap.put(route.getRouteTitle(), route);
            }
            routes.clear();
            routes.addAll(temiRoutes);
            routeAdapter.notifyDataSetChanged();
        };
        viewModel.getRouteLiveDataFromRepo().observe(getActivity(), routeListObserver);
    }

    @Override
    public void onRouteClick(int position) {
        TemiRoute selectedRoute = routes.get(position);
        viewModel.setSelectedRoute(selectedRoute);
        navController.navigate(R.id.action_homeFragment_to_patrolFragment);
    }

    @Override
    public void onRouteLongClick(int position) {
        Toast.makeText(getContext(), "Hi im a long click", Toast.LENGTH_SHORT).show();
    }

    private void setListener(AllAngleExpandableButton button) {
        button.setButtonEventListener(new ButtonEventListener() {
            @Override
            public void onButtonClicked(int index) {
                if (index == ADD_ROUTE_INDEX) {
                    navController.navigate(R.id.action_homeFragment_to_createRouteFragment);
                } else if (index == CONF_PAGE_INDEX) {
                    navController.navigate(R.id.action_homeFragment_to_configureFragment);
                } else if (index == SIGN_OUT_INDEX) {

                }
            }

            @Override
            public void onExpand() {
//                Toast.makeText(getContext(), "Expanded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCollapse() {
//                showToast("onCollapse");
//                Toast.makeText(getContext(), "Collapsed", Toast.LENGTH_SHORT).show();

            }
        });
    }
}