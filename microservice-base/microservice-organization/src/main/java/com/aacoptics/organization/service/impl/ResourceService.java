package com.aacoptics.organization.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aacoptics.common.web.entity.ResourceDefinition;
import com.aacoptics.common.web.entity.enums.EditType;
import com.aacoptics.common.web.entity.enums.PermissionType;
import com.aacoptics.common.web.entity.po.BasePo;
import com.aacoptics.organization.config.BusConfig;
import com.aacoptics.organization.entity.param.ResourceQueryParam;
import com.aacoptics.organization.entity.po.Resource;
import com.aacoptics.organization.entity.po.Role;
import com.aacoptics.organization.entity.po.RoleResource;
import com.aacoptics.organization.entity.po.User;
import com.aacoptics.organization.events.EventSender;
import com.aacoptics.organization.mapper.ResourceMapper;
import com.aacoptics.organization.service.IResourceService;
import com.aacoptics.organization.service.IRoleResourceService;
import com.aacoptics.organization.service.IRoleService;
import com.aacoptics.organization.service.IUserService;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResourceService extends ServiceImpl<ResourceMapper, Resource> implements IResourceService {

    @javax.annotation.Resource
    private IRoleResourceService roleResourceService;

    @javax.annotation.Resource
    private IRoleService roleService;

    @javax.annotation.Resource
    private IUserService userService;

    @javax.annotation.Resource
    private EventSender eventSender;

    @javax.annotation.Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean add(Resource resource) {
        boolean isSuccess = this.save(resource);
        ResourceDefinition resourceDefinition = resourceToResourceDefinition(resource);
        resourceDefinition.setEditType(EditType.ADD);
        eventSender.send(BusConfig.ROUTING_KEY, resourceDefinition);
        return isSuccess;
    }

    @Override
    @CacheInvalidate(name = "resource::", key = "#id")
    @CacheInvalidate(name = "resource::", key = "targetObject.getResourceKeys()", multi = true)
    @CacheInvalidate(name = "resource4user::", key = "targetObject.getResource4UserKeys()", multi = true)
    public boolean delete(Long id) {
        ResourceDefinition resourceDefinition = resourceToResourceDefinition(get(id));
        resourceDefinition.setEditType(EditType.DELETE);
        eventSender.send(BusConfig.ROUTING_KEY, resourceDefinition);
        return this.removeById(id);
    }

    @Override
    @CacheInvalidate(name = "resource::", key = "#resource.id")
    @CacheInvalidate(name = "resource::", key = "#resource.code")
    @CacheInvalidate(name = "resource4user::", key = "targetObject.getResource4UserKeys()", multi = true)
    public boolean update(Resource resource) {
        ResourceDefinition resourceDefinition = resourceToResourceDefinition(resource);
        resourceDefinition.setEditType(EditType.UPDATE);
        eventSender.send(BusConfig.ROUTING_KEY, resourceDefinition);
        return this.updateById(resource);
    }

    @Override
    @Cached(name = "resource::", key = "#id", cacheType = CacheType.REMOTE)
    public Resource get(Long id) {
        return this.getById(id);
    }

    @Override
    @Cached(name = "resource::", key = "#code", cacheType = CacheType.REMOTE)
    public Resource getByCode(String code) {
        return this.getOne(new LambdaQueryWrapper<Resource>().eq(Resource::getCode, code));
    }

    @Override
    public List<Resource> listAll() {
        return this.list();
    }

    @Override
    public List<Resource> listByAuthentication() {
        return this.list(new LambdaQueryWrapper<Resource>().eq(Resource::getPermissionType, PermissionType.AUTHENTICATION));
    }

    @Override
    @Cached(name = "resource4user::", key = "#username", cacheType = CacheType.REMOTE)
    public List<Resource> listByUserName(String username) {
        //根据用户名查询到用户所拥有的角色
        User user = userService.getByUniqueId(username);
        List<Role> roles = roleService.listByUserId(user.getId());
        //提取用户所拥有角色id列表
        Set<Long> roleIds = roles.stream().map(BasePo::getId).collect(Collectors.toSet());
        //根据角色列表查询到角色的资源的关联关系
        List<RoleResource> roleResources = roleResourceService.queryByRoleIds(roleIds);
        //根据资源列表查询出所有资源对象
        Set<Long> resourceIds = roleResources.stream().map(RoleResource::getResourceId).collect(Collectors.toSet());
        resourceIds.addAll(this.list(new LambdaQueryWrapper<Resource>().eq(Resource::getPermissionType, PermissionType.WHITELIST)).stream().map(Resource::getId).collect(Collectors.toSet()));
        if (resourceIds.size() == 0) return new ArrayList<>();
        //根据resourceId列表查询出resource对象
        return (List<Resource>) this.listByIds(resourceIds);
    }

    @Override
    public List<Resource> listByRoleId(Long roleId) {
        //根据资源列表查询出所有资源对象
        Set<Long> resourceIds = roleResourceService.queryByRoleId(roleId);
        if (resourceIds.size() == 0) return new ArrayList<>();
        //根据resourceId列表查询出resource对象
        return (List<Resource>) this.listByIds(resourceIds);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public IPage<Resource> query(Page page, ResourceQueryParam resourceQueryParam) {
        QueryWrapper<Resource> queryWrapper = resourceQueryParam.build();
        queryWrapper.like(StringUtils.isNotBlank(resourceQueryParam.getName()), "name", resourceQueryParam.getName());
        queryWrapper.like(StringUtils.isNotBlank(resourceQueryParam.getType()), "type", resourceQueryParam.getType());
        queryWrapper.eq(StringUtils.isNotBlank(resourceQueryParam.getUrl()), "url", resourceQueryParam.getUrl());
        queryWrapper.eq(StringUtils.isNotBlank(resourceQueryParam.getMethod()), "method", resourceQueryParam.getMethod());
        queryWrapper.eq(ObjectUtil.isNotNull(resourceQueryParam.getPermissionType()), "permission_type", resourceQueryParam.getPermissionType());
        return this.page(page, queryWrapper);
    }

    private ResourceDefinition resourceToResourceDefinition(Resource resource) {
        ResourceDefinition resourceDefinition = new ResourceDefinition();
        BeanUtils.copyProperties(resource, resourceDefinition);
        return resourceDefinition;
    }

    public Set<String> getResource4UserKeys() {
        Set<String> resource4UserKeys = stringRedisTemplate.keys("resource4user::*");
        if (CollUtil.isEmpty(resource4UserKeys)) return Collections.singleton("resource4user::*");
        return resource4UserKeys.stream().map(key -> key.replace("resource4user::", StringUtils.EMPTY)).collect(Collectors.toSet());
    }

    public Set<String> getResourceKeys() {
        Set<String> resourceKeys = stringRedisTemplate.keys("resource::*");
        if (CollUtil.isEmpty(resourceKeys)) return Collections.singleton("resource::*");
        return resourceKeys.stream().map(key -> key.replace("resource::", StringUtils.EMPTY)).collect(Collectors.toSet());
    }
}
