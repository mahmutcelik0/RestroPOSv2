package com.restropos.systemshop.service;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.exception.AlreadyUsedException;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemshop.constants.UserTypes;
import com.restropos.systemshop.dto.BasicUserDto;
import com.restropos.systemshop.entity.user.BasicUser;
import com.restropos.systemshop.entity.user.CashDesk;
import com.restropos.systemshop.entity.user.Kitchen;
import com.restropos.systemshop.populator.BasicUserDtoPopulator;
import com.restropos.systemshop.repository.BasicUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BasicUserService {
    @Autowired
    private BasicUserRepository basicUserRepository;

    @Autowired
    private BasicUserDtoPopulator basicUserDtoPopulator;

    public BasicUser findBasicUserByEmail(String email) throws NotFoundException {
        return basicUserRepository.findBasicUserByEmail(email).orElseThrow(() -> new NotFoundException(CustomResponseMessage.USER_NOT_FOUND));
    }

    public Optional<BasicUser> findOptionalBasicUserByEmail(String email) {
        return basicUserRepository.findBasicUserByEmail(email);
    }

    public List<BasicUserDto> getAllStaffs(UserTypes userType) {
        return basicUserDtoPopulator.populateAll(basicUserRepository.getAllStaffsByRole(userType.getName()));
    }

    public ResponseEntity<ResponseMessage> addStaff(BasicUser basicUser) {
        try {
            if (basicUserRepository.existsBasicUserByEmail(basicUser.getEmail()))
                throw new AlreadyUsedException(CustomResponseMessage.WRONG_CREDENTIAL);
            else if (basicUser.getRole().getRoleName().equals(UserTypes.KITCHEN.getName())){
                Kitchen kitchen = new Kitchen(basicUser);
                basicUserRepository.save(kitchen);
            }else {
                CashDesk cashDesk = new CashDesk(basicUser);
                basicUserRepository.save(cashDesk);
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseMessage(HttpStatus.BAD_REQUEST, CustomResponseMessage.WRONG_CREDENTIAL));
        }

        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK, CustomResponseMessage.USER_CREATED));
    }

    public ResponseEntity<ResponseMessage> deleteStaff(String email) {
        try {
            basicUserRepository.deleteSystemUserByEmail(email);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, CustomResponseMessage.USER_NOT_FOUND));
        }
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK, CustomResponseMessage.USER_DELETED_SUCCESSFULLY));
    }


}