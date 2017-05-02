package com.nehow.models;

import cn.mogutrip.hotel.order.entity.Order;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by andrew on 4/29/2017.
 */
@Setter
@Getter
public class CancellationRequest {
    private String queryId;
    private Order order;
    public CancellationRequest(){
    }

}
