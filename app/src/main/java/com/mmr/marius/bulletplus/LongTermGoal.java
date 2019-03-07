package com.mmr.marius.bulletplus;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class LongTermGoal {
    private static final String TAG = "com.marius.longtermgoal";
    private String title;
    private String description;
    private String userId;
    private Date created;
    private long category;
    private String id;
    private Boolean done;

    public LongTermGoal(){
        //needed for FireBase
    }

    public LongTermGoal(String title, String description, String userId, long category){
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.created = new Date();
        this.category = category;
        this.done = false;
        this.id = md5(this.title + this.description + this.userId + this.created + this.category);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription(){
        return description;
    }

    public String getUserId(){
        return userId;
    }

    public Date getCreated() {
        return created;
    }

    public long getCategory() {
        return category;
    }

    public Boolean getDone(){
        return done;
    }

    public String getId(){
        return id;
    }

    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}

