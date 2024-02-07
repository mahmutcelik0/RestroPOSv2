package com.restropos.systemshop.populator;

import com.restropos.systemcore.populator.AbstractPopulator;
import com.restropos.systemshop.dto.CustomerDto;
import com.restropos.systemshop.entity.Customer;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class CustomerDtoPopulator extends AbstractPopulator<Customer, CustomerDto> {
    @Override
    protected CustomerDto populate(Customer customer, CustomerDto customerDto) {
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setPhoneNumber(customer.getPhoneNumber());

        if(!ObjectUtils.isEmpty(customer.getProfilePhoto())){
            customerDto.setProfilePhoto(customer.getProfilePhoto());
        }
        return customerDto;
    }

    @Override
    public CustomerDto generateTarget() {
        return new CustomerDto();
    }
}
