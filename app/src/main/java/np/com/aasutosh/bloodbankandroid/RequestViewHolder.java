package np.com.aasutosh.bloodbankandroid;

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
    public void setTypeOfRequest(String typeOfRequest) {
        TextView tvTypeOfRequest = myView.findViewById(R.id.tvRequestListName);
        tvTypeOfRequest.setText(typeOfRequest);
    }
    public void setPhoneNum(String phoneNum) {
        TextView tvPhoneNum = myView.findViewById(R.id.tvRequestListPhoneNum);
        tvPhoneNum.setText(phoneNum);
    }
}