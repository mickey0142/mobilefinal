package th.ac.kmitl.a59070142;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Toast;

public class RegisterFragment extends Fragment {

    SQLiteDatabase myDB;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            myDB = getActivity().openOrCreateDatabase("my.db", Context.MODE_PRIVATE, null);
        }
        catch (NullPointerException e)
        {
            Log.d("final", "openOrCreateDatabase return NullPointerException : " + e.getMessage());
        }
        myDB.execSQL("CREATE TABLE IF NOT EXISTS user (id INTEGER PRIMARY KEY AUTOINCREMENT, userId VARCHAR(12), name VARCHAR(50), age INTEGER, password VARCHAR(30))");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initRegisterButton();
    }

    void initRegisterButton()
    {
        Button registerButton = getView().findViewById(R.id.register_register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText userId = getView().findViewById(R.id.register_user_id);
                EditText name = getView().findViewById(R.id.register_name);
                EditText age = getView().findViewById(R.id.register_age);
                EditText password = getView().findViewById(R.id.register_password);
                String userIdStr = userId.getText().toString();
                String nameStr = name.getText().toString();
                String ageStr = age.getText().toString();
                int ageInt = -1;
                String passwordStr = password.getText().toString();
                String message = "";
                boolean success = true;

                if (userIdStr.length() < 6 || userIdStr.length() > 12)
                {
                    success = false;
                    message += "User Id length must be 6 - 12 character long\n";
                    Log.d("final", "user id condition incorrect");
                }
                if (nameStr.length() < 3 || !nameStr.contains(" "))
                {
                    success = false;
                    message += "Name must have firstname and lastname seperated by space\n";
                    Log.d("final", "name condition incorrect");
                }
                try
                {
                    ageInt = Integer.parseInt(ageStr);
                    if (ageInt < 10 || ageInt > 80)
                    {
                        success = false;
                        message += "age must be 10 - 80\n";
                        Log.d("final", "age condition incorrect");
                    }
                }
                catch (Exception e)
                {
                    success = false;
                    message += "age must be number\n";
                    Log.d("final", "age condition incorrect");
                }
                if (passwordStr.length() <= 6)
                {
                    success = false;
                    message += "password must be longer than 6 character";
                    Log.d("final", "password condition incorrect");
                }
                if (!success)
                {
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    Log.d("final", "register failed :\n" + message);
                }
                else
                {
                    ContentValues row = new ContentValues();
                    row.put("userId", userIdStr);
                    row.put("name", nameStr);
                    row.put("age", ageInt);
                    row.put("password", passwordStr);
                    myDB.insert("user", null, row);
                    Log.d("final", "insert data into table");
                    Toast.makeText(getContext(), "Register complete", Toast.LENGTH_SHORT).show();
                    try
                    {
                        getFragmentManager().popBackStack();
                    }
                    catch (NullPointerException e)
                    {
                        Log.d("final", "popBackStack return NullPointerException : " + e.getMessage());
                    }
                }
            }
        });
    }
}
