package com.aevin.domain.orders;

import com.aevin.domain.BaseEntity;
import com.aevin.domain.items.Item;
import jakarta.persistence.*;
import lombok.*;
import org.w3c.dom.DOMImplementationList;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "order_item")
@Entity
@Builder
@AllArgsConstructor
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oder_id")
    private Order order;

    private int orderPrice;

    private int count;

    public static OrderItem createOrderItem(Item item, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());
        item.removeStock(count);

        return orderItem;
    }

    public int getTotalPrice(){
        return orderPrice * count;
    }

    public void cancel() {
        this.getItem().addStock(count);
    }


}
