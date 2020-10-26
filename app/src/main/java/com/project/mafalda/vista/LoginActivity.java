package com.project.mafalda.vista;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.project.mafalda.MainActivity;
import com.project.mafalda.R;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private SignInButton btnIngresar;
    private GoogleApiClient googleApiClient;
    private static final int COD = 333;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnIngresar = findViewById(R.id.btn_ingresar);
        btnIngresar.setSize(SignInButton.SIZE_WIDE);
        btnIngresar.setColorScheme(SignInButton.COLOR_AUTO);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();


        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /** REALIZAR LA OPTENCIÓN DEL ID DE LA CUENTA GOOGLE PARA ENVIARLA AL ACTIVITY PRINCIPAL
                 * Y, A PARTIR DE ESE DATO LLEVAR EL SEGUIMIENTO DE IMAGENES CONTESTADAS**/
                /*
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

                 */
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,COD);
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "ERROR: "+connectionResult.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == COD){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            validarElResultado(result);
        }else{
            Toast.makeText(this, "ERROR EN EL CÓDIGO", Toast.LENGTH_SHORT).show();
        }
    }

    private void validarElResultado(GoogleSignInResult result) {
        if(result.isSuccess()){
            abrirMenu();
        }else{
            Toast.makeText(this, "RESULTADO ERRADO "+result, Toast.LENGTH_LONG).show();
        }

    }

    private void abrirMenu() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

