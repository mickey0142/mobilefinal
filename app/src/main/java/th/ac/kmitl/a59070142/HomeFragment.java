package th.ac.kmitl.a59070142;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class HomeFragment extends Fragment {

    SharedPreferences sharedPref;

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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initText();
        initProfileButton();
        initFriendButton();
        initSignOutButton();
    }

    void initText()
    {
        TextView hello = getView().findViewById(R.id.home_hello);
        TextView quote = getView().findViewById(R.id.home_quote);

        hello.setText("Hello " + sharedPref.getString("name", "name not found"));

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

    void initProfileButton()
    {
        Button profileButton = getView().findViewById(R.id.home_profile_button);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("final", "go to profile");
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new ProfileFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    void initFriendButton()
    {
        Button friendButton = getView().findViewById(R.id.home_friend_button);
        friendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("final", "go to friend");
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new FriendFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    void initSignOutButton()
    {
        Button signOutButton = getView().findViewById(R.id.home_sign_out);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("final", "sign out");
                sharedPref.edit()
                        .clear()
                        .apply();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new LoginFragment())
                        .commit();
            }
        });
    }
}
