package com.cherisle.azurlanestatlab;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class AlslHomeFragment extends Fragment
{
    Context context; //Declare the variable context
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_alsl_home, container, false);
        context = rootView.getContext(); // Assign your rootView to context

        //TextView tv_data = rootView.findViewById(R.id.data_txt);
        //tv_data.setText("Hello Nick");
        //readCacheFile(rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {

        super.onViewCreated(view, savedInstanceState);
        //LinearLayout ll_nodecount_btn = (LinearLayout) getView().findViewById(R.id.home_nodecount_btn);
        //ll_nodecount_btn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_homenodecount_to_nodes, null));
    }

    /*
    private void readCacheFile(ViewGroup rv)
    {
        final ViewGroup rootView = rv;
        byte[] bytes = new byte[1024];
        String fileName = "homecontent.txt";
        File cache = new File(context.getFilesDir().getAbsolutePath(),fileName);
        if(cache.exists())
        {
            try
            {
                FileInputStream outputStream = new FileInputStream(cache);
                outputStream.read(bytes);

                String login_info = new String(bytes);

                JSONObject credential = new JSONObject(login_info);
                final String count = credential.getString("node_count");
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        TextView tv_activeNodes = (TextView) rootView.findViewById(R.id.homepage_activeNodesCnt);
                        tv_activeNodes.setText(count);
                    }
                });
                outputStream.close();
            }
            catch (JSONException jsone)
            {
                Log.e("Exception", "JSON Exception: " + jsone.toString());
            }
            catch (IOException e)
            {
                Log.e("Exception", "File read failed: " + e.toString());
            }
        }
        else // no cache available
        {

        }
    }

    private void writeToCache(String count)
    {
        String fileName = "homecontent.txt";
        try
        {
            FileOutputStream outputStream = new FileOutputStream(new File(context.getFilesDir().getAbsolutePath(),fileName));
            JSONObject credential = new JSONObject();
            credential.put("node_count",count);
            outputStream.write(credential.toString().getBytes());
            outputStream.close();
        }
        catch (JSONException jsone)
        {
            Log.e("Exception", "JSON Exception: " + jsone.toString());
        }
        catch (IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    */
}
