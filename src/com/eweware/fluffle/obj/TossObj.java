package com.eweware.fluffle.obj;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by davidvronay on 5/10/16.
 */
@Entity
@Index
public class TossObj {
    @Id public Long id;
}
