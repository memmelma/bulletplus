package com.mmr.marius.bulletplus;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class LongTermGoal {
    private static final String TAG = "com.marius.longtermgoal";
    private Date created;
    private String title;
    private long category;
    private String id;

    public LongTermGoal(){
        //needed for FireBase
    }

    public LongTermGoal(String title, long category){
        this.title = title;
        this.created = new Date();
        this.category = category;
        this.id = md5(this.title + this.created + this.category);
    }

    public Date getCreated() {
        return created;
    }

    public String getTitle() {
        return title;
    }

    public long getCategory() {
        return category;
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

