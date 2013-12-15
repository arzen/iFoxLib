package com.arzen.iFoxLib.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.arzen.iFoxLib.R;
import com.arzen.iFoxLib.bean.Top.TopList;
import com.arzen.iFoxLib.fragment.TopFragment;

public class TopAdapter extends BaseAdapter {

	public List<TopList> mDatas;
	public LayoutInflater mLayoutInflater;
	public Context mContext;

	public TopAdapter(Context context) {
		mContext = context;
		mLayoutInflater = LayoutInflater.from(context);
	}

	public void setDatas(List<TopList> datas) {
		mDatas = datas;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas == null ? 0 : mDatas.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mDatas.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.adapter_top_item, null);

			viewHolder = new ViewHolder(convertView);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.setData(position, mDatas.get(position));
		return convertView;
	}

	public class ViewHolder {
		private ImageView mImgNumber;
		private TextView mTvGameName;
		private TextView mTvName;
		private TextView mTvScore;
		private TextView mBtnInvite;

		public ViewHolder(View view) {
			mImgNumber = (ImageView) view.findViewById(R.id.imgNumber);
			mTvGameName = (TextView) view.findViewById(R.id.tvGameName);
			mTvName = (TextView) view.findViewById(R.id.tvName);
			mTvScore = (TextView) view.findViewById(R.id.tvScore);
			mBtnInvite = (TextView) view.findViewById(R.id.btnInvite);
		}

		public void setData(int position, TopList top) {
			if (position < 5) {
				mImgNumber.setVisibility(View.VISIBLE);
				switch (position) {
				case 0:
					mImgNumber.setImageResource(R.drawable.icon_1);
					break;
				case 1:
					mImgNumber.setImageResource(R.drawable.icon_2);
					break;
				case 2:
					mImgNumber.setImageResource(R.drawable.icon_3);
					break;
				case 3:
					mImgNumber.setImageResource(R.drawable.icon_4);
					break;
				case 4:
					mImgNumber.setImageResource(R.drawable.icon_5);
					break;
				}
			} else {
				mImgNumber.setVisibility(View.INVISIBLE);
			}
			mTvGameName.setText(top.getPlay_name());
			mTvName.setText(top.getUname());
			mTvScore.setText(top.getScore() + "");
			if (top.getInvited() == 1) {
				mBtnInvite.setVisibility(View.INVISIBLE);
			} else {
				mBtnInvite.setVisibility(View.VISIBLE);

				mBtnInvite.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent it = new Intent(Intent.ACTION_VIEW);

						it.putExtra("sms_body", TopFragment.mInviteString);

						it.setType("vnd.android-dir/mms-sms");

						mContext.startActivity(it);
					}
				});
			}

		}
	}

}
