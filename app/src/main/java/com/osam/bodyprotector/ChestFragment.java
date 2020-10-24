package com.osam.bodyprotector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChestFragment extends Fragment {

    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<ExerciseData> ExerData = new ArrayList<ExerciseData>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }


        private class ViewHolder {
            public ImageView ExerImg;
            public ImageView check_babel;
            public ImageView check_dumbel;
            public ImageView check_outfit;
            public TextView ExerciseName;
            public TextView ExerciseDescription;
            public TextView ExerciseDifficulty;
        }

        public void addItem(String ExerciseName, String ExerciseDescription, Drawable ExerciseImg, int ExerciseDifficulty, int isDumbel, int isOutfit, int isBabel, String ExerPart){
            ExerciseData addInfo = null;
            addInfo = new ExerciseData();
            addInfo.ExerciseName = ExerciseName;
            addInfo.ExerPart = ExerPart;
            addInfo.ExerciseDescription = ExerciseDescription;
            addInfo.ExerciseDifficulty = ExerciseDifficulty;
            addInfo.ExerciseImg = ExerciseImg;
            addInfo.isBabel = isBabel;
            addInfo.isDumbel = isDumbel;
            addInfo.isOutfit = isOutfit;

            ExerData.add(addInfo);
        }


        @Override
        public int getCount() {
            return ExerData.size();
        }

        @Override
        public Object getItem(int position) {
            return ExerData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("ResourceType")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.xml.exerciseitem, null);

                holder.ExerImg = (ImageView) convertView.findViewById(R.id.img_exercise);
                holder.check_babel = (ImageView) convertView.findViewById(R.id.check_babel);
                holder.check_dumbel = (ImageView) convertView.findViewById(R.id.check_dumbel);
                holder.check_outfit = (ImageView) convertView.findViewById(R.id.check_outfit);
                holder.ExerciseName = (TextView) convertView.findViewById(R.id.txt_exercisename);
                holder.ExerciseDescription = (TextView) convertView.findViewById(R.id.txt_exercisedescription);
                holder.ExerciseDifficulty = (TextView) convertView.findViewById(R.id.txt_exerInfodifficulty);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            final ExerciseData mData = ExerData.get(position);

            switch (mData.ExerciseDifficulty){
                case 0:
                    holder.ExerciseDifficulty.setText("난이도 하");
                    break;
                case 1:
                    holder.ExerciseDifficulty.setText("난이도 중");
                    break;
                case 2:
                    holder.ExerciseDifficulty.setText("난이도 상");
                    break;
            }
            holder.ExerImg.setImageDrawable(mData.ExerciseImg);
            holder.ExerciseDescription.setText(mData.ExerciseDescription);
            if(mData.isDumbel == 1) {
                holder.check_dumbel.setImageResource(android.R.drawable.checkbox_on_background);
            }
            else{
                holder.check_dumbel.setImageResource(android.R.drawable.checkbox_off_background);
            }
            if(mData.isBabel == 1) {
                holder.check_babel.setImageResource(android.R.drawable.checkbox_on_background);
            }
            else{
                holder.check_babel.setImageResource(android.R.drawable.checkbox_off_background);
            }
            if(mData.isOutfit == 1) {
                holder.check_outfit.setImageResource(android.R.drawable.checkbox_on_background);
            }
            else{
                holder.check_outfit.setImageResource(android.R.drawable.checkbox_off_background);
            }
            holder.ExerciseName.setText(mData.ExerciseName);
            return convertView;
        }
    }

    private ListView ExerList;

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chest, container, false);
        TextView text_partname = (TextView)v.findViewById(R.id.text_partname);

        ExerList = (ListView)v.findViewById(R.id.FragListView);
        final ListViewAdapter adapter = new ListViewAdapter(getActivity().getBaseContext());
        ExerList.setAdapter(adapter);


        /*ExerList.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });*/

        final String[] name;
        final String[] description;
        final int[] babel;
        final int[] dumbel;
        final int[] outfit;
        final int[] difficulty;
        if(getArguments() != null){
            Bundle args = getArguments();
            String s = args.getString("partname");
            name = args.getStringArray("name");
            description = args.getStringArray("description");
            babel = args.getIntArray("babel");
            dumbel = args.getIntArray("dumbel");
            outfit = args.getIntArray("outfit");
            difficulty = args.getIntArray("difficulty");
            for(int i = 0; i < 17; i++){
                adapter.addItem(name[i], description[i],getResources().getDrawable(R.drawable.ic_launcher_background),difficulty[i],dumbel[i],outfit[i],babel[i],"arms");
            }
            adapter.notifyDataSetChanged();
            text_partname.setText(s);

            ExerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ExerciseData data = adapter.ExerData.get(position);
                    Intent intent = new Intent(getActivity(), ExerInfoActivity.class);
                    intent.putExtra("youtubeURL", "https://www.youtube.com/watch?v=3VPNc9HAdmU");
                    intent.putExtra("ExerciseTitle", name[position]);
                    intent.putExtra("ExerciseDescription", description[position]);
                    intent.putExtra("ExerciseInfo", "설명을 쓰자~~");
                    intent.putExtra("ExerciseDifficulty", difficulty[position]);
                    intent.putExtra("ExerciseBabel", babel[position]);
                    intent.putExtra("ExerciseDumbel", dumbel[position]);
                    intent.putExtra("ExerciseOutfit", outfit[position]);
                    startActivity(intent);
                }
            });
        }

        return v;
    }
}