package com.cherisle.azurlanestatlab;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class ShipsAdapter extends RecyclerView.Adapter<ShipsAdapter.ShipViewHolder>
{
    Context context;
    ArrayList<AzurLaneShip> list;
    RecyclerView rv_reference;

    public class ShipViewHolder extends RecyclerView.ViewHolder
    {
        public View svh_view;
        public ImageView image;
        public TextView tv_name;
        public TextView tv_association;
        public TextView tv_class;
        public ImageView iv_remove_card;
        public TableLayout tv_exp;
        public TableRow tr_variants;
        public boolean collapsed;
        public int height_wrapcontent;
        public ShipViewHolder reference;
        public ShipViewHolder(View view)
        {
            super(view);
            collapsed = false;
            reference = this;
            svh_view = view;
            image = (ImageView) view.findViewById(R.id.ship_image);
            tv_name = (TextView) view.findViewById(R.id.ship_title);
            tv_association = (TextView) view.findViewById(R.id.ship_association);
            tv_class = (TextView) view.findViewById(R.id.ship_class);
            iv_remove_card = (ImageView) view.findViewById(R.id.ship_remove_card);
            tv_exp = (TableLayout) view.findViewById(R.id.ship_exploded_view);
            tr_variants = (TableRow) view.findViewById(R.id.tr_ship_stat_variants);
            tv_exp.measure(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT);
            height_wrapcontent = tv_exp.getMeasuredHeight();

            iv_remove_card.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int idx_view_pos = reference.getAdapterPosition();
                    list.remove(idx_view_pos);
                    notifyDataSetChanged();
                }
            });

            view.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(View v)
                {
                    ShipCardAnimation sca = new ShipCardAnimation(tv_exp,reference);
                    if(collapsed)
                    {
                        //Log.i("ALSL","collapsed: collapsing");
                        collapsed = false;

                        //Log.i("ALSL","layoutparams: [before collapsing] " + tv_exp.getMeasuredWidth() + "w x " + tv_exp.getMeasuredHeight() + "h");
                        sca.setDuration(750);

                        // set the starting height (the current height) and the new height that the view should have after the animation
                        sca.setParams(tv_exp.getLayoutParams().height, 0);
                        tv_exp.startAnimation(sca);
                        tv_exp.requestLayout();
                    }
                    else
                    {
                        //Log.i("ALSL","collapsed: expanding");
                        collapsed = true;

                        //Log.i("ALSL","layoutparams: [before expanding] " + tv_exp.getMeasuredWidth() + "w x " + tv_exp.getMeasuredHeight() + "h");
                        sca.setDuration(750);

                        // set the starting height (the current height) and the new height that the view should have after the animation
                        sca.setParams(tv_exp.getLayoutParams().height, height_wrapcontent);
                        tv_exp.startAnimation(sca);
                        tv_exp.requestLayout();
                    }
                }
            });
        }
    }

    public ShipsAdapter(Context context, ArrayList<AzurLaneShip> list, RecyclerView rv)
    {
        this.context = context;
        this.list = list;
        this.rv_reference = rv;
    }

    @NonNull
    @Override
    public ShipViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int ii)
    {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cards_ships, viewGroup, false);
        final ShipViewHolder holder = new ShipViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShipViewHolder svh, int ii)
    {
        //Log.i("ALSL","bvh: position - " + ii);

        updateVariant(svh,ii);

        Picasso.get().load("https://azurlane.koumakan.jp" + list.get(ii).getChibiSrc())
            .resize( dpToPx(50),0)
            .onlyScaleDown().into(svh.image);

        svh.tv_name.setText(list.get(ii).getName());
        setDisplayStats(svh, list.get(ii));
    }

    @Override
    public int getItemCount() {
        if(list.size() == 0)
        {
            rv_reference.invalidate();
        }
        return list.size();
    }

    public int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public void setDisplayStats(ShipViewHolder svh, AzurLaneShip ship)
    {
        TextView tv_disp_association = svh.tv_association;
        TextView tv_disp_class = svh.tv_class;
        TextView tv_disp_health = svh.tv_exp.findViewById(R.id.ship_val_health);
        TextView tv_disp_armor = svh.tv_exp.findViewById(R.id.ship_val_armor);
        TextView tv_disp_reload = svh.tv_exp.findViewById(R.id.ship_val_reload);
        TextView tv_disp_luck = svh.tv_exp.findViewById(R.id.ship_val_luck);
        TextView tv_disp_firepower = svh.tv_exp.findViewById(R.id.ship_val_firepower);
        TextView tv_disp_torpedo = svh.tv_exp.findViewById(R.id.ship_val_torpedo);
        TextView tv_disp_evasion = svh.tv_exp.findViewById(R.id.ship_val_evasion);
        TextView tv_disp_speed = svh.tv_exp.findViewById(R.id.ship_val_speed);
        TextView tv_disp_antiair = svh.tv_exp.findViewById(R.id.ship_val_antiair);
        TextView tv_disp_aviation = svh.tv_exp.findViewById(R.id.ship_val_aviation);
        TextView tv_disp_oilconsumption = svh.tv_exp.findViewById(R.id.ship_val_oilconsumption);
        TextView tv_disp_accuracy = svh.tv_exp.findViewById(R.id.ship_val_accuracy);
        TextView tv_disp_antisubmarine = svh.tv_exp.findViewById(R.id.ship_val_antisubmarine);

        tv_disp_association.setText(ship.getAssociation());
        tv_disp_class.setText(ship.getClassification());
        tv_disp_health.setText(ship.getHealth());
        tv_disp_armor.setText(ship.getArmor());
        tv_disp_reload.setText(ship.getReload());
        tv_disp_luck.setText(ship.getLuck());
        tv_disp_firepower.setText(ship.getFirepower());
        tv_disp_torpedo.setText(ship.getTorpedo());
        tv_disp_evasion.setText(ship.getEvasion());
        tv_disp_speed.setText(ship.getSpeed());
        tv_disp_antiair.setText(ship.getAntiair());
        tv_disp_aviation.setText(ship.getAviation());
        tv_disp_oilconsumption.setText(ship.getOilConsumption());
        tv_disp_accuracy.setText(ship.getAccuracy());
        tv_disp_antisubmarine.setText(ship.getAntisubmarine());
    }

    private void updateVariant(ShipViewHolder svh,int idx)
    {
        final ShipViewHolder svh_reference = svh;
        final AzurLaneShip als_idx = list.get(idx);
        TableRow tr_variants = svh.tr_variants;
        tr_variants.removeAllViews();

        int size_stat_variants = als_idx.getStatVariants().size();

        //Log.i("ALSL","bvh: ssv name - " + list.get(idx).getName());
        //Log.i("ALSL","bvh: ssv size - " + size_stat_variants);

        for(int jj=0; jj< size_stat_variants; jj++)
        {
            TableRow.LayoutParams tr_params = new TableRow.LayoutParams(0, dpToPx(24), .15f);

            if(jj != size_stat_variants - 1)
            {
                tr_params.setMargins(0,dpToPx(2),dpToPx(4),dpToPx(4));
            }
            else
            {
                tr_params.setMargins(0,dpToPx(2),0,dpToPx(4));
            }

            TextView tv_variant = new TextView(svh.svh_view.getContext());
            final String txt_variant = als_idx.getStatVariants().get(jj);

            //general variant clickables
            if(txt_variant.equals("100"))
            {
                tv_variant.setBackgroundResource(R.drawable.border_variant_100);
            }
            else if(txt_variant.equals("120"))
            {
                tv_variant.setBackgroundResource(R.drawable.border_variant_100);
            }
            else if(txt_variant.contains("R"))
            {
                tv_variant.setBackgroundResource(R.drawable.border_variant_retrofit);
            }
            else
            {
                tv_variant.setBackgroundResource(R.drawable.border_variant_100);
            }

            //selected variant clickable
            if(als_idx.getCurrentStatVariant().equals(txt_variant))
            {
                Log.i("ALSL","selected: name - " + als_idx.getName() + " - " + txt_variant);
                if(txt_variant.contains("R"))
                {
                    Log.i("ALSL","selected: retrofit");
                    tv_variant.setBackgroundResource(R.drawable.border_variant_retrofit_selected);
                }
                else if(txt_variant.contains("Base"))
                {
                    Log.i("ALSL","selected: base");
                    tv_variant.setBackgroundResource(R.drawable.border_variant_100_selected);
                }
                else
                {
                    Log.i("ALSL","selected: 100 or 120");
                    tv_variant.setBackgroundResource(R.drawable.border_variant_100_selected);
                }
            }

            tv_variant.setLayoutParams(tr_params);
            tv_variant.setText(txt_variant);
            tv_variant.setGravity(Gravity.CENTER);
            tv_variant.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv_variant.setTextSize(TypedValue.COMPLEX_UNIT_PX,dpToPx(12));

            tv_variant.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //Log.i("ALSL","variant_click: " + als_idx.getName() + " - " + txt_variant);
                    String clicked_stat_variant = "";
                    switch(txt_variant)
                    {
                        case "100":
                            clicked_stat_variant = "100";
                            break;
                        case "100R":
                            clicked_stat_variant = "100 Retrofit";
                            break;
                        case "120":
                            clicked_stat_variant = "120";
                            break;
                        case "120R":
                            clicked_stat_variant = "120 Retrofit";
                            break;
                        case "Base":
                            clicked_stat_variant = "Base Stats";
                            break;
                        default:
                            clicked_stat_variant = "100";
                            break;
                    }
                    als_idx.setCurrentStatVariant(txt_variant);
                    als_idx.getDbHelper().setShipData(als_idx,als_idx.getName(),clicked_stat_variant);
                    setDisplayStats(svh_reference,als_idx);
                    notifyDataSetChanged();
                }
            });

            tr_variants.addView(tv_variant);

            svh.tr_variants.invalidate();
        }
    }
}