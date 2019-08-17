package pn.com.aasutosh.bloodbankandroid;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DonateViewHolder extends RecyclerView.ViewHolder {
    View view;

    public DonateViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
    }

    public void setName(String name) {
        TextView tvName = view.findViewById(R.id.tvDonateListName);
        tvName.setText(name);
    }

    public void setBloodGroup(String bloodGroup) {
        TextView tvBloodGroup = view.findViewById(R.id.tvDonateListBloodGroup);
        tvBloodGroup.setText(bloodGroup);
    }

    public void setPhoneNum(String phoneNum) {
        TextView tvPhoneNum = view.findViewById(R.id.tvDonateListPhoneNum);
        tvPhoneNum.setText(phoneNum);
    }

    public void setAddress(String address) {
        TextView tvAddress = view.findViewById(R.id.tvDonateListAddress);
        tvAddress.setText(address);
    }
    public void setPostedBy(String postedBy) {
        TextView tvPostedBy = view.findViewById(R.id.tvDonateListPostedBy);
        tvPostedBy.setText(postedBy);
    }
}
