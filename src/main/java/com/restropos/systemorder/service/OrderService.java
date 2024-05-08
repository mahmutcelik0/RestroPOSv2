package com.restropos.systemorder.service;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.exception.WrongCredentialsException;
import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemcore.security.SecurityProvideService;
import com.restropos.systemmenu.entity.Product;
import com.restropos.systemmenu.service.ProductService;
import com.restropos.systemmenu.service.WorkspaceTableService;
import com.restropos.systemorder.OrderStatus;
import com.restropos.systemorder.dto.OrderDto;
import com.restropos.systemorder.dto.OrderProductDto;
import com.restropos.systemorder.dto.ReviewDto;
import com.restropos.systemorder.dto.ReviewResponse;
import com.restropos.systemorder.entity.Order;
import com.restropos.systemorder.entity.OrderProduct;
import com.restropos.systemorder.entity.OrderSelectedModifier;
import com.restropos.systemorder.populator.OrderDtoPopulator;
import com.restropos.systemorder.repository.OrderRepository;
import com.restropos.systemshop.entity.Workspace;
import com.restropos.systemshop.entity.user.Customer;
import com.restropos.systemshop.entity.user.SystemUser;
import com.restropos.systemshop.populator.CustomerDtoPopulator;
import com.restropos.systemshop.service.CustomerService;
import com.restropos.systemshop.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
    @Autowired
    private CustomerService customerService;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private SecurityProvideService securityProvideService;

    @Autowired
    private WorkspaceService workspaceService;
    @Autowired
    private CustomerDtoPopulator customerDtoPopulator;

    public ResponseEntity<ResponseMessage> createOrder(OrderDto orderDto) throws NotFoundException {
        List<OrderProductDto> orderProductDtoList = orderDto.getOrderProducts();
        Order order = Order.builder().orderStatus(OrderStatus.RECEIVED).orderCreationTime(new Date()).workspaceTable(workspaceTableService.getWorkspaceTableById(orderDto.getWorkspaceTableDto().getTableId())).customer(customerService.findCustomerByPhoneNumber(orderDto.getCustomerDto().getPhoneNumber())).build();

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

        OrderDto response = orderDtoPopulator.populate(order);
        firebaseService.save(response);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK, CustomResponseMessage.ORDER_CREATED));
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
                                        totalProductCalculatedPrice.set(0, totalProductCalculatedPrice.get(0) + (productSubmodifier.getPrice() * orderProductDto.getQuantity()));
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

    public ResponseEntity<ResponseMessage> waiterTakeOrder(String orderId) throws NotFoundException {
        SystemUser systemUser = securityProvideService.getSystemUser();
        Order order = orderRepository.findByIdAndBusinessDomain(orderId, systemUser.getWorkspace().getBusinessDomain()).orElseThrow(() -> new NotFoundException(CustomResponseMessage.ORDER_NOT_FOUND));
        order.setWaiter(systemUser);
        order.setOrderStatus(OrderStatus.PREPARING);
        orderRepository.save(order);
        firebaseService.save(orderDtoPopulator.populate(order));
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK, CustomResponseMessage.WAITER_HAS_TAKEN_ORDER));
    }

    public ResponseEntity<ResponseMessage> kitchenTakeOrder(String orderId) throws NotFoundException {
        SystemUser systemUser = securityProvideService.getSystemUser();
        Order order = orderRepository.findByIdAndBusinessDomain(orderId, systemUser.getWorkspace().getBusinessDomain()).orElseThrow(() -> new NotFoundException(CustomResponseMessage.ORDER_NOT_FOUND));
        order.setKitchen(systemUser);
        order.setOrderStatus(OrderStatus.SERVING);
        orderRepository.save(order);
        firebaseService.save(orderDtoPopulator.populate(order));
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK, CustomResponseMessage.KITCHEN_HAS_PREPARED_ORDER));
    }

    public ResponseEntity<ResponseMessage> waiterServeOrder(String orderId) throws NotFoundException {
        SystemUser systemUser = securityProvideService.getSystemUser();
        Order order = orderRepository.findByIdAndBusinessDomain(orderId, systemUser.getWorkspace().getBusinessDomain()).orElseThrow(() -> new NotFoundException(CustomResponseMessage.ORDER_NOT_FOUND));
        order.setOrderStatus(OrderStatus.ON_TABLE);
        orderRepository.save(order);
        firebaseService.save(orderDtoPopulator.populate(order));
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK, CustomResponseMessage.WAITER_HAS_SERVED_ORDER));
    }

    public ResponseEntity<ResponseMessage> cashDeskPay(String orderId) throws NotFoundException {
        SystemUser systemUser = securityProvideService.getSystemUser();
        Order order = orderRepository.findByIdAndBusinessDomain(orderId, systemUser.getWorkspace().getBusinessDomain()).orElseThrow(() -> new NotFoundException(CustomResponseMessage.ORDER_NOT_FOUND));
        firebaseService.delete(orderDtoPopulator.populate(order));
        order.setOrderStatus(OrderStatus.COMPLETED);
        order.setCashDesk(systemUser);
        orderRepository.save(order);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK, CustomResponseMessage.CASH_DESK_PAID));
    }

    public ResponseEntity<ResponseMessage> cashDeskPayAll(List<String> orderIdList) throws NotFoundException {
        SystemUser systemUser = securityProvideService.getSystemUser();
        orderIdList.forEach(e -> {
            Order order = null;
            try {
                order = orderRepository.findByIdAndBusinessDomain(e, systemUser.getWorkspace().getBusinessDomain()).orElseThrow(() -> new NotFoundException(CustomResponseMessage.ORDER_NOT_FOUND));
            } catch (NotFoundException ex) {
                throw new RuntimeException(ex);
            }
            firebaseService.delete(orderDtoPopulator.populate(order));
            order.setOrderStatus(OrderStatus.COMPLETED);
            order.setCashDesk(systemUser);
            orderRepository.save(order);
        });
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK, CustomResponseMessage.CASH_DESK_PAID));
    }

    public ResponseEntity<ResponseMessage> kitchenCancelOrder(String orderId) throws NotFoundException {
        SystemUser systemUser = securityProvideService.getSystemUser();
        Order order = orderRepository.findByIdAndBusinessDomain(orderId, systemUser.getWorkspace().getBusinessDomain()).orElseThrow(() -> new NotFoundException(CustomResponseMessage.ORDER_NOT_FOUND));
        firebaseService.delete(orderDtoPopulator.populate(order));
        order.setOrderStatus(OrderStatus.CANCELED);
        order.setKitchen(systemUser);
        orderRepository.save(order);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK, CustomResponseMessage.KITCHEN_HAS_CANCELED_ORDER));
    }

    public List<OrderDto> getCustomerOrders(String customerInfo, String businessDomain) {
        return orderDtoPopulator.populateAll(orderRepository.findByCustomerInfoAndBusinessDomainAndStatus(customerInfo, businessDomain, OrderStatus.COMPLETED, OrderStatus.CANCELED));
    }

    public List<OrderDto> getBusinessOrders() throws NotFoundException {
        SystemUser systemUser = securityProvideService.getSystemUser();
        return orderDtoPopulator.populateAll(orderRepository.findAllBusinessDomain(systemUser.getWorkspace().getBusinessDomain()));
    }

    public ResponseEntity<OrderDto> reviewOrder(ReviewDto reviewDto) throws NotFoundException, WrongCredentialsException {
        OrderDto orderDto = reviewDto.getOrderDto();
        Order order = orderRepository.findByIdAndBusinessDomain(orderDto.getId(), orderDto.getWorkspaceTableDto().getWorkspaceDto().getBusinessDomain()).orElseThrow(() -> new NotFoundException(CustomResponseMessage.ORDER_NOT_FOUND));
        Customer customer = order.getCustomer();
        if (!securityProvideService.getCustomer().getPhoneNumber().equalsIgnoreCase(customer.getPhoneNumber()) || !order.getWorkspaceTable().getWorkspace().getBusinessDomain().equalsIgnoreCase(orderDto.getWorkspaceTableDto().getWorkspaceDto().getBusinessDomain()))
            throw new WrongCredentialsException(CustomResponseMessage.CUSTOMER_NOT_FOUND);

        if (StringUtils.hasText(orderDto.getOrderReviewComment())) {
            order.setReviewComment(orderDto.getOrderReviewComment());
        }
        if (!ObjectUtils.isEmpty(orderDto.getOrderReviewStar())) {
            Workspace workspace = order.getWorkspaceTable().getWorkspace();
            if(workspace.getTotalReviewCount() == null) workspace.setTotalReviewCount(0);
            if(workspace.getMeanOfWorkspaceStar() == null) workspace.setMeanOfWorkspaceStar(0.0);
            order.setReviewStar(orderDto.getOrderReviewStar());
            workspace.setMeanOfWorkspaceStar(calculate(workspace.getMeanOfWorkspaceStar(),workspace.getTotalReviewCount(),orderDto.getOrderReviewStar()));
            workspace.setTotalReviewCount(workspace.getTotalReviewCount()+1);
        }
        order.setOrderCommentTime(new Date());
        if(!CollectionUtils.isEmpty(orderDto.getOrderProducts())){
            orderDto.getOrderProducts().forEach(e -> {
                if (!ObjectUtils.isEmpty(e.getOrderProductReviewStar())) {
                    order.getOrderProducts().stream().filter(orderProduct -> orderProduct.getProduct().getProductName().equalsIgnoreCase(e.getProduct().getProductName())).findFirst().ifPresent(orderProduct -> {
                        orderProduct.setUserReviewStar(e.getOrderProductReviewStar());
                        Product product = orderProduct.getProduct();
                        if(product.getTotalReviewCount() == null) product.setTotalReviewCount(0);
                        if(product.getMeanOfProductStar() == null) product.setMeanOfProductStar(0.0);
                        product.setMeanOfProductStar(calculate(product.getMeanOfProductStar(),product.getTotalReviewCount(),e.getOrderProductReviewStar()));
                        product.setTotalReviewCount(product.getTotalReviewCount()+1);
                    });
                }
            });
        }

        Order savedOrder = orderRepository.save(order);
        return ResponseEntity.ok(orderDtoPopulator.populate(savedOrder));
    }

    private double calculate(Double mean,Integer total,Integer newValue){
        return (mean* total+newValue)/(total+1);
    }

    public ResponseEntity<List<ReviewResponse>> getAllReviewsOfBusinessDomain(String businessDomain) throws WrongCredentialsException {
        boolean workspaceExist = workspaceService.checkWorkspaceDomainExists(businessDomain);
        if(!workspaceExist) throw new WrongCredentialsException(CustomResponseMessage.WORKSPACE_COULD_NOT_FOUND);
        List<Order> orders = orderRepository.findAllBusinessDomainAndCommented(businessDomain);
        List<ReviewResponse> responseList = new ArrayList<>();
        orders.forEach(e->{
            responseList.add(new ReviewResponse(e.getOrderCommentTime(),e.getReviewComment(),e.getReviewStar(),customerDtoPopulator.populate(e.getCustomer())));
        });
        return ResponseEntity.ok(responseList);
    }
}