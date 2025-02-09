package com.aevin.domain.items;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@ToString
@Getter
@NoArgsConstructor
@Table(name = "item_img")
@Entity
public class ItemImg {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_img_id")
    private Long id;

    private String imgName;     //이미지 파일명

    private String oriImgName;      //원본 이미지 파일명

    private String imgUrl;      //이미지 조회 경로

    private String repimgYn;        //대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    public ItemImg(String imgName, String oriImgName, String imgUrl, String repimgYn, Item item) {
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
        this.repimgYn = repimgYn;
        this.item = item;
    }

    public void updateItemImg(String oriImgName, String imgName, String imgUrl) {
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

}
