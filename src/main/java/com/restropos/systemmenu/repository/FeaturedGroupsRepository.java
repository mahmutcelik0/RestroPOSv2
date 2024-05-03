package com.restropos.systemmenu.repository;

import com.restropos.systemmenu.entity.FeaturedGroupId;
import com.restropos.systemmenu.entity.FeaturedGroups;
import com.restropos.systemshop.entity.Workspace;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeaturedGroupsRepository extends JpaRepository<FeaturedGroups, FeaturedGroupId> {
    List<FeaturedGroups> findAllByWorkspaceOrderByGroupName(Workspace workspace);

    @Transactional
    @Modifying
    void deleteFeaturedGroupsByGroupNameAndWorkspace(String groupName,Workspace workspace);

    boolean existsFeaturedGroupsByGroupNameAndWorkspace(String groupName,Workspace workspace);

    List<FeaturedGroups> findAllByWorkspaceBusinessDomainOrderByGroupName(String businessDomain);
}
