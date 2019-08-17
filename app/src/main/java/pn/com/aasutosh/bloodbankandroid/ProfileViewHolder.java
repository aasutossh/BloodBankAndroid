package pn.com.aasutosh.bloodbankandroid;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileViewHolder extends RecyclerView.ViewHolder {
    View view;

    public ProfileViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;

    }

    public void setName(String name) {
        TextView tvName = view.findViewById(R.id.tvProfileName);
        tvName.setText(name);

    }

    public void setBloodGroup(String bloodGroup) {
        TextView tvBloodGroup = view.findViewById(R.id.tvProfileBloodGroup);
        tvBloodGroup.setText(bloodGroup);
    }

    public void setPhoneNumber(String phoneNumber) {
        TextView tvPhoneNum = view.findViewById(R.id.tvProfilePhoneNum);
        tvPhoneNum.setText(phoneNumber);

    }

    public void setDistrict(String district) {
        TextView tvDistrict = view.findViewById(R.id.tvProfileDistrict);
        tvDistrict.setText(district);
    }

    public void setStatus(String status) {
        TextView tvStatus = view.findViewById(R.id.tvProfileStatus);
        tvStatus.setText(status);
    }
}
