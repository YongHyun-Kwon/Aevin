package com.aevin.web.dto;

import com.aevin.domain.items.Item;
import com.aevin.domain.items.ItemSellStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "상세 내용은 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    @Builder
    public ItemFormDto(Long id,String itemNm, Integer price, String itemDetail, Integer stockNumber, ItemSellStatus itemSellStatus) {
        this.id = id;
        this.itemNm = itemNm;
        this.price = price;
        this.itemDetail = itemDetail;
        this.stockNumber = stockNumber;
        this.itemSellStatus = itemSellStatus;
    }

    public Item toEntity(ItemFormDto dto) {
        Item entity = Item.builder()
                .itemNm(dto.itemNm)
                .itemDetail(dto.itemDetail)
                .itemSellStatus(dto.itemSellStatus)
                .price(dto.price)
                .stockNumber(dto.stockNumber)
                .build();

        return entity;
    }

    public static ItemFormDto of(Item entity) {
        ItemFormDto dto = ItemFormDto.builder()
                .id(entity.getId())
                .itemNm(entity.getItemNm())
                .itemDetail(entity.getItemDetail())
                .itemSellStatus(entity.getItemSellStatus())
                .price(entity.getPrice())
                .stockNumber(entity.getStockNumber())
                .build();

        return dto;

    }
}
