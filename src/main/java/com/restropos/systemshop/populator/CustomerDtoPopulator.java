package com.restropos.systemshop.populator;

import com.restropos.systemcore.populator.AbstractPopulator;
import com.restropos.systemshop.dto.CustomerDto;
import com.restropos.systemshop.entity.user.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class CustomerDtoPopulator extends AbstractPopulator<Customer, CustomerDto> {
    @Autowired
    private ImageDtoPopulator imageDtoPopulator;

    @Override
    protected CustomerDto populate(Customer customer, CustomerDto customerDto) {
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setPhoneNumber(customer.getPhoneNumber());

        if(!ObjectUtils.isEmpty(customer.getImage())){
            customerDto.setImageDto(imageDtoPopulator.populate(customer.getImage()));
        }
        return customerDto;
    }

    @Override
    public CustomerDto generateTarget() {
        return new CustomerDto();
    }
}
