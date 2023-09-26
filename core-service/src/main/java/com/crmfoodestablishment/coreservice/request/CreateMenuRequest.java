package com.crmfoodestablishment.coreservice.request;

import com.crmfoodestablishment.coreservice.entity.Dish;
import com.crmfoodestablishment.coreservice.entity.Season;
import java.util.List;

public record CreateMenuRequest(
        String name,

        String comment,

        Season season,

        List<Dish> dishes
) {

}
