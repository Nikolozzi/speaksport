package com.gmail.khitirinikoloz.speaksport.ui.home;

import com.gmail.khitirinikoloz.speaksport.entity.EventPost;
import com.gmail.khitirinikoloz.speaksport.model.Post;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class SampleDataGenerator {

    private static final String EVENT_TITLE_1 =
            "Hi everyone, I want to schedule a baseball event tomorrow. There's a " +
                    "stadium in Varketili. Please see the full description for more details.";
    public static final String EVENT_TITLE_2 =
            "The national handball team is organizing a charity event. All registered teams will" +
                    "receive a symbolic prize";
    public static final String EVENT_TITLE_3 =
            "Hi strongmen, I want to schedule a powerlifting event next Wednesday, will have three" +
                    "different weight categories and nice prizes";


    static List<Post> getSamplePosts() {
        return Arrays.asList(
                new EventPost(EVENT_TITLE_1, null, Calendar.getInstance(), Calendar.getInstance(),
                        "Tbilisi, Guramishvili Avenue", "baseball"),
                new EventPost(EVENT_TITLE_2, null, Calendar.getInstance(), Calendar.getInstance(),
                        "Batumi, Chavchavadze Avenue", "handball"),
                new EventPost(EVENT_TITLE_3, null, Calendar.getInstance(), Calendar.getInstance(),
                        "Kutaisi, Rustaveli Avenue", "powerlifting")
        );
    }
}
