package com.cherisle.azurlanestatlab;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompareShipsFragment extends Fragment
{
    private Context context;
    private View rootView;
    private MainActivity ma_reference;

    private ShipsAdapter mAdapter;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private ArrayList<AzurLaneShip> list = new ArrayList<>();

    private AutoCompleteTextView ac_et_ship;

    public CompareShipsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ma_reference = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_compare_ships, container, false);
        context = rootView.getContext();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
            android.R.layout.simple_dropdown_item_1line, ma_reference.getDbHelper().getAllShipNames());

        ac_et_ship = (AutoCompleteTextView) rootView.findViewById(R.id.et_autocomplete_ship);
        ac_et_ship.setAdapter(adapter);

        ac_et_ship.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3)
            {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(arg1.getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);

                ac_et_ship.setText("");

                Object item = parent.getItemAtPosition(position);
                String ship_name = item.toString();

                boolean exist = false;
                for(int ii=0; ii<list.size(); ii++)
                {
                    if(list.get(ii).getName().equals(ship_name))
                    {
                        exist = true;
                        break;
                    }
                }

                if(!exist)
                {
                    AzurLaneShip als = new AzurLaneShip(ship_name,ma_reference.getDbHelper());
                    ma_reference.getDbHelper().setStatVariants(als,als.getName());
                    ma_reference.getDbHelper().setShipData(als,als.getName(),als.getCurrentStatVariant());
                    list.add(als);
                    mAdapter.notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(context, "Ship already exists!",Toast.LENGTH_SHORT).show();
                }

            }
        });

        recyclerView = (RecyclerView) rootView.findViewById(R.id.ships_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context.getApplicationContext());
        recyclerView.setItemViewCacheSize(0);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ShipsAdapter(context,list,recyclerView);
        recyclerView.setAdapter(mAdapter);

        return rootView;
    }

    //GETTERS
}
