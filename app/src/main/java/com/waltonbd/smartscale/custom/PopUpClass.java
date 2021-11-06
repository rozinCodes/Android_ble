package com.waltonbd.smartscale.custom;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.cardview.widget.CardView;

import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.databinding.LayoutPopupBinding;
import com.waltonbd.smartscale.util.DensityUtil;
import com.waltonbd.smartscale.view.BaseActivity;
import com.waltonbd.smartscale.view.DeviceManagement;

public class PopUpClass {

    //PopupWindow display method
    public void showPopupWindow(View view) {
        final Context context = view.getContext();
        //Create a View object yourself through inflater
        final LayoutPopupBinding binding = LayoutPopupBinding.inflate(LayoutInflater.from(context));

        //Specify the length and width through constants
        int width = CardView.LayoutParams.WRAP_CONTENT;
        int height = CardView.LayoutParams.WRAP_CONTENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(binding.getRoot(), width, height, focusable);

        //Set the location of the window on the screen
        //popupWindow.showAtLocation(view, Gravity.END, 0, 0);
        popupWindow.showAsDropDown(view, -DensityUtil.dip2px(context, 130), -14);
        //dimBehind(popupWindow);

        //Initialize the elements of our window, install the handler
        binding.deviceManagement.setOnClickListener(view1 -> {
            popupWindow.dismiss();
            context.startActivity(new Intent(context, DeviceManagement.class));
        });

        binding.dataCompare.setOnClickListener(view1 -> {
            popupWindow.dismiss();
            BaseActivity.startActivity(context, context.getString(R.string.title_comparison));
        });
    }

    private void dimBehind(PopupWindow popupWindow) {
        View container;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            container = (View) popupWindow.getContentView().getParent();
        } else {
            container = popupWindow.getContentView();
        }
        if (popupWindow.getBackground() != null) {
            container = (View) container.getParent();
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) container.getLayoutParams();
        layoutParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND; // add a flag here instead of clear others
        layoutParams.dimAmount = 0.3f;
        assert windowManager != null;
        windowManager.updateViewLayout(container, layoutParams);
    }

}
