package br.com.opet.tds.facebookappopet;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends Activity {

    private CallbackManager callbackManager;//responsavel pela validacao do usuario e resposta da operaçao
    private LoginButton loginButton;// botao do login facebook

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());//tudo que é associado ao facebook é carregado nesse momento, feito antes do setcontentview
        setContentView(R.layout.activity_main);

        loginButton = (LoginButton) findViewById(R.id.login_button);

        callbackManager = CallbackManager.Factory.create(); //faz com que o callback crie a manipulacao tudo automatico

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) {

        }
        catch (NoSuchAlgorithmException e) {

        }

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {  //verifica qual é o status do usuario no momento da conexao
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("ID_FB",loginResult.getAccessToken().getUserId());
                redirect(loginResult.getAccessToken().getUserId());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        });

        AccessToken accessToken = AccessToken.getCurrentAccessToken();//verifica se tem um token valido e redireciona para a tela
        if(accessToken != null){
            redirect(accessToken.getUserId()); //devolve qual o id que está logado
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //precisa exister esse metodo
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void redirect(String ID){ //todos os ids no facebook sáo string
        Intent intent = new Intent(MainActivity.this,FriendsActivity.class);
        intent.putExtra("FB_ID",ID);
        startActivity(intent);
    }
}
