package com.restropos.systemorder.service;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.exception.WrongCredentialsException;
import com.restropos.systemmenu.entity.Product;
import com.restropos.systemmenu.service.ProductService;
import com.restropos.systemmenu.service.WorkspaceTableService;
import com.restropos.systemorder.OrderStatus;
import com.restropos.systemorder.dto.OrderDto;
import com.restropos.systemorder.dto.OrderProductDto;
import com.restropos.systemorder.entity.Order;
import com.restropos.systemorder.entity.OrderProduct;
import com.restropos.systemorder.entity.OrderSelectedModifier;
import com.restropos.systemorder.populator.OrderDtoPopulator;
import com.restropos.systemorder.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;
    @Autowired
    private OrderDtoPopulator orderDtoPopulator;
    @Autowired
    private WorkspaceTableService workspaceTableService;

    public Order createOrder(OrderDto orderDto) throws NotFoundException {
        List<OrderProductDto> orderProductDtoList = orderDto.getOrderProducts();
        Order order = Order.builder()
                .orderStatus(OrderStatus.RECEIVED)
                .orderCreationTime(LocalDateTime.now())
                .workspaceTable(workspaceTableService.getWorkspaceTableById(orderDto.getWorkspaceTableDto().getTableId()))
                .build();

        List<OrderProduct> orderProducts = orderProductDtoList.stream().map(e -> {
            try {
                Product product = productService.getProductByBusinessDomainAndProductName(e.getProduct().getProductName(), e.getProduct().getWorkspace().getBusinessDomain());
                if (e.getQuantity() <= 0) throw new WrongCredentialsException(CustomResponseMessage.WRONG_CREDENTIAL);
                OrderProduct orderProduct = new OrderProduct();
                orderProduct.setProduct(product);
                orderProduct.setQuantity(e.getQuantity());
                orderProduct.setOrder(order);
                setProductModifiersAndSubmodifiers(product, e, orderProduct);
                orderProduct.setCalculatedPrice(e.getCalculatedPrice());
                return orderProduct;
            } catch (NotFoundException | WrongCredentialsException ex) {
                throw new RuntimeException(ex);
            }
        }).toList();
        order.setOrderProducts(orderProducts);
        order.setTotalOrderPrice(calculateTotalOrderPrice(orderProducts));
        orderRepository.save(order);
        return order;
    }

    private Long calculateTotalOrderPrice(List<OrderProduct> orderProducts) {
        AtomicLong totalOrderPrice = new AtomicLong();
        orderProducts.forEach(e -> totalOrderPrice.set(totalOrderPrice.get() + e.getCalculatedPrice()));
        return totalOrderPrice.get();
    }

    private void setProductModifiersAndSubmodifiers(Product product, OrderProductDto orderProductDto, OrderProduct orderProduct) {
        List<Double> totalProductCalculatedPrice = new ArrayList<>(Collections.singletonList(product.getPrice() * orderProductDto.getQuantity()));
        if (!CollectionUtils.isEmpty(orderProductDto.getProductSelectedModifiers())) {
            orderProductDto.getProductSelectedModifiers().forEach(productSelectedModifierDto -> {
                AtomicBoolean modifierFound = new AtomicBoolean(false);
                OrderSelectedModifier orderSelectedModifier = new OrderSelectedModifier();
                product.getProductModifiers().forEach(productModifier -> {
                    if (productSelectedModifierDto.getName().equalsIgnoreCase(productModifier.getProductModifierName())) {
                        modifierFound.set(true);
                        orderSelectedModifier.setOrderProducts(orderProduct); //dene
                        orderSelectedModifier.setProductModifier(productModifier);
                        if (!CollectionUtils.isEmpty(productSelectedModifierDto.getSelections())) {
                            productSelectedModifierDto.getSelections().forEach(productSelectedSubmodifierDto -> {
                                AtomicBoolean submodifierFound = new AtomicBoolean(false);
                                productModifier.getProductSubmodifierSet().forEach(productSubmodifier -> {
                                    if (productSelectedSubmodifierDto.getLabel().equalsIgnoreCase(productSubmodifier.getProductSubmodifierName())) {
                                        submodifierFound.set(true);
                                        totalProductCalculatedPrice.set(0, totalProductCalculatedPrice.get(0) + productSubmodifier.getPrice());
                                        orderSelectedModifier.getProductSubmodifiers().add(productSubmodifier);
                                    }
                                });
                                if (!submodifierFound.get()) throw new RuntimeException("SUBMODIFIER NOT FOUND");
                            });
                        }
                    }
                });
                if (!modifierFound.get()) throw new RuntimeException("MODIFIER NOT FOUND");
                orderProduct.getOrderSelectedModifiers().add(orderSelectedModifier);
            });
        }

        if (totalProductCalculatedPrice.get(0).doubleValue() != orderProductDto.getCalculatedPrice())
            throw new RuntimeException("PRICE IS NOT VALID");
    }

    public List<OrderDto> getOrders() {
        return orderDtoPopulator.populateAll(orderRepository.findAll());
    }
}
