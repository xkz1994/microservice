package com.aacoptics.wlg.equipment.entity.form;

import com.aacoptics.common.web.entity.form.BaseForm;
import com.aacoptics.wlg.equipment.entity.po.InspectionOrder;
import com.aacoptics.wlg.equipment.entity.po.InspectionOrderItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@ApiModel
@Data
public class InspectionOrderForm extends BaseForm<InspectionOrder> {

    @NotBlank(message = "资产名称不能为空")
    @ApiModelProperty(value = "资产名称")
    private String mchName;

    @NotBlank(message = "规格不能为空")
    @ApiModelProperty(value = "规格")
    private String spec;

    @NotBlank(message = "型号不能为空")
    @ApiModelProperty(value = "型号")
    private String typeVersion;

    @ApiModelProperty(value = "状态")
    private String status;


    private List<InspectionOrderItem> inspectionOrderItemList;

}
