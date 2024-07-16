package com.practice.fullstackbackendspringboot.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoListTotal {

    private Long pendingOrderTotal;
    private Long toProcessShipmentTotal;
    private Long processedShipmentTotal;
    private Long pendingCancelledOrders;

}
