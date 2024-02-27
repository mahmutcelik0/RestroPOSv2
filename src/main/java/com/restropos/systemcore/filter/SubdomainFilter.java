package com.restropos.systemcore.filter;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.exception.UnauthorizedException;
import com.restropos.systemcore.utils.LogUtil;
import com.restropos.systemshop.service.WorkspaceService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//@Service
public class SubdomainFilter{ // extends OncePerRequestFilter
//    @Autowired
//    private WorkspaceService workspaceService;
//
//    @Override
//    @SneakyThrows
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        if(!getSubdomains().contains(request.getServerName())){
//            LogUtil.printLog(request.getServerName(),SubdomainFilter.class);
//            throw new UnauthorizedException(CustomResponseMessage.USER_PERMISSION_PROBLEM);
//        }
//        LogUtil.printLog(request.getServerName(),SubdomainFilter.class);
//        filterChain.doFilter(request, response);
//    }
//
//    public List<String> getSubdomains(){
//        List<String> subdomainOrigins = new ArrayList<>();
//
//        workspaceService.getAllWorkspaces()
//                        .forEach(e -> {
//                            subdomainOrigins.add(softwareRefactor(e));
//                            subdomainOrigins.add(localRefactor(e));
//                        });
//        subdomainOrigins.add("subdomain1"); //todo SEDAT OZEL ISTEK ILERDE SILINMESI SART
//        subdomainOrigins.add("subdomain2"); //todo SEDAT OZEL ISTEK ILERDE SILINMESI SART
//        subdomainOrigins.add("subdomain3"); //todo SEDAT OZEL ISTEK ILERDE SILINMESI SART
//        subdomainOrigins.add("localhost"); //todo SEDAT OZEL ISTEK ILERDE SILINMESI SART
//
//        return subdomainOrigins;
//    }
//
//    public String softwareRefactor(String string){
//        return string+"restropos.software";
//    }
//
//    public String localRefactor(String string){
//        return string+"restropos.localhost";
//    }
}
