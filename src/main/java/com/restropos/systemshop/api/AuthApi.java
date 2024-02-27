package com.restropos.systemshop.api;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.dto.BearerToken;
import com.restropos.systemcore.dto.EnableToken;
import com.restropos.systemcore.dto.LoginDto;
import com.restropos.systemcore.exception.AlreadyUsedException;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.exception.TimeExceededException;
import com.restropos.systemcore.exception.WrongCredentialsException;
import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemcore.security.UsernamePasswordAuthenticationProvider;
import com.restropos.systemcore.service.SecureTokenService;
import com.restropos.systemcore.utils.JwtTokenUtil;
import com.restropos.systemcore.utils.LogUtil;
import com.restropos.systemrefactor.dto.OtpResponseDto;
import com.restropos.systemrefactor.service.EmailService;
import com.restropos.systemrefactor.service.SmsService;
import com.restropos.systemshop.constants.UserTypes;
import com.restropos.systemshop.dto.CustomerDto;
import com.restropos.systemshop.dto.EmailSecuredUserDto;
import com.restropos.systemshop.dto.RegisterDto;
import com.restropos.systemshop.facade.UserFacade;
import com.restropos.systemshop.facade.WorkspaceFacade;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthApi {
    @Autowired
    private UsernamePasswordAuthenticationProvider providerManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private SecureTokenService secureTokenService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private WorkspaceFacade workspaceFacade;

    @PostMapping("/login/email")
    public ResponseEntity<?> loginForEmail(@RequestBody @Valid LoginDto loginDto) {
        Authentication authentication = providerManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenUtil.generateTokenForEmail(new EmailSecuredUserDto(authentication.getPrincipal().toString()), authentication.getAuthorities().stream().findFirst().get().toString());

        BearerToken bearerToken = getBearerToken(accessToken);

        return ResponseEntity.ok(bearerToken);
    }



    @PostMapping("/login/phoneNumber")
    public ResponseEntity<?> loginForPhoneNumber(@RequestBody @Valid EnableToken enableToken) throws NotFoundException, WrongCredentialsException, TimeExceededException {
        Authentication authentication = providerManager.authenticate(new UsernamePasswordAuthenticationToken(enableToken.getAccountInformation(),"", List.of(new SimpleGrantedAuthority(UserTypes.CUSTOMER.getName()))));

        secureTokenService.enableAccountWithToken(enableToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenUtil.generateTokenForPhoneNumber(authentication.getPrincipal().toString(), authentication.getAuthorities().stream().findFirst().orElseThrow(()->new RuntimeException(CustomResponseMessage.AUTHORITY_EXCEPTION)).toString());

        BearerToken bearerToken = getBearerToken(accessToken);

        return ResponseEntity.ok(bearerToken);
    }

    @PostMapping("/workspace/register")
    public ResponseEntity<ResponseMessage> registerNewWorkspace(@RequestBody @Valid RegisterDto registerDto){
        ResponseEntity<ResponseMessage> response = workspaceFacade.registerNewWorkspace(registerDto);
        if (response.getStatusCode().is2xxSuccessful()){
            return emailService.sendWorkspaceVerifyEmail(registerDto.getSystemUser().getEmail());
        }
        return response;
    }

    @PostMapping("/customer/register")
    public ResponseEntity<ResponseMessage> registerNewCustomer(@RequestBody @Valid CustomerDto customerDto) throws AlreadyUsedException {
        return userFacade.registerNewCustomer(customerDto);
    }

    @PostMapping("/account/enable")
    public ResponseEntity<ResponseMessage> enableAccountWithToken(@RequestBody EnableToken enableToken) throws NotFoundException, TimeExceededException, WrongCredentialsException {
        return secureTokenService.enableAccountWithToken(enableToken);
    }

    @GetMapping("/sendVerifyEmail")
    public ResponseEntity<ResponseMessage> sendVerifyEmailToAdmin(@RequestParam String email){
        return emailService.sendWorkspaceVerifyEmail(email);
    }

    @GetMapping("/sendVerifySms")
    public OtpResponseDto sendOtp(@RequestParam String phoneNumber) throws NotFoundException {
        LogUtil.printLog("inside sendOtp :: "+ phoneNumber, CustomTestApi.class);
        return smsService.sendSMS(phoneNumber);
    }

    @GetMapping("/workspace/valid")
    public boolean workspaceValid(@RequestParam @Pattern(regexp = "[A-Za-z0-9](?:[A-Za-z0-9\\-]{0,61}[A-Za-z0-9])?",message = CustomResponseMessage.BUSINESS_DOMAIN_PATTERN) String businessDomain){
        return workspaceFacade.checkWorkspaceDomainValid(businessDomain);
    }

    @GetMapping("/customer/valid")
    public boolean customerValid(@RequestParam String phoneNumber){
        if(!phoneNumber.startsWith("+")) phoneNumber = "+"+phoneNumber;
        return userFacade.customerValid(phoneNumber);
    }

    private static BearerToken getBearerToken(String accessToken) {
        return BearerToken.builder().accessToken(accessToken).tokenType("Bearer ").build();
    }
}