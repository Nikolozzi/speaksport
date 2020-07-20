package com.gmail.khitirinikoloz.speaksport.ui.post.util;

import android.content.Context;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gmail.khitirinikoloz.speaksport.R;
import com.gmail.khitirinikoloz.speaksport.repository.signup.response.UserResponse;

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
}
