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
    private static final String EVENT_TITLE_2 =
            "The national handball team is organizing a charity event. All registered teams will" +
                    "receive a symbolic prize";
    private static final String EVENT_TITLE_3 =
            "Hi strongmen, I want to schedule a powerlifting event next Wednesday, will have three" +
                    "different weight categories and nice prizes";
    private static final String DESCRIPTION = "All the teams willing to participate must write to me " +
            "the list of names of their teammates. Depending on the number of participants the " +
            "event may be split on 2 days.";


    static List<Post> getSamplePosts() {
        return Arrays.asList(
                new EventPost(EVENT_TITLE_1, DESCRIPTION, Calendar.getInstance(), Calendar.getInstance(),
                        "Tbilisi, Guramishvili Avenue", "baseball"),
                new EventPost(EVENT_TITLE_2, DESCRIPTION, Calendar.getInstance(), Calendar.getInstance(),
                        "Batumi, Chavchavadze Avenue", "handball"),
                new EventPost(EVENT_TITLE_3, DESCRIPTION, Calendar.getInstance(), Calendar.getInstance(),
                        "Kutaisi, Rustaveli Avenue", "powerlifting")
        );
    }
}
