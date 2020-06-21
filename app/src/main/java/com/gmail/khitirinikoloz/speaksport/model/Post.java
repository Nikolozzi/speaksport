package com.gmail.khitirinikoloz.speaksport.model;

import java.io.Serializable;

public interface Post extends Serializable {
    String getTitle();
    String getTopic();
    String getDescription();
}
