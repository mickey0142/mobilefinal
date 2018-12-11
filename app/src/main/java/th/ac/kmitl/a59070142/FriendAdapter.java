package th.ac.kmitl.a59070142;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FriendAdapter extends ArrayAdapter {

    ArrayList<JSONObject> friendList;
    Context context;

    public FriendAdapter(Context context, int resource, ArrayList<JSONObject> objects)
    {
        super(context, resource, objects);
        this.friendList = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View friendItem = LayoutInflater.from(context).inflate(R.layout.fragment_friend_list_item, parent, false);
        TextView header = friendItem.findViewById(R.id.friend_item_header);
        TextView email = friendItem.findViewById(R.id.friend_item_email);
        TextView phone = friendItem.findViewById(R.id.friend_item_phone);
        TextView website = friendItem.findViewById(R.id.friend_item_website);
        JSONObject friendObj = friendList.get(position);
        try
        {
            header.setText(friendObj.getString("id") + " : " + friendObj.getString("name"));
            email.setText(friendObj.getString("email"));
            phone.setText(friendObj.getString("phone"));
            website.setText(friendObj.getString("website"));
        }
        catch (JSONException e)
        {
            Log.d("test", "catch JSONException : " + e.getMessage());
        }

        return friendItem;
    }
}
