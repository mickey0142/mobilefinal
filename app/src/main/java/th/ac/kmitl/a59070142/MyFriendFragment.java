package th.ac.kmitl.a59070142;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MyFriendFragment extends Fragment {

    int id;
    String name;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        id = bundle.getInt("id");
        name = bundle.getString("name");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_friend, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initText();
        initBackButton();
        initTodoButton();
        initPostButton();
        initAlbumButton();
    }

    void initText()
    {
        TextView header = getView().findViewById(R.id.my_friend_header);
        header.setText(id + " : " + name);
    }

    void initBackButton()
    {
        Button backButton = getView().findViewById(R.id.my_friend_back_button);
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

    void initTodoButton()
    {
        Button todoButton = getView().findViewById(R.id.my_friend_todo);
        todoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                Fragment fragment = new TodoFragment();
                fragment.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.replace(R.id.main_view, fragment).addToBackStack(null).commit();
            }
        });
    }

    void initPostButton()
    {
        Button postButton = getView().findViewById(R.id.my_friend_post);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                Fragment fragment = new PostFragment();
                fragment.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.replace(R.id.main_view, fragment).addToBackStack(null).commit();
            }
        });
    }

    void initAlbumButton()
    {
        Button albumButton = getView().findViewById(R.id.my_friend_album);
        albumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                Fragment fragment = new AlbumFragment();
                fragment.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.replace(R.id.main_view, fragment).addToBackStack(null).commit();
            }
        });
    }
}
