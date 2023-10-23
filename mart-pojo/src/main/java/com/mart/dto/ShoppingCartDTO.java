package com.mart.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class ShoppingCartDTO implements Serializable {

    private Long GoodsId;
    private Long setmealId;
    private String GoodsFlavor;

}
