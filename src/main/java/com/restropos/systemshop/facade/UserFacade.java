package com.restropos.systemshop.facade;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.exception.AlreadyUsedException;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.exception.UnauthorizedException;
import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemcore.utils.LogUtil;
import com.restropos.systemimage.constants.FolderEnum;
import com.restropos.systemimage.service.ImageService;
import com.restropos.systemshop.constants.UserTypes;
import com.restropos.systemshop.dto.CustomerDto;
import com.restropos.systemshop.dto.SystemUserDto;
import com.restropos.systemshop.dto.SystemUserDtoResponse;
import com.restropos.systemshop.entity.user.Customer;
import com.restropos.systemshop.entity.user.SystemUser;
import com.restropos.systemshop.populator.WorkspaceDtoPopulator;
import com.restropos.systemshop.service.CustomerService;
import com.restropos.systemshop.service.RoleService;
import com.restropos.systemshop.service.SystemUserService;
import com.restropos.systemshop.service.WorkspaceService;
import com.restropos.systemverify.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class UserFacade {
    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private WorkspaceDtoPopulator workspaceDtoPopulator;

    @Autowired
    private SmsService smsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ImageService imageService;

    public ResponseEntity<ResponseMessage> registerNewCustomer(CustomerDto customerDto, MultipartFile multipartFile) throws AlreadyUsedException, NotFoundException, IOException {
        if (customerService.checkCustomerExists(customerDto.getPhoneNumber())) {
            throw new AlreadyUsedException(CustomResponseMessage.PHONE_NUMBER_ALREADY_USED);
        } else {
            Customer customer = null;
            try{
                customer = Customer.builder().firstName(customerDto.getFirstName()).lastName(customerDto.getLastName()).phoneNumber(customerDto.getPhoneNumber()).loginDisabled(true).image(imageService.save(multipartFile, FolderEnum.CUSTOMERS)).build();
                customerService.save(customer);
                ResponseEntity<ResponseMessage> response =  smsService.sendSMS(customer.getPhoneNumber());
                if(!response.getStatusCode().is2xxSuccessful()) throw new Exception(CustomResponseMessage.MESSAGE_COULD_NOT_SEND);
                return response;
            }catch (Exception e){
                LogUtil.printLog(CustomResponseMessage.USER_COULD_NOT_CREATED,UserFacade.class);
                assert customer != null;
                imageService.delete(customer.getImage().getImageName(),FolderEnum.CUSTOMERS);
                return ResponseEntity.internalServerError().body(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR,CustomResponseMessage.USER_COULD_NOT_CREATED));
            }
        }
    }

    public boolean customerValid(String phoneNumber) {
        return customerService.checkCustomerValid(phoneNumber);
    }

    public ResponseEntity<SystemUserDto> getUser(String businessDomain) throws NotFoundException, UnauthorizedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var role = authentication.getAuthorities().stream().findFirst().orElseThrow(() -> new NotFoundException(CustomResponseMessage.ROLE_NOT_FOUND));

        if (role.getAuthority().equals(UserTypes.ADMIN.getName()) || role.getAuthority().equals(UserTypes.WAITER.getName()) ||
                role.getAuthority().equals(UserTypes.CASH_DESK.getName()) || role.getAuthority().equals(UserTypes.KITCHEN.getName())) {
            SystemUser systemUser = systemUserService.findSystemUserByEmail(authentication.getPrincipal().toString());
            if (!systemUser.getWorkspace().getBusinessDomain().equals(businessDomain))
                throw new UnauthorizedException(CustomResponseMessage.USER_PERMISSION_PROBLEM);
            SystemUserDto systemUserDto = SystemUserDto.builder().firstName(systemUser.getFirstName()).lastName(systemUser.getLastName()).email(systemUser.getEmail()).workspaceDto(workspaceDtoPopulator.populate(systemUser.getWorkspace())).role(systemUser.getRole().getRoleName()).build();
            return ResponseEntity.ok(systemUserDto);
        }
        return ResponseEntity.badRequest().body(null);
    }

    public List<SystemUserDtoResponse> getStaffByRole(UserTypes userType) {
        return systemUserService.getStaffByRole(userType);
    }

    public List<SystemUserDtoResponse> getAllStaffsExceptAdmin() {
        return systemUserService.getAllStaffsExceptAdmin();
    }

    public ResponseEntity<SystemUserDtoResponse> addNewStaff(SystemUserDto systemUserDto) throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SystemUser systemUser = SystemUser.builder()
                .firstName(systemUserDto.getFirstName())
                .lastName(systemUserDto.getLastName())
                .email(systemUserDto.getEmail())
                .role(roleService.getRole(systemUserDto.getRole()))
                .password(passwordEncoder.encode(systemUserDto.getPassword()))
                .workspace(systemUserService.getSystemUser(authentication.getPrincipal().toString()).getWorkspace())
                .build();

        return systemUserService.addStaff(systemUser);
    }

    public ResponseEntity<ResponseMessage> deleteStaff(String email) {
        return systemUserService.deleteStaff(email);
    }

    public ResponseEntity<SystemUserDtoResponse> updateStaff(SystemUserDto systemUserDto, String email) throws NotFoundException {
        return systemUserService.updateStaff(systemUserDto,email);
    }
}
