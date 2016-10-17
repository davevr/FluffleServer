package com.eweware.fluffle.obj;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;
import org.joda.time.DateTime;

/**
 * Created by davidvronay on 10/17/16.
 */

@Entity
@Index
public class ReceiptObj {
    @Id
    public Long id;
    public Long playerId;
    public DateTime purchaseDate;
    public String productId;
    public String store;
    @Unindex
    public String receiptData;
}
