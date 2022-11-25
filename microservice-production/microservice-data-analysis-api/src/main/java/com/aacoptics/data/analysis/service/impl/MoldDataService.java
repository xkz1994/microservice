package com.aacoptics.data.analysis.service.impl;

import com.aacoptics.data.analysis.entity.form.QueryParams;
import com.aacoptics.data.analysis.entity.po.MoldData;
import com.aacoptics.data.analysis.entity.po.MoldFlowData;
import com.aacoptics.data.analysis.entity.po.StructureData;
import com.aacoptics.data.analysis.exception.BusinessException;
import com.aacoptics.data.analysis.mapper.MoldDataMapper;
import com.aacoptics.data.analysis.mapper.StructureDataMapper;
import com.aacoptics.data.analysis.service.IMoldDataService;
import com.aacoptics.data.analysis.service.IStructureDataService;
import com.aacoptics.data.analysis.util.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class MoldDataService extends ServiceImpl<MoldDataMapper, MoldData> implements IMoldDataService {

    @Autowired
    MoldDataMapper moldDataMapper;


    @Override
    public void importMoldDataExcel(InputStream in) throws Exception {
        Workbook workbook = ExcelUtil.getWorkbook(in);
        List<String[]> excelDataList = ExcelUtil.read(workbook).get(0);
        String[] titleArray = excelDataList.get(0);//标题行
        String title = titleArray[0];
        if (!"项目量产模具数据表".equals(title)) {
            throw new BusinessException("Excel模板错误，请确认!");
        }
        for (int i = 3; i < excelDataList.size(); i++) {
            String[] dataArray = excelDataList.get(i);
            if (dataArray == null || dataArray.length == 0) {
                break;
            }

            String department = dataArray[0];
            String category = dataArray[1];
            String lensNumber = dataArray[2];
            String project = dataArray[3];
            String partName = dataArray[4];
            String material = dataArray[5];

            if (StringUtils.isEmpty(department)) {
                throw new BusinessException("第" + (i + 1) + "行，事业部不能为空");
            }
            if (StringUtils.isEmpty(category)) {
                throw new BusinessException("第" + (i + 1) + "行，类别不能为空");
            }
            if (StringUtils.isEmpty(lensNumber)) {
                throw new BusinessException("第" + (i + 1) + "行，镜片数不能为空");
            }
            if (StringUtils.isEmpty(project)) {
                throw new BusinessException("第" + (i + 1) + "行，项目名不能为空");
            }
            if (StringUtils.isEmpty(partName)) {
                throw new BusinessException("第" + (i + 1) + "行，零件名称不能为空");
            }
            if (StringUtils.isEmpty(material)) {
                throw new BusinessException("第" + (i + 1) + "行，材料不能为空");
            }

            MoldData moldData = this.getMoldData(category, project, partName, material);
            if (moldData == null) {
                moldData = new MoldData();
            }


            String moldNo = dataArray[6];
            String moldType = dataArray[7];
            String moldCorePassivation = dataArray[8];
            String runnerType = dataArray[9];
            String cavityInnerDiameter = dataArray[10];
            String cavityInnerDiameterRange = dataArray[11];
            String firstRunner = ExcelUtil.handleDecimal(dataArray[12], 1);
            String secondRunner = ExcelUtil.handleDecimal(dataArray[13], 1);
            String thirdRunner = ExcelUtil.handleDecimal(dataArray[14], 1);
            String partingSurface = ExcelUtil.handleDecimal(dataArray[15], 1);
            String splitPositionR1 = ExcelUtil.handleDecimal(dataArray[16], 1);
            String splitPositionR2 = ExcelUtil.handleDecimal(dataArray[17], 1);
            String gateType = dataArray[18];
            String gateWidth = ExcelUtil.handleDecimal(dataArray[19], 2);
            String gateThickness = ExcelUtil.handleDecimal(dataArray[20], 2);
            String gateR1Thickness = ExcelUtil.handleDecimal(dataArray[21], 2);
            String gateR2Thickness = ExcelUtil.handleDecimal(dataArray[22], 2);
            String moldOpeningType = dataArray[23];

            // 设置参数
            moldData.setDepartment(department);
            moldData.setCategory(category);
            moldData.setLensNumber(lensNumber);
            moldData.setProject(project);
            moldData.setPartName(partName);
            moldData.setMaterial(material);
            moldData.setMoldNo(moldNo);
            moldData.setMoldType(moldType);
            moldData.setMoldCorePassivation(moldCorePassivation);
            moldData.setRunnerType(runnerType);
            moldData.setCavityInnerDiameter(cavityInnerDiameter);
            moldData.setCavityInnerDiameterRange(cavityInnerDiameterRange);
            moldData.setFirstRunner(firstRunner);
            moldData.setSecondRunner(secondRunner);
            moldData.setThirdRunner(thirdRunner);
            moldData.setPartingSurface(partingSurface);
            moldData.setSplitPositionR1(splitPositionR1);
            moldData.setSplitPositionR2(splitPositionR2);
            moldData.setGateType(gateType);
            moldData.setGateWidth(gateWidth);
            moldData.setGateThickness(gateThickness);
            moldData.setGateR1Thickness(gateR1Thickness);
            moldData.setGateR2Thickness(gateR2Thickness);
            moldData.setMoldOpeningType(moldOpeningType);

            this.saveOrUpdate(moldData);

        }

    }

    @Override
    public IPage<MoldData> getDataByConditions(Page<MoldData> iPage, String category, String project, String partName, String material) {
        QueryWrapper<MoldData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(category), "category", category)
                .eq(StringUtils.isNotBlank(project), "project", project)
                .eq(StringUtils.isNotBlank(partName), "part_name", partName)
                .eq(StringUtils.isNotBlank(material), "material", material);
        IPage<MoldData> page = this.page(iPage, queryWrapper);
        return page;
    }

    @Override
    public List<MoldData> getAllDataByConditions(QueryParams queryParams) {
        QueryWrapper<MoldData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(queryParams.getCategory()), "category", queryParams.getCategory())
                .eq(StringUtils.isNotBlank(queryParams.getProject()), "project", queryParams.getProject())
                .eq(StringUtils.isNotBlank(queryParams.getPartName()), "part_name", queryParams.getPartName())
                .eq(StringUtils.isNotBlank(queryParams.getMaterial()), "material", queryParams.getMaterial());
        List<MoldData> moldDatas = moldDataMapper.selectList(queryWrapper);
        return moldDatas;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(MoldData moldData) {
        return this.updateById(moldData);
    }

    @Override
    public List<MoldData> getCategory() {
        QueryWrapper<MoldData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("category").select("category");
        List<MoldData> moldDatas = moldDataMapper.selectList(queryWrapper);
        return moldDatas;
    }

    @Override
    public List<MoldData> getProject() {
        QueryWrapper<MoldData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("project").select("project");
        List<MoldData> moldDatas = moldDataMapper.selectList(queryWrapper);
        return moldDatas;
    }

    @Override
    public List<MoldData> getPartName() {
        QueryWrapper<MoldData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("part_name").select("part_name");
        List<MoldData> moldDatas = moldDataMapper.selectList(queryWrapper);
        return moldDatas;
    }

    @Override
    public List<MoldData> getMaterial() {
        QueryWrapper<MoldData> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("material").select("material");
        List<MoldData> moldDatas = moldDataMapper.selectList(queryWrapper);
        return moldDatas;
    }

    private MoldData getMoldData(String category, String project, String partName, String material) {

        QueryWrapper<MoldData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(category), "category", category)
                .eq(StringUtils.isNotBlank(project), "project", project)
                .eq(StringUtils.isNotBlank(partName), "part_name", partName)
                .eq(StringUtils.isNotBlank(material), "material", material);

        List<MoldData> moldDataList = moldDataMapper.selectList(queryWrapper);
        if (moldDataList != null && moldDataList.size() > 0) {
            return moldDataList.get(0);
        }
        return null;
    }
}
