package com.technogenis.carmechanics.UserStart;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.technogenis.carmechanics.MainActivity;
import com.technogenis.carmechanics.R;


public class UserLoginFragment extends Fragment {

    EditText ed_email,ed_password;
    String email,password;
    Button btn_signIn;

    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    Button btn_google;

    public static final int ERROR_DIALOG_REQUEST_DOWNLOAD = 101;
    public static final int PERMISSION_REQUEST_ENABLE_GPS = 102;
    public static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 103;

    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    private boolean mLocationPermissionGranted = false;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_login, container, false);

        mAuth = FirebaseAuth.getInstance();

        ed_email = view.findViewById(R.id.ed_email);
        ed_password = view.findViewById(R.id.ed_password);
        btn_signIn = view.findViewById(R.id.btn_signIn);
        btn_google = view.findViewById(R.id.btn_google);

        btn_signIn.setOnClickListener(v -> {
            progressDialog = ProgressDialog.show(getActivity(), "", "Please wait", true);
            email = ed_email.getText().toString().trim();
            password = ed_password.getText().toString().trim();

            if (isValid(email,password))
            {
                signInWithEmailPassword(email,password);
            }

        });


        btn_google.setVisibility(View.GONE);

        return view;
    }

    private void signInWithEmailPassword(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            getActivity().finish();
                            progressDialog.dismiss();
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Login Failed! Register First", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private boolean isValid(String email, String password)
    {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (!email.matches(emailPattern) || email.isEmpty())
        {
            ed_email.setError("invalid Email");
            ed_email.requestFocus();
            progressDialog.dismiss();
            return false;
        }

        if (password.isEmpty())
        {
            ed_password.setError("field required");
            ed_password.requestFocus();
            progressDialog.dismiss();
            return false;
        }

        if (password.length() < 8)
        {
            ed_password.setError("minimum 8 character required");
            ed_password.requestFocus();
            progressDialog.dismiss();
            return false;
        }
        return true;
    }
}