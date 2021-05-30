package com.mcuevapps.mutualert.ui.contacts;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcuevapps.mutualert.data.ContactViewModel;
import com.mcuevapps.mutualert.retrofit.response.AlertContact;

import java.util.List;

import com.mcuevapps.mutualert.R;

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = ContactRecyclerViewAdapter.class.getSimpleName();

    private Context ctx;
    private List<AlertContact> mValues;
    private ContactViewModel contactViewModel;

    public ContactRecyclerViewAdapter(Context contexto, List<AlertContact> items) {
        ctx = contexto;
        mValues = items;
        contactViewModel = new ViewModelProvider((FragmentActivity) ctx).get(ContactViewModel.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(mValues != null) {
            holder.mItem = mValues.get(position);

            holder.textViewAlias.setText(holder.mItem.getAlias());
            holder.textViewPhone.setText(holder.mItem.getPhone());

            holder.imageViewShowMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contactViewModel.openDialogContactMenu(ctx, holder.mItem);
                }
            });
        }
    }

    public void setData(List<AlertContact> contactList) {
        this.mValues = contactList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mValues != null){
            return mValues.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textViewAlias;
        public final TextView textViewPhone;
        public final ImageView imageViewShowMenu;
        public AlertContact mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            textViewAlias = (TextView) view.findViewById(R.id.textViewAlias);
            textViewPhone = (TextView) view.findViewById(R.id.textViewNumber);
            imageViewShowMenu = (ImageView) view.findViewById(R.id.imageViewShowMenu);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textViewAlias.getText() + "'";
        }
    }
}