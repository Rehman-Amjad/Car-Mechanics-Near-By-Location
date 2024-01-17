package com.technogenis.carmechanics.Start;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.technogenis.carmechanics.Model.UserModel;
import com.technogenis.carmechanics.R;


public class SignUpFragment extends Fragment {

    EditText ed_name,ed_phoneNumber,ed_email,ed_password,ed_confirmPassword;
    Button btn_create;
    String name,phoneNumber,email,password,confirmPassword,userUID ="";
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        initViews(view);

        mAuth = FirebaseAuth.getInstance();

        btn_create.setOnClickListener(v -> {

            progressDialog = ProgressDialog.show(getActivity(), "", "Please wait", true);
            name = ed_name.getText().toString().trim();
            phoneNumber = ed_phoneNumber.getText().toString().trim();
            email = ed_email.getText().toString().trim();
            password = ed_password.getText().toString().trim();
            confirmPassword = ed_confirmPassword.getText().toString().trim();

            if (isValid(name,phoneNumber,email,password,container))
            {
                if (password.equals(confirmPassword))
                {
                    createAccountWithEmailPassword(email,password);
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "password Don't Match", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void createAccountWithEmailPassword(String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            savedDataToFirebase();
//                            emailVerfication(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Failed to create account",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void savedDataToFirebase()
    {
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        userUID = user.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GarageOwnerAccounts");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String key  = reference.push().getKey();
                UserModel model  = new UserModel(name,phoneNumber,email,password,userUID,key);
                reference.child(userUID).setValue(model);
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Account Created", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean isValid(String name, String phoneNumber, String email,
                            String password, ViewGroup container)
    {
        if (name.isEmpty())
        {
            ed_name.setError("field required");
            ed_name.requestFocus();
            progressDialog.dismiss();
            return false;
        }

        if (phoneNumber.isEmpty() || phoneNumber.length() <10)
        {
            ed_phoneNumber.setError("field required");
            ed_phoneNumber.requestFocus();
            progressDialog.dismiss();
            return false;
        }

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

        if (password.length() < 9)
        {
            ed_password.setError("password length short");
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

        if (confirmPassword.isEmpty())
        {
            ed_confirmPassword.setError("field required");
            ed_confirmPassword.requestFocus();
            progressDialog.dismiss();
            return false;
        }
        return true;
    }

    private void initViews(View view)
    {
        ed_name = view.findViewById(R.id.ed_name);
        ed_phoneNumber = view.findViewById(R.id.ed_phoneNumber);
        ed_email = view.findViewById(R.id.ed_email);
        ed_password = view.findViewById(R.id.ed_password);
        ed_confirmPassword = view.findViewById(R.id.ed_confirmPassword);
        btn_create = view.findViewById(R.id.btn_create);
    }
}