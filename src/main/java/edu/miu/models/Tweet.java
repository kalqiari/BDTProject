package edu.miu.models;


import org.apache.hbase.thirdparty.com.google.gson.annotations.SerializedName;

public class Tweet {

    public class Data {
        @SerializedName("data.id")
        private long id;
        @SerializedName("data.text")
        private String text;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    Data data;

    public Tweet(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + data.getId() +
                ", text='" + data.getText() + '\'' +
                '}';
    }
}
