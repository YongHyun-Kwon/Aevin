package com.aevin.domain.carts;

import com.aevin.domain.BaseEntity;
import com.aevin.domain.members.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
@Table(name = "cart")
@Entity
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cart_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Cart(Member member) {
        this.member = member;
    }

    public static Cart createCart(Member member) {
        Cart cart = new Cart();
         cart.member = member;
         return cart;
    }
}
