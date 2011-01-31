package truesculpt.ui.views;

import truesculpt.actions.BaseAction;
import truesculpt.main.R;
import truesculpt.managers.ActionsManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HistoryAdapter extends BaseAdapter
{
	class ViewHolder
	{
		TextView title;
		TextView description;
		ImageView image;
	}

	LayoutInflater inflater;

	ActionsManager mActionsManager = null;

	public HistoryAdapter(Context context, ActionsManager manager)
	{
		mActionsManager = manager;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount()
	{
		int n = mActionsManager.getActionsList().size();
		return n;
	}

	@Override
	public Object getItem(int position)
	{
		Object obj = mActionsManager.getActionsList().get(position);
		return obj;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;

		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.historyitem, null);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.description = (TextView) convertView.findViewById(R.id.description);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			convertView.setTag(holder);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		BaseAction action = mActionsManager.getActionsList().get(position);
		holder.title.setText(action.GetActionName());
		holder.description.setText(action.getDescription());
		holder.image.setImageResource(action.GetImageResourceID());

		return convertView;
	}

}