package th.ac.kmitl.a59070142;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends Fragment {

    SharedPreferences sharedPref;
    SQLiteDatabase myDB;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        myDB = getActivity().openOrCreateDatabase("my.db", Context.MODE_PRIVATE, null);
        myDB.execSQL("CREATE TABLE IF NOT EXISTS user (id INTEGER PRIMARY KEY AUTOINCREMENT, userId VARCHAR(12), name VARCHAR(50), age INTEGER, password VARCHAR(30))");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        checkAlreadyLoggedIn();
        initRegisterButton();
        initLoginButton();
    }

    void checkAlreadyLoggedIn()
    {
        Log.d("final", "shared get string is : " + sharedPref.getString("user id", "not found"));
        if (!sharedPref.getString("user id", "not found").equals("not found"))
        {
            Log.d("final", "already logged in.go to home");
//            getActivity().getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.main_view, new HomeFragment())
//                    .commit();
        }
    }

    void initRegisterButton()
    {
        TextView registerButton = getView().findViewById(R.id.login_register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("final", "go to register");
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new RegisterFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    void initLoginButton()
    {
        Button loginButton = getView().findViewById(R.id.login_login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText userId = getView().findViewById(R.id.login_user_id);
                EditText password = getView().findViewById(R.id.login_password);
                String userIdStr = userId.getText().toString();
                String passwordStr = password.getText().toString();

                if (userIdStr.isEmpty() || passwordStr.isEmpty())
                {
                    Toast.makeText(getContext(), "Please fill out this form", Toast.LENGTH_SHORT).show();
                    Log.d("final", "field is empty");
                    return;
                }

                Cursor cursor = myDB.rawQuery("select userId, name, age, password from user where userId = '" + userIdStr + "' and password = '" + passwordStr + "'", null);
                if (cursor.moveToNext())
                {
                    Log.d("final", "login success");
                    sharedPref.edit()
                            .putString("user id", cursor.getString(0))
                            .putString("name", cursor.getString(1))
                            .putInt("age", cursor.getInt(2))
                            .putString("password", cursor.getString(3))
                            .apply();
                    Toast.makeText(getContext(), "login success", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Log.d("final", "login failed");
                    Toast.makeText(getContext(), "Invalid user or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
