package com.restropos.systemmenu.api;

import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemmenu.dto.FeaturedGroupsDto;
import com.restropos.systemmenu.service.FeaturedGroupsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/featuredGroups")
public class FeaturedGroupsApi {
    @Autowired
    private FeaturedGroupsService featuredGroupsService;

    @GetMapping
    public List<FeaturedGroupsDto> getAllFeaturedGroups() throws NotFoundException {
        return featuredGroupsService.getAllFeaturedGroups();
    }

    @PostMapping
    public ResponseEntity<FeaturedGroupsDto> addNewFeaturedProduct(@RequestBody FeaturedGroupsDto featuredGroupsDto) throws NotFoundException {
        return featuredGroupsService.addNewFeaturedProduct(featuredGroupsDto);
    }

    @DeleteMapping
    public ResponseEntity<ResponseMessage> deleteFeaturedGroup(@RequestBody FeaturedGroupsDto featuredGroupsDto) throws NotFoundException {
        return featuredGroupsService.deleteFeaturedGroup(featuredGroupsDto.getGroupName());
    }

    @GetMapping("/customer")
    public List<FeaturedGroupsDto> getAllFeaturedGroupsForCustomer(HttpServletRequest request)  {
        return featuredGroupsService.getAllFeaturedGroups(request.getHeader("Origin"));
    }

}