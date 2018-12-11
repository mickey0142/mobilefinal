package th.ac.kmitl.a59070142;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FriendFragment extends Fragment {

    String result;
    JSONArray jsonArray;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initBackButton();
        initRestAPI();
    }

    void initBackButton()
    {
        Button backButton = getView().findViewById(R.id.friend_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    getFragmentManager().popBackStack();
                }
                catch (NullPointerException e)
                {
                    Log.d("final", "popBackStack return NullPointerException : " + e.getMessage());
                }
            }
        });
    }

    void initRestAPI()
    {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();
                try {
                    Request request = new Request.Builder().url("https://jsonplaceholder.typicode.com/users").build();

                    Response response = client.newCall(request).execute();
                    result = response.body().string();
                    jsonArray = new JSONArray(result);
                } catch (IOException e) {
                    Log.d("test", "catch IOException : " + e.getMessage());
                } catch (JSONException e) {
                    Log.d("test", "catch JSONException : " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                try
                {
                    final ArrayList<JSONObject> friendList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        friendList.add(obj);
                    }
                    ListView friendListView = getView().findViewById(R.id.friend_list_view);
                    FriendAdapter friendAdapter = new FriendAdapter(getContext(), R.layout.fragment_friend_list_item, friendList);
                    friendListView.setAdapter(friendAdapter);
                    friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Bundle bundle = new Bundle();
                            try{
                                bundle.putInt("id", friendList.get(position).getInt("id"));
                                bundle.putString("name", friendList.get(position).getString("name"));
                            }
                            catch (JSONException e)
                            {
                                Log.d("test", "catch JSONException : " + e.getMessage());
                            }
                            Fragment fragment = new MyFriendFragment();
                            fragment.setArguments(bundle);
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            ft.replace(R.id.main_view, fragment).addToBackStack(null).commit();
                        }
                    });
                }
                catch (JSONException e)
                {
                    Log.d("test", "catch JSONException : " + e.getMessage());
                }
            }
        };
        task.execute();
    }
}
