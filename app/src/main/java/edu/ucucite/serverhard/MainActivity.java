package edu.ucucite.serverhard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import dmax.dialog.SpotsDialog;
import edu.ucucite.serverhard.common.Common;
import edu.ucucite.serverhard.model.ServerUserModel;

public class MainActivity extends AppCompatActivity {

    private static int APP_REQUEST_CODE = 7171;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;
    private AlertDialog dialog;
    private DatabaseReference serverRef;
    private List<AuthUI.IdpConfig> providers;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(listener);
    }

    @Override
    protected void onStop() {
        if(listener != null)
            firebaseAuth.removeAuthStateListener(listener);
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    private void init() {
        providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());

        serverRef = FirebaseDatabase.getInstance().getReference(Common.SERVER_REF);
        firebaseAuth = FirebaseAuth.getInstance();
        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
        listener = firebaseAuthLocal -> {

            FirebaseUser user = firebaseAuthLocal.getCurrentUser();
            if (user != null)
            {
                //Check user from firebase
                checkServerUserFromFirebase(user);
            }
            else
            {
                phoneLogin();
            }
        };
    }

    private void checkServerUserFromFirebase(FirebaseUser user) {
        dialog.show();
        serverRef.child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())
                        {
                            ServerUserModel userModel = dataSnapshot.getValue(ServerUserModel.class);
                            if (userModel.isActive())
                            {
                                goToHomeActivity(userModel);
                            }
                            else
                            {
                                dialog.dismiss();
                                Toast.makeText(MainActivity.this, "Wait for Admin permission", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            //User not exist
                            dialog.dismiss();
                            showRegisterDialog(user);
                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, ""+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showRegisterDialog(FirebaseUser user) {

        androidx.appcompat.app.AlertDialog.Builder builder =new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Register");
        builder.setMessage("Please fill information \n Account will be accepted later");

        View itemView = LayoutInflater.from(this).inflate(R.layout.layout_register, null);
        EditText edit_name = itemView.findViewById(R.id.edit_name);
        EditText edit_phone = itemView.findViewById(R.id.edit_phone);

        //Set data
        edit_phone.setText(user.getPhoneNumber());
        builder.setNegativeButton("CANCEL", (dialogInterface, which) -> dialogInterface.dismiss())
                .setPositiveButton("REGISTER", (dialogInterface, which) -> {
                    if(TextUtils.isEmpty(edit_name.getText().toString()))
                    {
                        Toast.makeText(MainActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ServerUserModel serverUserModel = new ServerUserModel();
                    serverUserModel.setUid(user.getUid());
                    serverUserModel.setName(edit_name.getText().toString());
                    serverUserModel.setPhone(edit_phone.getText().toString());
                    serverUserModel.setActive(false); //Default Failed, active user by manual in Firebase

                    dialog.show();

                    serverRef.child(serverUserModel.getUid())
                            .setValue(serverUserModel)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialogInterface.dismiss();
                                    Toast.makeText(MainActivity.this, "Successfully Registered"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    //goToHomeActivity(serverUserModel);
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialog.show();
                        }
                    });

                });

        builder.setView(itemView);

        androidx.appcompat.app.AlertDialog registerDialog = builder.create();
        registerDialog.show();
    }

    private void goToHomeActivity(ServerUserModel serverUserModel) {

        dialog.dismiss();

        Common.currentServerUser = serverUserModel;
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void phoneLogin() {
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),APP_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == APP_REQUEST_CODE )
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK)
            {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            }
            else
            {
                Toast.makeText(this, "Failed to Sign in", Toast.LENGTH_SHORT).show();
            }
        }
    }
}