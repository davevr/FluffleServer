package com.eweware.fluffle.obj;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import org.joda.time.DateTime;

/**
 * Created by davidvronay on 5/10/16.
 */
@Entity
@Index
public class TossObj {
    @Id public Long id;
    public Long tosserId;
    public Long catcherId;
    public DateTime startTossDate;
    public DateTime endTossDate;
    public Long bunnyId;
    public Boolean isValid;
    public Integer price;
}
