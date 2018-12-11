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

public class TodoAdapter extends ArrayAdapter {

    ArrayList<JSONObject> todoList;
    Context context;

    public TodoAdapter(Context context, int resource, ArrayList<JSONObject> objects)
    {
        super(context, resource, objects);
        this.todoList = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View todoItem = LayoutInflater.from(context).inflate(R.layout.fragment_todo_list_item, parent, false);
        TextView id = todoItem.findViewById(R.id.todo_item_id);
        TextView title = todoItem.findViewById(R.id.todo_item_title);
        TextView completed = todoItem.findViewById(R.id.todo_item_completed);
        JSONObject todoObj = todoList.get(position);
        try
        {
            id.setText("" + todoObj.getInt("id"));
            title.setText(todoObj.getString("title"));
            if(todoObj.getBoolean("completed"))
            {
                completed.setVisibility(View.VISIBLE);
            }
            else
            {
                completed.setVisibility(View.GONE);
            }
        }
        catch (JSONException e)
        {
            Log.d("test", "catch JSONException : " + e.getMessage());
        }

        return todoItem;
    }
}
