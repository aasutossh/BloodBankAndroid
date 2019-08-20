package pn.com.aasutosh.bloodbankandroid;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RequestViewHolder extends RecyclerView.ViewHolder {
    View myView;
    public RequestViewHolder(@NonNull View itemView) {
        super(itemView);
        this.myView = itemView;

    }
    public void setName(String name) {
        TextView tvName = myView.findViewById(R.id.tvRequestListName);
        tvName.setText(name);
    }
    public void setQuantity(int quantity) {
        TextView tvQuantity = myView.findViewById(R.id.tvRequestListQuantity);
        tvQuantity.setText(String.valueOf(quantity));
    }
    public void setBloodGroup(String bloodGroup) {
        TextView tvBloodGroup = myView.findViewById(R.id.tvRequestListBloodGroup);
        tvBloodGroup.setText(bloodGroup);
    }
    public void setPhoneNum(String phoneNum) {
        TextView tvPhoneNum = myView.findViewById(R.id.tvRequestListPhoneNum);
        tvPhoneNum.setText(phoneNum);
    }
    public void setAddress(String address) {
        TextView tvAddress = myView.findViewById(R.id.tvRequestListAddress);
        tvAddress.setText(address);
    }
    public void setDateAddress(String dateAddress) {
        TextView tvDateAddress = myView.findViewById(R.id.tvRequestListDateTime);
        tvDateAddress.setText(dateAddress);
    }
    public void setPostedBy(String nameOfPoster) {
        TextView tvPostedBy = myView.findViewById(R.id.tvRequestListPostedBy);
        tvPostedBy.setText(nameOfPoster);
    }
}