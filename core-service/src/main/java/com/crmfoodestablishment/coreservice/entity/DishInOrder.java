package com.crmfoodestablishment.coreservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dish_order")
public class DishInOrder {

    @EmbeddedId
    @Column(name = "id", nullable = false, unique = true)
    private DishOrderId id;

    @Column(name = "count", nullable = false)
    private Short count;

    @ManyToOne
    @MapsId("dishId")
    private Dish dish;

    @ManyToOne
    @MapsId("orderId")
    private Order order;

    public DishOrderId getId() {
        return this.id;
    }

    public Short getCount() {
        return this.count;
    }

    public Dish getDish() {
        return this.dish;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setId(DishOrderId id) {
        this.id = id;
    }

    public void setCount(Short count) {
        this.count = count;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof DishInOrder)) return false;
        final DishInOrder other = (DishInOrder) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$count = this.getCount();
        final Object other$count = other.getCount();
        if (this$count == null ? other$count != null : !this$count.equals(other$count)) return false;
        final Object this$dish = this.getDish();
        final Object other$dish = other.getDish();
        if (this$dish == null ? other$dish != null : !this$dish.equals(other$dish)) return false;
        final Object this$order = this.getOrder();
        final Object other$order = other.getOrder();
        if (this$order == null ? other$order != null : !this$order.equals(other$order)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof DishInOrder;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $count = this.getCount();
        result = result * PRIME + ($count == null ? 43 : $count.hashCode());
        final Object $dish = this.getDish();
        result = result * PRIME + ($dish == null ? 43 : $dish.hashCode());
        final Object $order = this.getOrder();
        result = result * PRIME + ($order == null ? 43 : $order.hashCode());
        return result;
    }

    public String toString() {
        return "DishInOrder(id=" + this.getId() + ", count=" + this.getCount() + ", dish=" + this.getDish() + ", order=" + this.getOrder() + ")";
    }
}