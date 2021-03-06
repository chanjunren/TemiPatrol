package com.robosolutions.temipatrol.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.robosolutions.temipatrol.R;
import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;

public class SignInFragment extends Fragment {
    private GoogleSignInClient mGoogleSignInClient;
    private final String TAG = "SignInFragment";
    private NavController navController;
    private GlobalViewModel viewModel;

    private Animation topAnim, bottomAnim;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        topAnim = AnimationUtils.loadAnimation(getContext(), R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_animation);
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sign_in_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider((getActivity())).get(GlobalViewModel.class);

        ImageView temiPatrolLogo = view.findViewById(R.id.temiPatrolLogo);
        TextView appTitle = view.findViewById(R.id.temiPatrolTitle);
        TextView appDescription = view.findViewById(R.id.temiPatrolDescription);
        temiPatrolLogo.setAnimation(topAnim);
        appTitle.setAnimation(bottomAnim);
        appDescription.setAnimation(bottomAnim);

        CardView signInBtn = view.findViewById(R.id.signInBtn);
        signInBtn.setAnimation(bottomAnim);
        signInBtn.setOnClickListener(v -> {
            signIn();
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("Ok", (dialog, id) -> {
            dialog.dismiss();
        }).setView(R.layout.reminder_dialog);
        builder.create().show();

        Scope WRITE_SCOPE = new Scope("https://www.googleapis.com/auth/drive.file");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(WRITE_SCOPE)
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Log.i(TAG, "Sign in success!");
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        handleSignInResult(task);
                    } else {
                        Log.i(TAG, "Launcher failed, please remember to add your device's " +
                                "Oauth token to the Google Developer Console ");
                    }
                }
        );
        signInLauncher.launch(signInIntent);

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            viewModel.initializeGoogleServices(account, getContext());
            // Signed in successfully, show authenticated UI.
            navController.navigate(R.id.action_signInFragment_to_homeFragment);
//            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
        }
    }
}