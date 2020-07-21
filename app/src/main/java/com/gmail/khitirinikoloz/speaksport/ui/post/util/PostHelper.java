package com.gmail.khitirinikoloz.speaksport.ui.post.util;

import android.content.Context;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.gmail.khitirinikoloz.speaksport.R;
import com.gmail.khitirinikoloz.speaksport.repository.signup.response.UserResponse;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PostHelper {

    public static String getPublicationTime(final Date postedAt) {
        final long diff = Calendar.getInstance().getTime().getTime() -
                postedAt.getTime();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        String timeText = minutes + "m ago";
        if (minutes >= 60) {
            long hours = TimeUnit.MINUTES.toHours(minutes);
            timeText = hours + "h ago";
            if (hours >= 24) {
                long days = TimeUnit.HOURS.toDays(hours);
                timeText = days + "d ago";
            }
        }

        return timeText;
    }

    public static void loadUserImage(final Context context, final ImageView avatarImg,
                                     final UserResponse userResponse) {
        if (userResponse.getPhoto() != null) {
            final String fileAsString = userResponse.getPhoto().getFileObject();
            if (fileAsString != null) {
                byte[] fileAsBytes = android.util.Base64.decode(fileAsString, Base64.DEFAULT);
                Glide.with(context).load(fileAsBytes).into(avatarImg);
            }
        } else
            Glide.with(context).load(R.drawable.avatar).into(avatarImg);
    }

    public static void showSnackBarSuccess(final View rootView, final int layoutId,
                                           final String message, @ColorInt int color) {
        final CoordinatorLayout coordinatorLayout = rootView.findViewById(layoutId);
        coordinatorLayout.setVisibility(View.VISIBLE);
        coordinatorLayout.bringToFront();
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, message, BaseTransientBottomBar.LENGTH_LONG);
        snackbar.setBackgroundTint(color);
        final Snackbar.SnackbarLayout snackBarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarLayout.setRotation(180);
        final TextView snackbarText = snackBarLayout.findViewById(com.google.android.material.R.id.snackbar_text);
        snackbarText.setTextSize(18);
        snackbar.show();
    }
}
