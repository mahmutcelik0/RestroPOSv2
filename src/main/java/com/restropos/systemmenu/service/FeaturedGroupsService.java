package com.restropos.systemmenu.service;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemcore.security.SecurityProvideService;
import com.restropos.systemcore.utils.RequestUtils;
import com.restropos.systemmenu.dto.FeaturedGroupsDto;
import com.restropos.systemmenu.entity.FeaturedGroups;
import com.restropos.systemmenu.entity.Product;
import com.restropos.systemmenu.populator.FeaturedGroupsDtoPopulator;
import com.restropos.systemmenu.repository.FeaturedGroupsRepository;
import com.restropos.systemshop.entity.Workspace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
public class FeaturedGroupsService {
    @Autowired
    private FeaturedGroupsRepository featuredGroupsRepository;
    @Autowired
    private SecurityProvideService securityProvideService;
    @Autowired
    private FeaturedGroupsDtoPopulator featuredGroupsDtoPopulator;
    @Autowired
    private ProductService productService;

    public List<FeaturedGroupsDto> getAllFeaturedGroups() throws NotFoundException {
        Workspace workspace = securityProvideService.getWorkspace();

        return featuredGroupsRepository.findAllByWorkspaceOrderByGroupName(workspace)
                .stream()
                .map(e->{
                    var group = featuredGroupsDtoPopulator.populate(e);
                    group.setProducts(group.getProducts().stream().sorted((o1, o2) -> o1.getProductName().compareToIgnoreCase(o2.getProductName())).toList());
                    return group;
                })
                .toList();
    }

    public ResponseEntity<ResponseMessage> deleteFeaturedGroup(String groupName) throws NotFoundException {
        Workspace workspace = securityProvideService.getWorkspace();
        if (!featuredGroupsRepository.existsFeaturedGroupsByGroupNameAndWorkspace(groupName, workspace))
            return ResponseEntity.internalServerError().body(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, CustomResponseMessage.FEATURE_GROUP_DOES_NOT_EXIST));
        featuredGroupsRepository.deleteFeaturedGroupsByGroupNameAndWorkspace(groupName, workspace);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK, CustomResponseMessage.FEATURE_GROUP_DELETED));
    }

    public ResponseEntity<FeaturedGroupsDto> addNewFeaturedProduct(FeaturedGroupsDto featuredGroupsDto) throws NotFoundException {
        Workspace workspace = securityProvideService.getWorkspace();
        if (featuredGroupsRepository.existsFeaturedGroupsByGroupNameAndWorkspace(featuredGroupsDto.getGroupName(), workspace)) {
            throw new NotFoundException(CustomResponseMessage.FEATURE_GROUP_ALREADY_EXIST);
        }
        FeaturedGroups featuredGroups = new FeaturedGroups();
        featuredGroupsDto.getProducts().forEach(e -> {
            try {
                featuredGroups.getProductSet().add(productService.getProductByWorkspaceAndProductName(e.getProductName(), workspace));
            } catch (NotFoundException ex) {
                throw new RuntimeException(ex.getMessage());
            }
        });
        featuredGroups.setGroupName(featuredGroupsDto.getGroupName());
        featuredGroups.setWorkspace(workspace);

        FeaturedGroups savedGroups = featuredGroupsRepository.save(featuredGroups);
        return ResponseEntity.ok(featuredGroupsDtoPopulator.populate(savedGroups));
    }

    public List<FeaturedGroupsDto> getAllFeaturedGroups(String origin)  {
        List<FeaturedGroups> featuredGroups = featuredGroupsRepository.findAllByWorkspaceBusinessDomainOrderByGroupName(RequestUtils.getDomainFromOrigin(origin));
        featuredGroups.forEach(e -> {
            e.setProductSet(sortProductSet(e.getProductSet()));
        });
        return featuredGroupsDtoPopulator.populateAll(featuredGroupsRepository.findAllByWorkspaceBusinessDomainOrderByGroupName(RequestUtils.getDomainFromOrigin(origin)));
    }

    private Set<Product> sortProductSet(Set<Product> productSet) {
        TreeSet<Product> sortedSet = new TreeSet<>(Comparator.comparing(Product::getProductName));
        sortedSet.addAll(productSet);
        return sortedSet;
    }
}