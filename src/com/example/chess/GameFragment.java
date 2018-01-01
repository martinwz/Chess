package com.example.chess;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
public class GameFragment extends Fragment{
		public GameFragment(){
		}
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
			View root=inflater.inflate(R.layout.gamelist, container,false);
			//人机对战
			root.findViewById(R.id.gamelist_ib_pvsc).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(getActivity(),PvsCActivity.class);
					startActivity(intent);
				}
			});
			//玩家对战
			root.findViewById(R.id.igamelist_ib_pvsp).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(getActivity(),PvsPActivity.class);
					startActivity(intent);
				}
			});
			return root;
		}
}
