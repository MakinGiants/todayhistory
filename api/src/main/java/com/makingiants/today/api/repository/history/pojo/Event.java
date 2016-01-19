package com.makingiants.today.api.repository.history.pojo;

import com.google.gson.annotations.SerializedName;

public class Event {
  String title;
  String date;
  @SerializedName("url") String imageUrl;

  public Event(String title, String date, String imageUrl) {
    this.title = title;
    this.date = date;
    this.imageUrl = imageUrl;
  }

  public String getTitle() {
    return title;
  }

  public String getDate() {
    return date;
  }

  public String getImageUrl() {
    return imageUrl;
  }
}
