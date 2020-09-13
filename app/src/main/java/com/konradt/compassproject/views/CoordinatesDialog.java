package com.konradt.compassproject.views;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.konradt.compassproject.R;
import com.konradt.compassproject.contracts.CoordinatesDialogContract;
import com.konradt.compassproject.presenters.CoordinatesDialogPresenter;

import org.jetbrains.annotations.NotNull;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class CoordinatesDialog extends DialogFragment implements DialogInterface.OnClickListener,
        CoordinatesDialogContract.View {

    private static final String KEY_TITLE = "title";

    private EditText etLatitude;
    private EditText etLongitude;

    private CoordinatesDialogContract.Presenter presenter;

    public CoordinatesDialog() {

    }

    public static CoordinatesDialog newInstance(String title) {
        CoordinatesDialog dialog = new CoordinatesDialog();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_coordinates, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        setBuilder(builder, view);
        initEditText(view);
        initPresenter();
        return builder.create();
    }

    private void initEditText(View view) {
        etLatitude = view.findViewById(R.id.etLatitude);
        etLatitude.setKeyListener(DigitsKeyListener.getInstance(true, true));
        etLongitude = view.findViewById(R.id.etLongitude);
        etLongitude.setKeyListener(DigitsKeyListener.getInstance(true, true));
    }

    private void initPresenter() {
        presenter = new CoordinatesDialogPresenter(this);
        presenter.checkLatitude(getContext());
        presenter.checkLongitude(getContext());
    }

    private void setBuilder(AlertDialog.Builder builder, View view) {
        builder.setView(view)
                .setTitle(getArguments().getString(KEY_TITLE))
                .setPositiveButton(getString(R.string.bt_save), this)
                .setNegativeButton(getString(R.string.bt_cancel), this);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                presenter.saveCoordinates(
                        getContext(),
                        etLatitude.getText().toString(),
                        etLongitude.getText().toString()
                );
                presenter.closeDialog(dialog);
                break;
            case BUTTON_NEGATIVE:
                presenter.closeDialog(dialog);
                break;
        }
    }

    @Override
    public void setLatitudeText(@NotNull String text) {
        etLatitude.setText(text);
    }

    @Override
    public void setLongitudeText(@NotNull String text) {
        etLongitude.setText(text);
    }

    @Override
    public void showToast(int stringId) {
        Toast.makeText(getContext(), stringId, Toast.LENGTH_LONG).show();
    }
}
