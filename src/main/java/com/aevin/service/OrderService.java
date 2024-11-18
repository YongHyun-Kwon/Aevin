package com.aevin.service;

import com.aevin.domain.items.Item;
import com.aevin.domain.items.ItemImg;
import com.aevin.domain.items.ItemImgRepository;
import com.aevin.domain.items.ItemRepository;
import com.aevin.domain.members.Member;
import com.aevin.domain.members.MemberRepository;
import com.aevin.domain.orders.Order;
import com.aevin.domain.orders.OrderItem;
import com.aevin.domain.orders.OrderRepository;
import com.aevin.web.dto.OrderDto;
import com.aevin.web.dto.OrderHisDto;
import com.aevin.web.dto.OrderItemDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository  memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;


    public Long order(OrderDto orderDto, String email) {

        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();

        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHisDto> getOrderList(String email, Pageable pageable) {

        List<Order> orders = orderRepository.findOrders(email, pageable);
        Long totalCount = orderRepository.countOrder(email);

        List<OrderHisDto> orderHisDtos = new ArrayList<>();

        for(Order order : orders) {
            OrderHisDto orderHisDto = new OrderHisDto(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for(OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHisDto.addOrderItemDto(orderItemDto);
            }
            orderHisDtos.add(orderHisDto);
        }
        return new PageImpl<OrderHisDto>(orderHisDtos, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email) {

        Member curMember = memberRepository.findByEmail(email);

        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        Member saveMember = order.getMember();

        if (!StringUtils.equals(curMember.getEmail(), saveMember.getEmail())) {
            return false;
        }

        return true;

    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }

    public Long orders(List<OrderDto> orderDtoList, String email) {

        Member member = memberRepository.findByEmail(email);
        List<OrderItem> orderItemList = new ArrayList<>();

        for(OrderDto orderDto : orderDtoList) {
            Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
            orderItemList.add(orderItem);
        }

        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);
        return order.getId();
    }

}
