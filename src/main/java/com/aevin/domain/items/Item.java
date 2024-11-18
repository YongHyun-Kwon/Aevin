package com.aevin.domain.items;

import com.aevin.domain.BaseEntity;
import com.aevin.domain.BaseTimeEntity;
import com.aevin.exception.OutOfStockException;
import com.aevin.web.dto.ItemFormDto;
import jakarta.persistence.*;
import lombok.*;


@ToString
@Getter @Setter
@NoArgsConstructor
@Table(name = "item")
@Entity
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    private Long id; //상품 코드

    @Column(nullable = false, length = 50)
    private String itemNm; //상품명

    @Column(name = "price", nullable = false)
    private int price;  //가격

    @Column(nullable = false)
    private int stockNumber;  //재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail;  //상품 상세설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;  //상품 판매 상태

    @Builder
    public Item(String itemNm, int price, int stockNumber, String itemDetail, ItemSellStatus itemSellStatus) {
        this.itemNm = itemNm;
        this.price = price;
        this.stockNumber =stockNumber;
        this.itemDetail = itemDetail;
        this.itemSellStatus = itemSellStatus;
    }

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int stockNumber) {
        int restStock = this.stockNumber - stockNumber;
        if( restStock < 0 ) {
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량: " + this.stockNumber + ")");
        }
        this.stockNumber = stockNumber;
    }

    public void addStock(int stockNumber) {
        this.stockNumber = stockNumber;
    }
}
