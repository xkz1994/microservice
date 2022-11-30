package com.aacoptics.okr.core.mapper;
import org.apache.ibatis.annotations.Param;

import com.aacoptics.okr.core.entity.po.KeyResultDetail;
import com.aacoptics.okr.core.entity.po.ObjectiveDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface KeyResultDetailMapper extends BaseMapper<KeyResultDetail> {

}