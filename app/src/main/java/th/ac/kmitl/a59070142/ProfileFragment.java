package th.ac.kmitl.a59070142;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ProfileFragment extends Fragment {

    SharedPreferences sharedPref;
    SQLiteDatabase myDB;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            sharedPref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        }
        catch (NullPointerException e)
        {
            Log.d("final", "getSharedPrefences return NullPointerException : " + e.getMessage());
        }
        myDB = getActivity().openOrCreateDatabase("my.db", Context.MODE_PRIVATE, null);
        myDB.execSQL("CREATE TABLE IF NOT EXISTS user (id INTEGER PRIMARY KEY AUTOINCREMENT, userId VARCHAR(12), name VARCHAR(50), age INTEGER, password VARCHAR(30))");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
        initSaveButton();
    }

    void initData()
    {
        EditText userId = getView().findViewById(R.id.profile_user_id);
        EditText name = getView().findViewById(R.id.profile_name);
        EditText age = getView().findViewById(R.id.profile_age);
        EditText password = getView().findViewById(R.id.profile_password);
        EditText quote = getView().findViewById(R.id.profile_quote);
        userId.setText(sharedPref.getString("user id", "user id not found"));
        name.setText(sharedPref.getString("name", "name not found"));
        age.setText("" + sharedPref.getInt("age", -1));
        password.setText(sharedPref.getString("password", "password not found"));

        File root = Environment.getExternalStorageDirectory();
        File file = new File(root, "quote.txt");
        if (!file.exists())
        {
            quote.setText("there is no quote");
        }
        else
        {
            StringBuilder text = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
                br.close();
            }
            catch (IOException e) {
                Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("final", "read file error : " + e.getMessage());
            }
            quote.setText(text.toString());
        }
    }

    void initSaveButton()
    {
        Button saveButton = getView().findViewById(R.id.profile_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText userId = getView().findViewById(R.id.profile_user_id);
                EditText name = getView().findViewById(R.id.profile_name);
                EditText age = getView().findViewById(R.id.profile_age);
                EditText password = getView().findViewById(R.id.profile_password);
                String userIdStr = userId.getText().toString();
                String nameStr = name.getText().toString();
                String ageStr = age.getText().toString();
                String passwordStr = password.getText().toString();
                int ageInt = -1;

                EditText quote = getView().findViewById(R.id.profile_quote);
                String quoteStr = quote.getText().toString();

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
                if (quoteStr.isEmpty())
                {
                    success = false;
                    message += "please enter your quote";
                    Log.d("final", "quote is empty");
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
                    myDB.update("user", row, "userId = '" + sharedPref.getString("user id", "user id not found") + "'", null);
                    Log.d("final", "update data into table");
                    Toast.makeText(getContext(), "update complete", Toast.LENGTH_SHORT).show();

                    sharedPref.edit()
                            .putString("user id", userIdStr)
                            .putString("name", nameStr)
                            .putInt("age", ageInt)
                            .putString("password", passwordStr)
                            .apply();

                    try {
                        File root = Environment.getExternalStorageDirectory();
                        File textFile = new File(root, "quote.txt");
                        FileWriter writer = new FileWriter(textFile);
                        writer.append(quoteStr);
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d("final", "write text file error : " + e.getMessage());
                        Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
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
