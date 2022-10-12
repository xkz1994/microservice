package com.aacoptics.wlg.equipment.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("em_repair_order")
public class RepairOrder extends BasePo {


    /**
     * 工单号
     */
    @TableField(value = "order_number")
    private String orderNumber;

    /**
     * 资产编码
     */
    @TableField(value = "mch_code")
    private String mchCode;

    /**
     * 维修时间
     */
    @TableField(value = "repair_datetime")
    private LocalDateTime repairDatetime;


    /**
     * 责任人
     */
    @TableField(value = "duty_person_id")
    private String dutyPersonId;

    /**
     * 状态
     */
    @TableField(value = "status")
    private String status;

    /**
     * 故障描述
     */
    @TableField(value = "fault_desc")
    private String faultDesc;

    /**
     * 故障照片
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField(value = "fault_image_id")
    private Long faultImageId;

    /**
     * 维修描述
     */
    @TableField(value = "repair_desc")
    private String repairDesc;

    /**
     * 工单来源，手动创建，点检工单，保养工单
     */
    @TableField(value = "source_type")
    private String sourceType;


    /**
     * 来源工单ID
     */
    @TableField(value = "source_order_id")
    private Long sourceOrderId;


    /**
     * 来源工单项ID
     */
    @TableField(value = "source_order_item_id")
    private Long sourceOrderItemId;
}
