package com.robosolutions.temipatrol.views;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.robosolutions.temipatrol.R;
import com.robosolutions.temipatrol.client.JsonConnectionTests;
import com.robosolutions.temipatrol.client.JsonPostman;
import com.robosolutions.temipatrol.model.ConfigurationEnum;
import com.robosolutions.temipatrol.model.TemiConfiguration;
import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class TestDialogFragment extends DialogFragment {
    private static final String TAG = "TestDialogFragment";
    private GlobalViewModel viewModel;

    private String serverIp;
    private ProgressBar progressBar;
    private TextView dialogHeader;
    private ImageView resultImage;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.test_dialog_fragment, null);
        viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        initConfigurations();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
            .setNegativeButton("Stop", (dialog, id) -> {
                dialog.dismiss();
                // User cancelled the dialog
            });

        progressBar = view.findViewById(R.id.progressSpinner);
        dialogHeader = view.findViewById(R.id.dialogHeader);
        resultImage = view.findViewById(R.id.testResultImg);

        testConnection();

        return builder.create();
    }

    private void initConfigurations() {
        final Observer<List<TemiConfiguration>> configListener = configs -> {
            for (TemiConfiguration config: configs) {
                if (config.getKey() == ConfigurationEnum.SERVER_IP_ADD) {
                    serverIp = config.getValue();
                }
            }
        };
        viewModel.getConfigurationLiveDataFromRepo().observe(getActivity(), configListener);
    }

    private void testConnection() {
        String TEST_TAG = "CONNECTION_TEST";
        boolean errorEncountered = false;
        try {
            Log.i(TEST_TAG, "Testing tag");
            JsonPostman jsonPostman = new JsonPostman(serverIp);
            JSONObject humanDistanceMsg = new JSONObject(JsonConnectionTests.HUMAN_DIST_REQ);
            JSONObject maskDetectionMsg = new JSONObject(JsonConnectionTests.MASK_DETECTION_REQ);
//            JSONObject humanDetectionMsg = new JSONObject(JsonConnectionTests.HUMAN_DETECTION_REQ);
            boolean test1 = sendImageToServerAndGetHumanDistanceResult(jsonPostman, humanDistanceMsg);
            boolean test2 = sendImageToServerAndGetMaskDetectionResult(jsonPostman, maskDetectionMsg);
        } catch (Exception e) {
            Log.e(TEST_TAG, "ERROR: " + e.toString());
            errorEncountered = true;
//            Toast.makeText(getContext(), "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
            showConnectionFailure();
        } finally {
            if (!errorEncountered) {
//                Toast.makeText(getContext(), "Connection Success!", Toast.LENGTH_SHORT).show();
                showConnectionSuccess();
            }
        }
    }

    private Boolean sendImageToServerAndGetMaskDetectionResult(JsonPostman postman, JSONObject maskDetectionReq)
            throws ExecutionException, InterruptedException {
        // for testing
        Future<Boolean> result = viewModel.getExecutorService().submit(() ->
                postman.testMaskDetectionConnection(maskDetectionReq));
        return result.get();
    }

    private Boolean sendImageToServerAndGetHumanDistanceResult(JsonPostman postman, JSONObject humanDistReq)
            throws ExecutionException, InterruptedException {
        // for testing
        Future<Boolean> result = viewModel.getExecutorService().submit(() ->
                postman.testHumanDistanceConnection(humanDistReq));
        return result.get();
    }

    private void showConnectionSuccess() {
        progressBar.setVisibility(View.GONE);
        resultImage.setVisibility(View.VISIBLE);
        resultImage.setImageResource(R.drawable.ic_success_icon);

        dialogHeader.setText("Connection Success");
    }

    private void showConnectionFailure() {
        progressBar.setVisibility(View.GONE);
        resultImage.setVisibility(View.VISIBLE);
        resultImage.setImageResource(R.drawable.ic_fail_icon);
        dialogHeader.setText("Failed To Connect");
    }
}