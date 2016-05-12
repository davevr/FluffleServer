package com.eweware.fluffle.obj;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

/**
 * Created by davidvronay on 5/10/16.
 */
@Entity
@Index
public class PlayerObj {
    @Id public Long id;
    public String username;
    public String nickname;
    @Unindex public transient String passwordhash;
    @Unindex public transient String passwordsalt;
    @Unindex public String userimage;
    public DateTime creationDate;
    public DateTime lastActiveDate;
    public Boolean signedOn;
    public Boolean isAdmin;
    public int totalBunnies;
    public int totalCarrotsFed;
    public DateTime lastAwardDate;
    public List<DateTime> RepeatPlayList;
    public int carrotCount;


}
