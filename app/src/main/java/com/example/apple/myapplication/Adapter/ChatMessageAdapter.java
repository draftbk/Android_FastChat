package com.example.apple.myapplication.Adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.apple.myapplication.Bean.Content;
import com.example.apple.myapplication.R;


public class ChatMessageAdapter extends BaseAdapter
{
	private LayoutInflater mInflater;
	private List<Content> mDatas;
	private String myName;

	public ChatMessageAdapter(Context context, List<Content> mDatas,String myName)
	{
		mInflater = LayoutInflater.from(context);
		this.mDatas = mDatas;
		this.myName=myName;
	}

	@Override
	public int getCount()
	{
		return mDatas.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public int getItemViewType(int position)
	{
		Content content = mDatas.get(position);
		if (content.getNickname().equals(myName))
		{
			return 1;
		}
		return 0;
	}

	@Override
	public int getViewTypeCount()
	{
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Content content = mDatas.get(position);
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			// ͨ��ItemType���ò�ͬ�Ĳ���
			if (getItemViewType(position) == 0)
			{
				convertView = mInflater.inflate(R.layout.item_from_msg, parent,
						false);
				viewHolder = new ViewHolder();
				viewHolder.nickName=(TextView)convertView.findViewById(R.id.text_nickname);
				viewHolder.mDate = (TextView) convertView
						.findViewById(R.id.id_form_msg_date);
				viewHolder.mMsg = (TextView) convertView
						.findViewById(R.id.id_from_msg_info);
			} else
			{
				convertView = mInflater.inflate(R.layout.item_to_msg, parent,
						false);
				viewHolder = new ViewHolder();
				viewHolder.nickName=(TextView)convertView.findViewById(R.id.text_nickname);
				viewHolder.mDate = (TextView) convertView
						.findViewById(R.id.id_to_msg_date);
				viewHolder.mMsg = (TextView) convertView
						.findViewById(R.id.id_to_msg_info);
			}
			convertView.setTag(viewHolder);
		} else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.nickName.setText(content.getNickname());
		viewHolder.mDate.setText(content.getTime());
		viewHolder.mMsg.setText(content.getContent());
		return convertView;
	}

	private final class ViewHolder
	{
		TextView mDate;
		TextView mMsg;
		TextView nickName;
	}

}
