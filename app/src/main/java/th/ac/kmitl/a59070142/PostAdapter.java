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

public class PostAdapter extends ArrayAdapter {

    ArrayList<JSONObject> postList;
    Context context;

    public PostAdapter(Context context, int resource, ArrayList<JSONObject> objects)
    {
        super(context, resource, objects);
        this.postList = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View postItem = LayoutInflater.from(context).inflate(R.layout.fragment_post_list_item, parent, false);
        TextView header = postItem.findViewById(R.id.post_item_header);
        TextView body = postItem.findViewById(R.id.post_item_body);
        JSONObject postObj = postList.get(position);
        try
        {
            header.setText(postObj.getInt("id") + " : " + postObj.getString("title"));
            body.setText(postObj.getString("body"));
        }
        catch (JSONException e)
        {
            Log.d("test", "catch JSONException : " + e.getMessage());
        }

        return postItem;
    }
}
